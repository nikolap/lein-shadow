# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## 0.1.6 - 2019-09-26
### Added
- Abort on npm failure and print error out [PR 2  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/2)
### Improved
- Prefix lein-shadow log messages with `lein-shadow` [PR 2  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/2)

## 0.1.5 - 2019-08-19
### Added
- If `deps.cljs` file is present in `:source-paths`, use it for `:npm-deps` and `:npm-dev-deps` -- [PR 1  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/1)
- Support for `:npm-dev-deps` -- [PR 1  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/1)

## 0.1.4 - 2019-07-05
### Fixed
- Support npm.cmd call on Windows OS

## 0.1.3 - 2019-05-22
### Added
- meta-merge in shadow-cljs config from user defined `config.edn` file

## 0.1.2 - 2019-05-10
### Added
- Skip `npm install` when `:npm-deps` are not present
- Automatically add `:lein true` to shadow-cljs.edn output

## 0.1.1 - 2019-05-09
### Changed
- `npm install` runs by default on every run
- Remove `deps` command as it's no longer required

## 0.1.0 - 2019-05-07
### Added
- Initial release
