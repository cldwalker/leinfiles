(ns u
  (:require [clojure.pprint :as pp])
  (:require [clojure.string :as string])
)

(defn class-path "Prints list of class paths" []
  (pp/pprint  (seq  (.getURLs  (java.lang.ClassLoader/getSystemClassLoader))))
)

(defn spy "Simple print debugging" [arg]
  (doto arg prn))

(defn java-methods "List of methods for a java class" [klass]
  (sort (distinct (map #(.getName %) (seq (.getMethods klass))))))

; mtable 'doc
(defn mtable "Prints meta of a function as a table" [sym]
  (table.core/table (meta (resolve sym))))

(defn dir-mtable [nsname]
  (table.core/table (map #( meta (resolve %)) (clojure.repl/dir-fn nsname))))

(defn sym-to-var [sym]
  "Converts a symbol to var"
  ((ns-interns ((meta (resolve sym)) :ns)) sym))

(defn split [f coll]
  "Like ruby's partition"
  [(filter f coll) (remove f coll)])

(defn doc-dir [nsname]
  "Prints docs for a given namespace"
  (let [ [resolved unresolved] (split resolve (clojure.repl/dir-fn nsname)) ]
    (doseq [sym resolved]
      (@#'clojure.repl/print-doc (meta (sym-to-var sym))))
    (when-not (empty? unresolved)
      (println (str "\n" "Unable to resolve these symbols: " (string/join ", " unresolved))))))

(set! clojure.core/*print-length* 100)
(set! clojure.core/*print-level* 5)

(println "Loaded user.clj!")
