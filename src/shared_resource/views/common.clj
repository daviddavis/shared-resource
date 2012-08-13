(ns shared-resource.views.common
  (:use noir.core
        hiccup.core
        hiccup.element
        [hiccup.page :only [include-css include-js html5]])
  (:require [shared-resource.models.user :as user]
            [noir.session :as session]))

(defpartial layout [& content]
  (html5
    [:head
     [:title "shared-resource"]
     (include-css "/css/reset.css")
     (include-css "//ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/themes/base/jquery-ui.css") ; REMOVE ME!
     (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js")
     (include-js "//ajax.googleapis.com/ajax/libs/jqueryui/1.8.22/jquery-ui.min.js")
     (include-js "/js/jquery.ui.datetimepicker.min.js")
     (include-js "/js/main.js")]
    [:body
     [:div#session
      [:div#user-info (:user/username (user/find-by-id (session/get :user)))]
      (if (session/get :user) (link-to "/sessions/destroy" "Logout") "")]
     [:div#flash.success (session/flash-get :success)]
     [:div#flash.error   (session/flash-get :error)]
     [:div#wrapper
      content]]))
