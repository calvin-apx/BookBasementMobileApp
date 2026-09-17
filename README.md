# Book Basement Android App

Android client for [Book Basement](https://github.com/calvin-apx/book_basement), a platform for donating, selling, buying, and recycling books.

## Features

- Browse books by genre, search and filter, and view details
- Donate, sell, or buy books by booking an appointment
- Recycle books through partner recycling facilities
- Cart and favorites
- Genre-based recommendations
- Book cover recognition with the camera, using an on-device ML Kit AutoML image-labeling model bundled in `app/src/main/assets`
- Google Books lookup for book details
- Real-time chat and push notifications through Firebase

## Stack

- Java, minSdk 16, targetSdk 29, AndroidX, Material Components
- Retrofit 2 and Gson for the Laravel backend
- Firebase Auth, Realtime Database, Storage, and Cloud Messaging
- ML Kit image labeling (AutoML) and Firebase ML Vision
- Glide, Picasso, CircleImageView, SweetAlert, Alerter

## Requirements

- Android Studio 4.0 or newer with JDK 8
- Android SDK 29
- A Firebase project with Auth, Realtime Database, Storage, and Cloud Messaging enabled
- The Book Basement backend running and reachable from the device

## Setup

1. Clone the repo and open it in Android Studio.
2. In the Firebase console, register an Android app with package name `com.example.bookbasement_02`, download `google-services.json`, and place it at `app/google-services.json`. The file is git-ignored on purpose.
3. Point the app at your backend by editing `IP` in `app/src/main/java/com/example/bookbasement_02/Constants/URL.java`. The app expects the Laravel project to be served at `http://<host>/BookBasementApp/public/`.
4. Sync Gradle and run on a device or emulator. The camera feature needs a physical device.

## Project layout

- `Activities/` screens
- `Fragments/` tabs inside the container activities
- `Adapters/` RecyclerView adapters
- `RestApi/` Retrofit interface and client
- `Models/` API and Firebase data classes
- `Helpers/` shared utilities
