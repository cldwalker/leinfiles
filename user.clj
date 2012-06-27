(ns u
  (:require [clojure.pprint :as pp])
)

(defn class-path "Prints list of class paths" []
  (pp/pprint  (seq  (.getURLs  (java.lang.ClassLoader/getSystemClassLoader))))
)

(defn spy "Simple print debugging" [& args]
  (do (println (flatten args))
      (flatten (identity args))))

(defn java-methods "List of methods for a java class" [klass]
  (sort (distinct (map #(.getName %) (seq (.getMethods klass))))))

; mtable 'doc
(defn mtable "Prints meta of a function as a table" [sym]
  (table.core/table (meta (resolve sym))))

; TODO: can't construct full symbol for doc correctly
;(defmacro ls
;  "Prints docs for all vars in a namespace"
;  [nsname]
;  `(doseq [v# (clojure.repl/dir-fn '~nsname)]
;     (clojure.repl/doc
;     (symbol (str '~nsname "/" v#))))
;  )

(set! clojure.core/*print-length* 100)
(set! clojure.core/*print-level* 5)

(println "Loaded user.clj!")
