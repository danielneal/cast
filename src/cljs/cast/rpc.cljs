(ns cast.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin]
   [tailrecursion.castra :refer [mkremote]]))

(defc state {:features nil})
(defc error nil)
(defc loading [])

(def get-state
  (mkremote 'cast.api/get-state state error loading))

(def add-feature
  (mkremote 'cast.api/add-feature state error loading))

(defn init []
  (get-state)
  (js/setInterval get-state 2000))
