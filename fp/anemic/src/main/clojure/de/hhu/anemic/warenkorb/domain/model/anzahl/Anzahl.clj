(ns de.hhu.anemic.warenkorb.domain.model.anzahl.Anzahl)

(defrecord Anzahl [anzahl])

(defn erstelle-anzahl [anzahl]
  (let [instance (->Anzahl anzahl)]
    (if (< anzahl 0)
      (throw (IllegalArgumentException. "Die Anzahl darf nicht negativ sein."))
      instance)))

(defn erhoehe-um [anzahl-obj n]
  (if (< n 0)
    (throw (IllegalArgumentException. "Die Erhöhung muss positiv sein"))
    (erstelle-anzahl (+ (:anzahl anzahl-obj) n))))

(defn reduziere-um [anzahl-obj n]
  (if (< n 0)
    (throw (IllegalArgumentException. "Die Reduzierung muss positiv sein"))
    (erstelle-anzahl (- (:anzahl anzahl-obj) n))))

(defn ist-groesser-als? [anzahl-obj max-anzahl]
  (> (:anzahl anzahl-obj) max-anzahl))