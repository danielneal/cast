(ns cast.db
  (:require
   [datomic.api :as d :refer [q]]))

;; -------------------------------
;;  Database Setup
;; -------------------------------

(def uri "datomic:mem://feature-list")
(d/create-database uri)

(def schema
  [{:db/id (d/tempid :db.part/db)
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
    :db.install/_attribute :db.part/db}])

(let [conn (d/connect uri)]
  (d/transact conn schema)) ;; FIX THIS: (-> "cast/schema.edn" clojure.java.io/resource slurp read-string)))

;; -------------------------------
;;  Database Helpers
;; -------------------------------

(defn latest
  "Returns the latest database"
  []
  (let [conn (d/connect uri)]
    (d/db conn)))

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

;; -------------------------------
;;  Commands
;; -------------------------------

(defn add-feature
  "Add a feature to the database"
  [feature]
  (let [feature (assoc feature
                  :feature/id (d/squuid)
                  :db/id (d/tempid :db.part/user))
        conn (d/connect uri)]
    (d/transact conn [(assoc feature :feature/id (d/squuid) :db/id (d/tempid :db.part/user))])))


