# Voice Android — VP25

Voice Android is the Android client for VP25 (Voice Project 2025), a project exploring a clean social platform for broadcasting short-lived verbal messages.

The application is currently in a primitive prototype stage and will evolve through experimentation.

## Concept

VP25 introduces **Voice Story** — short voice or text broadcasts that disappear after 24 hours, enabling lightweight and intentional sharing.

The prototype focuses on simple identity, controlled audience selection, and subscription-based feeds.

## Phase-1 Prototype Features

* Login using mobile number and one-time password (OTP)
* Subscribe/unsubscribe feature between users
* Ability to send disappearing textual broadcasts to subscribers
* Custom audience targeting among subscribers (family, colleagues, etc.)

---

## Technology

* Kotlin
* Jetpack Compose

The architecture and supporting technologies may change as the project evolves.

## Project Structure

* `app/` — Main Android application module
* `.github/workflows/` — CI/CD automation

## Getting Started

### Requirements

* Android Studio (latest stable)
* JDK 17+
* Android SDK

### Clone

Clone the repo. Open in Android Studio and run on an emulator or device.

### Build via CLI

```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

---

## Contributing

See **CONTRIBUTING.md** for contribution guidelines and workflow.

## License

Source code is released under the MIT License.
See `LICENSE.md`.

## Trademark

Project name and brand assets are not covered by the MIT license.
See `TRADEMARK.md`.
