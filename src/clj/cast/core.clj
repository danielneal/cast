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
   [clojure.core.match :refer [match]]
   [datomic.api :as d]
   [cast.db :as cdb]
   [cast.authenticate :as authenticate]
   [clojure.tools.nrepl.server :as nrepl]
   [lighttable.nrepl.handler :as light]))


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
             (let [db (d/db cdb/conn)]
               (match [ev-id ?ev-data]

                      [:authentication/login ([username password] :seq)]
                      (let [u (cdb/user-with-name db username)
                            e (cdb/all-visible-entities db u)
                            ch (cdb/updates)]

                        (go-loop []
                                 (when-let [stmts (<! ch)]
                                   (chsk-send! nil [:db/updates stmts])
                                   (recur)))

                        (?reply-fn {:user-id u
                                    :entities e}))

                      [:vote/up ([feature user] :seq)]
                      (cdb/up-vote db feature user)

                      [:vote/down ([feature user] :seq)]
                      (cdb/down-vote db feature user)

                      :else
                      (println "Command not found for " ev-id ?ev-data)))
             (recur))))

; ----------------------
; Custom boot tasks
; ----------------------

(deftask start-server
  "Starts the app"
  [{:keys [port join? key docroot dev?]
      :or {port    8000
           join?   false
           dev? true
           key     "a 16-byte secret"
           docroot (get-env :out-path)}}]
  (comp (r/head) (if dev? (r/dev-mode) identity) (if dev? (r/cors #".*localhost.*") (r/cors #".*danielneal.*"))
        (r/session-cookie key) (r/files docroot) (if dev? (r/reload) identity)
        (boot/with-pre-wrap
          (swap! r/server #(or % (-> (@r/middleware websocket-routes)
                                     (httpkit/run-server {:port port :join? join?}))))
          (process-events ch-chsk)
          @(promise))))

(def nrepl-server (atom nil))

(deftask repl-light
  "Launch a lighttable nrepl in the project."
  []
  (boot/with-pre-wrap
    (swap! nrepl-server #(or % (nrepl/start-server
                                :port 0
                                :handler (nrepl/default-handler light/lighttable-ops))))
    (println "nrepl server running on " (:port @nrepl-server))))
