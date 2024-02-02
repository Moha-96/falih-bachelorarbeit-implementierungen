(ns de.hhu.anemic.warenkorb.domain.model.artikel.Artikel
  (:require [de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID :as artikelID]
            [de.hhu.anemic.warenkorb.domain.model.preis.Preis :as preis]))

(defrecord Artikel [id name beschreibung preis])

(defn validiere-artikel [artikel]
  (when (or (nil? (:id artikel))
            (nil? (:name artikel)) (empty? (:name artikel))
            (nil? (:beschreibung artikel)) (empty? (:beschreibung artikel))
            (nil? (:preis artikel)))
    (throw (IllegalArgumentException. "Pflichtfelder fehlen im Artikel"))))

(defn erstelle-artikel [name beschreibung preis]
  (let [neuer-artikel (->Artikel (artikelID/erstelleArtikelID)
                                 name
                                 beschreibung
                                 (preis/erstelle-preis preis "EUR"))]
    (validiere-artikel neuer-artikel)
    neuer-artikel))

(defn setze-neuen-namen [artikel neuer-name]
  (if (or (nil? neuer-name) (empty? (clojure.string/trim neuer-name)))
    (throw (IllegalArgumentException. "Neuer Name darf nicht null oder leer sein."))
    (erstelle-artikel neuer-name (:beschreibung artikel) (:preis artikel))))

(defn setze-neuen-preis [artikel neuer-preis]
  (if (or (nil? neuer-preis) (< (.compareTo (:betrag neuer-preis) BigDecimal/ZERO) 0))
    (throw (IllegalArgumentException. "Neuer Preis darf nicht null sein und muss nicht-negativ sein."))
    (erstelle-artikel (:name artikel) (:beschreibung artikel) neuer-preis)))

(defn setze-neue-beschreibung [artikel neue-beschreibung]
  (if (or (nil? neue-beschreibung) (empty? (clojure.string/trim neue-beschreibung)))
    (throw (IllegalArgumentException. "Neue Beschreibung darf nicht null oder leer sein."))
    (erstelle-artikel (:name artikel) neue-beschreibung (:preis artikel))))
