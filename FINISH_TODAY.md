# TripPlanner Part 3: finish-today guide (solo backup)

Everything for Part 3 is already written in this project. You do **not** need to paste code.
Your job: open it, make it compile, connect Firebase, test on your phone, record the video, submit.

Time budget (started 07:45, deadline 23:59). Aim to be **submitted by 20:00**, so a late problem can't sink you.

| When | Do |
|---|---|
| 07:45 - 08:30 | Part A: open, build, run on the phone |
| 08:30 - 09:30 | Part B: Firebase (your own project) |
| 09:30 - 11:00 | Part C: test every feature |
| 11:00 - 13:00 | Part D: check Part 1 features, translations, README |
| 13:00 - 15:00 | Part E: Play Store preparation (release build, listing) |
| 15:00 - 17:00 | Part F: record the video on the phone |
| 17:00 - 20:00 | Submit, then keep time spare for surprises |

---

## Part A: Open it and get it compiling

1. Unzip `TripPlanner-Part3-Final.zip`. You get a folder `TripPlanner`.
2. Android Studio -> **File -> Open** -> choose that `TripPlanner` folder (the one containing `settings.gradle.kts`).
3. Wait for the Gradle sync (first time: 5-15 minutes). Accept any "install SDK / JDK 17" prompts.
4. **Build -> Clean Project**, then **Build -> Rebuild Project**.
5. Plug in your phone (USB debugging on), pick it in the device dropdown, press **Run**.

**This code has never been compiled by its author** (it was written without Android Studio available), so a few red errors are possible.
Fix procedure: open the **Build** panel, copy the **first** line starting with `e:` (file name + message), and send it to Claude. Usually one fix clears many errors.

Quick fixes you may meet:

| Message | Fix |
|---|---|
| `Could not find com.google.firebase:firebase-bom:35.1.0` | Open `gradle/libs.versions.toml`, change `firebaseBom` to the newest version on firebase.google.com/support/release-notes/android |
| `SDK location not found` | File -> Project Structure -> SDK Location, or let Android Studio create `local.properties` |
| `Duplicate class` / Java version errors | Settings -> Build Tools -> Gradle -> Gradle JDK = **17** |
| Gradle sync fails on `kotlin` / `ksp` | Check `libs.versions.toml` has `kotlin = "2.2.20"`, `ksp = "2.2.20-2.0.2"`, `room = "2.7.2"` |

## Part B: Firebase (your own project, about 20 minutes)

Do this yourself so you don't depend on anyone's account.

1. Go to https://console.firebase.google.com -> **Create a project** -> name `TripPlanner` -> Analytics off.
2. Click the **Android icon** (Add app):
   - Package name: `com.example.budgetx` (exactly)
   - Debug SHA-1: in Android Studio open the **Gradle** panel (elephant icon, right side) -> `app` -> `Tasks` -> `android` -> double-click **signingReport**. Copy the `SHA1:` line under **Variant: debug**.
   - **Register app** -> **Download google-services.json**.
3. Replace `app/google-services.json` with the downloaded file.
4. **Build -> Authentication -> Get started -> Sign-in method**:
   - Enable **Google** (choose a support email) and Save.
   - Enable **Anonymous** and Save. (The app uses it so cloud sync works for email/password users.)
