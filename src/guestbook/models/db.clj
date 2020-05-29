(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager)) ; we must 'import' java classes


(def db
  "Definition for database connection."
  {:classname "org.sqlite.JDBC" ; class for JDBC driver
   :subprotocol "sqlite"        ; the db communication protocol
   :subname "db.sq3"})          ; name of db file used by sqlite


(defn create-guestbook-table
  []
  (sql/with-connection ; ensure db connection is properly cleaned after use
    db
    ;; first, create a table for guestbook entries
    (sql/create-table
     :guestbook
     [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
     [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
     [:name "TEXT"]
     [:message "TEXT"])
    ;; then, create an index for the guestbook table
    (sql/do-commands
     "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

(comment
  ;; evaluate ONLY IF the table does not exist
  (create-guestbook-table)
  )
