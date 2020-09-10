(ns leiningen.shadow
  (:require [clojure.string :as string]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [leiningen.core.main :as lein]
            [leiningen.run :as run]
            [meta-merge.core :refer [meta-merge]]
            [clojure.data.json :as json]))

(def ^:const run-shadow-cljs ["-m" "shadow.cljs.devtools.cli"])
(def ^:const shadow-cljs-preamble "
 ;;
 ;; >>>>>>>>>>>>>>>>>>>>>>>>>> DO NOT EDIT THIS FILE <<<<<<<<<<<<<<<<<<<<<<<<<<<
 ;;
 ;;
 ;;
 ;;
 ;;
 ;;
 ;; This file is generated by lein-shadow.
 ;; Any changes you make will be overwritten and lost.
 ;; Instead you should edit the value keyed :shadow-cljs in project.clj.
 ;;
 ;; More details: https://gitlab.com/nikperic/lein-shadow/-/tree/docs/shadow-cljs.edn.md
 ;;
 ;; Don't even check this file into your git repo. Add it to .gitignore.
 ;;
 ;;
 ;;
 ;;
 ;;
 ;;
 ;;
 ;; >>>>>>>>>>>>>>>>>>>>>>>> YOUR CHANGES WILL BE LOST <<<<<<<<<<<<<<<<<<<<<<<<<
 ;;
 ")
;; WARNING: A newline is required after the comment otherwise the entire
;;          configuration string will be commented out when concatenated.

(def windows? (string/starts-with? (System/getProperty "os.name") "Windows"))

(def npm-command
  (if windows?
    ["cmd" "/C" "npm"]
    ["npm"]))

(defn dependencies->npm-packages
  [dependencies]
  (reduce
    (fn [npm-args [package version]]
      (conj npm-args (str package "@" version)))
    []
    dependencies))

(defn dependencies->npm-args
  [npm-deps npm-dev-deps]
  (cond-> []
          (not-empty npm-deps)
          (conj (into ["install" "--save" "--save-exact"] (dependencies->npm-packages npm-deps)))

          (not-empty npm-dev-deps)
          (conj (into ["install" "--save-dev" "--save-exact"] (dependencies->npm-packages npm-dev-deps)))))

(defn npm-sh!
  [preamble npm-args]
  (let [command (into npm-command npm-args)]
    (lein/info "lein-shadow - running:" (string/join " " command))
    (let [result (apply sh command)]
      (case (:exit result)
        0 (lein/info "lein-shadow -" preamble (string/replace (:out result) "\n" ""))
        1 (lein/abort "lein-shadow - could not run the NPM command. Make sure NPM is installed and present in your Path https://nodejs.org/en/. Exiting.")
        (lein/abort "lein-shadow - NPM exited with an unsuccessful exit code:" (:exit result) ". Output:"
                    (:out result) (:err result))))))

(defn npm-deps!
  [npm-deps npm-dev-deps]
  (if (or (some? npm-deps)
          (some? npm-dev-deps))
    (do
      (npm-sh! "NPM version" ["--version"])
      (let [npm-args (dependencies->npm-args npm-deps npm-dev-deps)]
        (run! (fn [args]
                (npm-sh! "NPM install successful" args))
              npm-args)))
    (lein/info "lein-shadow - npm packages not managed, skipping npm install")))

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

(defn read-deps-cljs
  "Reads the first `deps.cljs` file found in `:source-paths`, otherwise nil."
  [project]
  (reduce
    (fn [_ source-path]
      (when-let [file (io/file source-path "deps.cljs")]
        (when (.exists file)
          (lein/info "lein-shadow - reading NPM dependencies from" (.getPath file))
          (-> file
              (slurp)
              (edn/read-string)
              (reduced)))))
    nil
    (:source-paths project)))

(def ^:const managed-regex #"This file is generated by lein-shadow")
(def ^:const backup-path "shadow-cljs.edn.backup")

(defn overwrite-shadow-check!
  []
  (let [shadow-file (io/file "shadow-cljs.edn")]
    (when (and (.exists shadow-file)
               (->> shadow-file
                    (slurp)
                    (re-find managed-regex)
                    (not)))
      (lein/warn "lein-shadow - unmanaged shadow-cljs.edn file exists. Backing up at " backup-path)
      (io/copy shadow-file (io/file backup-path)))))

(defn read-package-json
  "Read, or create if not-exists, the package.json file in the npm-deps install-dir."
  [npm-deps-install-dir package-name]
  (let [package-json-file (io/file npm-deps-install-dir "package.json")]
    (->
      (if (.exists package-json-file)
        (slurp package-json-file)
        (let [package-json-content (str "{\n  \"name\": \"" package-name "\"\n}")]
          (lein/info "lein-shadow - creating empty package.json")
          (spit package-json-file package-json-content)
          package-json-content))
      (json/read-str :key-fn keyword))))

(defn package-locks-exist?
  "Returns true if package-lock.json or npm-shrinkwrap.json exist."
  [npm-deps-install-dir]
  (let [package-lock-json-file (io/file npm-deps-install-dir "package-lock.json")
        npm-shrinkwrap-json-file (io/file npm-deps-install-dir "npm-shrinkwrap.json")]
    (or (.exists package-lock-json-file)
        (.exists npm-shrinkwrap-json-file))))

(defn shadow
  "Helps keep your project configuration in your `project.clj` file when using
   shadow-cljs.

   Refer to one of the following shadow-cljs documentation sources for exhaustive
   CLI args:
   - lein run -m shadow.cljs.devtools.cli --help
   - https://shadow-cljs.github.io/docs/UsersGuide.html

   Any command that can be run as `shadow-cljs <action> <zero or more build ids>`
   can be run via Leiningen with `lein shadow <action> <zero or more build ids>`.
   Some possibilities include:

   - lein shadow compile <one or more build ids>
   - lein shadow release <one or more build ids>
   - lein shadow watch   <one or more build ids>"
  [{:keys [name group] :as project} & args]
  (if (first args)
    (let [{:keys [npm-deps npm-dev-deps]} (or (read-deps-cljs project) project)
          npm-deps-install-dir  (get-in project [:shadow-cljs :npm-deps :install-dir] lein/*cwd*)
          package-name          (if (= name group) name (str group "." name))
          {package-json-deps     :dependencies
           package-json-dev-deps :devDependencies} (read-package-json npm-deps-install-dir package-name)
          npm-install-command   (if (package-locks-exist? npm-deps-install-dir) "ci" "install")
          run-npm-install?      (not (and (empty? package-json-deps) (empty? package-json-dev-deps)))]
      (when run-npm-install? 
        (npm-sh! (format "Executing 'npm %s' for existing package.json" npm-install-command) [npm-install-command]))
      (overwrite-shadow-check!)
      (npm-deps! npm-deps npm-dev-deps)
      (lein/info "lein-shadow - running shadow-cljs...")
      (if-let [config (:shadow-cljs project)]
        (let [shadow-cljs-profile (read-default-shadow-config)
              config'             (merge-config shadow-cljs-profile config)
              project'            (merge-project shadow-cljs-profile project)
              args'               (concat run-shadow-cljs args)]
          (->> (assoc config' :lein true)
               (pr-str)
               (str shadow-cljs-preamble)
               (spit "shadow-cljs.edn"))
          (apply run/run project' args'))
        (lein/warn "lein-shadow - no :shadow-cljs config key defined in project.clj. Please add a config to go into shadow-cljs.edn")))
    (lein/warn "lein-shadow - no command specified.")))
