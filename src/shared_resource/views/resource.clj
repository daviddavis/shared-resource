(ns shared-resource.views.resource
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.element)
  (:require [shared-resource.models.resource :as r]
            [shared-resource.models.reservation :as resv]
            [shared-resource.views.common :as common]
            [shared-resource.views.reservations :as rp]
            [noir.response :as resp]))

;; Partials


(defpartial resource-list [
                           {:keys [
                                   id
                                   name
                                   description
                                   ]
                            :as resource-item}
                           ]
  (when resource-item
  [:li.resource-item
   [:h2 (link-to  (str "resources/" id) name)]
   [:div.content description]
   ;;(when (user/admin?)
   ]))

(defpartial show-resource [
                           {:keys [
                                   name
                                   description
                                   ]
                            :as resource-item}
                           ]  
  [:h2 name]
  [:div.content description]
  ;;(when (user/admin?)
  )

(defpartial resources-page [resources]
  (common/layout
   [:ul.resources
    (map resource-list resources)]
   ))


(defpartial resource-page [resource]
  (common/layout
   (show-resource resource))) 

;; Page structure

 (defpage "/resources" []
   (resources-page (r/get-all)))

(defpage "/" []
  (noir.cljs.core/include-scripts)
  (resources-page (r/get-all)))

(defpage "/resources/:id" {:keys [id]}
  (resource-page (r/get-by-id id)))

;(defpage "/resource/:id/reservations" {:keys [id]}
;  (resource-reservation-page (r/get-by-id id) (resv/get-by-resource-id id)))
