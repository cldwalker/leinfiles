(ns u
  (:require  [clojure.pprint :as pp])
)

(defn class-path "Prints list of class paths" []
  (pp/pprint  (seq  (.getURLs  (java.lang.ClassLoader/getSystemClassLoader))))
)

(defn spy "Simple print debugging" [& args]
  (do (println (flatten args))
      (flatten (identity args))))

(println "Loaded user.clj!")
