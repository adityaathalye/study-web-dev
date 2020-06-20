(ns picture-gallery.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(defn- get-config
  [env-name]
  (let [default-config "resources/env/default.edn"
        env-name-config (str "resources/env/" env-name ".edn")
        to-edn (comp clojure.edn/read-string slurp)]
    (merge (to-edn default-config)
           (when (.exists (clojure.java.io/file env-name-config))
             (to-edn env-name-config)))))


;; Create pool with c3po
;; ref: https://www.mchange.com/projects/c3p0/#basics
;; ref: http://clojure-doc.org/articles/ecosystem/java_jdbc/reusing_connections.html


(defn pool
  [{:keys [classname subprotocol subname user password]
    :as db-spec}]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass classname)
               (.setJdbcUrl (str "jdbc:" subprotocol ":" subname))
               (.setUser user)
               (.setPassword password)
               ;; expire excess connection after 30 mins of inactivity
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity
               (.setMaxIdleTime (* 30 60 60)))]
    {:datasource cpds}))


(def db-spec
  "Raw DB spec conforming to what get-connection expects.
  ref: https://github.com/clojure/java.jdbc/blob/master/src/main/clojure/clojure/java/jdbc.clj#L274"
  (get-config "dev"))


(defonce db-conn
  ^{:doc "A pooled connection, useful for most CRUD purposes, except
  for prepared statements. Those must be done separately."}
  (pool db-spec))


(defn create-one-user
  "Create a single user record IFF it is absent in users table.
  Return true if created, false otherwise.

  Use concurrency-safe UPSERT, available since PostgreSQL v9.5, so we can
  avoid having to try/catch postgres insert exceptions here.

  ref: https://www.postgresql.org/docs/9.5/sql-insert.html#SQL-ON-CONFLICT"
  [{:keys [id pass] :as user}]
  (= 1
     (first (sql/execute!
             db-conn
             ["INSERT INTO users (id, pass) VALUES (?,?) ON CONFLICT (id) DO NOTHING"
              id pass]))))


(comment
  (sql/query db-conn
             ["select * from users limit 10"])

  ;; Postgres supports UPSERT by way of "ON CONFLICT", which is concurrency-safe
  (let [userid "foo"
        pass (str "randompass" (rand))]
    (sql/execute!
     db-conn
     ["insert into users (id, pass) values (?,?) on conflict (id) do nothing"
      userid pass]))
  )
