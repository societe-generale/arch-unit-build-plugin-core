# Changelog - see https://keepachangelog.com for conventions

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

## [2.5.1] - 2020-03-23

### Fixed
- bug fixed : excludedPaths not taken into account for preConfiguredRules 

## [2.5.0] - 2020-03-22

### Added
- PR #11 - HexagonalArchitectureTest now validates that no Dto / Vo classes are present in domain
- PR #14 - Possibility to exclude some classes from being analyzed - new config element is available from now on 

## [2.4.1] - 2019-11-29

### Changed
- PR #8 - HexagonalArchitectureTest now validates that no prohibited annotation is used
- PR #9 - Taking into account Junit5 @Disabled in the rules that were checking Junit4 @Ignore 
 

## [2.4.0] - 2019-11-15

### Changed
- PR #5 - introduced ScopePathProvider concept to allow build specific plugins to use their own

## [2.3.2] - 2019-11-11

### Changed
- Enabling to run provided ArchUnit rules like GeneralCodingRules by enabling the instantiation of classes with private constructor  

## [2.3.1] - 2019-11-08

### Added
- PR #3 - new rule : HexagonalArchitectureTest
- PR #4 - new rule : DontReturnNullCollectionTest

### Changed
- PR #2 : upgraded to ArchUnit 0.12.0

## [2.3.0] - 2019-10-06

### initial version 
- we extracted the code from https://github.com/societe-generale/arch-unit-maven-plugin/blob/master/CHANGELOG.md#230---2019-10-06 and aligned the version


 
