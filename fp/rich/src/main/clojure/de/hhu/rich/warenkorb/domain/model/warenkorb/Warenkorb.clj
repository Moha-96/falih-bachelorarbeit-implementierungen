(ns de.hhu.rich.warenkorb.domain.model.warenkorb.Warenkorb
  (:require [de.hhu.rich.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.rich.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbID :as warenkorb-id]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile :as warenkorbzeile])
  (:import (de.hhu.rich.warenkorb.domain.model.kunde.KundeID KundeID)))

(defprotocol IWarenkorb
  (validiere-warenkorb [warenkorb])
  (finde-zeile-zu-artikel [this artikel-id]
    "Findet die Warenkorbzeile für einen Artikel.")
  (fuege-hinzu [this artikel anzahl]
    "Fügt einen Artikel mit einer bestimmten Anzahl zum Warenkorb hinzu.")
  (reduziere [this artikel anzahl]
    "Reduziert die Anzahl eines Artikels im Warenkorb.")
  (entferne [this artikel]
    "Entfernt einen Artikel komplett aus dem Warenkorb.")
  )

(defrecord Warenkorb [id kunde-id warenkorbzeilen gesamt-preis max-einkaufswert]
  IWarenkorb
  (validiere-warenkorb [warenkorb]
    (let [gesamt-preis (:gesamt-preis warenkorb)
          max-einkaufswert (:max-einkaufswert warenkorb)]
      (when (or (nil? (:id warenkorb))
                (nil? (:kunde-id warenkorb))
                (nil? gesamt-preis)
                (nil? max-einkaufswert)
                (preis/ist-groesser-als? gesamt-preis max-einkaufswert))
        (throw (IllegalArgumentException. "Fehlerhafter Warenkorb")))))

  (finde-zeile-zu-artikel [this artikel-id]
    (first (filter #(= artikel-id (:artikel-id %)) (:warenkorbzeilen this))))

  (fuege-hinzu [this artikel anzahl]
    (let [zeile-mit-artikel (finde-zeile-zu-artikel this (:id artikel))
          neue-warenkorb (if zeile-mit-artikel
                           (let [aktualisierte-zeile (warenkorbzeile/erhoehe-um zeile-mit-artikel anzahl)
                                 alte-warenkorbzeilen (remove #(= artikel (:id %)) (:warenkorbzeilen this))]
                             (->Warenkorb (:id this)
                                          (:kunde-id this)
                                          alte-warenkorbzeilen
                                          (conj aktualisierte-zeile)
                                          (preis/erhoehe-um (:gesamt-preis this) (warenkorbzeile/berechne-gesamtpreis aktualisierte-zeile))
                                          (:max-einkaufswert this)))
                           (let [neue-zeile (warenkorbzeile/erstelle-warenkorbzeile (:id artikel)
                                                                                    anzahl
                                                                                    (:preis artikel)
                                                                                    (anzahl/erstelle-anzahl 10))
                                 neue-warenkorbzeilen (conj (:warenkorbzeilen this) neue-zeile)
                                 neuer-gesamtpreis (preis/erhoehe-um (:gesamt-preis this) (warenkorbzeile/berechne-gesamtpreis neue-zeile))]
      (->Warenkorb (:id this)
                   (:kunde-id this)
                   neue-warenkorbzeilen
                   neuer-gesamtpreis
                   (:max-einkaufswert this))))]
(validiere-warenkorb neue-warenkorb)))

(reduziere [this artikel anzahl]
    (let [zeile-mit-artikel (finde-zeile-zu-artikel this (:id artikel))]
      (when zeile-mit-artikel
        (let [neue-warenkorbzeilen (if (> (warenkorbzeile/get-anzahl zeile-mit-artikel) anzahl)
                                     (conj (:warenkorbzeilen this)
                                           (warenkorbzeile/reduziere-anzahl zeile-mit-artikel anzahl))
                                     (remove #(= artikel (:id %)) (:warenkorbzeilen this)))
              neuer-gesamtpreis (preis/reduziere-um (:gesamt-preis this) gesamtpreis-von-artikel)]
          (->Warenkorb (UUID/randomUUID) (:kunde-id this) neue-warenkorbzeilen
                       (preis/erstelle-preis neuer-gesamtpreis "EUR") (:max-einkaufswert this))))
      (validiere-warenkorb this))

    (entferne [this artikel]
              (let [zeile-mit-artikel (finde-zeile-zu-artikel this (:id artikel))]
                (when zeile-mit-artikel
                  (let [gesamtpreis-von-artikel (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)
                        neue-warenkorbzeilen (remove #(= artikel (:id %)) (:warenkorbzeilen this))
                        neuer-gesamtpreis (preis/reduziere-um (:gesamt-preis this) gesamtpreis-von-artikel)]
                    (->Warenkorb (:id this)
                                 (:kunde-id this)
                                 neue-warenkorbzeilen
                                 neuer-gesamtpreis
                                 (:max-einkaufswert this))))
                (validiere-warenkorb this)))))


(defn erstelle-warenkorb [kunde-id max-einkaufswert]
  (let [neuer-warenkorb (->Warenkorb (warenkorb-id/erstelleWarenkorbID)
                                     (KundeID kunde-id)
                                     []
                                     (preis/erstelle-preis "0.00" "EUR")
                                     (preis/erstelle-preis max-einkaufswert "EUR"))]
    (validiere-warenkorb neuer-warenkorb)
    neuer-warenkorb))

(defn erstelle-neuen-warenkorb [warenkorb neue-warenkorbzeilen neuer-gesamtpreis]
  (->Warenkorb (:id warenkorb)
               (:kunde-id warenkorb)
               neue-warenkorbzeilen
               neuer-gesamtpreis
               (:max-einkaufswert warenkorb)))

