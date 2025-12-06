# Changelog - see https://keepachangelog.com for conventions

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

## [5.0.0] - 2025-12-07

### Changed

- Java 21 → 25
- JUnit 5/4 → JUnit 6 BOM
- Migration from Nexus Staging → `central-publishing-maven-plugin`
- Many libraries jumped several major versions
- Migration from `javax.inject` → `jakarta.inject`

#### Dependency version changes

| Library                | Old Version  | New Version  | Change                                   |
|------------------------|--------------|--------------|------------------------------------------|
| ArchUnit               | 1.3.0        | 1.4.1        | Upgrade                                  |
| Guava                  | 32.1.2-jre   | 33.5.0-jre   | Upgrade                                  |
| Apache Commons Lang    | 3.13.0       | 3.20.0       | Upgrade                                  |
| Roaster                | 2.28.0.Final | 2.30.3.Final | Upgrade                                  |
| javax.inject           | 1            | *Removed*    | Removed (Replaced by jakarta.inject-api) |
| jakarta.inject-api     | —            | 2.0.1        | Added replacement                        |
| Mockito Core           | 5.4.0        | 5.20.0       | Upgrade                                  |
| JUnit 4                | 4.13.2       | 4.13.2       | Same                                     |
| Joda-Time              | 2.12.5       | 2.14.0       | Upgrade                                  |
| Spring Beans           | 6.0.12       | 7.0.1        | Upgrade                                  |
| Powermock JUnit4       | 2.0.9        | 2.0.9        | Same                                     |
| Lombok                 | 1.18.30      | 1.18.42      | Upgrade                                  |
| AssertJ                | 3.24.2       | 3.27.6       | Upgrade                                  |
| SLF4J (nop)            | 2.0.7        | 2.0.17       | Upgrade                                  |
| JUnit Jupiter          | 5.10.0       | 6.0.1        | Upgrade (Major)                          |
| Jackson Annotations    | 2.15.2       | 2.20         | Upgrade                                  |
| Commons IO             | 2.14.0       | 2.21.0       | Upgrade                                  |
| jakarta.annotation-api | 2.1.1        | 3.0.0        | Upgrade                                  |
| jsr305                 | 3.0.2        | *Removed*    | Removed                                  |
| spotbugs-annotations   | —            | 4.9.8        | Added                                    |

#### Plugin version changes

| Plugin                     | Old Version | New Version | Change  |
|----------------------------|-------------|-------------|---------|
| maven-compiler-plugin      | 3.11.0      | 3.14.1      | Upgrade |
| maven-jar-plugin           | 3.3.0       | 3.5.0       | Upgrade |
| maven-surefire-plugin      | 3.1.2       | 3.5.4       | Upgrade |
| maven-deploy-plugin        | 3.1.1       | 3.1.4       | Upgrade |
| maven-release-plugin       | 3.0.1       | 3.3.0       | Upgrade |
| jacoco-maven-plugin        | 0.8.11      | 0.8.14      | Upgrade |
| maven-resources-plugin     | 3.3.1       | 3.4.0       | Upgrade |
| maven-source-plugin        | 3.3.0       | 3.4.0       | Upgrade |
| maven-javadoc-plugin       | 3.5.0       | 3.12.0      | Upgrade |
| maven-gpg-plugin           | 3.1.0       | 3.2.8       | Upgrade |
| nexus-staging-maven-plugin | 1.6.13      | *Removed*   | Removed |
| central-publishing-plugin  | —           | 0.9.0       | Added   |

#### Removed libraries

| Removed Library                                 | Reason                                                       |
|-------------------------------------------------|--------------------------------------------------------------|
| javax.inject:javax.inject                       | Replaced with jakarta.inject-api                             |
| com.google.code.findbugs:jsr305                 | Replaced with spotbugs-annotations                           |
| org.sonatype.plugins:nexus-staging-maven-plugin | Publishing model changed (new Sonatype Central plugin added) |

#### Added libraries

| Added Library                                              | Purpose                      |
|------------------------------------------------------------|------------------------------|
| jakarta.inject:jakarta.inject-api:2.0.1                    | Replacement for javax.inject |
| com.github.spotbugs:spotbugs-annotations:4.9.8             | Replacement for jsr305       |
| org.sonatype.central:central-publishing-maven-plugin:0.9.0 | New publishing workflow      |

## [4.0.1] - 2024-01-28

### Changed

- bumping jacoco for JDK 21
- bumping github action versions
- PR #82 : upgraded tests to Junit 5
- PR #86 : adding Jakarta validation and annotation in model domain
- PR #88 : add configuration switch to disable fallback to root directory

## [4.0.0] - 2023-11-16

### Changed

