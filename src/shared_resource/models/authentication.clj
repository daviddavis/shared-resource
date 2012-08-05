(ns shared-resource.models.authentication
  (:use [shared-resource.config])
  (:require [clj-ldap.client :as ldap]))


(defn ldap-config [key]
  (config-value (str "ldap." key)))

(defn authenticate? [user-entry password]
  "Authenticate a user against ldap and return true if successful, false otherwise."
  (let [host        (ldap-config "host")
        port        (ldap-config "port")
        bind-dn     (ldap-config "user")
        bind-pass   (ldap-config "pass")
        ldap-server (ldap/connect {:host host :port port :bind-dn bind-dn :password bind-pass})
        user-dn     (:dn user-entry)]
    (ldap/bind? ldap-server user-dn password)))

(defn find-user [username]
  "Find a user in ldap and a user entry (a map of values like dn, etc.)"
  (let [host              (ldap-config "host")
        port              (ldap-config "port")
        bind-dn           (ldap-config "user")
        bind-pass         (ldap-config "pass")
        ldap-server       (ldap/connect {:host host :port port :bind-dn bind-dn :password bind-pass})
        connection-string (ldap-config "connection-string")
        filter-string     (str (ldap-config "username-field") "=" username)]
    (first (ldap/search ldap-server connection-string {:filter filter-string}))))

(defn user-attributes [user-entry]
  (let [username  ((keyword (ldap-config "username-field"))  user-entry)
        email     ((keyword (ldap-config "email-field"))     user-entry)
        full-name ((keyword (ldap-config "full-name-field")) user-entry)]
    {:username username :email email :full-name full-name}))
