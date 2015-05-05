(ns user
  (:require clojure.pprint
            clojure.java.io
            clojure.java.javadoc
            [clojure.java.shell :as sh]
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

(defn eval-def "Can def inside a namespace" [qnsp qdef]
  (binding [*ns* (find-ns qnsp)] (eval qdef)))

(defn doc-dir "Prints docs for a given namespace" [nsname]
  (let [resolved (map #(ns-resolve (the-ns nsname) %) (clojure.repl/dir-fn nsname))]
    (doseq [v resolved]
      (@#'clojure.repl/print-doc (meta v)))))

; TODO: macroize for non-reply repls
(defn jdoc "javadoc an object" [obj]
  (clojure.java.javadoc/javadoc (class obj)))

; =========
; Inspectors: inspect vars, namespaces, fns, envs, properties ...

(defn java-methods "List of methods for a java class" [klass]
  (sort (distinct (map #(.getName %) (seq (.getMethods klass))))))

(defn java-methods-for "List of methods for java object" [obj]
  (java-methods (class obj)))

;; http://www.learningclojure.com/2010/09/astonishing-macro-of-narayan-singhal.html
(defmacro def-let
  "like let, but binds the expressions globally."
  [bindings & more]
  (let [let-expr (macroexpand `(let ~bindings))
        names-values (partition 2 (second let-expr))
        defs   (map #(cons 'def %) names-values)]
    (concat (list 'do) defs more)))

;; https://gist.github.com/alandipert/1619740
(defn findcore
  "Returns a lazy sequence of functions in clojure.core that, when applied to args,
  return ret."
  ([args ret]
     (findcore (filter #(not (:macro (meta %)))
                       (vals (ns-publics 'clojure.core))) args ret))
  ([[f & fns] args ret]
     (lazy-seq
      (when f
        (if (binding [*out* (proxy [java.io.Writer] []
                              (write [_])
                              (close [])
                              (flush []))]
              (try
                (= ret (apply f args))
                (catch Throwable t)))
          (cons (:name (meta f)) (findcore fns args ret))
          (findcore fns args ret))))))

(defmacro fc [args ret]
  `(findcore '~args '~ret))

(defn- background-sh
  "Execute command in the background and return its pid"
  [cmd]
  (->>
   (sh/sh "bash" "-c" (format "%s 1>test.log 2>&1 & \n echo $!" cmd))
   :out
   (re-find #"(\d+)\n")
   second))

; Configuration
(set! clojure.core/*print-length* 100)
(set! clojure.core/*print-level* 5)

(println "Loaded user.clj!")
