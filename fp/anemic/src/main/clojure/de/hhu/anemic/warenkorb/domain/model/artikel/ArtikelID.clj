(ns de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID
  (:import (java.util UUID)))

(defrecord ArtikelID [id])

(defn erstelleArtikelID []
  (ArtikelID (UUID/randomUUID)))
