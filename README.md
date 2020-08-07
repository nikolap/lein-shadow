# lein-shadow

[![Clojars Project](https://img.shields.io/clojars/v/lein-shadow.svg)](https://clojars.org/lein-shadow)

A Leiningen plugin to help keep your project configuration in your `project.clj` 
file when using [shadow-cljs](https://github.com/thheller/shadow-cljs).

This plugin copies the configuration from a `:shadow-cljs` key in `project.clj` 
to the `shadow-cljs.edn` file then executes `shadow-cljs` with the args provided.

It also manages your `npm` dependencies for the current project by using either:
1. the `:npm-deps` and `:npm-dev-deps` keys in the first `deps.cljs` file found
   in the `:source-paths`; or
2. the `:npm-deps` and `:npm-dev-deps` keys in in `project.clj`.  

It will only use **one** of these for `npm` dependencies, not both. The presence of `deps.cljs` takes priority over `project.clj`

Option 1. is especially useful for library authors as `shadow-cljs` expects 
libraries to provide transitive `npm` dependencies in a `deps.cljs` file on
the classpath.

Instead of writing to `package.json` as that may cause issues with other tooling
(such as `npm` or `shadow-cljs` itself) `npm install --save{-dev} ...` is
executed with the list of dependencies on every run.

## Usage

Put `[lein-shadow "{YOUR_VERSION}"]` into the `:plugins` vector of your project.clj.

This plugin requires that you have [shadow-cljs](https://github.com/thheller/shadow-cljs) in your `:dependencies` as well.

### Examples

The most basic command is:

    $ lein shadow compile app

This utilizes the default args `watch app` for shadow-cljs.

Some other possible commands are:

    $ lein shadow watch app

    $ lein shadow release app
    
## Workflows

1. Entire configuration in `project.clj`, no package.json (see [examples/lein-shadow-example](/examples/lein-shadow-example))
2. Clojure deps in `project.clj`, `package.json` used for js deps and/or other configuration (see [examples/lein-shadow-example](/examples/lein-shadow-example))
3. All deps in `project.clj`, with a custom `package.json` configuration for scripting (see [examples/lein-shadow-package-alt-example](/examples/lein-shadow-package-alt-example))
4. Clojure deps in `project.clj`, with js deps in a `deps.cljs` file on the source path (see [examples/lein-shadow-deps-example](/examples/lein-shadow-deps-example))

## License

Copyright © 2019 Nikola Peric

Distributed under the [MIT License](https://opensource.org/licenses/MIT).
