(ns picture-gallery.models.db
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
