(ns shared-resource.models.user
  (:use shared-resource.datomic
        [shared-resource.config]
        [datomic.api :only (q db) :as d])
  (:require [shared-resource.datomic :as db]
            [clojure.string :as string]
            [shared-resource.models.authentication :as auth]))

(def authentication (config-value "authentication"))

(defn create-user [username full-name email]
  (let [conn (d/connect (uri datomic-config))]
    (d/transact
     conn
     [{:db/id #db/id [:db.part/user]
       :user/username (string/lower-case username)
       :user/name full-name
       :user/email email}])))

(defn get-all-usernames []
  (let [conn (d/connect (uri datomic-config))]
    (q '[:find ?n :where [?c user/username ?n ]] (db conn))))

(defn find-by-username [username]
  (let [name (string/lower-case username)]
    (first (first (execute `[:find ?c :where [?c user/username ~name]])))))

(defn find-by-id [id]
  (db/find-entity id))

(defn destroy-user [username]
  (let [conn (d/connect (uri datomic-config))]
    (d/transact
     conn
     [[:db.fn/retractEntity (find-by-username username)]])))

(defn find-or-create-user [{:keys [username full-name email]}]
  (if-let [user-record (find-by-username username)]
    user-record
    (create-user username full-name email)))

(defn authenticate? [username password save-user]
  (if-let [user-entry (auth/find-user username)]
   (when (auth/authenticate? user-entry password)
    (if save-user (find-or-create-user (auth/user-attributes user-entry)))
      true)
      false))

(defn login?
  "Calls an authentication method based on auth config"
  ([username password] (login? username password false))
  ([username password save-user]
    (if (= authentication "none")
      ; just save the user if save-user and return true
      (do
        (if save-user (find-or-create-user {:username username :full-name "John Doe" :email "johndoe@mail.com"}))
        true)
      ; actually authenticate the user
      (authenticate? username password save-user))))
