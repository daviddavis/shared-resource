(ns shared-resource.models.authentication
  (:use [shared-resource.config])
  (:require [clj-ldap.client :as ldap]))


(defn ldap-config [key]
  (config-value (str "ldap." key)))

(defn ldap-authenticate? [username password]
  "Authenticate a user against ldap and return true if successful, false otherwise."
  (let [ldap-server (ldap/connect {:host (ldap-config "host") :port (ldap-config "port") :bind-dn (ldap-config "user") :password (ldap-config "pass")})
        user-entry  (ldap/search ldap-server "ou=netfriends,DC=netfriends,DC=com" {:filter (str "sAMAccountName=" username) :attributes [:dn]})
        user-dn     (apply :dn user-entry)]
    (ldap/bind? ldap-server user-dn password)))
