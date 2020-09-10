# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## 0.2.3 - 2020-09-10
### Improved
- Use `npm ci` instead of `npm install` when applicable [PR 8  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/8)
### Fixed
- Replace package deps names `/` with `.` for package.json compatibility [PR 7  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/7)

## 0.2.2 - 2020-08-07
[PR 5  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/5)
### Fixed
- Fix edge cases with package.json / npm deps management

## 0.2.1 - 2020-06-04
### Fixed
- Fix typo in generated shadow-cljs.edn file [PR 4 - zefu](https://gitlab.com/nikperic/lein-shadow/merge_requests/4)

## 0.2.0 - 2020-05-13
[PR 3  - Isaac Johnston](https://gitlab.com/nikperic/lein-shadow/merge_requests/3)
### Added
- Windows support
### Improved
- Make comment to not edit shadow-cljs.edn very obvious
- Better docstrings and help text
- Logging
- Back up shadow-cljs.edn file if it appears to be unmanaged by lein-shadow
### Changed
- No longer overwrite package.json, instead install deps via npm

## 0.1.7 - 2019-11-13
### Fixed
- Fix issue with keyword encoding when creating package.json file

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
