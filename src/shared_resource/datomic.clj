(ns shared-resource.datomic
  (:use [datomic.api :only (q db) :as d]
        [shared-resource.config]))

(defn datomic-config [key]
  (config-value (str "datomic." key)))

(defn uri [db-config] (str "datomic:" (db-config "edition") "://" (db-config "host") ":" (db-config "port") "/" (db-config "database")))

(defn create-database []
  (d/create-database (uri datomic-config)))

(defn load-schema []
  (let [conn (d/connect (uri datomic-config))]
    (do
      ;; schema for users
      (println "Creating user schema")
      (d/transact
       conn
       [{:db/id #db/id[:db.part/db]
         :db/ident :user/username
         :db/valueType :db.type/string
         :db/cardinality :db.cardinality/one
         :db/doc "A user's login name"
         :db/index true
         :db/unique :db.unique/value
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :user/name
         :db/valueType :db.type/string
         :db/cardinality :db.cardinality/one
         :db/doc "A user's full name"
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :user/email
         :db/valueType :db.type/string
         :db/cardinality :db.cardinality/one
         :db/doc "A user's email address"
         :db.install/_attribute :db.part/db}])

      ;; schema for resources
      (println "Creating resource schema")
      (d/transact
       conn
       [{:db/id #db/id[:db.part/db]
         :db/ident :resource/name
         :db/valueType :db.type/string
         :db/cardinality :db.cardinality/one
         :db/doc "A resource's name"
         :db/index true
         :db/unique :db.unique/value
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :resource/description
         :db/valueType :db.type/string
         :db/cardinality :db.cardinality/one
         :db/doc "A resource's description"
         :db/fulltext true
         :db.install/_attribute :db.part/db}])

      ;; schema for a reservation
      (println "Creating reservation schema")
      (d/transact
       conn
       [{:db/id #db/id[:db.part/db]
         :db/ident :reservation/user
         :db/valueType :db.type/ref
         :db/cardinality :db.cardinality/one
         :db/doc "A reservation's user"
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :reservation/resource
         :db/valueType :db.type/ref
         :db/cardinality :db.cardinality/one
         :db/doc "A reservation's resource"
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :reservation/startTime
         :db/valueType :db.type/instant
         :db/cardinality :db.cardinality/one
         :db/doc "A reservation's start time"
         :db.install/_attribute :db.part/db}
        {:db/id #db/id[:db.part/db]
         :db/ident :reservation/endTime
         :db/valueType :db.type/instant
         :db/cardinality :db.cardinality/one
         :db/doc "A reservation's end time"
         :db.install/_attribute :db.part/db}]))))


;; This should really be abstracted and model specific code should be moved to
;; that model.
(defn create-resource [resource-name description]
  (let [conn (d/connect (uri datomic-config))]
    (d/transact
     conn
     [{:db/id #db/id [:db.part/user]
       :resource/name resource-name
       :resource/description description}])))

(defn get-all-resources []
  (let [conn (d/connect (uri datomic-config))]
    (q '[:find ?n ?a ?b :where [?n :resource/name ?a] [?n :resource/description ?b]] (db conn))))


(defn find-resource [id]
(let [conn (d/connect (uri datomic-config))
      entity (datomic.api/entity (db conn) id)]
  {
   :name (:resource/name entity)
   :description (:resource/description entity)
   }
  ))

