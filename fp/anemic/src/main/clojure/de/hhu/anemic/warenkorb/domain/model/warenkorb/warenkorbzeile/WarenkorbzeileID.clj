(ns de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID
  (:import (java.util UUID)))

(defrecord WarenkorbzeileID [id])

(defn erstelle-WarenkorbzeileID []
  (WarenkorbzeileID (UUID/randomUUID)))
