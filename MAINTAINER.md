# Voice Android — Maintainer Notes

Operational documentation for building, CI/CD, and distribution.

---

## Build

```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

---

## CI/CD

Workflow file:

```
.github/workflows/build-and-deliver.yml
```

### Triggers

* Push to `main`, `master`
* Sprint branches (`S1`, `S2`, `S3`, …)
* Pull requests targeting these branches

Sprint branch pattern:

```
S[0-9]*
```

---

## Workflow Steps

1. Build debug APK
2. Upload APK as GitHub Artifact
3. Send email notification

Artifact retention: 30 days.

---

## Email Notifications

**Success**

* Push → build info + download link
* Pull request → PR info + download link

**Failure**

* Error summary
* Logs link

Recipients configured via repository secret.

---

## Downloading APK

1. Open workflow run
2. Navigate to Artifacts
3. Download APK

Repository access required.

---

## Required GitHub Secrets

* `EMAIL_TO`
* `EMAIL_FROM`
* `SMTP_USERNAME`
* `SMTP_PASSWORD` (Gmail app password)

SMTP guide:
https://support.google.com/accounts/answer/185833

---

## Release Notes (Current Approach)

* Sprint branches represent development cycles
* Debug APK distributed via CI artifacts
* Release APK built manually when needed

---

## Maintenance Checklist

* Verify CI passes for pull requests
* Confirm artifact generation
* Use the apk artifact for usability testing
* Accept or respond to pull requests
* Update docs when workflows change
