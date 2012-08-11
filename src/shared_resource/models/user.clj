(ns shared-resource.models.user
  (:use shared-resource.datomic
        [datomic.api :only (q db) :as d])
  (:require [shared-resource.models.authentication :as auth]))

(defn create-user [username full-name email]
  (let [conn (d/connect (uri datomic-config))]
    (d/transact
     conn
     [{:db/id #db/id [:db.part/user]
       :user/username username
       :user/name full-name
       :user/email email}])))

(defn get-all-usernames []
  (let [conn (d/connect (uri datomic-config))]
    (q '[:find ?n :where [?c user/username ?n ]] (db conn))))

(defn find-user [username]
  (let [conn (d/connect (uri datomic-config))]
    (first (first (q `[:find ?c :where [?c user/username ~username]] (db conn))))))

(defn destroy-user [username]
  (let [conn (d/connect (uri datomic-config))]
    (d/transact
     conn
     [[:db.fn/retractEntity (find-user username)]])))

(defn find-or-create-user [{:keys [username full-name email]}]
  (if-let [user-record (find-user username)]
    user-record
    (create-user username full-name email)))

(defn login?
  ([username password] (login? username password false))
  ([username password save-user]
    (if-let [user-entry (auth/find-user username)]
      (when (auth/authenticate? user-entry password)
        (if save-user (find-or-create-user (auth/user-attributes user-entry)))
        true)
      false)))
