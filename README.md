# TripPlanner

TripPlanner is an Android app for planning trips end-to-end: create a trip, build a
day-by-day itinerary, save places you want to visit, track expenses (with optional
receipt photos), view your destination on a map, and set reminders for important
dates. Trip data is stored locally on-device using Room, with a lightweight REST
integration for looking up real places by name.

## Features
- **Accounts** — register and log in with a hashed password (PBKDF2 + per-user salt).
- **Settings** — update your name/email and change your password from the Dashboard.
- **Trips** — create a trip with a destination, date range and notes.
- **Itinerary** — add day-by-day activities with times and locations, and mark them complete.
- **Saved Places** — search for a real place by name (via a REST API), pick it from the
  results to auto-fill its address and coordinates, or enter details manually.
- **Map** — view your trip's destination on a map.
- **Expenses** — log expenses by category, attach a receipt photo, and see a running total.
- **Reminders** — schedule a local notification for a specific date and time.

## Project structure
app/src/main/java/com/example/budgetx/
├── data/
│ ├── entity/ # Room entities (User, Trip, Activity, SavedPlace, Expense, Reminder)
│ ├── dao/ # Room DAOs
│ └── AppDatabase.kt
├── network/
│ └── NominatimApiService.kt # REST client for OpenStreetMap's Nominatim place search
├── util/
│ ├── PasswordHasher.kt # PBKDF2 password hashing/verification
│ └── DateTimeUtils.kt # Date/time parsing shared by Reminders
└── *Activity.kt # One screen per Activity, using ViewBinding

## Architecture
- **UI**: one `AppCompatActivity` per screen with `ViewBinding` (no fragments/navigation
  component — kept intentionally simple for this module).
- **Persistence**: Room (SQLite) for all on-device data. `AppDatabase` is a singleton
  accessed via `AppDatabase.getDatabase(context)`. All DAO calls run on
  `Dispatchers.IO` inside `lifecycleScope.launch { }` from the Activities.
- **Security**: passwords are never stored in plain text. `PasswordHasher` hashes
  passwords with PBKDF2WithHmacSHA256, a random 16-byte salt per user, and 10,000
  iterations; the stored value is `iterations:saltBase64:hashBase64`.
- **Networking**: `NominatimApiService` calls OpenStreetMap's free Nominatim geocoding
  REST API (no API key required) to search for places by name. Requests run on a
  background thread via `withContext(Dispatchers.IO)`; on failure or no matches it
  returns an empty list so the UI can fall back to manual entry. Results (name,
  latitude, longitude) are shown in `SavedPlacesActivity` and persisted into the
  existing `SavedPlace` Room entity when the user saves.

## Setup / build instructions
1. Open the project root (this folder) in Android Studio.
2. Let Gradle sync — the project uses the version catalog in `gradle/libs.versions.toml`.
3. Run on an emulator or device with **API 26+** (`minSdk = 26`).
4. First run: register a new account from the login screen, then log in.
5. Saved Places' search requires an internet connection (`INTERNET` permission is
   already declared in the manifest).

### Running tests
- **Unit tests** (pure Kotlin/JVM — password hashing, date/time helpers):

./gradlew testDebugUnitTest

- **Instrumented tests** (Room DAOs, run on a device/emulator):

./gradlew connectedDebugAndroidTest

- A GitHub Actions workflow (`.github/workflows/android-ci.yml`) runs the unit test
  suite automatically on every push and pull request.

## Notes
- The local database version was bumped when passwords moved from plain text to
  hashed storage; `fallbackToDestructiveMigration()` means upgrading clears any
  existing local data — expected at this stage of the project.
- Notifications require the runtime `POST_NOTIFICATIONS` permission on Android 13+;
  the Reminders screen requests it automatically the first time it's opened.
- Nominatim's usage policy caps free use at roughly 1 request/second — fine for a
  single user searching by hand, but not meant for bulk/automated querying.
