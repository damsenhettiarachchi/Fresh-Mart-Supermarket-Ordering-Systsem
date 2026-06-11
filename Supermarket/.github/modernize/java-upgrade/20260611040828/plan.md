# Upgrade Plan: Supermarket (20260611040828)

- **Generated**: 2026-06-11 04:08:00
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- Java 17.0.12: C:\Program Files\Java\jdk-17\bin (current project JDK, used by step 2)
- Java 21: **<TO_BE_INSTALLED>** (required by step 3)
- Java 23.0.2: C:\Program Files\Java\jdk-23\bin (available but not target LTS)
- Java 24.0.1: C:\Users\damse\.jdks\openjdk-24.0.1\bin (available but not target LTS)

**Build Tools**
- Maven Wrapper: 3.9.11 (compatible with Java 21)

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: N/A
- Run tests before and after the upgrade: true

## Upgrade Goals

- Upgrade project runtime to Java 21 LTS

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- |
| Java | 17 | 21 | User requested upgrade to latest LTS |
| Spring Boot | 3.5.6 | 3.5.6 | Current version already supports Java 21 |
| Maven Wrapper | 3.9.11 | 3.9.0 | Compatible with Java 21 |
| maven-compiler-plugin | managed by Spring Boot parent | 3.11.0* recommended | Explicit `<release>` needed for JDK 21 target |
| lombok | managed by Spring Boot parent | 1.18.x | Should remain compatible with Java 21 |
| jakarta.persistence-api | 3.1.0 | 3.1.0 | Already Jakarta-compatible |
| hibernate-core | 6.4.4.Final | 6.x | Already compatible with Spring Boot 3.x and Jakarta |

## Derived Upgrades

- Upgrade `<java.version>` in `pom.xml` from `17` to `21` because the project runtime target is Java 21.
- Add explicit `<release>${java.version}</release>` to `maven-compiler-plugin` configuration to ensure compilation targets Java 21.
- Keep Spring Boot at `3.5.6` because it already supports Java 21 and no Spring Boot version change is required for this runtime upgrade.
- No Maven wrapper upgrade is needed because `3.9.11` is compatible with Java 21.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|------------|---------|--------|--------|--------|
| pom.xml | `<java.version>` | 17 | upgrade | 21 | User requested latest LTS runtime |
| pom.xml | `maven-compiler-plugin` configuration | none | add | `<release>${java.version}</release>` | Ensure compiler targets Java 21 explicitly |

### Source Code Changes

| File | Location | Current | Required Change | Reason |
|------|----------|---------|----------------|--------|
| None detected | N/A | N/A | N/A | Project already uses Jakarta and Spring Boot 3.x APIs |

### Configuration Changes

| File | Property/Setting | Current | Required Change | Reason |
|------|------------------|---------|----------------|--------|
| None | N/A | N/A | N/A | No application properties require Java 21-specific changes |

### CI/CD Changes

| File | Location | Current | Required Change |
|------|----------|---------|----------------|
| None detected | N/A | N/A | N/A |

### Risks & Warnings

- **No version control detected**: Changes will not be tracked in Git. Review `plan.md` and `progress.md` carefully before applying changes.
- **Third-party driver compatibility**: `com.microsoft.sqlserver:mssql-jdbc` is runtime-managed and may require a newer driver if the current managed version is incompatible with Java 21. Mitigation: verify with clean compile and tests.
- **Lombok annotation processing**: JDK 21 compilation should be validated with explicit release and clean build to ensure annotation processing remains stable.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Ensure Java 21 is available for upgrade validation and confirm Maven Wrapper compatibility.
  - **Changes to Make**: Install JDK 21 if missing; verify Maven Wrapper can run under JDK 21.
  - **Verification**: `java -version` with JDK 21 available, `./mvnw -q -version` using wrapper.

- Step 2: Setup Baseline
  - **Rationale**: Verify the current project builds and tests successfully on the existing Java 17 environment.
  - **Changes to Make**: None to source or build files; capture baseline success/failure.
  - **Verification**: `./mvnw clean compile test-compile -q && ./mvnw clean test -q` using Java 17.

- Step 3: Upgrade Project to Java 21
  - **Rationale**: Apply the runtime target change and explicit compiler release setting required for Java 21.
  - **Changes to Make**: Update `pom.xml` `<java.version>` to `21`; add `<release>${java.version}</release>` to `maven-compiler-plugin` configuration.
  - **Verification**: `./mvnw clean test-compile -q` using JDK 21.

- Step 4: CVE Validation & Fix
  - **Rationale**: Scan direct dependencies for known vulnerabilities and resolve any reported issues without changing the overall target.
  - **Changes to Make**: Update dependency versions only if a CVE scan identifies direct dependency issues.
  - **Verification**: `./mvnw -q -DexcludeTransitive=true dependency:list` and CVE scan results.

- Step 5: Final Validation
  - **Rationale**: Confirm the project builds and tests fully under Java 21 and that the upgrade goal is met.
  - **Changes to Make**: Fix any compilation or test failures introduced by Java 21.
  - **Verification**: `./mvnw clean test -q` using JDK 21.
