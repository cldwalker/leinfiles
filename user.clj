(ns u
  (:require [clojure.pprint])
  (:require [clojure.string :as string]))

; =======
; Utilities: general purpose fns to be used mostly inside other fns

(defn sym-to-var [sym]
  "Converts a symbol to var"
  ((ns-interns ((meta (resolve sym)) :ns)) sym))

(defn split [f coll]
  "Like ruby's partition"
  [(filter f coll) (remove f coll)])

; TODO: only display vars local to a namespace
(defn ns-dynamic-vars
  "dynamic vars for a namespace as determined by *var* convention"
  ([] (ns-dynamic-vars *ns*))
  ([nsname]
   (let [nsmap (ns-map nsname)]
     (->> nsmap (map first) (filter #(re-find #"^\*.*\*$" (str %))) (map #(nsmap %))))))

; =========
; Misc: Collection of useful fns for doc, debugging, etc.

;TODO: macroize so it can sit in front of a call like apply
(defn spy "Simple print debugging" [arg]
  (doto arg prn))

(defn doc-dir "Prints docs for a given namespace" [nsname]
  (let [ [resolved unresolved] (split resolve (clojure.repl/dir-fn nsname)) ]
    (doseq [sym resolved]
      (@#'clojure.repl/print-doc (meta (sym-to-var sym))))
    (when-not (empty? unresolved)
      (println (str "\n" "Unable to resolve these symbols: " (string/join ", " unresolved))))))

; TODO: macroize for non-reply repls
;(defn jdoc "javadoc an object" [obj]
;  (user/javadoc (class obj)))

(def ^:dynamic *display* :table)

(defn display
  "Pretty prints data or returns it depending on value of *display*. Default is to print with table."
  [data & options]
  (case *display*
    :pprint (do (clojure.pprint/pprint data) (println ""))
    :self (identity data)
    (apply table.core/table data options)))

(defn pster "Print full error stack"
  ([] (pster *e))
  ([err] (->> err .getStackTrace (map clojure.repl/stack-element-str) display)))

; =========
; Inspectors: inspect vars, namespaces, fns, envs, properties ...

(defn java-methods "List of methods for a java class" [klass]
  (sort (distinct (map #(.getName %) (seq (.getMethods klass))))))

(defn java-methods-for "List of methods for java object" [obj]
  (java-methods (class obj)))

; mtable 'doc
(defn var-meta "Prints meta of a symbol" [sym]
  (display (meta (resolve sym))))

(defn vars-meta "Prints public vars for a namespace with its meta info"
  ([] (vars-meta *ns*))
  ([nsname]
    (display (map #( meta (ns-resolve (the-ns nsname) %)) (clojure.repl/dir-fn nsname)))))

(defn vars-values
  "Prints dynamic vars for a namespace mapped to their values"
   [& options]
   (let [opts (apply hash-map options)]
     (apply
       display
       (cons
         ["Var" "Value"]
         (map #(identity [% (deref %)]) (ns-dynamic-vars (get opts :ns *ns*))))
       :sort true
       options)))

(defn class-paths "Prints list of class paths" []
  (display (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader)))))

(defn properties "List properties and their values" []
  (display
    (->> (System/getProperties) .stringPropertyNames
      (reduce #(assoc %1 %2 (System/getProperty %2)) {}))))

(defn envs "List of envs and their values" []
  (->> (System/getenv) keys (reduce #(assoc %1 %2 (System/getenv %2)) {}) display))

; Configuration
(set! clojure.core/*print-length* 100)
(set! clojure.core/*print-level* 5)

(println "Loaded user.clj!")
