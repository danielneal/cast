(defproject
 cast
 "0.1.0-SNAPSHOT"
 :dependencies
 [[org.clojure/clojure "1.5.1"]
  [tailrecursion/boot.core "2.3.1" :exclusions [[org.clojure/clojure]]]
  [org.clojure/clojurescript
   "0.0-2202"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/boot.task
   "2.1.3"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/warp "0.1.0" :exclusions [[org.clojure/clojure]]]
  [org.clojure/data.json "0.2.3"]
  [org.clojure/tools.reader "0.8.3"]
  [com.datomic/datomic-free
   "0.9.4724"
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
  [com.fasterxml.jackson.core/jackson-core "2.1.1"]
  [org.apache.tomcat/tomcat-jdbc
   "7.0.27"
   :exclusions
   [[commons-logging]]]
  [tailrecursion/boot.ring
   "0.1.0"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [ring "1.2.1"]
  [ring/ring-devel "1.2.1"]
  [clj-stacktrace "0.2.5"]
  [ns-tracker "0.2.1"]
  [org.clojure/java.classpath "0.2.0"]
  [com.fasterxml.jackson.core/jackson-databind "2.1.1"]
  [com.datomic/datomic-lucene-core "3.3.0"]
  [org.clojure/google-closure-library "0.0-20140226-71326067"]
  [commons-codec "1.3"]
  [org.hornetq/hornetq-core-client "2.3.17.Final"]
  [org.clojure/google-closure-library-third-party
   "0.0-20140226-71326067"]
  [org.clojure/tools.cli "0.2.2" :exclusions [[org.clojure/clojure]]]
  [org.mozilla/rhino "1.7R4"]
  [hiccup "1.0.3"]
  [org.hornetq/hornetq-journal "2.3.17.Final"]
  [tailrecursion/hoplon
   "5.8.3"
   :exclusions
   [[org.clojure/clojure] [tailrecursion/boot.core]]]
  [tailrecursion/castra "1.2.0"]
  [clj-tagsoup "0.3.0"]
  [org.clojure/data.xml "0.0.3"]
  [org.clojars.nathell/tagsoup "1.2.1"]
  [tailrecursion/extype "0.1.0"]
  [org.clojure/core.incubator "0.1.2"]
  [ring/ring-core "1.2.1"]
  [ring/ring-codec "1.0.0"]
  [tailrecursion/cljson "1.0.6"]
  [ring/ring-jetty-adapter "1.2.1"]
  [cheshire "5.2.0"]
  [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.2.1"]
  [clj-time "0.4.4"]
  [commons-io "2.4"]
  [tigris "0.1.1"]
  [joda-time "2.1"]
  [org.codehaus.janino/commons-compiler-jdk "2.6.1"]
  [org.jboss.logging/jboss-logging "3.1.0.GA"]
  [com.fasterxml.jackson.core/jackson-annotations "2.1.1"]
  [org.clojure/tools.namespace "0.1.3"]
  [commons-fileupload "1.3"]
  [tailrecursion/javelin "3.2.0"]
  [tailrecursion/cljs-priority-map "1.0.3"]
  [org.clojure/data.priority-map "0.0.2"]
  [riddley "0.1.6"]
  [org.apache.maven/maven-model "3.0.4"]
  [org.codehaus.plexus/plexus-utils "2.0.6"]
  [com.h2database/h2 "1.3.171"]
  [org.codehaus.janino/commons-compiler "2.6.1"]
  [org.apache.tomcat/tomcat-juli "7.0.27"]
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
  [org.eclipse.jetty/jetty-server "7.6.8.v20121106"]
  [org.eclipse.jetty.orbit/javax.servlet "2.5.0.v201103041518"]
  [org.eclipse.jetty/jetty-continuation "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-http "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-io "7.6.8.v20121106"]
  [org.eclipse.jetty/jetty-util "7.6.8.v20121106"]]
 :source-paths
 ["src/hl" "src/clj" "src/cljs"])
