# lein-shadow

A Leiningen plugin to help keep your project configuration in your `project.clj` 
file when using [shadow-cljs](https://github.com/thheller/shadow-cljs).

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

## Usage

Put `[lein-shadow "0.2.1"]` into the `:plugins` vector of your project.clj.

This plugin requires that you have [shadow-cljs](https://github.com/thheller/shadow-cljs) in your `:dependencies` as well.

### Examples

The most basic command is:

    $ lein shadow compile app

This utilizes the default args `watch app` for shadow-cljs.

Some other possible commands are:

    $ lein shadow watch app

    $ lein shadow release app

## License

Copyright © 2019 Nikola Peric

Distributed under the [MIT License](https://opensource.org/licenses/MIT).
