(ns de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile
  (:require [de.hhu.anemic.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.anemic.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID :as warenkorbzeileID])
  (:import (de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID ArtikelID)))

(defrecord Warenkorbzeile [id artikel-id anzahl preis max-artikel-anzahl])
(defn validiere-warenkorbzeile [warenkorbzeile]
  (let [anzahl (:anzahl warenkorbzeile)
        max-artikel-anzahl (:max-artikel-anzahl warenkorbzeile)]
    (when (or (nil? (:id warenkorbzeile))
              (nil? (:artikel-id warenkorbzeile))
              (nil? anzahl) (< (.anzahl anzahl) 0)
              (nil? (:preis warenkorbzeile))
              (nil? max-artikel-anzahl)
              (anzahl/ist-groesser-als? anzahl max-artikel-anzahl))
      (throw (IllegalArgumentException. "Fehlerhafte Warenkorbzeile")))))

(defn erstelle-warenkorbzeile [artikel-id anzahl preis max-artikel-anzahl]
  (let [neue-warenkorbzeile (->Warenkorbzeile (warenkorbzeileID/erstelle-WarenkorbzeileID)
                                              (ArtikelID artikel-id)
                                              (anzahl/erstelle-anzahl anzahl)
                                              (preis/erstelle-preis preis "EUR")
                                              (anzahl/erstelle-anzahl max-artikel-anzahl))]
    (validiere-warenkorbzeile neue-warenkorbzeile)
    neue-warenkorbzeile))

(defn erhoehe-um [warenkorbzeile anzahl]
  (validiere-warenkorbzeile (assoc warenkorbzeile :anzahl (anzahl/erhoehe-um (:anzahl warenkorbzeile) anzahl)))
  (assoc warenkorbzeile :anzahl (anzahl/erhoehe-um (:anzahl warenkorbzeile) anzahl)))

(defn reduziere-um [warenkorbzeile anzahl]
  (validiere-warenkorbzeile (assoc warenkorbzeile :anzahl (anzahl/reduziere-um (:anzahl warenkorbzeile) anzahl)))
  (assoc warenkorbzeile :anzahl (anzahl/reduziere-um (:anzahl warenkorbzeile) anzahl)))

(defn berechne-gesamtpreis [warenkorbzeile]
  (validiere-warenkorbzeile warenkorbzeile)
  (let [gesamt-betrag (* (:betrag (:preis warenkorbzeile)) (:anzahl warenkorbzeile))]
    (preis/erstelle-preis (bigdec gesamt-betrag) (:waehrung (:preis warenkorbzeile)))))

