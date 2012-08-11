(ns shared-resource.models.reservation
  (:require [shared-resource.datomic :as db]))

(defn create-reservation [resource user start-time end-time]
  (db/create-reservation resource user start-time end-time))

(defn get-all-reservations []
  (db/execute '[:find ?n ?a ?b ?c ?d :where [?n :reservation/user ?a]
                [?n :reservation/resource ?b] [?n :reservation/startTime ?c]
                [?n :reservation/endTime ?d]]))

(defn find-reservation [id]
  (db/find-reservation id))

(defn get-all []
  (map (partial zipmap '(:id :user :resource :start :end)) (get-all-reservations)))