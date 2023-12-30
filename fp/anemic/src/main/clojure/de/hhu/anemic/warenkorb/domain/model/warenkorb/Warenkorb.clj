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

(defn erstelle-warenkorb [kunde-id max-einkaufswert]
  (let [neuer-warenkorb (->Warenkorb (warenkorb-id/erstelleWarenkorbID)
                                     (KundeID kunde-id)
                                     []
                                     (preis/erstelle-preis "0.00" "EUR")
                                     (preis/erstelle-preis max-einkaufswert "EUR"))]
    (validiere-warenkorb neuer-warenkorb)
    neuer-warenkorb))

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
    (assoc warenkorb (:warenkorbzeilen neue-warenkorbzeilen) (:gesamt-preis neuer-gesamtpreis))))

(defn fuege-hinzu [warenkorb artikel anzahl]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))
        gesamtpreis-von-artikel (if zeile-mit-artikel
                                  (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)
                                  (preis/erstelle-preis (:preis artikel) "EUR"))
        neuer-warenkorb (if zeile-mit-artikel
                          (warenkorbzeile/erhoehe-um zeile-mit-artikel anzahl)
                          (fuege-warenkorbzeile-hinzu warenkorb artikel anzahl))]
    (assoc neuer-warenkorb :gesamt-preis (preis/erhoehe-um (:gesamt-preis neuer-warenkorb) gesamtpreis-von-artikel))))

(defn reduziere [warenkorb artikel anzahl]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))]
    (when zeile-mit-artikel
      (warenkorbzeile/reduziere-um zeile-mit-artikel anzahl)
      (when (= 0 (:anzahl zeile-mit-artikel))
        (assoc-in warenkorb [:warenkorbzeilen] (remove #(= artikel (:id %)) (:warenkorbzeilen warenkorb)))))
    (validiere-warenkorb warenkorb)))

(defn entferne [warenkorb artikel]
  (let [zeile-mit-artikel (finde-zeile-zu-artikel warenkorb (:id artikel))]
    (when zeile-mit-artikel
      (let [gesamtpreis-von-artikel (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)]
        (assoc-in warenkorb [:warenkorbzeilen] (remove #(= artikel (:id %)) (:warenkorbzeilen warenkorb)))
        (assoc-in warenkorb [:gesamt-preis] (preis/reduziere-um (:gesamt-preis warenkorb) gesamtpreis-von-artikel)))))
  (validiere-warenkorb warenkorb))