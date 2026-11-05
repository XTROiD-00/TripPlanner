🌍 TripPlanner

Smart Trip Planning, Even Offline

TripPlanner is an Android application created to assist users in planning organising and managing their trips in a convenient platform.

TripPlanner brings together trip management, itinerary planning, expense tracking, saved places, maps and reminders. The offline- design lets users access and handle travel information without a constant internet connection.

TripPlanner aims to make travel planning easier by giving users the tools they need to organise their journeys manage budgets and keep track of activities.

📱 Application Overview

Planning a trip means handling details, such as destinations, travel dates, activities, accommodation plans, expenses and important reminders.

TripPlanner combines all these responsibilities into an application.

The application stores data locally so that essential trip information stays accessible offline. Internet is used for external services like searching for real-world places.

Main Objectives

- Simplify the trip-planning process.

- Provide a way to manage multiple trips.

- Allow users to plan activities and itineraries.

- Help users monitor travel expenses.

- Provide access to saved destinations and locations.

- Allow users to schedule reminders.

- Protect user account information.

- Support future cloud. Additional integrations.

✨. Functionality

🔐 1. User Authentication and Security

TripPlanner provides an account management system that lets users register log in and manage their information.

Features include:

- User registration.

- Login using account credentials.

- Password verification.

- Password hashing.

- Individual password salts.

- Account information management.

- Password changes.

- User session management.

- Logout functionality.

Password security

Passwords are never stored as text.

The application uses:

- PBKDF2WithHmacSHA256.

- 16-Byte salts.

- Password hashing and verification.

- Iteration-based password derivation.

The stored password format is:

iterations:saltBase64:hashBase64

✈️ 2. Trip Management

Users can. Manage their travel plans.

Features include:

- Create trips.

- Add trip names.

- Specify destinations.

- Select departure dates.

- Select return dates.

- Add trip notes.

- View trip details.

- Manage existing trips.

- Delete trips.

📅 3. Itinerary Management

TripPlanner allows users to organise activities for trips.

Features include:

- Add activities.

- Assign activities to dates.

- Set activity start times.

- Specify activity locations.

- Organise daily schedules.

- Mark activities as completed.

💰 4. Expense Tracking

The expense management system helps users monitor their travel spending.

Features include:

- Record expenses.

- Categorise expenses.

- Enter expense amounts.

- View recorded expenses.

- Attach receipt photographs.

- Calculate expense totals.

📍 5. Saved Places and Maps

Users can save places associated with their trips.

Features include:

- Search for real-world places.

- Save places for reference.

- Store addresses.

- Record geographical coordinates.

- Add additional place information.

- View destinations on a map.

- Manually enter locations when necessary.

OpenStreetMap integration

TripPlanner uses the Nominatim REST API to search for real-world locations.

Search results can provide:

- Place names.

- Addresses and location information.

- Latitude.

- Longitude.

Users can also enter place details manually when online searching is unavailable.

🔔 6. Reminders and Notifications

The reminder system allows users to schedule notifications for travel activities.

Features include:

- Create reminders.

- Select reminder dates.

- Set reminder times.

- Schedule notifications.

- Receive notification alerts.

- Manage important travel dates.

Android 13 and later require notification permission before notifications can be displayed.

⚙️ 7. Settings and Account Management

The settings functionality provides access, to account-related options.

Features include:

- View account information.

- Update details.

- Change passwords.

- Manage account sessions.

- Log out.

📶 Offline-First Architecture

One of TripPlanners design considerations is offline accessibility.

The application uses a Room database allowing users to access stored trip information without requiring an active internet connection.

Offline Workflow

User performs an action

|

v

Application processes data

v

Data saved locally

|

v

Data remains accessible offline

|

v

Internet connection available

|

v

External services can be accessed.

Room provides the base for storing data

Cloud synchronisation is a feature that can be added to keep local and remote data in sync.

🌐 REST API Integration

TripPlanner includes a REST integration that searches real‑world places using OpenStreetMaps Nominatim service.

The app uses network requests to get location data.

Networking tasks run in the background with Kotlin coroutines and Dispatchers.IO.

If a search fails or finds nothing the app lets users enter location details by hand.

API Integration

OpenStreetMap Nominatim.

Communication via REST.

Search for locations.

Getting latitude and longitude.

Network tasks run in the background.

Handling errors. Allowing manual input.

Nominatims policy limits requests to one per second.

🔥 Future Firebase Integration

Firebase offers chances to grow TripPlanner past storage.

Potential integrations include:

