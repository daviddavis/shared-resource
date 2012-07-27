(ns shared-resource.models.resource
  (:require ;[shared-resource.config :as config]
            ;[clj-time.core :as ctime]
            ;[clj-time.format :as tform]
            ;[clj-time.coerce :as coerce]
            [clojure.string :as string]
            ;[shared-resource.models.user :as users]
            [noir.validation :as validation]
            [noir.session :as session])
  )

;(def date-format (tform/formatter "MM/dd/yy" (ctime/default-time-zone)))
;(def time-format (tform/formatter "h:mma" (ctime/default-time-zone)))
(defn now [] (new java.util.Date))


(def get-all
  {
   :perma-link "http://google.com"
   :name "Awesome Resource"
   :description "Too awesome to describe."
   :start-time (now)
   :end-time (now)
   }
  )
