(ns leiningen.shadow
  (:require [clojure.string :as string]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [jsonista.core :as j]
            [leiningen.core.main :as lein]
            [leiningen.run :as run]
            [meta-merge.core :refer [meta-merge]]))

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

(def windows?
  (string/starts-with? (System/getProperty "os.name") "Windows"))

(def npm-command (if windows? "npm.cmd" "npm"))

(defn npm-deps!
  [dependencies]
  (if (some? dependencies)
    (do
      (lein/info "Preparing npm packages")
      (spit "package.json"
            (deps-vec->package-json dependencies))
      (lein/info "Installing npm packages")
      (sh npm-command "install")
      (lein/info "npm packages successfully installed"))
    (lein/info "npm packages not managed by lein-shadow, skipping npm install")))

(defn read-default-shadow-config []
  (let [file (io/file (System/getProperty "user.home") ".shadow-cljs" "config.edn")]
    (when (.exists file)
      (-> file slurp edn/read-string))))

(defn merge-config [shadow-config project-shadow-config]
  (meta-merge (dissoc shadow-config
                      :source-paths
                      :dependencies)
              project-shadow-config))

(defn merge-project [shadow-config project]
  (meta-merge (select-keys shadow-config [:source-paths :dependencies])
              project))

(defn shadow
  "Helps keep your project configuration in your project.clj file when using shadow-cljs.

Refer to shadow-cljs docs for exhaustive CLI args, some possible args include:

  compile [build]
  release [build]
  watch   [build]"
  [project & args]
  (if (first args)
    (do
      (npm-deps! (:npm-deps project))
      (lein/info "Running shadow-cljs...")
      (if-let [config (:shadow-cljs project)]
        (let [shadow-cljs-profile (read-default-shadow-config)
              config'             (merge-config shadow-cljs-profile config)
              project'            (merge-project shadow-cljs-profile project)
              args'               (concat run-shadow-cljs args)]
          (->> (assoc config' :lein true)
               pr-str
               (str shadow-cljs-preamble)
               (spit "shadow-cljs.edn"))
          (apply run/run project' args'))
        (lein/warn "No shadow-cljs config key defined in project.clj. Please add a config to go into shadow-cljs.edn")))
    (lein/warn "No command specified.")))
