(ns leiningen.shadow
  (:require [clojure.java.shell :refer [sh]]
            [jsonista.core :as j]
            [leiningen.core.main :as lein]
            [leiningen.run :as run]))

(def ^:const run-shadow-cljs ["-m" "shadow.cljs.devtools.cli"])
(def ^:const shadow-cljs-preamble ";; This file is generated by lein-shadow, do not manually edit. Instead, edit project.clj shadow-cljs key.\n")

(defn deps-vec->deps-map
  [dependencies]
  (reduce (fn [tree [package version]]
            (assoc tree (str package) (str version)))
          {} dependencies))

(defn deps-vec->package-json
  [dependencies]
  (j/write-value-as-string
    {:dependencies (deps-vec->deps-map dependencies)}))

(defn npm-deps!
  [dependencies]
  (lein/info "Preparing npm packages")
  (spit "package.json"
        (deps-vec->package-json dependencies))
  (lein/info "Installing npm packages")
  (sh "npm" "install")
  (lein/info "npm packages successfully installed"))

(defn shadow
  "Helps keep your project configuration in your project.clj file when using shadow-cljs.

Refer to shadow-cljs docs for exhaustive CLI args, some possible args include:

  compile [build]
  release [build]
  watch   [build]

You can also run an npm install on packages defined in your project.clj file under the :npm-deps key with this command:
  deps"
  [project & args]
  (if-let [command (first args)]
    (if (= "deps" command)
      (npm-deps! (:npm-deps project))
      (if-let [config (:shadow-cljs project)]
        (let [args' (concat run-shadow-cljs args)]
          (spit "shadow-cljs.edn" (str shadow-cljs-preamble (pr-str config)))
          (apply run/run project args'))
        (lein/warn "No shadow-cljs config key defined in project.clj. Please add a config to go into shadow-cljs.edn")))
    (lein/warn "No command specified.")))
