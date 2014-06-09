(ns cast.api
  (:require [tailrecursion.castra :refer [defrpc]]
            [cast.db :as db]))

(defrpc get-state [])
