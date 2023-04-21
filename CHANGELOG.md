# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [3.0.0]

### Changed
- dependency upgrade kotlin 1.8.10
- dependency upgrade gradle 8.0.2
- dependency upgrade java 17
- dependency upgrade com.gradle.plugin-publish

## [2.0.0]

### Changed

- plugin only has to be applied on rootproject
- changed Contributer Email to be optional
- changed Developer Email to be optional

### Added

- added inceptionYear information
- added warn log when plugin is applied to sub projects
- added applyFromRoot property allows applying all infos defined in the rootProject

## [1.0.0]

### Added

- Allow adding POM Meta info via ```publishingInfo```

[unreleased]: https://github.com/hndrs/gradle-publishing-info-plugin/compare/v3.0.0...HEAD

[3.0.0]: https://github.com/hndrs/gradle-publishing-info-plugin/compare/v2.0.2...v3.0.0

[2.0.0]: https://github.com/hndrs/gradle-publishing-info-plugin/compare/a9b56be382ab065e05c602815dba1d77536f6595...v2.0.0

[1.0.0]: https://github.com/hndrs/gradle-publishing-info-plugin/compare/a9b56be382ab065e05c602815dba1d77536f6595...v1.0.0
