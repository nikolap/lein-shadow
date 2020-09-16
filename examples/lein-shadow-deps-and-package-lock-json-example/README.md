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

In this example `package.json` and 
[`package-lock.json`](https://docs.npmjs.com/configuring-npm/package-lock-json.html)
are stored in Git to reproduce exact copies of `node_modules` on subsequent checkouts.

To upgrade a package:
1. Edit `src/cljs/deps.cljs`, this is still the source of truth for direct (not transitive) dependencies
2. Run `lein shadow...`
3. `lein-shadow` will detect the difference, and run `npm install --save ...` or `yarn add ...` for the package(s)
4. Commit the changes to `package.json` and `package-lock.json` to lock down the new package version(s).
