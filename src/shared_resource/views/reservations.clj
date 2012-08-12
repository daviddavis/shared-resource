(ns shared-resource.views.reservations
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.element
        hiccup.form)
  (:require [shared-resource.models.reservation :as reservation]
            [shared-resource.models.resource :as resource]
            [shared-resource.views.common :as common]
            [noir.response :as resp]
            [noir.session :as session]))

(defpartial reservation-div [reservation]
  (when reservation
    [:div.reservation
     [:div.start (:start-time reservation)]
     [:div.end   (:end-time reservation)]
     [:div.user  (:user/name (:user reservation))]]))

(defpartial reservation-list [reservations]
  [:div.reservations (map reservation-div reservations)])

(defn resource-options []
  (map #(conj [] (:name %) (:id %)) (resource/get-all)))

(defpartial new-reservation-page [resource start end]
  (common/layout
    (form-to [:post "/reservations"]
      (label "resource" "Resource: ")
      (drop-down "resource" (resource-options) (Long/parseLong resource))
      (label "start" "Start: ")
      (text-field "start")
      (label "end" "End: ")
      (text-field "end")
      (submit-button "Submit"))))

;; Routes

(defpage "/reservations/new" {:keys [resource]}
  (new-reservation-page resource "" ""))

(defpage [:post "/reservations"] {:keys [resource start end]}
  (let [resource-id (Long/parseLong resource)
        user-id     (session/get :user)]
    (if (reservation/create-reservation resource-id user-id start end)
      (do
        (session/flash-put! :success "Successfully create reservation.")
        (resp/redirect (str "/resources/" resource)))
      (do
        (session/flash-put! :error "Failed to save reservation.")
        (new-reservation-page start end resource)))))