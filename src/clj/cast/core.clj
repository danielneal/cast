;; Copyright (c) Alan Dipert and Micha Niskin. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns cast.core
  (:require
   [taoensso.sente :as sente]
   [compojure.route :as route]
   [org.httpkit.server :as httpkit]
   [compojure.core :refer [GET POST defroutes]]
   [tailrecursion.boot.core :as boot :refer [deftask get-env set-env!]]
   [tailrecursion.boot.task.ring :as r]
   [clojure.core.async :as async :refer [<! >! go go-loop put!]]
   [clojure.core.match :refer [match]]))

; ----------------------
; Websocket setup
; ----------------------

(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! {})]
  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids)) ; Watchable, read-only atom

(defroutes websocket-routes
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post                req)))

; -----------------------
; Commands
; -----------------------

(defn process-events
  [ch]
  (go-loop []
           (when-let [{[ev-id ?ev-data] :event ?reply-fn :?reply-fn} (<! ch)]
             (match [ev-id ?ev-data]
                    [:cast/login ([username password] :seq)] (do (println username password) (?reply-fn 1))

                    :else
                    (println "Command not found for " ev-id ?ev-data))
             (recur))))


; ----------------------
; Custom boot tasks
; ----------------------


(deftask start-server
  "Starts the app"
  [& {:keys [port join? key docroot]
      :or {port    8000
           join?   false
           key     "a 16-byte secret"
           docroot (get-env :out-path)}}]
  (comp (r/head) (r/dev-mode) (r/cors #".*localhost.*")
        (r/session-cookie key) (r/files docroot) (r/reload)
        (boot/with-pre-wrap
          (swap! r/server #(or % (-> (@r/middleware websocket-routes)
                                     (httpkit/run-server {:port port :join? join?}))))
          (process-events ch-chsk))))

(def nrepl-server (atom nil))

(deftask repl-light
  "Launch a lighttable nrepl in the project."
  []
  (set-env! :dependencies
            '[[lein-light-nrepl "0.0.13"]
              [org.clojure/tools.nrepl "0.2.3"]])
  (boot/with-pre-wrap
    (require 'clojure.tools.nrepl.server)
    (require 'lighttable.nrepl.handler)
    (let [start-server (resolve 'clojure.tools.nrepl.server/start-server)
          default-handler (resolve 'clojure.tools.nrepl.server/default-handler)
          lighttable-ops (resolve 'lighttable.nrepl.handler/lighttable-ops)]
      (swap! nrepl-server #(or % (start-server
                                  :port 0
                                  :handler (default-handler lighttable-ops))))
      (println "nrepl server running on " (:port @nrepl-server)))))

