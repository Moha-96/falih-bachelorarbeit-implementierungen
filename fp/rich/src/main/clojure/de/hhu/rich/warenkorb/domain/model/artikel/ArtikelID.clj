(ns de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID
  (:import (java.util UUID)))

(defrecord ArtikelID [id])

(defn erstelleArtikelID []
  (ArtikelID (UUID/randomUUID)))
