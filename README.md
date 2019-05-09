# lein-shadow

A Leiningen plugin to help keep your project configuration in your project.clj file when using [shadow-cljs](https://github.com/thheller/shadow-cljs).

This plugin copies the configuration from a :shadow-cljs key in project.clj to a shadow-cljs.edn file then executes shadow-cljs with the args provided.

It also manages your npm dependencies in the :npm-deps key, checking for any changes on every run.

## Usage

Put `[lein-shadow "0.1.0"]` into the `:plugins` vector of your project.clj.

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