| Firebase Service | Intended Purpose |

| ---------------- | ---------------- |

| Firebase Authentication | Account authentication |

Google Sign-In | Google account login |

| Cloud Firestore | Cloud storage of trip data |

| Firebase Cloud Messaging | Push notifications |

| Firebase Storage | Receipt and image storage |

These services can back a future cloud‑connected version of TripPlanner.

Firebase features must be. Set up before they are considered active.

🌍 Future Multilingual Support

TripPlanner can be expanded to support languages making it easier for users who speak different languages.

Suggested language options are:

🇿 English

🇿🇦 isiZulu

🇿🇦 Afrikaans

A language selection can be added to the settings screen.

🛠️ Technologies Used

| Technology | Purpose |

| ----------- | -------

| Kotlin | Primary Android programming language |

| Android Studio | Development environment |

| Android SDK | Android app development |

| Room Database | Local database management |

| SQLite | Local data storage |

| ViewBinding | Connecting layouts to code |

| Kotlin Coroutines | Background tasks |

| PBKDF2WithHmacSHA256 | Password security |

| OpenStreetMap | Location information |

Nominatim API | Place search and geocoding |

| Git | Version control |

| GitHub | Source code hosting and collaboration |

GitHub Actions | Continuous Integration |

Additional technologies for development:

| Technology | Purpose |

| ----------- | -------

| Firebase Authentication | Cloud authentication |

| Cloud Firestore | Cloud database |

| Firebase Storage | File storage |

| Firebase Cloud Messaging | Push notifications |

| WorkManager | Background processing and sync |

| Retrofit | REST API communication |

| Node.js | Backend runtime |

| Express.js | Backend API framework |

🏗️ Project Architecture

TripPlanner follows a simple Android architecture that separates app screens, database work, networking and utility functions.

Current Project Structure

TripPlanner/

│

├──.github/

│ └── workflows/

│ └── android-ci.yml

│

├── TripPlanner/

│ └── app/

│ └── src/

│ └── main/

│ ├── java/

│ │ └── com/example/budgetx/

│ │ ├── data/

│ │ │ ├── entity/

│ │ │ ├── dao/

│ │ │ └── AppDatabase.kt

│ │ │

│ │ ├── network/

│ │ │ └── NominatimApiService.kt

│ │ │

│ │ ├── util/

│ │ │ ├── PasswordHasher.kt

│ │ │ └── DateTimeUtils.kt

│ │ │

│ │ └─ Activities

│ │

│ └── res/

│

├── build.gradle.kts

├── settings.gradle.kts

├── gradle.properties

├── gradlew

├── gradlew.bat

├── README.md

└──.gitignore

Architectural Components

Presentation Layer

Handles app screens and user interactions.

Data Layer

Handles database entities, DAOs and local storage.

Networking Layer

Handles API communication and location searches.

Utility Layer

Contains functions, like password hashing and date/time processing.

🤖 GitHub and Version Control

GitHub is used throughout TripPlanners development to manage source code and aid collaboration.

GitHub Utilisation

GitHub provides:

- Central source code storage.

- Version control.

- Development history.

- Collaboration among contributors.

- Project backup.

- Change tracking.

- Repository documentation.

- Integration, with automated workflows.

Repository

GitHub Repository:

https://github.com/XTROiD-00/TripPlanner

Collaboration

The repository lists four contributors:

- Thaps1

- Tlogelo

- XTROiD-00

- Katlego-Motsoaledi

Git lets multiple developers work on parts of the app while keeping a shared history.

.gitignore

The project uses.gitignore to stop files and sensitive data from being committed.

Examples of files and directories that should be excluded include:

-.gradle/

- build/

- **/build/

- local.properties

-.idea/

- *.iml

- *.jks

- *.keystore

- keystore.properties

- node_modules/

-.env

- *.db

This keeps the repository cleaner and safer.

⚡ GitHub Actions and Continuous Integration

GitHub Actions automates development and testing.

The intended Android CI workflow is located at:

.github/workflows/android-ci.yml

The workflow runs automatically when developers push changes or open pull requests.

CI Workflow

Developer updates source code

|

v

Code pushed to GitHub

v

GitHub Actions triggered

|

v

Source code checkout

|

v

Java environment configured

|

v

Gradle configured

|

v

Unit tests executed

|

v

Debug APK assembled

|

v

Build result reported

Automated Tasks

The workflow is designed to:

- Check out the repository.

- Set up the Java environment.

- Configure Gradle.

- Execute unit tests.

- Build the Android application.