- PR 73 : upgrade to archunit 1.2.0, and now building with JDK 21 - Thanks @RoqueIT !!
- PR 75 : now considering jakarta.annotation.Nonnull - Thanks @paul58914080 !!

## [3.0.0] - 2022-10-08

### Changed

- PR 68 : upgrading to archunit 1.0.0 and JDK 11
- PR 69 : replacing custom java parsing code by Roaster library (https://github.com/forge/roaster)

## [2.9.6] - 2022-08-02

### Changed

- upgraded Lombok (to 1.18.24) to enable building with jdk 17
- PR 65 : finer logs to avoid cluttering when nothing gets analyzed - thanks @Treehopper !!
- various log related changes

### Fixed

- PR 67 : fix when parsing java file content - thanks @Treehopper !!

## [2.9.5] - 2022-05-29

### Changed

- fixed potential bug in DontReturnNullCollectionTest using isAssignableTo instead of
  isAssignableFrom. see also https://github.com/TNG/ArchUnit/issues/872
- simplified code in TestClassesNamingRuleTest

## [2.9.4] - 2022-05-25

### Changed

- optimized implementation of DontReturnNullCollectionTest, according
  to https://github.com/TNG/ArchUnit/issues/872#issuecomment-1136561118

## [2.9.3] - 2022-05-24

### Changed

- PR 62 : ApplyOn and ConfigurableRule are now serializable
- PR 63 : changed the implementation for DontReturnNullCollectionTest rule

## [2.9.2] - 2022-04-18

### Fixed

- somehow, the published 2.9.1 jar seemed to NOT contain the ifx it was supposed to contain (
  see https://github.com/societe-generale/arch-unit-build-plugin-core/issues/61). Performed a new
  release, and it seemed to be OK now.
- now publishing to most recent Nexus OSS server (
  see https://issues.sonatype.org/browse/OSSRH-79883)

## [2.9.1] - 2022-04-11

### Fixed

- PR 59 : fixed regression : now all rules allow empty class lists without failing

## [2.9.0] - 2022-04-10

### Changed

- PR 54 : upgrading to ArchUnit 0.23.1 - there may be breaking changes due to this
- PR 55 : tweak in DontReturnNullCollectionTest to avoid failing on lambdas.
- PR 58 : exception on serialVersionUID in ConstantsAndStaticNonFinalFieldsNamesRuleTest

## [2.8.0] - 2021-07-22

### Added

- PR-49 : BREAKING RuleInvokerService constructor signature has changed + some new logging methods
  need to be implemented.
  Can exclude generated source code that will be merged with the rest of the code later, based on a
  directory. thanks @KayWeinert !!

### Changed

- PR-50 : upgrading to ArchUnit 0.20.1

## [2.7.3] - 2021-03-30

### Changed

- upgrading to ArchUnit 0.17
- PR-48 : upgrading to Guava 30.1.1-jre

### Added

- PR-47 : ExclusionImportOption supports package excludes - thanks @KayWeinert !!

## [2.7.2] - 2021-02-01

### Changed

- upgrading to ArchUnit 0.16

## [2.7.1] - 2021-01-27

### Fixed

- PR-45 : replacing dots in path only for the package path - thanks @markusschaefer !!

## [2.7.0] - 2021-01-22

### Changed

- PR-43 : upgrading to ArchUnit 0.15 - thanks @nvervelle !!

## [2.6.1] - 2020-08-19

### Fixed

- logging properly the full path when we fail to load the resources to analyze

## [2.6.0] - 2020-08-19

### Changed

- PR-39 : BREAKING change in some interfaces. Introducing `RootClassFolder` for strong typing, and
  using it as return type in ScopePathProvider.
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
- PR #14 - Possibility to exclude some classes from being analyzed - new config element is available
  from now on

## [2.4.1] - 2019-11-29

### Changed

- PR #8 - HexagonalArchitectureTest now validates that no prohibited annotation is used
- PR #9 - Taking into account Junit5 @Disabled in the rules that were checking Junit4 @Ignore

## [2.4.0] - 2019-11-15

### Changed

- PR #5 - introduced ScopePathProvider concept to allow build specific plugins to use their own

## [2.3.2] - 2019-11-11

### Changed

- Enabling to run provided ArchUnit rules like GeneralCodingRules by enabling the instantiation of
  classes with private constructor

## [2.3.1] - 2019-11-08

### Added

- PR #3 - new rule : HexagonalArchitectureTest
- PR #4 - new rule : DontReturnNullCollectionTest

### Changed

- PR #2 : upgraded to ArchUnit 0.12.0

## [2.3.0] - 2019-10-06

### initial version

- we extracted the code
  from https://github.com/societe-generale/arch-unit-maven-plugin/blob/master/CHANGELOG.md#230---2019-10-06
  and aligned the version


 
