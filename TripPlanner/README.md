TripPlanner

TripPlanner is an Android application created to help users organise trips in one place. TripPlanner allows users to create trips manage itineraries, save places, record travel expenses view destinations on a map and set reminders.

Features

User Accounts

Register an account by providing a name, an email address and a password.

Log in to TripPlanner using registered credentials.

User and trip information is stored locally on the device.

Trip Management

Create trips with:

Trip name

Destination

Start date

End date

Optional notes

View all trips belonging to the logged-in user.

Open a trip to access trip planning features.

Itinerary

Add activities to a trip.

Set an activity date and optional start and end times.

Add descriptions and locations.

Mark activities as completed.

View activities in date and time order.

Saved Places

Save places that you want to visit during a trip.

Store a place name, an address and optional notes.

View saved places for each trip.

Maps

View the selected trip destination using OpenStreetMap through an Android WebView.

The destination is used as the map search query.

Expenses

Add expenses to a trip.

Record:

Amount

Category

Description

Date

Optional receipt/photo path

View expense history.

View the expenses for a trip.

Expense records are stored locally.

Reminders

Create reminders for a trip.

Select a reminder date and time.

Schedule local Android notifications.

Reminders are stored locally in the Room database.

Technology Stack

Platform: Android

Language: Kotlin

UI: XML layouts with ViewBinding

Architecture: Activity-based Android application

Database: Room / SQLite

Database version: Room 2.6.1

Android Gradle Plugin: 8.3.0

Kotlin: 1.9.22

Compile SDK: 34

Target SDK: 34

Minimum SDK: 26

Build system: Gradle, with Kotlin DSL

Dependency processing: KSP

Maps: OpenStreetMap loaded through WebView

Notifications: Android AlarmManager and NotificationManager

Project Structure

TripPlanner/

├── app/

│   ├── src/

│   │   ├── main/

│   │   │   ├── java/com/example/budgetx/

│   │   │   │   ├── LoginActivity.kt

│   │   │   │   ├── RegisterActivity.kt

│   │   │   │   ├── DashboardActivity.kt

│   │   │   │   ├── CreateTripActivity.kt

│   │   │   │   ├── TripDetailsActivity.kt

│   │   │   │   ├── ItineraryActivity.kt

│   │   │   │   ├── SavedPlacesActivity.kt

│   │   │   │   ├── MapActivity.kt

│   │   │   │   ├── AddExpenseActivity.kt

│   │   │   │   ├── HistoryActivity.kt

│   │   │   │   ├── RemindersActivity.kt

│   │   │   │   ├── ReminderReceiver.kt

│   │   │   │   └── data/

│   │   │   │       ├── AppDatabase.kt

│   │   │   │       ├── dao/

│   │   │   │       └── entity/

│   │   │   └── res/

│   │   │       ├── layout/

│   │   │       ├── drawable/

│   │   │       ├── mipmap-*/

│   │   │       ├── values/

│   │   │       └─ xml/

│   │   ├── test/

│   │   └── androidTest/

│   ├── build.gradle.kts

│   └── proguard-rules.pro

├── build.gradle.kts

├── gradle/

│   ├── libs.versions.toml

│   └── wrapper/

├── gradle.properties

├── settings.gradle.kts

├── gradlew

└── gradlew.bat

Database

TripPlanner uses Room for local data storage.

The database is named:

trip_planner_database

The main database entities are:

Entity

Purpose

User

Stores registered user accounts

Trip

Stores trip information

Activity

Stores activities

SavedPlace

Stores places saved for a trip

Expense

Stores trip expenses

Reminder

Stores trip reminders

The relationships are organised around the user and trip:

User

└── Trips

├── Activities

├── Saved Places

├── Expenses

└── Reminders

Deleting a parent user or a trip can cause related records to be deleted automatically because the Room foreign‑key relationship is set to cascade deletion.

User Flow

Launch App

↓

Login / Register

↓

Dashboard

↓

Create or Select Trip

↓

Trip Details

├── Itinerary

├── Saved Places

├── Map

├── Expenses

└── Reminders

Requirements

Before running the project install:

Android Studio

Android SDK 34

JDK 17

An Android emulator or physical Android device

Gradle wrapper included with the project

TripPlanner supports devices running Android API 26 or higher.

Installation and Setup

Clone the project.

Open the TripPlanner Android project in Android Studio.

Allow Android Studio to sync the Gradle project.

Make sure the Android SDK required by the project is installed.

Start an Android emulator with API 26 or higher or connect a physical Android device.

Run the TripPlanner app configuration.

When TripPlanner launches create an account using the registration screen.

Log in to TripPlanner. Create your first trip.

Building from the Command Line

On Windows:

gradlew.bat assembleDebug

On macOS/Linux:

./gradlew assembleDebug

The generated debug APK can normally be found under:

app/build/outputs/apk/debug/

Testing

Unit tests can be run with:

./gradlew testDebugUnitTest

On Windows:

gradlew.bat

Instrumented Android tests can be run with:

./gradlew connectedDebugAndroidTest

