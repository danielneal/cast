#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.3.1"

(set-env!
  :project      'cast
  :version      "0.1.1"
  :dependencies '[[tailrecursion/boot.task   "2.1.3"]
                  [tailrecursion/hoplon      "5.10.6"]
                  [org.clojure/clojurescript "0.0-2227"]
                  [tailrecursion/boot.ring   "0.1.0"]
                  [tailrecursion/castra "1.2.0"]

                  [com.datomic/datomic-free "0.9.4766"]
                  [datascript "0.1.5"]
                  [org.clojure/algo.generic "0.1.0"]
                  [clj-http "0.9.2"]
                  [org.clojure/core.match "0.2.1"]
                  [digest "1.4.4"]]

  :out-path     "resources/public"
  :src-paths    #{"src/hl" "src/cljs" "src/clj" "src/semantic"})

;; Static resources (css, images, etc.):
(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all]
         '[tailrecursion.castra.task :as c])

(def nrepl-server (atom nil))

(deftask repl-light
  "Launch nrepl in the project."
  []
  (set-env! :dependencies
  '[[lein-light-nrepl "0.0.13"]
    [org.clojure/tools.nrepl "0.2.3"]
    [org.clojure/clojure "1.5.1"]])
  (fn [continue]
    (fn [event]
      (require 'clojure.tools.nrepl.server)
      (require 'lighttable.nrepl.handler)
      (let [start-server (resolve 'clojure.tools.nrepl.server/start-server)
            default-handler (resolve 'clojure.tools.nrepl.server/default-handler)
            lighttable-ops (resolve 'lighttable.nrepl.handler/lighttable-ops)]
        (swap! nrepl-server #(or % (start-server
                                    :port 0
                                    :handler (default-handler lighttable-ops))))
        (println "nrepl running on " (:port @nrepl-server))
        (continue event)
        @(promise)))))

(deftask development
  "Build cast for development."
  []
  (comp (repl-light) (watch) (hoplon {:prerender false :pretty-print true :source-map true}) (c/castra-dev-server 'cast.api)))

(deftask production
  "Build cast for production. Include display name and port so easier to kill the process"
  [display-name port]
  (comp (fn [continue] (fn [event] (continue event) @(promise))) (c/castra-dev-server 'cast.api :port port) (hoplon {:optimizations :advanced})))

