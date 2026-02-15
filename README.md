# Voice Android

An Android application for VP25.

## Project Structure

- `app/` - Main application module
- `.github/workflows/` - GitHub Actions CI/CD workflows

## Development

### Prerequisites

- JDK 17
- Android SDK
- Gradle (wrapper included)

### Building

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

## CI/CD

This project uses GitHub Actions for continuous integration and delivery.

### Automated Workflows

The build workflow (`.github/workflows/build-and-deliver.yml`) runs on:
- Push to `master`, `main`, or sprint branches (`S1`, `S2`, etc.)
- Pull requests targeting these branches

### What the workflow does:

1. **Build** - Compiles the debug APK
2. **Upload** - Stores APK as GitHub Artifact (30-day retention)
3. **Notify** - Sends email notification to the team

### Email Notifications

**On successful build:**
- **Push to branch**: Email with build details and download link
- **Pull request**: Email with PR info and download link

**On build failure:**
- Email with error details and link to logs

All emails are sent to the configured team email (`EMAIL_TO` secret).

### Downloading APKs

APKs are available as GitHub Artifacts:
1. Click the download link in the email
2. Log into GitHub (if not already)
3. Navigate to the workflow run
4. Download the APK from the Artifacts section

**Note:** GitHub Artifacts require repository access to download.

### Required Secrets

Configure these in GitHub repository settings:

- `EMAIL_TO` - Recipient email address
- `EMAIL_FROM` - Sender email address
- `SMTP_USERNAME` - Gmail SMTP username
- `SMTP_PASSWORD` - Gmail app password ([setup guide](https://support.google.com/accounts/answer/185833))

### Workflow Configuration

Sprint branch naming: `S1`, `S2`, `S3`, etc. (matches pattern `S[0-9]*`)

APK retention: 30 days (configurable in workflow file)
