(ns database-access.core
  (:require [clojure.java.jdbc :as sql]))


(defn- get-config
  [env-name]
  (let [default-config "resources/env/default.edn"
        env-name-config (str "resources/env/" env-name ".edn")
        to-edn (comp clojure.edn/read-string slurp)]
    (merge (to-edn default-config)
           (when (.exists (clojure.java.io/file env-name-config))
             (to-edn env-name-config)))))


(def db
  (get-config "dev"))


(defn create-users-table
  []
  (sql/with-connection db
    (sql/create-table
     :users
     [:id "varchar(32) PRIMARY KEY"]
     [:pass "varchar(100)"])))

(comment
  (create-users-table)
  )


(defn add-user
  [user]
  (sql/with-connection db
    (sql/insert-record :users user)))

(comment
  (add-user {:id "foo" :pass "bar"})

  (doseq [user-x (range 10)]
    (add-user {:id (str "foo" user-x)
               :pass (str "bar" user-x)}))
  )


(defn add-user2
  [users]
  (sql/with-connection db
    (apply sql/insert-records
           :users
           users)))

(comment
  (add-user2
   (map (fn [n]
          {:id (str "foo" n)
           :pass (str "bar" n)})
        (range 20 30))))
