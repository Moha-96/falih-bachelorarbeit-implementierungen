(ns de.hhu.anemic.warenkorb.domain.model.preis.Preis)

(defrecord Preis [betrag waehrung])

(defn erstelle-preis [betrag waehrung]
  (if (neg? (.compareTo betrag BigDecimal/ZERO))
    (throw (IllegalArgumentException. "Der Betrag darf nicht negativ sein"))
    (Preis. betrag waehrung)))

(defn erhoehe-um [preis betrag]
  (if (<= betrag BigDecimal/ZERO)
    (throw (IllegalArgumentException. "Die Erhöhung muss größer als 0 sein"))
    (erstelle-preis (+ (:betrag preis) betrag)
                    (:waehrung preis))))

(defn reduziere-um [preis betrag]
  (if (<= betrag BigDecimal/ZERO)
    (throw (IllegalArgumentException. "Die Reduzierung muss größer als 0 sein"))
    (erstelle-preis (- (:betrag preis) betrag)
                    (:waehrung preis))))

(defn ist-groesser-als? [preis max-preis]
  (> (.compareTo (:betrag preis) (:betrag max-preis)) 0))