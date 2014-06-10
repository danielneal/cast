(ns cast.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [cast.db :as cast-db]
            [datomic.api :as d]))

(defrpc get-entities []
  (let [db (d/db cast-db/conn)
        attributes [:feature/title :user/name :vote/user]]
    (mapcat #(cast-db/load-entities db (cast-db/all-with-attribute db %)) attributes)))

(defrpc transact! [tx-data]
  (d/transact cast-db/conn (map-indexed
                            #(let [id (:db/id %2)]
                               (if (< (or id 0) 0)
                                 (assoc  %2 :db/id (d/tempid :db.part/user (- 0 (inc %1))))
                                 %2)) tx-data))
  (get-entities))

