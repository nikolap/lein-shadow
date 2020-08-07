# lein-shadow example with entire configuration in project.clj, and custom script in package.json

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

  ;; Note that if a `deps.cljs` file is found in `:source-paths` it will take
  ;; priority over `:npm-deps` here in `project.clj` and be used instead.
  :npm-deps [[create-react-class "15.6.3"]
             [react "16.8.6"]
             [react-dom "16.8.6"]]
;; ...
```

### package.json

```json
{
  "name": "lein-shadow-example",
  "scripts": {
    "hello": "echo 'Hello world'"
  },
 ...
}
```