#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.3.1"

(set-env!
  :project      'cast
  :version      "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.task   "2.1.3"]
                  [tailrecursion/hoplon      "5.10.6"]
                  [org.clojure/clojurescript "0.0-2227"]
                  [tailrecursion/boot.ring   "0.2.1"]

                  [com.datomic/datomic-free "0.9.4766"]
                  [datascript "0.1.5"]
                  [org.clojure/algo.generic "0.1.0"]
                  [clj-http "0.9.2"]
                  [org.clojure/core.match "0.2.1"]
                  [digest "1.4.4"]
                  [com.taoensso/sente "0.14.1"]
                  [compojure "1.1.8"]
                  [http-kit "2.1.16"]]

  :out-path     "resources/public"
  :src-paths    #{"src/hl" "src/cljs" "src/clj" "src/semantic"})

;; Static resources (css, images, etc.):
(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all])
(require '[cast.core :as core])

(deftask development
  "Build cast for development."
  []
  (comp (watch) (hoplon {:prerender false :source-map true}) (core/repl-light) (core/start-server)))

(deftask production
  "Build cast for production."
  []
  (hoplon {:optimizations :advanced}))


