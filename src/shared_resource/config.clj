(ns shared-resource.config)

(defn load-props [file] 
  (into {} (doto (java.util.Properties.)
             (.load (-> (Thread/currentThread)
             (.getContextClassLoader)
             (.getResourceAsStream file))))))

(def config-map (load-props "config.properties"))

(defn config-value [key & args]
      (apply format (cons (get config-map key) args)))
