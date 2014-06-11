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

