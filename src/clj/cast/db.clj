(ns cast.db
  (:require
   [datomic.api :as d :refer [q]]))

;; -------------------------------
;;  Database Setup
;; -------------------------------

(defn init! [uri]
  (let [schema [{:db/id (d/tempid :db.part/db)
                 :db/ident :feature/title
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/doc "The title of a feature"
                 :db.install/_attribute :db.part/db}

                {:db/id (d/tempid :db.part/db)
                 :db/ident :feature/description
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/doc "The description of a feature"
                 :db.install/_attribute :db.part/db}

                {:db/id (d/tempid :db.part/db)
                 :db/ident :feature/votes
                 :db/valueType :db.type/long
                 :db/cardinality :db.cardinality/one
                 :db/doc "The name of votes the feature has received"
                 :db.install/_attribute :db.part/db}

                {:db/id (d/tempid :db.part/db)
                 :db/ident :feature/id
                 :db/unique :db.unique/identity
                 :db/valueType :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/doc "A unique id for a feature"
                 :db.install/_attribute :db.part/db}]
        conn (d/connect uri)]
    (d/create-database uri)
    (d/transact conn schema)
    conn))

(def conn (init! "datomic:mem://cast"))


;; -------------------------------
;;  Queries
;; -------------------------------

(defn features-all
  "Returns all the features in the given database"
  [db]
  (->> (q '[:find ?e
            :in $
            :where [?e :feature/id ?f]] db)
       (map #(d/touch (d/entity db (first %))))
       (into [])))

