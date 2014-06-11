(ns cast.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [cast.db :as cast-db]
            [datomic.api :as d]))

(defrpc get-entities []
  (let [db (d/db cast-db/conn)
        attributes [:feature/title :user/name :vote/user :page/name]]
    (mapcat #(cast-db/load-entities db (cast-db/all-with-attribute db %)) attributes)))

(defn resolve-ids [tx-data]
  (clojure.walk/postwalk (fn [e] (if (and (number? e) (neg? e))
                                    (d/tempid :db.part/user e)
                                   e)) tx-data))
(defrpc transact! [tx-data]
  (let [processed-tx-data (resolve-ids tx-data)]
    (d/transact cast-db/conn processed-tx-data)
    (get-entities)))
