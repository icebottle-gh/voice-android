# Resolve Version Incompatibility Issues

It was decided to reconfigure the project because certain functionalities were not working as expected. Android studio had trouble resolving imports. Gradle build was failing with errors related to version incompatibilities. Beside above two issues, IDE was recommending to upgrade AGP and Kotlin language version.

Below versions shall be used now.
- AGP 8.11.1
- Kotlin 2.2.0
- Gradle 8.13

Room database version is updated to 2.7.0-rc01 by personal discretion. I am assuming it will work well with Kotlin 2.1.0 and above from previous experience.

KAPT is replaced with KSP because upgraded Room database version requires KSP for annotation processing.

junit and espresso-core versions set to 1.1.5 and 3.5.1 respectively, as these versions are strictly required by ui-test-junit.

All above configuration changes are made in build.gradle.kts files of the project. A build was successfully generated.

---

Author: Muhammed Salih
Date: 2024-07-21