On Windows:

gradlew.bat connectedDebugAndroidTest

Permissions

TripPlanner declares the following Android permissions/features:

CAMERA. Supports camera‑related/receipt functionality.

INTERNET. Required for loading the map through OpenStreetMap.

POST_NOTIFICATIONS. Required for notifications on supported Android versions.

Camera hardware is optional.

On Android 13 and newer notification permission may need to be granted before reminders can display notifications.

Data Storage

TripPlanner currently uses an on‑device Room database than a remote database.

This means:

Trip information is stored locally.

Expenses are stored locally.

Itinerary activities are stored locally.

Saved places are stored locally.

Reminders are stored locally.

Data is associated with the user account.

The database uses fallbackToDestructiveMigration(). During database‑version changes existing local database data may be cleared rather than migrated.

Security Note

The current implementation performs credential checking against the password stored in the Room User entity. The project should therefore be treated as a local/student application than a production authentication system.

For a production release authentication should be moved to a backend and passwords should be stored using a modern password‑hashing approach such as Argon2id, bcrypt or an appropriately configured PBKDF2 implementation.

Map Functionality

The map screen loads an OpenStreetMap search page in a WebView using the trip destination.

An internet connection is required for the map page to load.

Notifications

Reminders use Androids AlarmManager and a BroadcastReceiver.

When a scheduled reminder is triggered:

Android sends the alarm broadcast.

ReminderReceiver receives the broadcast.

A notification channel is created if necessary.

A TripPlanner reminder notification is displayed.

Development Notes

TripPlanner uses:

AppCompatActivity for screens.

ViewBinding for connecting Kotlin code to XML layouts.

Kotlin coroutines with lifecycleScope.

Dispatchers.IO for database operations.

Room DAOs for database access.

RecyclerView where list‑based UI requires it.

XML layouts than Jetpack Compose.

The package namespace is:

com.example.budgetx

The Android application ID is:

com.example.budgetx

Future Improvements

Potential improvements include:

Remote/cloud database support.

Secure server‑based authentication.

Proper password. Account recovery.

Google. Another dedicated map SDK.

Deleting trips.

Deleting itinerary activities.

Deleting saved places.

Deleting reminders.

Recurring reminders.

Expense filtering by category and date.

Expense. Spending summaries.

Trip sharing, between users.

Cloud. Synchronisation.

Automated CI/CD builds.

Expanded. Instrumented test coverage.

Project Status

TripPlanner is an Android trip‑planning application that keeps data on the device and lets you create and manage trips, itineraries, saved places, expenses, maps and reminders.TripPlanner

TripPlanner is an Android application created to help users organise trips in one place. TripPlanner allows users to create trips manage itineraries, save places, record travel expenses view destinations on a map and set reminders.

Features

User Accounts

Register an account by providing a name, an email address and a password.

Log in to TripPlanner using registered credentials.

User and trip information is stored locally on the device.

Trip Management

Create trips with:

Trip name

Destination

Start date

End date

Optional notes

View all trips belonging to the logged-in user.

Open a trip to access trip planning features.

Itinerary

Add activities to a trip.

Set an activity date and optional start and end times.

Add descriptions and locations.

Mark activities as completed.

View activities in date and time order.

Saved Places

Save places that you want to visit during a trip.

Store a place name, an address and optional notes.

View saved places for each trip.

Maps

View the selected trip destination using OpenStreetMap through an Android WebView.

The destination is used as the map search query.

Expenses

Add expenses to a trip.

Record:

Amount

Category

Description

Date

Optional receipt/photo path

View expense history.

View the expenses for a trip.

Expense records are stored locally.

Reminders

Create reminders for a trip.

Select a reminder date and time.

Schedule local Android notifications.

Reminders are stored locally in the Room database.

Technology Stack

Platform: Android

Language: Kotlin

UI: XML layouts with ViewBinding

Architecture: Activity-based Android application

Database: Room / SQLite

Database version: Room 2.6.1

Android Gradle Plugin: 8.3.0

Kotlin: 1.9.22

Compile SDK: 34

Target SDK: 34

Minimum SDK: 26

Build system: Gradle, with Kotlin DSL

Dependency processing: KSP

Maps: OpenStreetMap loaded through WebView

Notifications: Android AlarmManager and NotificationManager

Project Structure

TripPlanner/

├── app/

│   ├── src/

│   │   ├── main/

│   │   │   ├── java/com/example/budgetx/

│   │   │   │   ├── LoginActivity.kt

│   │   │   │   ├── RegisterActivity.kt

│   │   │   │   ├── DashboardActivity.kt

│   │   │   │   ├── CreateTripActivity.kt

│   │   │   │   ├── TripDetailsActivity.kt

│   │   │   │   ├── ItineraryActivity.kt

│   │   │   │   ├── SavedPlacesActivity.kt

│   │   │   │   ├── MapActivity.kt

│   │   │   │   ├── AddExpenseActivity.kt

│   │   │   │   ├── HistoryActivity.kt

│   │   │   │   ├── RemindersActivity.kt

