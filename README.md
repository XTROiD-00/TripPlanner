# TripPlanner

An Android app for planning trips: create a trip, build a day-by-day itinerary, save places,
log expenses (with optional receipt photos), view the destination on a map, and set reminders.
All data is stored on-device with Room.

## Part 3 additions
- **Sign in with Google** (Firebase Auth + Credential Manager) alongside email/password, and the session is remembered.
- **Settings screen**: app language (English, isiZulu, Afrikaans), notifications on/off, sync now, push token, log out.
- **Offline mode with sync**: everything is saved to Room first; a WorkManager job uploads pending trips/expenses when the phone has internet again. Cards show *Waiting to sync* / *Synced*.
- **Cloud**: trips go to the REST API (`backend/`), expenses go to Cloud Firestore (NoSQL), receipt photos go to Firebase Storage (blob storage, with a Firestore fallback). Data is read back after a reinstall.
- **Push notifications**: Firebase Cloud Messaging (topic `trip_updates` + per-device token), plus a local "Back online" notice after an offline sync.
- **Multi-language**: all text lives in `res/values*/strings.xml`.
- **App icon and Play Store assets** in `play-store-assets/`. See `FINISH_TODAY.md` for setup, testing and release steps.

## Features
- **Accounts**: register and log in; passwords are hashed with PBKDF2WithHmacSHA256 (random salt per user).
- **Trips**: destination, date range and notes. A best-effort copy is also posted to the REST API in `backend/`.
- **Itinerary**: activities with date, times and location; tick them off as completed (saved).
- **Saved places**: name, address and notes per trip.
- **Map**: the trip destination on OpenStreetMap (WebView).
- **Expenses**: categories, receipt photo via the camera app, running total.
- **Reminders**: local notifications at a chosen date and time.

## Build
Requirements: Android Studio (Koala or newer), JDK 17, Android SDK 35.

1. Open **this folder** (the one containing `settings.gradle.kts`) in Android Studio.
2. Let Gradle sync (AGP 8.6, Kotlin 2.2.20, KSP 2.2.20-2.0.2, Room 2.7.2).
3. Run on a device or emulator with API 26+.
4. Register an account, then log in.

Command line: `./gradlew testDebugUnitTest assembleDebug`

If Android Studio offers an AGP upgrade you can accept it; the project also builds as-is.

## Project layout
```
app/src/main/java/com/example/budgetx/
  data/entity, data/dao, data/AppDatabase.kt   Room
  api/                                         Retrofit client for backend/
  util/                                        PasswordHasher, DateTimeUtils, UI helpers
  *Activity.kt                                 one screen each (ViewBinding)
backend/                                       optional Express + SQLite API
```

## Notes
- Upgrading from an older database version clears local data (`fallbackToDestructiveMigration`).
- Notifications need the POST_NOTIFICATIONS permission on Android 13+; the Reminders screen asks for it.
- The receipt camera uses the system camera app, so the app itself needs no CAMERA permission.

link to the video presentation:
https://youtu.be/wy9OzkZZv6o?si=WR4fPm_MB5lwpwc1
