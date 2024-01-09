(ns de.hhu.rich.warenkorb.domain.model.preis.Preis)

(defprotocol IPreis
  (erhoehe-um [this betrag]
    "Erhöht den Preis um den angegebenen Betrag.")
  (reduziere-um [this betrag]
    "Reduziert den Preis um den angegebenen Betrag.")
  (ist-groesser-als? [this max-preis]
    "Prüft, ob der Preis größer als der angegebene Maximalpreis ist."))

(defrecord Preis [betrag waehrung]
  IPreis
  (erhoehe-um [this betrag]
    (if (<= betrag BigDecimal/ZERO)
      (throw (IllegalArgumentException. "Die Erhöhung muss größer als 0 sein"))
      (->Preis (+ betrag (:betrag this)) (:waehrung this))))

  (reduziere-um [this betrag]
    (if (<= betrag BigDecimal/ZERO)
      (throw (IllegalArgumentException. "Die Reduzierung muss größer als 0 sein"))
      (->Preis (- (:betrag this) betrag) (:waehrung this))))

  (ist-groesser-als? [this max-preis]
    (> (.compareTo (:betrag this) (:betrag max-preis)) 0)))

(defn erstelle-preis [betrag waehrung]
  (if (neg? (.compareTo betrag BigDecimal/ZERO))
    (throw (IllegalArgumentException. "Der Betrag darf nicht negativ sein"))
    (->Preis betrag waehrung)))