│   │   │   │   ├── ReminderReceiver.kt

│   │   │   │   └── data/

│   │   │   │       ├── AppDatabase.kt

│   │   │   │       ├── dao/

│   │   │   │       └── entity/

│   │   │   └── res/

│   │   │       ├── layout/

│   │   │       ├── drawable/

│   │   │       ├── mipmap-*/

│   │   │       ├── values/

│   │   │       └─ xml/

│   │   ├── test/

│   │   └── androidTest/

│   ├── build.gradle.kts

│   └── proguard-rules.pro

├── build.gradle.kts

├── gradle/

│   ├── libs.versions.toml

│   └── wrapper/

├── gradle.properties

├── settings.gradle.kts

├── gradlew

└── gradlew.bat

Database

TripPlanner uses Room for local data storage.

The database is named:

trip_planner_database

The main database entities are:

Entity

Purpose

User

Stores registered user accounts

Trip

Stores trip information

Activity

Stores activities

SavedPlace

Stores places saved for a trip

Expense

Stores trip expenses

Reminder

Stores trip reminders

The relationships are organised around the user and trip:

User

└── Trips

├── Activities

├── Saved Places

├── Expenses

└── Reminders

Deleting a parent user or a trip can cause related records to be deleted automatically because the Room foreign‑key relationship is set to cascade deletion.

User Flow

Launch App

↓

Login / Register

↓

Dashboard

↓

Create or Select Trip

↓

Trip Details

├── Itinerary

├── Saved Places

├── Map

├── Expenses

└── Reminders

Requirements

Before running the project install:

Android Studio

Android SDK 34

JDK 17

An Android emulator or physical Android device

Gradle wrapper included with the project

TripPlanner supports devices running Android API 26 or higher.

Installation and Setup

Clone the project.

Open the TripPlanner Android project in Android Studio.

Allow Android Studio to sync the Gradle project.

Make sure the Android SDK required by the project is installed.

Start an Android emulator with API 26 or higher or connect a physical Android device.

Run the TripPlanner app configuration.

When TripPlanner launches create an account using the registration screen.

Log in to TripPlanner. Create your first trip.

Building from the Command Line

On Windows:

gradlew.bat assembleDebug

On macOS/Linux:

./gradlew assembleDebug

The generated debug APK can normally be found under:

app/build/outputs/apk/debug/

Testing

Unit tests can be run with:

./gradlew testDebugUnitTest

On Windows:

gradlew.bat

Instrumented Android tests can be run with:

./gradlew connectedDebugAndroidTest

On Windows:

gradlew.bat connectedDebugAndroidTest

Permissions

TripPlanner declares the following Android permissions/features:

CAMERA. Supports camera‑related/receipt functionality.

INTERNET. Required for loading the map through OpenStreetMap.

POST_NOTIFICATIONS. Required for notifications on supported Android versions.

Camera hardware is optional.

On Android 13 and newer notification permission may need to be granted before reminders can display notifications.

Data Storage

TripPlanner currently uses an on‑device Room database than a remote database.

This means:

Trip information is stored locally.

Expenses are stored locally.

Itinerary activities are stored locally.

Saved places are stored locally.

Reminders are stored locally.

Data is associated with the user account.

The database uses fallbackToDestructiveMigration(). During database‑version changes existing local database data may be cleared rather than migrated.

Security Note

The current implementation performs credential checking against the password stored in the Room User entity. The project should therefore be treated as a local/student application than a production authentication system.

For a production release authentication should be moved to a backend and passwords should be stored using a modern password‑hashing approach such as Argon2id, bcrypt or an appropriately configured PBKDF2 implementation.

Map Functionality

The map screen loads an OpenStreetMap search page in a WebView using the trip destination.

An internet connection is required for the map page to load.

Notifications

Reminders use Androids AlarmManager and a BroadcastReceiver.

When a scheduled reminder is triggered:

Android sends the alarm broadcast.

ReminderReceiver receives the broadcast.

A notification channel is created if necessary.

A TripPlanner reminder notification is displayed.

Development Notes

TripPlanner uses:

AppCompatActivity for screens.

ViewBinding for connecting Kotlin code to XML layouts.

Kotlin coroutines with lifecycleScope.

Dispatchers.IO for database operations.

Room DAOs for database access.

RecyclerView where list‑based UI requires it.

XML layouts than Jetpack Compose.

The package namespace is:

com.example.budgetx

The Android application ID is:

com.example.budgetx

Future Improvements

Potential improvements include:

Remote/cloud database support.

Secure server‑based authentication.

Proper password. Account recovery.

Google. Another dedicated map SDK.

Deleting trips.

Deleting itinerary activities.

Deleting saved places.

Deleting reminders.

Recurring reminders.

Expense filtering by category and date.

Expense. Spending summaries.

Trip sharing, between users.

Cloud. Synchronisation.

Automated CI/CD builds.

Expanded. Instrumented test coverage.

Project Status

TripPlanner is an Android trip‑planning application that keeps data on the device and lets you create and manage trips, itineraries, saved places, expenses, maps and reminders.
