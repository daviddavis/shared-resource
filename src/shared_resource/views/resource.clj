(ns shared-resource.views.resource
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.element)
  (:require [shared-resource.models.resource :as resource]
            [shared-resource.views.common :as common]
            [noir.response :as resp]))

;; Partials

(defpartial resource-list [
                           {:keys [
                                   perma-link
                                   name
                                   description
                                   start-time
                                   end-time
                                   ]
                            :as resource-item}
                           ]
  (when resource-item
    [:li.resource-item
     [:h2 (link-to perma-link name)]
     [:div.content description]
     [:ul.time
      [:li start-time]
      [:li end-time]
      ;;(when (user/admin?)
                                        ;[:li (link-to (resource/edit-url post) "edit")])
      ]
     ]))

(defpartial resources-page [resources]
  (common/layout
   [:ul.resources
    (map resource-list resources)]
   ))

;; Page structure

(defpage "/" []
  (resource-list resource/get-all))
