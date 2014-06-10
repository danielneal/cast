(ns cast.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin]
   [tailrecursion.castra :refer [mkremote]]))

(defc entities nil)
(defc error nil)
(defc loading [])

(def get-entities
  (mkremote 'cast.api/get-entities entities error loading))
