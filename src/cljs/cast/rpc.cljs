(ns cast.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]])
  (:require
   [tailrecursion.javelin]
   [tailrecursion.castra :refer [mkremote]]))

(defc entities nil)
(defc error nil)
(defc loading [])

(def command
  {:get-entities (mkremote 'cast.api/get-entities entities error loading)
   :up-vote (mkremote 'cast.api/up-vote entities error loading)
   :down-vote (mkremote 'cast.api/down-vote entities error loading)
   :add-feature (mkremote 'cast.api/add-feature entities error loading)
   :login (mkremote 'cast.api/login entities error loading)
   :logout (mkremote 'cast.api/logout entities error loading)})
