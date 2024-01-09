(ns de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile
  (:require [de.hhu.rich.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.rich.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID :as warenkorbzeileID])
  (:import (de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID ArtikelID)))

(defprotocol IWarenkorbzeile
  (get-id [this])
  (get-artikel-id [this])
  (get-anzahl [this])
  (get-preis [this])
  (get-max-artikel-anzahl [this])
  (erhoehe-anzahl [this n])
  (reduziere-anzahl [this n])
  (berechne-gesamtpreis [this]))

(defrecord Warenkorbzeile [id artikel-id anzahl preis max-artikel-anzahl]
  IWarenkorbzeile
  (get-id [_] id)
  (get-artikel-id [_] artikel-id)
  (get-anzahl [_] anzahl)
  (get-preis [_] preis)
  (get-max-artikel-anzahl [_] max-artikel-anzahl)

  (erhoehe-anzahl [this n]
    (if (or (nil? n) (< (.anzahl n) 0))
      (throw (IllegalArgumentException. "Die Erhöhung muss größer als 0 sein"))
      (assoc this :anzahl (anzahl/erhoehe-um anzahl n))))

  (reduziere-anzahl [this n]
    (if (or (nil? n) (< (.anzahl n) 0))
      (throw (IllegalArgumentException. "Die Reduzierung muss größer als 0 sein"))
      (assoc this :anzahl (anzahl/reduziere-um anzahl n))))

  (berechne-gesamtpreis [this]
    (let [gesamt-betrag (* (:betrag (:preis this)) anzahl)]
      (preis/erstelle-preis (bigdec gesamt-betrag) (:waehrung (:preis this))))))

(defn validiere-warenkorbzeile [warenkorbzeile]
  (when (or (nil? (:id warenkorbzeile))
            (nil? (:artikel-id warenkorbzeile))
            (nil? (:anzahl warenkorbzeile)) (< (.anzahl (:anzahl warenkorbzeile)) 0)
            (nil? (:preis warenkorbzeile))
            (nil? (:max-artikel-anzahl warenkorbzeile))
            (anzahl/ist-groesser-als? (:anzahl warenkorbzeile) (:max-artikel-anzahl warenkorbzeile)))
    (throw (IllegalArgumentException. "Fehlerhafte Warenkorbzeile"))))

(defn erstelle-warenkorbzeile [artikel-id anzahl preis max-artikel-anzahl]
  (let [neue-warenkorbzeile (->Warenkorbzeile (warenkorbzeileID/erstelle-WarenkorbzeileID)
                                              (ArtikelID artikel-id)
                                              (anzahl/erstelle-anzahl anzahl)
                                              (preis/erstelle-preis preis "EUR")
                                              (anzahl/erstelle-anzahl max-artikel-anzahl))]
    (validiere-warenkorbzeile neue-warenkorbzeile)
    neue-warenkorbzeile))