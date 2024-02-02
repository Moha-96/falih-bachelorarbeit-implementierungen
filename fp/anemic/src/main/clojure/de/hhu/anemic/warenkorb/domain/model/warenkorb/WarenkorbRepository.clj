(ns de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbRepository
  (:refer-clojure :exclude [find]))

(def find nil)
(def add! nil)
(def update! nil)

(defprotocol WarenkorbRepository
  (-find [this warenkorb-id])
  (-add! [this warenkorb])
  (-update! [this concurrency-version warenkorb]))

(defn set-implementation! [impl]
  (def find (partial -find impl))
  (def add! (partial -add! impl))
  (def update! (partial -update! impl)))