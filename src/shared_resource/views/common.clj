(ns shared-resource.views.common
  (:use noir.core
        hiccup.core
        hiccup.element
        [hiccup.page :only [include-css html5]])
  (:require [noir.session :as session]))

(defpartial layout [& content]
  (html5
    [:head
     [:title "shared-resource"]
     (include-css "/css/reset.css")]
    [:body
     [:div#session
      [:div#user-info (session/get :username)]
      (if (session/get :username) (link-to "/sessions/destroy" "Logout") "")]
     [:div#flash.success (session/flash-get :success)]
     [:div#flash.error   (session/flash-get :error)]
     [:div#wrapper
      content]]))
