(ns de.hhu.rich.warenkorb.domain.model.artikel.ArtikelRepository
  (:refer-clojure :exclude [find]))

(def find nil)
(def add! nil)
(def update! nil)

(defprotocol ArtikelRepository
  (-find [this artikel-id])
  (-add! [this artikel])
  (-update! [this concurrency-version artikel]))

(defn set-implementation! [impl]
  (def find (partial -find impl))
  (def add! (partial -add! impl))
  (def update! (partial -update! impl)))
