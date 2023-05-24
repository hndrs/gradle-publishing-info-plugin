pluginManagement {
    includeBuild("../")
}

include("subproject1")
include("subproject2")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")