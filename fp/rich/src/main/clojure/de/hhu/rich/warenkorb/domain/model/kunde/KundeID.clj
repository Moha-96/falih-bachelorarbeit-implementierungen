(ns de.hhu.rich.warenkorb.domain.model.kunde.KundeID
  (:import (java.util UUID)))

(defrecord KundeID [id])

(defn erstelle-KundeID []
  (KundeID (UUID/randomUUID)))