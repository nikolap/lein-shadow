# lein-shadow example with deps.cljs

## Relevant configuration

### project.clj

```clojure
;; ...
  :shadow-cljs
  {:source-paths ["src"]
   :builds       {:app {:target     :browser
                        :output-dir "resources/public/js"
                        :asset-path "/js"
                        :modules    {:app {:entries [lein-shadow-example.core]}}
                        :devtools   {:after-load lein-shadow-example.core/mount-root}}}
   :dev-http     {3000 {:root    "resources/public"
                        :handler lein-shadow-example.handler/app}}}
;; ...
```

### src/cljs/deps.cljs

```clojure
{:npm-deps {"create-react-class" "15.6.3"
            "react"              "16.8.6"
            "react-dom"          "16.8.6"}}
```

This example just has `:npm-deps` but if you had js deps just for dev you could use `:npm-dev-deps`