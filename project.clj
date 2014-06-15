(defproject
 cast
 "0.1.0-SNAPSHOT"
 :dependencies
 [[org.clojure/clojure "1.5.1"]
  [tailrecursion/boot.core "2.3.1" :exclusions [[org.clojure/clojure]]]
  [clj-http
   "0.9.2"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [org.apache.httpcomponents/httpclient
   "4.3.3"
   :exclusions
   [[org.clojure/clojure]]]
  [org.apache.httpcomponents/httpcore
   "4.3.2"
   :exclusions
   [[org.clojure/clojure]]]
  [digest
   "1.4.4"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [org.clojure/clojurescript
   "0.0-2227"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/boot.task
   "2.1.3"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [http-kit
   "2.1.16"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/warp "0.1.0" :exclusions [[org.clojure/clojure]]]
  [slingshot "0.10.3" :exclusions [[org.clojure/clojure]]]
  [org.clojure/data.json "0.2.3"]
  [com.taoensso/sente
   "0.14.1"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [com.taoensso/timbre "3.2.1"]
  [com.taoensso/encore "1.6.0"]
  [org.clojure/tools.reader "0.8.3"]
  [datascript
   "0.1.5"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [com.datomic/datomic-free
   "0.9.4766"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [org.hornetq/hornetq-server "2.3.17.Final"]
  [org.slf4j/log4j-over-slf4j "1.7.5" :scope "runtime"]
  [com.google.guava/guava "16.0.1"]
  [org.slf4j/jcl-over-slf4j "1.7.5"]
  [org.slf4j/slf4j-nop "1.7.5"]
  [io.netty/netty "3.6.7.Final"]
  [org.hornetq/hornetq-commons "2.3.17.Final"]
  [com.amazonaws/aws-java-sdk
   "1.6.6"
   :exclusions
   [[javax.mail/mail]
    [org.apache.httpcomponents/httpclient]
    [commons-logging]]]
  [org.clojure/algo.generic
   "0.1.0"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [potemkin "0.3.4" :exclusions [[org.clojure/clojure]]]
  [compojure
   "1.1.8"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [clout "1.2.0"]
  [commons-logging "1.1.3"]
  [org.clojure/tools.macro "0.1.0"]
  [org.clojure/tools.nrepl
   "0.2.3"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [ring/ring-core "1.2.2"]
  [ring/ring-codec "1.0.0"]
  [org.apache.tomcat/tomcat-jdbc
   "7.0.27"
   :exclusions
   [[commons-logging]]]
  [org.clojure/core.async
   "0.1.303.0-886421-alpha"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [clj-time "0.4.4"]
  [commons-codec "1.9" :exclusions [[org.clojure/clojure]]]
  [com.fasterxml.jackson.core/jackson-databind "2.1.1"]
  [crouton "0.1.2" :exclusions [[org.clojure/clojure]]]
  [org.clojure/tools.analyzer.jvm "0.1.0-beta12"]
  [org.clojure/core.memoize "0.5.6"]
  [org.clojure/core.cache "0.6.3"]
  [org.ow2.asm/asm-all "4.1"]
  [com.datomic/datomic-lucene-core "3.3.0"]
  [lein-light-nrepl
   "0.0.13"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [clj-stacktrace "0.2.7"]
  [cheshire "5.2.0"]
  [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.2.1"]
  [com.fasterxml.jackson.core/jackson-core "2.2.1"]
  [commons-io "2.4"]
  [tailrecursion/boot.ring
   "0.2.1"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [ring "1.2.1"]
  [jumblerg/ring.middleware.cors "1.0.1"]
  [ring/ring-devel "1.2.1"]
  [ns-tracker "0.2.1"]
  [org.clojure/java.classpath "0.2.0"]
  [org.clojure/google-closure-library "0.0-20140226-71326067"]
  [tailrecursion/hoplon
   "5.10.6"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/castra "1.3.0"]
  [clj-tagsoup "0.3.0"]
  [org.clojure/data.xml "0.0.3"]
  [org.clojars.nathell/tagsoup "1.2.1"]
  [tailrecursion/extype "0.1.0"]
  [org.clojure/core.incubator "0.1.2"]
  [tailrecursion/cljson "1.0.6"]
  [ring/ring-jetty-adapter "1.2.1"]
  [io.hoplon.vendor/jquery "1.8.2-0"]
  [ibdknox/tools.reader "0.8.1"]
  [io.aviso/pretty "0.1.10"]
  [org.hornetq/hornetq-core-client "2.3.17.Final"]
  [org.clojure/google-closure-library-third-party
   "0.0-20140226-71326067"]
  [org.clojure/tools.cli "0.2.2" :exclusions [[org.clojure/clojure]]]
  [org.mozilla/rhino "1.7R4"]
  [org.jsoup/jsoup "1.7.1"]
  [org.apache.httpcomponents/httpmime
   "4.3.3"
   :exclusions
   [[org.clojure/clojure]]]
  [hiccup "1.0.3"]
  [org.hornetq/hornetq-journal "2.3.17.Final"]
  [org.clojure/core.match
   "0.2.1"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tigris "0.1.1"]
  [joda-time "2.1"]
  [org.codehaus.janino/commons-compiler-jdk "2.6.1"]
  [ibdknox/analyzer "0.0.2"]
  [org.jboss.logging/jboss-logging "3.1.0.GA"]
  [com.fasterxml.jackson.core/jackson-annotations "2.1.1"]
  [org.clojure/tools.analyzer "0.1.0-beta12"]
  [fs "1.3.3"]
  [org.apache.commons/commons-compress "1.3"]
  [com.cemerick/pomegranate "0.2.0"]
  [org.sonatype.aether/aether-connector-wagon "1.13.1"]
  [org.tcrawley/dynapath "0.2.3"]
  [org.apache.maven.wagon/wagon-http "2.2"]
  [org.sonatype.aether/aether-connector-file "1.13.1"]
  [org.apache.maven.wagon/wagon-http-shared4 "2.2"]
  [org.apache.maven/maven-aether-provider "3.0.4"]
  [org.codehaus.plexus/plexus-component-annotations
   "1.5.5"
   :exclusions
   [[junit]]]
  [org.apache.maven/maven-model-builder "3.0.4"]
  [org.codehaus.plexus/plexus-interpolation "1.14"]
  [org.sonatype.aether/aether-util "1.13.1"]
  [org.sonatype.aether/aether-api "1.13.1"]
  [org.codehaus.plexus/plexus-classworlds "2.4"]
  [org.sonatype.aether/aether-impl "1.13.1"]
  [org.apache.maven/maven-repository-metadata "3.0.4"]
  [org.clojure/tools.namespace "0.1.3"]
  [commons-fileupload "1.3"]
  [clj-tuple "0.1.2"]
  [org.apache.maven/maven-model "3.0.4"]
  [org.codehaus.plexus/plexus-utils "2.0.6"]
  [com.h2database/h2 "1.3.171"]
  [org.codehaus.janino/commons-compiler "2.6.1"]
  [org.sonatype.aether/aether-spi "1.13.1"]
  [org.apache.tomcat/tomcat-juli "7.0.27"]
  [tailrecursion/javelin "3.3.1"]
  [tailrecursion/cljs-priority-map "1.0.3"]
  [org.clojure/data.priority-map "0.0.2"]
  [riddley "0.1.6"]
  [net.java.dev.stax-utils/stax-utils "20040917"]
  [org.slf4j/jul-to-slf4j "1.7.5"]
  [org.slf4j/slf4j-api "1.7.5"]
  [ring/ring-servlet "1.2.1"]
  [org.fressian/fressian "0.6.5"]
  [com.google.javascript/closure-compiler "v20131014"]
  [com.google.code.findbugs/jsr305 "1.3.9"]
  [args4j "2.0.16"]
  [com.google.protobuf/protobuf-java "2.4.1"]
  [org.json/json "20090211"]
  [org.jgroups/jgroups "3.2.12.Final"]
  [clojure-complete "0.2.3"]
  [org.sonatype.sisu/sisu-inject-plexus "2.2.3"]
  [org.sonatype.sisu/sisu-inject-bean "2.2.3"]
  [org.sonatype.sisu/sisu-guice
   "3.0.3"
   :classifier
   "no_aop"
   :exclusions
   [[javax.inject] [aopalliance]]]
  [org.apache.maven.wagon/wagon-provider-api "2.2"]
  [org.eclipse.jetty/jetty-server "7.6.8.v20121106"]
  [org.eclipse.jetty.orbit/javax.servlet "2.5.0.v201103041518"]
  [org.eclipse.jetty/jetty-continuation "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-http "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-io "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-util "7.6.8.v20121106"]]
 :source-paths
 ["src/hl" "src/semantic" "src/clj" "src/cljs"]
 :main
 cast.core
 :profiles
 {:uberjar {:aot :all}})
