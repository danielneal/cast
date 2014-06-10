(ns cast.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [cast.db :as cast-db]
            [datomic.api :as d]))

(defrpc get-entities []
  (let [db (d/db cast-db/conn)
        attributes [:feature/title :user/name :vote/user]]
    (mapcat #(cast-db/load-entities db ((cast-db/all-with-attribute %) db)) attributes)))




