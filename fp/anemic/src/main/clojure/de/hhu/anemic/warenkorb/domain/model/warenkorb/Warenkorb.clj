(ns de.hhu.anemic.warenkorb.domain.model.warenkorb.Warenkorb
  (:require [de.hhu.anemic.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.anemic.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbID :as warenkorb-id]
            [de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile :as warenkorbzeile])
  (:import (de.hhu.anemic.warenkorb.domain.model.kunde.KundeID KundeID)))

(defrecord Warenkorb [id kunde-id warenkorbzeilen gesamt-preis max-einkaufswert])

(defn validiere-warenkorb [warenkorb]
  (let [gesamt-preis (:gesamt-preis warenkorb)
        max-einkaufswert (:max-einkaufswert warenkorb)]
    (when (or (nil? (:id warenkorb))
              (nil? (:kunde-id warenkorb))
              (nil? gesamt-preis)
              (nil? max-einkaufswert)
              (preis/ist-groesser-als? gesamt-preis max-einkaufswert))
      (throw (IllegalArgumentException. "Fehlerhafter Warenkorb")))))

(defn erstelle-neuen-warenkorb [kunde-id max-einkaufswert]
  (let [neuer-warenkorb (->Warenkorb (warenkorb-id/erstelleWarenkorbID)
                                     (KundeID kunde-id)
                                     []
                                     (preis/erstelle-preis "0.00" "EUR")
                                     (preis/erstelle-preis max-einkaufswert "EUR"))]
    (validiere-warenkorb neuer-warenkorb)
    neuer-warenkorb))

(defn erstelle-warenkorb [warenkorb neue-warenkorbzeilen neuer-gesamtpreis]
  (->Warenkorb (:id warenkorb)
               (:kunde-id warenkorb)
               neue-warenkorbzeilen
               neuer-gesamtpreis
               (:max-einkaufswert warenkorb)))

(defn finde-zeile-zu-artikel [warenkorb artikel-id]
  (first (filter #(= artikel-id (:artikel-id %)) (:warenkorbzeilen warenkorb))))

(defn fuege-warenkorbzeile-hinzu [warenkorb artikel anzahl]
  (let [neue-warenkorbzeilen (conj (:warenkorbzeilen warenkorb)
                                   (warenkorbzeile/erstelle-warenkorbzeile (:id artikel)
                                                                           (anzahl/erstelle-anzahl anzahl)
                                                                           (:preis artikel)
                                                                           (anzahl/erstelle-anzahl 10)))
        neuer-gesamtpreis (preis/erhoehe-um (:gesamt-preis warenkorb)
                                            (warenkorbzeile/berechne-gesamtpreis (last neue-warenkorbzeilen)))]
    (erstelle-warenkorb warenkorb neue-warenkorbzeilen neuer-gesamtpreis)))

(defn fuege-hinzu [warenkorb artikel anzahl]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))
        gesamtpreis-von-artikel (if zeile-mit-artikel
                                  (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)
                                  (preis/erstelle-preis (:preis artikel) "EUR"))
        neuer-warenkorb (if zeile-mit-artikel
                          (conj (remove #(= artikel (:id %)) (:warenkorbzeilen warenkorb))
                                (warenkorbzeile/erhoehe-um zeile-mit-artikel anzahl))
                          (fuege-warenkorbzeile-hinzu warenkorb artikel anzahl))]
    (erstelle-warenkorb warenkorb (:warenkorbzeilen neuer-warenkorb) gesamtpreis-von-artikel)))

(defn reduziere [warenkorb artikel anzahl]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))]
    (when zeile-mit-artikel
      (let [neue-warenkorbzeile (warenkorbzeile/reduziere-um zeile-mit-artikel anzahl)]
        (when (= 0 (:anzahl neue-warenkorbzeile))
          (conj (remove #(= artikel (:id %)) (:warenkorbzeilen warenkorb)) neue-warenkorbzeile))
        (erstelle-warenkorb warenkorb (:warenkorbzeilen warenkorb) (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel))))))

(defn entferne [warenkorb artikel]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))]
    (when zeile-mit-artikel
      (let [gesamtpreis-von-artikel (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)]
        (erstelle-warenkorb warenkorb (vec (remove #(= artikel (:id %)) (:warenkorbzeilen warenkorb)))
                                  (preis/reduziere-um (:gesamt-preis warenkorb) gesamtpreis-von-artikel))))))
