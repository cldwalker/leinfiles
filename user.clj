(ns u
  (:require  [clojure.pprint :as pp])
)

(defn load-path []
  (pp/pprint  (seq  (.getURLs  (java.lang.ClassLoader/getSystemClassLoader))))
)

(println "Loaded user.clj!")
