(ns shared-resource.views.welcome
  (:require [shared-resource.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to shared-resource"]))
