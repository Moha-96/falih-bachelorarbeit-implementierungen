(ns de.hhu.rich.warenkorb.application.ApplicationService
  (:require [de.hhu.rich.warenkorb.domain.model.anzahl.Anzahl :as anzahl]
            [de.hhu.rich.warenkorb.domain.model.artikel.ArtikelRepository :as artikel-repository]
            [de.hhu.rich.warenkorb.domain.model.preis.Preis :as preis]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.Warenkorb :as warenkorb]
            [de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbRepository :as warenkorb-repository]))

(defn erstelle-warenkorb-fuer-kunde [kunde-id preis]
  (let [max-einkaufswert (preis/erstelle-preis preis "EUR")
        warenkorb (warenkorb/erstelle-warenkorb kunde-id max-einkaufswert)]
    (warenkorb-repository/add! warenkorb)))

(defn get-warenkorb [warenkorb-id]
  (warenkorb-repository/find warenkorb-id))

(defn get-artikel [artikel-id]
  (artikel-repository/find artikel-id))

(defn lege-artikel-in-den-warenkorb [artikel-id anzahl warenkorb-id]
  (let [warenkorb (get-warenkorb warenkorb-id)
        artikel (get-artikel artikel-id)]
    (warenkorb/fuege-hinzu warenkorb artikel (anzahl/erstelle-anzahl anzahl))
    (warenkorb-repository/update! warenkorb)))

(defn entferne-artikel-aus-dem-warenkorb [artikel-id warenkorb-id]
  (let [warenkorb (get-warenkorb warenkorb-id)
        artikel (get-artikel artikel-id)]
    (warenkorb/entferne warenkorb artikel)
    (warenkorb-repository/update! warenkorb)))

(defn reduziere-anzahl-von-artikel-in-dem-warenkorb [artikel-id anzahl warenkorb-id]
  (let [warenkorb (get-warenkorb warenkorb-id)
        artikel (get-artikel artikel-id)]
    (warenkorb/reduziere warenkorb artikel (anzahl/erstelle-anzahl anzahl))
    (warenkorb-repository/update! warenkorb)))

