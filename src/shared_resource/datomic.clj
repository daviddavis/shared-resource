(ns shared-resource.datomic
  (:use [datomic.api :only (q db) :as db]
        [shared-resource.config]))

(defn db-config [key]
  (config-value (str "datomic." key)))

(def uri (str "datomic:" (db-config "edition") "://" (db-config "host") ":" (db-config "port") "/" (db-config "database")))
