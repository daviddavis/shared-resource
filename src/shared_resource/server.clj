(ns shared-resource.server
  (:require [noir.server :as server]
            [noir.cljs.core]))

(def cljs-options {:advanced {:externs ["externs/jquery.js"]}})
(server/load-views-ns 'shared-resource.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (noir.cljs.core/start mode cljs-options)
    (server/start port {:mode mode
                        :ns 'shared-resource})))

