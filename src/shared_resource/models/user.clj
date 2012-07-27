(ns shared-resource.models.user
  (:require [clj-ldap.client :as ldap]))

(def ldap-server (ldap/connect {:host "ldap.example.com"}))
