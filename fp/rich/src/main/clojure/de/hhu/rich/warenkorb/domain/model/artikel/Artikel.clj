(ns de.hhu.rich.warenkorb.domain.model.artikel.Artikel
  (:require [de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID :as artikelID]
            [de.hhu.rich.warenkorb.domain.model.preis.Preis :as preis]))

(defprotocol IArtikel
  (get-id [this])
  (get-name [this])
  (get-beschreibung [this])
  (get-preis [this])
  (set-name [this new-name])
  (set-beschreibung [this new-beschreibung])
  (set-preis [this new-preis]))

(defrecord Artikel [id name beschreibung preis]
  IArtikel
  (get-id [_] id)
  (get-name [_] name)
  (get-beschreibung [_] beschreibung)
  (get-preis [_] preis)
  (set-name [this new-name]
    (if (or (nil? new-name) (empty? (clojure.string/trim new-name)))
      (throw (IllegalArgumentException. "Neuer Name darf nicht null oder leer sein."))
      (assoc this :name new-name)))
  (set-beschreibung [this new-beschreibung]
    (if (or (nil? new-beschreibung) (empty? (clojure.string/trim new-beschreibung)))
      (throw (IllegalArgumentException. "Neue Beschreibung darf nicht null oder leer sein."))
      (assoc this :beschreibung new-beschreibung)))
  (set-preis [this new-preis]
    (if (or (nil? new-preis) (< (.compareTo (:betrag new-preis) BigDecimal/ZERO) 0))
      (throw (IllegalArgumentException. "Neuer Preis darf nicht null sein und muss nicht-negativ sein."))
      (assoc this :preis new-preis))))

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
