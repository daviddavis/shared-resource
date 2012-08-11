(ns shared-resource.views.reservations
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.element
        hiccup.form)
  (:require [shared-resource.models.reservation :as reservation]
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