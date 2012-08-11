(ns shared-resource.datomic
  (:use [datomic.api :only (q db) :as d]
        [shared-resource.DatabaseException]
        [shared-resource.config]))

(defn datomic-config [key]
  (config-value (str "datomic." key)))

(defn uri [db-config] (str "datomic:"
                           (db-config "edition") "://"
                           (db-config "host") ":"
                           (db-config "port") "/"
                           (db-config "database")))

(def conn (d/connect (uri datomic-config)))

(defn create-database []
  (d/create-database (uri datomic-config)))

(defn load-schema []
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
       :db.install/_attribute :db.part/db}])))

(defn execute [query]
  (q query (db conn)))

(defn find-entity [id]
  (datomic.api/entity (db conn) id))

;; This should really be abstracted and model specific code should be moved to
;; that model.
(defn create-resource [resource-name description]
  (d/transact
   conn
   [{:db/id #db/id [:db.part/user]
     :resource/name resource-name
     :resource/description description}]))

(defn get-all-resources []
  (q '[:find ?n ?a ?b :where [?n :resource/name ?a] [?n :resource/description ?b]] (db conn)))

(defn find-resource [id]
  (let [entity (find-entity id)]
    {
     :id (:db/id entity)
     :name (:resource/name entity)
     :description (:resource/description entity)
     }
    ))

(defn get-all-reservations []
  (q '[:find ?n ?a ?b ?c ?d :where [?n :reservation/user ?a]
       [?n :reservation/resource ?b] [?n :reservation/startTime ?c]
       [?n :reservation/endTime ?d]] (db conn)))

(defn parse-datetime
  ([datetime] (parse-datetime datetime "yyyy-MM-dd hh:mm aa"))
  ([datetime format]
     (-> (java.text.SimpleDateFormat. "yyyy-MM-dd hh:mm aa") (.parse datetime))))

(defn create-reservation [resource-id user-id start end]
  "Create a reservation. Requires datetimes in the form yyyy-MM-dd hh:mm aa"
  (if (not-any? nil? [resource-id user-id start end])
    (let [start-time (parse-datetime start)
          end-time   (parse-datetime end)]
      (d/transact
       conn
       [{:db/id #db/id [:db.part/user]
         :reservation/resource resource-id
         :reservation/user user-id
         :reservation/startTime start-time
         :reservation/endTime end-time}]))))
    ;(throw (new shared-resource.DatabaseException
                ;"All fields are required for creating a reservation"))))

(defn find-reservation [id]
  (let [entity (find-entity id)]
    {
     :start-time (:reservation/startTime entity)
     :end-time (:reservation/endTime entity)
     :resource (:reservation/resource entity)
     :user (:reservation/user entity)
     }
    ))