5. Copy the **Web client ID**: Authentication -> Sign-in method -> Google -> *Web SDK configuration*. (It also appears in the new `google-services.json` as the `oauth_client` entry with `"client_type": 3`.) It looks like `1234567890-abcdef.apps.googleusercontent.com`.
6. Open `app/src/main/res/values/strings.xml` and replace `PASTE_WEB_CLIENT_ID_HERE` with it. (Just that value, between the tags.)
7. **Build -> Firestore Database -> Create database** (any region, production mode), then the **Rules** tab, paste, **Publish**:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{email}/{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```
8. **Build -> Storage -> Get started.** Firebase may demand the paid *Blaze* plan (credit card) for new projects.
   - If it lets you in: Rules tab, paste and publish:
     ```
     rules_version = '2';
     service firebase.storage {
       match /b/{bucket}/o {
         match /receipts/{email}/{file} {
           allow read, write: if request.auth != null;
         }
       }
     }
     ```
   - If it asks for billing: **skip it.** The app automatically stores a small copy of each receipt in Firestore instead, so everything still works. Mention in your report that Cloud Storage needs the paid plan.
9. Messaging (push) needs no setup: it is switched on automatically.
10. Run the app again. Every teammate who runs the app from their own laptop must add their own SHA-1 (Project settings -> Your apps -> Add fingerprint).

## Part C: Test every feature (tick them off)

Do these on the real phone.

- [ ] **Register**: new email + password; log in. (Password is stored hashed: PBKDF2.)
- [ ] **Stay logged in**: close the app completely, reopen: lands on the dashboard.
- [ ] **Google sign-in (SSO)**: log out, tap *Sign in with Google*, pick an account.
- [ ] **Settings**: Dashboard -> Settings. Change language to isiZulu, then Afrikaans: all screens change. Toggle notifications.
- [ ] **Create trip** (REST API), add itinerary, place, expense (with receipt photo), reminder.
- [ ] **Offline mode**: turn on **airplane mode**. Create a trip + an expense. The orange offline banner appears and the trip card says *Waiting to sync*. Turn airplane mode off: the card changes to *Synced* and a *Back online* notification appears.
- [ ] **Cloud check**: Firebase console -> Firestore -> `users` -> your email -> `expenses` shows the expense. The REST API shows trips: open `https://tripplanner-9wey.onrender.com/api/trips` in a browser (first load can take ~1 minute).
- [ ] **Push notification**: Settings screen shows a *Push token*. Tap it to copy. Firebase console -> Messaging -> new notification -> **Send test message** -> paste the token. The notification arrives. (You can also send to the topic `trip_updates`.)
- [ ] **Reminder** notification fires at the chosen time.
- [ ] **Icon**: the launcher shows the blue map-pin icon, not the Android robot.

If **Google sign-in** says "[28444]" or "developer console not set up": the SHA-1 or Web client ID is wrong. Wait 5 minutes after adding the SHA-1, then try again.

## Part D: Check what the rubric also marks

- Open your **Part 1** document and tick each feature against the app. Part 3 says "your features specified in Part 1" carry marks.
- Have a Zulu speaker check `res/values-zu/strings.xml` (best-effort translation). Afrikaans is in `values-af`.
- Update `README.md` with: features, the Firebase/REST/NoSQL/blob architecture, how to build.
- Optional but good: the **REST API** was upgraded (`backend/server.js`: notes, owner, retry-safe uploads, per-user filtering). To use it, push the `backend` folder to the GitHub repo that Render deploys. The app also works with the old server.

## Part E: Play Store preparation

1. **Package name**: Google Play rejects `com.example.*`. If you really publish, choose e.g. `com.yourgroup.tripplanner`, change only `applicationId` in `app/build.gradle.kts`, then re-register the app in Firebase and replace `google-services.json` again. For the assignment ("prepare for publication") you can document this instead.
2. **Target API**: Google Play now requires new apps to target **API 36** (from 31 Aug 2026). This project targets 35 and AGP 8.6. To really upload, use *Tools -> AGP Upgrade Assistant*, then set `compileSdk = 36` and `targetSdk = 36`. Do this **last**, on a copy, after everything works.
3. **Signing key**: Build -> **Generate Signed App Bundle / APK** -> Android App Bundle -> *Create new* keystore. Store the `.jks` file and passwords somewhere safe (never commit them). Optionally create `keystore.properties` in the project root:
   ```
   storeFile=C:/path/to/my-release-key.jks
   storePassword=YOUR_STORE_PASSWORD
   keyAlias=YOUR_KEY_ALIAS
   keyPassword=YOUR_KEY_PASSWORD
   ```
   Then `./gradlew bundleRelease` signs automatically. Output: `app/build/outputs/bundle/release/app-release.aab`.
4. **Assets** are ready in `play-store-assets/`: `play-store-icon-512.png` and `play-store-feature-graphic-1024x500.png`. Take 2+ phone screenshots yourself.
5. **Store listing text** (paste into Play Console):
   - App name: `TripPlanner`
   - Short description (80 chars): `Plan trips, track expenses and keep receipts, even offline.`
   - Full description: `TripPlanner helps you plan every part of a trip: create trips, build a day-by-day itinerary, save places, view the destination on a map, set reminders and track expenses with receipt photos. Everything works offline and syncs to the cloud when you reconnect. Available in English, isiZulu and Afrikaans. Sign in with email or Google.`
6. **Also needed in Play Console**: a privacy policy URL (the app collects email addresses and receipt photos via Firebase), the Data safety form, content rating questionnaire, and a testing track. Take screenshots of each of these steps for your report.

## Part F: Video script (record on the phone, screen recording)

Show, in this order (about 4-5 minutes). Say what you are doing out loud.

1. App icon on the home screen, open the app.
2. **Register** a new user, then **log in**. Mention the password is hashed.
3. Log out, **sign in with Google (SSO)**.
4. **Settings**: change language (English -> isiZulu -> Afrikaans), toggle notifications.
5. **Create a trip** (REST API), add an itinerary item, saved place, map, reminder.
6. **Add an expense with a receipt photo.**
7. **Offline**: airplane mode on, create a trip, point at the *Waiting to sync* label and the offline banner; airplane mode off, show *Synced* and the *Back online* notification.
8. **Cloud proof**: show Firestore (expense document) and the REST API in the browser.
9. **Push notification**: send a test message from the Firebase console; show it arriving.
10. Close with the app icon and the Play Store assets.

## If something breaks

| Problem | What to do |
|---|---|
| Build error | Copy the first `e:` line to Claude |
| App crashes on start | Logcat tab (bottom of Android Studio), filter by `AndroidRuntime`, copy the red text |
| Google sign-in fails | SHA-1 / Web client ID (Part B steps 2, 5, 6) |
| Sync never happens | Firebase **Anonymous** sign-in not enabled, or Firestore rules not published |
| Teammates' parts arrive | Commit this version first (`git add . && git commit -m "Part 3 backup"`) on its own branch; merge their work only if it's clearly better |
