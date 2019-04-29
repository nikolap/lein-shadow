(ns leiningen.shadow
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli]
            [leiningen.core.main :as lein]
            [shadow.cljs.devtools.api :as api]
            [shadow.cljs.devtools.cli-opts :as cli-opts]
            [shadow.cljs.devtools.server :as server]))

(defn valid-builds
  [config]
  (->> config
       :builds
       keys
       (map name)
       (string/join ", ")))

(defn run-shadow-cljs!
  [config-map command build & rest-args]
  (let [server-config (dissoc config-map :builds)
        opts          (:options (cli/parse-opts rest-args cli-opts/cli-spec))]
    (case command
      ;; server commands
      "start"
      (server/start! server-config)
      "stop"
      (server/stop!)
      "reload"
      (server/reload!)

      (api/with-runtime
        (if-let [build-config (get-in config-map [:builds (keyword build)])]
         ;; build commands
         (case command
           "compile"
           (api/compile* build-config opts)
           "release"
           (api/release* build-config opts)
           "watch"
           (api/watch* build-config opts)
           "check"
           (api/check* build-config opts)
           ;; default:
           (lein/warn "Invalid command. Please use one of 'compile', 'release', or 'watch'"))
         (lein/warn (str "Unable to find build " build ". Currently valid builds are: " (valid-builds config-map))))))))

(defn shadow
  "Uses the :shadow-cljs key from your project.clj file as shadow-cljs config, separate from shadow-cljs.edn.

Valid commands:

  lein shadow compile <build>
  lein shadow watch   <build>
  lein shadow check   <build>
  lein shadow release <build>
  lein shadow start
  lein shadow stop
  lein shadow reload
  "
  [project & args]
  (if-let [config-map (:shadow-cljs project)]
    (if (empty? args)
      (lein/warn "No args provided.")
      (apply run-shadow-cljs! config-map args))
    (lein/warn "No shadow-cljs config key defined in project.clj. Please add a config to go into shadow-cljs.edn")))
