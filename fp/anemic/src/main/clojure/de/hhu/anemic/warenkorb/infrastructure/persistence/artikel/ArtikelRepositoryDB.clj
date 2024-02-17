(ns de.hhu.anemic.warenkorb.infrastructure.persistence.artikel.ArtikelRepositoryDB
  (:refer-clojure :exclude [find])
  (:require [de.hhu.anemic.warenkorb.domain.model.artikel.Artikel :refer [ArtikelRepository -find -add! -update!]]
            [de.hhu.anemic.warenkorb.domain.model.artikel.Artikel :refer [map->Artikel set-artikel-id]]
            [clojure.set :refer [rename-keys]]
            [clojure.java.jdbc :as db]))

(defn row->artikel [row]
  (-> row
      (select-keys [:id :kunde-id :warenkorbzeilen :gesamt-preis :max-einkaufswert])
      (rename-keys {:id :artikel-id})
      map->Artikel))

(defn artikel->row [artikel version]
  (-> Artikel
      (select-keys [:size :voyage-id])
      (rename-keys {:voyage-id :voyage_id})
      (assoc :version version)))

(defn find [mysql-config artikel-id]
  (let [row (-> (db/query mysql-config ["select * from warenkorbes where id=?" warenkorb-id])
                first)]
    (when row
      {:version (:version row) :warenkorb (row->warenkorb row)})))

(defn add! [mysql-config warenkorb]
  {:pre [(nil? (:warenkorb-id warenkorb))]}
  (let [warenkorb-id (-> (db/insert! mysql-config :warenkorbes (warenkorb->row warenkorb 1))
                         first
                         :generated_key)]
    (set-warenkorb-id warenkorb warenkorb-id)))

(defn update! [mysql-config version {:keys [warenkorb-id] :as warenkorb}]
  {:pre [warenkorb-id]}
  (let [changed-rows (first (db/update! mysql-config :warenkorbes
                                        (warenkorb->row warenkorb (inc version))
                                        ["id=? and version=?" warenkorb-id version]))]
    (if (= 0 changed-rows)
      (throw (ex-info "row has been modified"
                      {:exception-type :optimistic-concurrency}))
      nil)))

(defn new-warenkorb-repository [mysql-config]
  (reify WarenkorbRepository
    (-find [this warenkorb-id] (find mysql-config warenkorb-id))
    (-add! [this warenkorb] (add! mysql-config warenkorb))
    (-update! [this concurrency-version warenkorb] (update! mysql-config concurrency-version warenkorb))))