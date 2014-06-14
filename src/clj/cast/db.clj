(ns cast.db
  (:require [datomic.api :as d]
            [clojure.algo.generic.functor :refer [fmap]]))

; -------------------------------
;  Database Setup
; -------------------------------

(def schema
  [{:db/id (d/tempid :db.part/db)
    :db/ident :feature/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "The title of a feature"
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :feature/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The description of a feature"
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :feature/page
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "The page a feature is on"
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :vote/user
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "The user casting the vote"
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :vote/feature
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "The feature being voted for casting the vote"
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :user/name
    :db/unique :db.unique/identity
    :db/doc "The name of the user"
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :user/max-votes
    :db/doc "The maximum number of votes this user can have"
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :page/name
    :db/unique :db.unique/identity
    :db/doc "The name for a page (of features)"
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}])

(def uri "datomic:mem://cast")

(defn init! []
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (d/transact conn schema)
    conn))

(def conn (init!))

; -------------------------------
; Helpers
; -------------------------------

(defn load-entities
  "Takes a vector of entity ids and realizes them.
  If each entity is a vector (as returned from datomic,
  then assume the first element is the entity id."
  [db entity-ids]
  (map #(as-> (d/entity db %) e
              (d/touch e)
              (into {:db/id (:db/id e)} e)
              (fmap (fn [v] (if (instance? datomic.query.EntityMap v) (:db/id v) v)) e))
       entity-ids))

(defn resolve-ids [tx-data]
  (clojure.walk/postwalk
   (fn [e] (if (and (number? e) (neg? e))
             (d/tempid :db.part/user e)
             e)) tx-data))

(defn transact [tx-data]
  (let [processed-tx-data (resolve-ids tx-data)]
    (d/transact conn processed-tx-data)))

; -------------------------------
; Seed data
; -------------------------------

(d/transact conn
            (resolve-ids [{:db/id -1
                          :page/name "Ida"}
                         {:db/id -2
                          :page/name "SugarCRM"}
                         {:db/id -3
                          :feature/title "Ida feature 1"
                          :feature/description "A descriptions"
                          :feature/page -1}
                         {:db/id -4
                          :feature/title "SugarCRm feature 2 "
                          :feature/description "Another description"
                          :feature/page -2}
                         {:db/id -5
                          :feature/title "Ida feature2 "
                          :feature/description "Another description"
                          :feature/page -1}
                         {:db/id -6
                          :user/name "Daniel"
                          :user/max-votes 10}
                         {:db/id -7
                          :user/name "Bob"
                          :user/max-votes 10}]))


; -------------------------------
; Queries
; -------------------------------

(defn features
  "Return the features that should be visible to the given user."
  [db user-id]
  (d/q '[:find ?f
         :in $
         :where
         [?f :feature/title _]] db))

(defn pages
  "Return the pages that should be visible to the given user."
  [db user-id]
  (d/q '[:find ?p
         :in $
         :where
         [?p :page/name _]] db))

(defn votes
  "Return the votes that should be visible to the given user."
  [db user-id]
  (d/q '[:find ?v
         :in $
         :where
         [?v :vote/feature _]] db))

(defn users
  "Return all the users that should ve visible to the given user"
  [db user-id]
  (d/q '[:find ?u
         :in $ ?u
         :where [?u :user/name _]] db user-id))

(defn user-with-name
  "Return the user with the given name"
  [db user-name]
  (ffirst (d/q '[:find ?u
                 :in $ ?n
                 :where [?u :user/name ?n]] db user-name)))

(defn all-visible-entities [db user-id]
  (->> [features pages votes users]
       (mapcat #(% db user-id))
       (map first)
       (load-entities db)))
