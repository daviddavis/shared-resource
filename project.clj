(defproject shared-resource "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [noir "1.3.0-beta10"]
                           [com.datomic/datomic-free "0.8.3397"]
                           [org.clojars.pntblnk/clj-ldap "0.0.9"]]
            :plugins [[lein-swank "1.4.4"]]
            :main shared-resource.server)

