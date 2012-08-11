(ns shared-resource.views.sessions
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.element
        hiccup.form)
  (:require [shared-resource.models.user :as user]
            [shared-resource.views.common :as common]
            [noir.response :as resp]
            [noir.session :as session]))

(defpartial login-page [username]
  (common/layout
    (form-to [:post "/sessions"]
      (label "username" "Username: ")
      (text-field "username" username)
      (label "password" "Password: ")
      (password-field "password")
      (submit-button "Login"))))

(defn logged-in? []
  (not (nil? (session/get :user))))

;; Routes

(pre-route "/" {}
  (when-not (logged-in?)
    (resp/redirect "/sessions/new")))

(pre-route "/resources*" {}
  (when-not (logged-in?)
    (resp/redirect "/sessions/new")))

(defpage "/sessions/new" []
  (if (session/get :user) (resp/redirect "/") (login-page "")))

(defpage [:post "/sessions"] {:keys [username password]}
  (if (user/login? username password true)
    (do
      (session/clear!)
      (session/put! :user (user/find-by-username username))
      (session/flash-put! :success "Successfully logged in.")
      (resp/redirect "/"))
    (do
      (session/flash-put! :error "Failed to log in.")
      (login-page username))))

(defpage "/sessions/destroy" []
  (do
    (session/clear!)
    ;; (session/flash-put! :success "Logged out.") can't session/clear to work with this
    (resp/redirect "/sessions/new")))
