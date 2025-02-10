# Tasky - Multi-Module Android App

## Overview
Tasky is a multi-module Android application designed with scalability and maintainability in mind. The project follows a modular architecture, separating features into independent modules with their respective data, domain, and presentation layers. The app leverages modern Android technologies, including Jetpack Compose, Hilt for dependency injection, and a combination of MVI and MVVM patterns for state management.

## What Tasky Does
Tasky is a productivity application that allows users to manage their daily agenda, including tasks, reminders, and events. Users can create, edit, and delete agenda items, set reminders with notifications, and manage event attendees. The application ensures a seamless user experience with offline mode support, automatic data synchronization, and persistent session management. Tasky is designed to be intuitive, providing a clean UI built with Jetpack Compose and state-of-the-art architectural patterns.

## Project Structure
```
├── agenda
│   ├── data
│   ├── domain
│   └── presentation
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
├── auth
│   ├── data
│   ├── domain
│   └── presentation
├── build-logic
│   ├── convention
│   └── settings.gradle
├── build.gradle.kts
├── core
│   ├── data
│   ├── domain
│   └── presentation
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts
```

## Modularization Strategy
Tasky follows a feature-based modularization approach, where each feature is self-contained within its module. This ensures better separation of concerns, easier maintainability, and improved testability.

### Core Module
The `core` module contains shared utilities, common networking, database interactions, and UI components that are reused across different features.

### Feature Modules
Each feature (e.g., `agenda`, `auth`) follows a three-layered architecture:
- **Data Layer**: Handles API calls, database operations, and caching.
- **Domain Layer**: Contains business logic and use cases.
- **Presentation Layer**: Includes UI components built using Jetpack Compose.

## Technical Stack
### Architecture
- **Multi-module approach**: Each feature is isolated into its own module.
- **MVI + MVVM hybrid**: The UI state is managed using MVI principles with ViewModel integration for better state handling.
- **Dependency Injection**: Hilt is used for injecting dependencies across modules.

### UI and Navigation
- **Jetpack Compose**: The UI is fully built using Compose, providing a declarative and reactive UI experience.
- **Navigation Component**: Jetpack Compose Navigation is used for managing screen transitions.

### Data Layer
- **Room Database**: Local storage for offline support.
- **Retrofit + OkHttp**: Used for API communication.
- **Coroutines + Flow**: Ensures asynchronous and reactive data handling.

## Features
### Authentication
- User registration and login.
- Session management with token persistence.

### Agenda
- Displays user's agenda including tasks, reminders, and events.
- Tasks and reminders can be marked as completed.
- Events include attendee management and reminders.
- Offline mode support with automatic syncing when online.

### Notifications & Reminders
- Scheduled notifications for reminders and events.
- Persistent reminders even after device restart.
- Automatic syncing of reminders across devices.

## Build & Run
### Prerequisites
- Android Studio Giraffe or later.
- Kotlin 1.8+.
- Java 17+.
- Gradle 8+.


## Contribution Guidelines
1. Follow clean architecture principles.
2. Keep feature modules independent.
3. Follow Jetpack Compose best practices.
4. Ensure proper dependency injection using Hilt.
5. Maintain coding standards and write unit tests.

---
This README serves as a technical guide to understanding the project structure, modular approach, and implementation details of Tasky.



