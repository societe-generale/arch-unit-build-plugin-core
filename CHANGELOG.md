# Changelog - see https://keepachangelog.com for conventions

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

## [2.7.1] - 2021-01-27

### Fixed
- PR-45 : replacing dots in path only for the package path - thanks @markusschaefer !! 

## [2.7.0] - 2021-01-22

### Changed
- PR-43 : upgrading to ArchUnit - thanks @nvervelle !! 

## [2.6.1] - 2020-08-19

### Fixed
- logging properly the full path when we fail to load the resources to analyze

## [2.6.0] - 2020-08-19

### Changed
- PR-39 : BREAKING change in some interfaces. Introducing `RootClassFolder` for strong typing, and using it as return type in ScopePathProvider.
- PR-38 : BREAKING change in `Log` interface

## [2.5.3] - 2020-06-03

### Changed
- PR-18 : naming rule for constants
- PR-20 : final fields should be static
- PR-22 : test classes should follow naming convention
- PR-23 : test method should follow naming convention 
- PR-25 : fields ending by "Date" should not be String 


## [2.5.2] - 2020-03-23

### Fixed
- PR 17 : avoiding System.out calls, and enabling rules to log properly 

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


 