- Report whether the workflow succeeded or failed.

Gradle Commands

Run unit tests:

./gradlew testDebugUnitTest

Build the debug APK:

./gradlew assembleDebug

Run Android tests:

./gradlew

Automated testing finds build failures and defects earlier.

You can see the workflow status in the Actions tab of the GitHub repository.

🧪 Testing

TripPlanner has testing levels.

Unit Testing

Unit tests target isolated functionality, including:

- Password hashing.

- Password verification.

- Date and time utilities.

Command:

./gradlew testDebugUnitTest

Instrumented Testing

Instrumented tests target Android- features.

Examples include:

- Room database operations.

- DAO functionality.

- Data persistence.

Command:

./gradlew connectedDebugAndroidTest

These tests need an Android device or emulator.

🚀 Getting Started

Prerequisites

Before running TripPlanner ensure that you have:

- Android Studio.

- JDK 17.

- Android SDK.

- Gradle.

- An Android emulator or physical Android device.

- Internet access for downloading dependencies and using location searches.

Clone the Repository

git clone https://github.com/XTROiD-00/TripPlanner.git

Open the project in Android Studio.

Let Gradle sync dependencies before running the app.

Run the Application

Open Android Studio.

Open the TripPlanner project.

Wait for Gradle synchronisation.

Select an emulator or connected Android device.

Run the application.

Register an account.

Log in. Begin planning trips.

Running Tests

./gradlew testDebugUnitTest

🔒 Security Considerations

Security matters in TripPlanners design.

Current security measures include:

- Password hashing.

- password salts.

- Password verification.

- Avoiding plain-text password storage.

- Excluding sensitive configuration files from version control.

Additional recommended security measures include:

- API communication through HTTPS.

- Proper session management.

- Input validation.

- Database access controls.

- Secure cloud authentication.

- Appropriate Firebase security rules.

- Protection of application signing credentials.

🔄 Development Updates and Improvements

TripPlanner has gone through development stages to improve its features organization and maintainability.

1. Initial Application Development

Established the Android project.

Created the application structure.

Implemented the application screens.

Developed the trip management functionality.

2. Core Feature Development

Added trip. Management.

Developed itinerary management.

Introduced expense tracking.

Implemented saved places.

Added map functionality.

Developed the reminder system.

3. Database Development

Integrated Room Database.

Created database entities.

Implemented Data Access Objects (DAOs).

Established local data persistence.

Updated database versions when required.

4. Authentication and Security Improvements

Implemented user registration.

Developed login functionality.

Introduced password hashing.

Added individual password salts.

Implemented password verification.

Added account management functionality.

5. Networking Improvements

Integrated the Nominatim REST API.

Implemented real-world place searches.

Added geographical coordinate retrieval.

Introduced background network operations.

Added fallback handling, for searches.

6. User Experience Improvements

Improved application screen organization.

Added account settings.

Improved trip management.

Introduced receipt image attachments.

Added notification permissions.

Improved offline accessibility.

7. Development and Repository Improvements

Established GitHub repository management.

Introduced version control.

Added repository documentation.

Configured.gitignore.

Prepared GitHub Actions automation.

Added automated testing commands.

8. Proposed Advanced Improvements

Here are development opportunities:

Firebase Authentication.

Google Sign-In.

Cloud Firestore integration.

Firebase Storage.

Firebase Cloud Messaging.

WorkManager-based synchronisation.

Express backend.

Additional language support.

Advanced travel analytics.

🎯 Future Improvements

TripPlanner can grow with features and integrations.

Potential improvements include:

synchronisation across devices.

Improved real-time collaboration.

Shared group trips.

Travel budget analytics.

Additional language support.

Enhanced. Navigation integration.

Hotel and accommodation integration.

Travel recommendations.

Comprehensive automated testing.

Production deployment of the REST API.

Improved notification management.

📽️ Project Presentation

Watch the TripPlanner project presentation:

https://youtube.com/shorts/x15km_FW8Fg?si=tCYkOi-TDy2nqSwz

👨‍💻 Development Information

Category

Details

Project

TripPlanner

Platform

Android

Programming Language

Kotlin

IDE

Android Studio

Database

Room / SQLite

API Integration

OpenStreetMap Nominatim

Version Control

Git / GitHub

CI/CD

GitHub Actions

📄 License

This project was developed for demonstration purposes.

⭐ TripPlanner

Plan smarter. Travel better. Stay organised.

TripPlanner brings your travel plans, activities, expenses, destinations and reminders into one app. With accessibility, at its core.
