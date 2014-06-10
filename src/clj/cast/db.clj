(ns cast.db
  (:require [datomic.api :as d]))

; -------------------------------
;  Database Setup
; -------------------------------

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
    :db/doc "The name of the user"
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id (d/tempid :db.part/db)
    :db/ident :user/max-votes
    :db/doc "The maximum number of votes this user can have"
    :db/valueType :db.type/long
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
  [db entities]
  (map (comp (partial into {}) d/touch (partial d/entity db) #(if (vector? %) (first %) %)) entities))

; -------------------------------
; Queries
; -------------------------------

(defn all-with-attribute [attr]
  (fn [db]
    (d/q '[:find ?e
           :in $ ?a
           :where [?e ?a ?v]] db attr)))

(d/transact conn [{:db/id (d/tempid :db.part/user) :feature/title "Test" :feature/description "description"}
                  {:db/id (d/tempid :db.part/user) :feature/title "Another" :feature/description "feature"}
                  {:db/id (d/tempid :db.part/user) :user/name "Daniel" :user/max-votes 10}
                  {:db/id (d/tempid :db.part/user) :user/name "Henry" :user/max-votes 10}])

