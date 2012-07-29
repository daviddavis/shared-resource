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
   [:h2 name]
   [:div.content description]
   ;;(when (user/admin?)
                                        ;[:li (link-to (resource/edit-url post) "edit")])
   ]))

(defpartial resources-page [resources]
  (common/layout
   [:ul.resources
    (map resource-list resources)]
   ))

;; Page structure

(defpage "/" []
  (resources-page (resource/get-all)))
