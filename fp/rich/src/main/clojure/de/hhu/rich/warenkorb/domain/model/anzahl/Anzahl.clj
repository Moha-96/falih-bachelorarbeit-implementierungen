(ns de.hhu.rich.warenkorb.domain.model.anzahl.Anzahl)

(defprotocol IAnzahl
  (erhoehe-um [n]
    "Erhöht die Anzahl um n.")
  (reduziere-um [this n]
    "Reduziert die Anzahl um n.")
  (ist-groesser-als? [this max-anzahl]
    "Prüft, ob die Anzahl größer als max-anzahl ist."))

(defrecord Anzahl [anzahl]
  IAnzahl
  (erhoehe-um [n]
    (if (< n 0)
      (throw (IllegalArgumentException. "Die Erhöhung muss positiv sein"))
      (->Anzahl (+ anzahl n))))

  (reduziere-um [n]
    (if (< n 0)
      (throw (IllegalArgumentException. "Die Reduzierung muss positiv sein"))
      (->Anzahl (- anzahl n))))

  (ist-groesser-als? [max-anzahl]
    (> anzahl max-anzahl)))

(defn erstelle-anzahl [anzahl]
  (let [instance (->Anzahl anzahl)]
    (if (< anzahl 0)
      (throw (IllegalArgumentException. "Die Anzahl darf nicht negativ sein."))
      instance)))
