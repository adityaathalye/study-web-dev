(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager)) ; we must 'import' java classes


(def db
  "Definition for database connection."
  {:classname "org.sqlite.JDBC" ; class for JDBC driver
   :subprotocol "sqlite"        ; the db communication protocol
   :subname "db.sq3"})          ; name of db file used by sqlite
