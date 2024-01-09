(ns de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbID
  (:import (java.util UUID)))

(defrecord WarenkorbID [id])

(defn erstelleWarenkorbID []
  (WarenkorbID (UUID/randomUUID)))
