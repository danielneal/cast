(ns cast.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [cast.db :as cast-db]
            [datomic.api :as d]))

(defrpc get-entities []
  (let [db (d/db cast-db/conn)
        attributes [:feature/title :user/name :vote/user :page/name]]
    (mapcat #(cast-db/load-entities db (cast-db/all-with-attribute db %)) attributes)))

(defrpc up-vote [feature user]
  (d/transact cast-db/conn [{:db/id (d/tempid :db.part/user) :vote/feature feature :vote/user user}])
  (get-entities))

(defrpc down-vote [feature user]
  (when-let [v (ffirst (d/q '[:find ?v
                        :in $ ?f ?u
                        :where
                        [?v :vote/feature ?f]
                        [?v :vote/user ?u]]
                      (d/db cast-db/conn) feature user))]
    (d/transact cast-db/conn [[:db.fn/retractEntity v]]))
  (get-entities))

(defrpc add-feature [title description]
  (get-entities))

(defrpc login [username password]
  (get-entities))

(defrpc logout [username]
  (get-entities))
