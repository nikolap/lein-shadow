This plugin copies the configuration from a `:shadow-cljs` key in `project.clj` 
to the `shadow-cljs.edn` file then executes `shadow-cljs` with the args provided.

It also manages your `npm` dependencies for the current project by using either:
1. the `:npm-deps` and `:npm-dev-deps` keys in the first `deps.cljs` file found
   in the `:source-paths`; or
2. the `:npm-deps` and `:npm-dev-deps` keys in in `project.clj`.  

It will only use **one** of these for `npm` dependencies, not both. 

Option 1. is especially useful for library authors as `shadow-cljs` expects 
libraries to provide transitive `npm` dependencies in a `deps.cljs` file on
the classpath.

Instead of writing to `package.json` as that may cause issues with other tooling
(such as `npm` or `shadow-cljs` itself) `npm install --save{-dev} ...` is
executed with the list of dependencies on every run.