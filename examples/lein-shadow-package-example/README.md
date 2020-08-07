# lein-shadow example with clj configuration in project.clj, and js configuration in package.json

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

### package.json

```json
{
  "name": "lein-shadow-example",
  "dependencies": {
    "create-react-class": "15.6.3",
    "react": "16.8.6",
    "react-dom": "16.8.6"
  }
}
```