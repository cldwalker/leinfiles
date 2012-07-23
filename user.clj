(ns user
  (:require clojure.pprint
            table.core
            clojure.java.io
            [clojure.string :as string]))

; =======
; Utilities: general purpose fns to be used mostly inside other fns

(defn sym-to-var
  "Converts a symbol to var"
  [sym]
  ((ns-interns ((meta (resolve sym)) :ns)) sym))

(defn command-exists?
  "Determines if command exists in $PATH"
  [cmd]
  (some
    #(-> (str % "/" cmd) clojure.java.io/file .isFile)
    (-> (System/getenv "PATH") (clojure.string/split #":"))))

; TODO: only display vars local to a namespace
(defn ns-dynamic-vars
  "dynamic vars for a namespace as determined by *var* convention"
  ([] (ns-dynamic-vars *ns*))
  ([nsname]
   (let [nsmap (ns-map nsname)]
     (->> nsmap (map first) (filter #(re-find #"^\*.*\*$" (str %))) (map #(nsmap %))))))

; TODO: handle names in multiple namespaces
(defn invert-vec-map [vec-map]
  (apply hash-map
    (flatten (mapcat (fn [pair] (map #(identity [% (first pair)]) (second pair))) vec-map))))

; =========
; Misc: Collection of useful fns for doc, debugging, etc.

;TODO: macroize so it can sit in front of a call like apply
(defn spy "Simple print debugging" [arg]
  (doto arg prn))

(defn doc-dir "Prints docs for a given namespace" [nsname]
  (let [resolved (map #(ns-resolve (the-ns nsname) %) (clojure.repl/dir-fn nsname))]
    (doseq [v resolved]
      (@#'clojure.repl/print-doc (meta v)))))

; TODO: macroize for non-reply repls
(defn jdoc "javadoc an object" [obj]
  (clojure.java.javadoc/javadoc (class obj)))

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

(defn var-meta "Prints meta of a symbol" [sym]
  (display (meta (resolve sym))))

(defn vars-meta "Prints public vars for a namespace with its meta info"
  ([] (vars-meta *ns*))
  ([nsname] (->>
     (clojure.repl/dir-fn nsname)
     (map #(ns-resolve (the-ns nsname) %))
     (remove #(not (var? %)))
     (map meta) display)))

(defn var-search "Same as apropos but returns definitions by namespace"
   [str-or-pattern]
   (let [matches? (if (instance? java.util.regex.Pattern str-or-pattern)
                    #(re-find str-or-pattern  (str %))
                    #(.contains  (str %)  (str str-or-pattern)))]
     (->>
       (reduce
         #(assoc %1 %2 (vec (filter matches? (keys (ns-publics %2)))))
         {}
         (all-ns))
       (remove #(zero? (count (second %1))))
       invert-vec-map
       display)))

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
