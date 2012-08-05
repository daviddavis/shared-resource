(ns shared-resource.models.resource
  (:require ;[shared-resource.config :as config]
            ;[clj-time.core :as ctime]
            ;[clj-time.format :as tform]
            ;[clj-time.coerce :as coerce]
            [clojure.string :as string]
            ;[shared-resource.models.user :as users]
            [noir.validation :as validation]
            [noir.session :as session]
            [shared-resource.datomic :as db])
  )



(defn get-all []
  (map (partial zipmap '(:id :name :description)) (db/get-all-resources)))

(defn get-by-id [id]
  (db/find-resource (read-string id))
  )
