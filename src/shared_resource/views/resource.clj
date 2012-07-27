(ns shared-resource.views.resource
  (:use noir.core
        hiccup.core
        hiccup.page)
  (:require [shared-resource.models.resource :as resource]
            [shared-resource.views.common :as common]
            [noir.response :as resp]))

;; Partials

(defpartial resource [{:keys [perma-link name description start-time end-time] :as resource-item}]
            (when resource-item
              [:li.resource-item
               ;[:h2 (link-to perma-link name)]
               [:ul.time
                [:li start-time]
                [:li end-time]
                ;;(when (user/admin?)
                                        ;[:li (link-to (resource/edit-url post) "edit")])
                ]
               [:div.content description]]))

(defpartial resources-page [resources]
            (common/layout
              [:ul.resources
               (map resource resources)]
              ))

;; Page structure
(defpage "/resources/" []
         (resources-page (resource/get-all)))
