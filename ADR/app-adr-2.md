# Change package name and project name

A new package name org.noormahal.vp25.android and project name VP25 is chosen to succeed the temporary package name and project name which was used earlier.

Below steps needs to be executed on Android Studio to cleanup old project residues and start fresh after this update.

In Android Studio:
1. Invalidate Caches:
   - File → Invalidate Caches...
   - Check ✅ Clear file system cache and Local History
   - Check ✅ Clear downloaded shared indexes
   - Click Invalidate and Restart
2. After restart, rebuild:
   - Build → Clean Project
   - Build → Rebuild Project
3. Check Run Configuration:
   - Top toolbar → Click dropdown next to ▶️ (Run button)
   - Select Edit Configurations...
   - Look for Module: field → should say VP25.app
   - If it shows old package, delete the configuration and Android Studio will auto-create a new one
4. Uninstall from device again: # From Android Studio Terminal
   adb uninstall com.example.temp
   adb uninstall org.noormahal.vp25.android  # Just in case
5. Run the app fresh
   Errors could happen because Android Studio's cache/configuration is holding onto the old package name. Invalidating caches should fix it.

---

Author: Muhammed Salih
Date: 2026-02-22