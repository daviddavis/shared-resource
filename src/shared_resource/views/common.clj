(ns shared-resource.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "shared-resource"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))
