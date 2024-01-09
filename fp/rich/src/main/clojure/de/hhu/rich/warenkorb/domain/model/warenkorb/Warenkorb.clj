(ns de.hhu.rich.warenkorb.domain.model.warenkorb.Warenkorb
  (:require [de.hhu.rich.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.rich.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbID :as warenkorb-id]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile :as warenkorbzeile])
  (:import (de.hhu.rich.warenkorb.domain.model.kunde.KundeID KundeID)))

(defprotocol IWarenkorb
  (finde-zeile-zu-artikel [this artikel-id]
    "Findet die Warenkorbzeile für einen Artikel.")
  (fuege-hinzu [this artikel anzahl]
    "Fügt einen Artikel mit einer bestimmten Anzahl zum Warenkorb hinzu.")
  (reduziere [this artikel anzahl]
    "Reduziert die Anzahl eines Artikels im Warenkorb.")
  (entferne [this artikel]
    "Entfernt einen Artikel komplett aus dem Warenkorb.")
  (validiere-warenkorb [this]))

(defrecord Warenkorb [id kunde-id warenkorbzeilen gesamt-preis max-einkaufswert]
  IWarenkorb
  (finde-zeile-zu-artikel [this artikel-id]
    (first (filter #(= artikel-id (:artikel-id %)) (:warenkorbzeilen this))))

  (fuege-hinzu [this artikel anzahl]
    (let [zeile-mit-artikel (finde-zeile-zu-artikel this (:id artikel))
          gesamtpreis-von-artikel (if zeile-mit-artikel
                                    (warenkorbzeile/berechne-gesamtpreis zeile-mit-artikel)
                                    (preis/erstelle-preis (:preis artikel) "EUR"))
          neue-warenkorbzeilen (conj (:warenkorbzeilen this)
                                     (warenkorbzeile/erstelle-warenkorbzeile (:id artikel) anzahl
                                                       (:preis artikel) (Anzahl. 10)))
          neuer-gesamtpreis (preis/erhoehe-um (:gesamt-preis this) gesamtpreis-von-artikel)]
      (first (:kunde-id this) neue-warenkorbzeilen
                   (preis/erstelle-preis neuer-gesamtpreis "EUR") (:max-einkaufswert this))))

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
                  (let [gesamtpreis-von-artikel (.berechne-gesamtpreis zeile-mit-artikel)
                        neue-warenkorbzeilen (remove #(= artikel (:id %)) (:warenkorbzeilen this))
                        neuer-gesamtpreis (.reduziere-um (:gesamt-preis this) gesamtpreis-von-artikel)]
                    (->Warenkorb (UUID/randomUUID) (:kunde-id this) neue-warenkorbzeilen
                                 (preis/erstelle-preis neuer-gesamtpreis "EUR") (:max-einkaufswert this))))
                (validiere-warenkorb this)))

