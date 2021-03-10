# Gradle Publishing Info

This is a small helper plugin to add meta information to maven publications.

## Using the Plugin

Add the following dependency to you plugin block in your rootProject

```kotlin
plugins {
    id("io.hndrs.publishing-info").version("2.0.0")
}
```

The Plugin automatically registers a ```publishingInfo``` extension on all projects

#### Configuring the RootProject

```kotlin
publishingInfo {
    name = "Root Project"
    description = "Sample RootProject Description"
    inceptionYear = "2021"
    url = "https://github.com/hndrs/gradle-publishing-info-plugin"
    license = License(
        "https://github.com/hndrs/gradle-publishing-info-plugin/blob/main/LICENSE",
        "MIT License"
    )
    developers = listOf(
        Developer("maintainerid", "Maintainers Name", "maintainer@email.com")
    )
    contributers = listOf(
        Contributor("Contributer Name", "contributers email")
    )
    organization = Organization("Your Org", "https://yourdomain.com")
    scm = Scm(
        "scm:git:git://github.com/hndrs/gradle-publishing-info-plugin",
        "https://github.com/hndrs/gradle-publishing-info-plugin"
    )
}
```

> RootProject gradle file (build.gradle.kts)

#### Configuring SubProjects

```kotlin
publishingInfo {
    // applies all values from rootProject publishingInfo block
    applyFromRoot = true
    // overrides name
    name = "Sub Project"
    // overrides description
    description = "Sample SubProject Description"
}
```
