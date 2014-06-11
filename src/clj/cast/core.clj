;; Copyright (c) Alan Dipert and Micha Niskin. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns cast.core
  (:require
   [ring.adapter.jetty               :refer [run-jetty]]
   [ring.middleware.resource         :refer [wrap-resource]]
   [ring.middleware.session          :refer [wrap-session]]
   [ring.middleware.session.cookie   :refer [cookie-store]]
   [ring.middleware.file             :refer [wrap-file]]
   [ring.middleware.file-info        :refer [wrap-file-info]]
   [tailrecursion.castra.handler     :refer [castra]]
   [cast.db :as db]
   [clojure.tools.nrepl.server :as nrepl]
   [lighttable.nrepl.handler/lighttable-ops :as light]))

(def server (atom nil))
(def nrepl-server (atom nil))

(defn app [port public-path]
  (->
    (castra 'cast.api)
    (wrap-session {:store (cookie-store {:key "a 16-byte secret"})})
    (wrap-file public-path)
    (wrap-file-info)
    (run-jetty {:join? false :port port})))

(defn start-nrepl-server [port]
  (swap! nrepl-server #(or % (nrepl/start-server :port port (nrepl/default-handler light/lighttable-ops)))))

(defn start-server
  "Start castra demo server (port 33333)."
  [port public-path]
  (swap! server #(or % (app port public-path))))

(defn run-task
  [port public-path]
  (.mkdirs (java.io.File. public-path))
  (start-server port public-path)
  (start-nrepl-sever 50000)
  (fn [continue]
    (fn [event]
      (continue event))))

(defn -main
  "I don't do a whole lot."
  [& args])
