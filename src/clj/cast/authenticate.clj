(ns cast.authenticate
  (:require [clj-http.client :as client]
            [clojure.edn :as edn]
            [digest :as digest]
            [tailrecursion.cljson :refer [clj->cljson cljson->clj]]))

(def authentication (edn/read-string (slurp (clojure.java.io/resource "cast/config.edn"))))

(defn login-sugarcrm [{:keys [username password url]}]
  (let [params {:user_auth {:user_name username
                            :password (digest/md5 password)
                            :version 1}
                :application "Cast"}]
    (some-> (client/post url {:query-params {:method "login" :input_type "json" :response_type "json" :rest_data (client/json-encode params)}})
            (get :body)
            (client/json-decode)
            (get "id"))))



