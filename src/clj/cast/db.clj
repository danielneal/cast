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

(def seed-data
  [{:db/id (d/tempid :db.part/user -1)
    :page/name "Ida"}
   {:db/id (d/tempid :db.part/user -2)
    :page/name "SugarCRM"}
   {:db/id (d/tempid :db.part/user -3)
    :feature/title "Ida feature 1"
    :feature/description "A descriptions"
    :feature/page (d/tempid :db.part/user -1)}
   {:db/id (d/tempid :db.part/user -4)
    :feature/title "SugarCRm feature 2 "
    :feature/description "Another description"
    :feature/page (d/tempid :db.part/user -2)}
   {:db/id (d/tempid :db.part/user -5)
    :feature/title "Ida feature2 "
    :feature/description "Another description"
    :feature/page (d/tempid :db.part/user -1)}
   {:db/id (d/tempid :db.part/user -6)
    :user/name "Daniel"
    :user/max-votes 10}
   {:db/id (d/tempid :db.part/user -7)
    :user/name "Bob"
    :user/max-votes 10}])

(def uri "datomic:mem://cast")

(defn init! []
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (d/transact conn schema)
    (d/transact conn seed-data)
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
  (map (comp (partial fmap #(if (instance? datomic.query.EntityMap %) (:db/id %) %)) #(into {:db/id (:db/id %)} %) d/touch (partial d/entity db) #(if (vector? %) (first %) %)) entity-ids))

; -------------------------------
; Queries
; -------------------------------

(defn all-with-attribute [db attr]
    (d/q '[:find ?e
           :in $ ?a
           :where [?e ?a ?v]] db attr))

(defn ref? [db attr]
  (ffirst (d/q '[:find ?a
                 :in $ ?a
                 :where [?a :db/valueType :db.type/ref]] db attr)))

