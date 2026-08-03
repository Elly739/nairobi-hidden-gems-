# Implementation Plan - Nairobi Hidden Gems (Sprint 1: Foundation Completion)

This plan outlines the foundational setup for the Nairobi Hidden Gems Android app, focusing on architecture, navigation, and core UI shell using local mock data.

## User Review Required

> [!NOTE]
> **Hilt Integration**: Adding Hilt now to avoid future refactoring. This requires adding the Hilt Gradle plugin and KSP (Kotlin Symbol Processing).
> **Mock Data**: Sprint 1 will use local mock data for "Places" to validate the UI flow before integrating Firebase in Sprint 2.

## Proposed Changes

### 1. Build Configuration & Dependencies
Update `libs.versions.toml` and `app/build.gradle.kts` to include:
- **Navigation**: `androidx.navigation:navigation-compose`
- **ViewModel**: `androidx.lifecycle:lifecycle-viewmodel-compose`
- **Image Loading**: `io.coil-kt:coil-compose`
- **Dependency Injection (Hilt)**:
    - `com.google.dagger:hilt-android`
    - `androidx.hilt:hilt-navigation-compose`
    - Hilt Gradle Plugin & KSP.

### 2. Package Architecture
Refactor to the following structure under `com.example.nairobihiddengems`:
- `core/`: `navigation`, `theme`, `utils`.
- `data/`: `models`, `repository`, `remote`, `local`.
- `domain/`: `models`, `repository`, `usecases`.
- `ui/`: `splash`, `home`, `explore`, `details`, `profile`.

### 3. Navigation Architecture
- **Routes**: Define `Screen` sealed class (Splash, Home, Explore, Saved, Profile).
- **NavGraph**: Implement `AppNavGraph.kt` to manage app-wide transitions.
- **Main Shell**: Create a `MainScreen` wrapper with `Scaffold` and `BottomNavigationBar`.

### 4. Core UI Skeletons
- **Splash Screen**: Branded landing page with logo and transition logic.
- **Home Screen**: `LazyColumn` displaying mock `Place` cards (e.g., The Alchemist, Karura Forest).
- **Bottom Navigation**: Functional switching between Home, Explore, and Profile placeholders.

---

## Verification Plan

### Automated Tests
- Ensure the project builds successfully with Hilt and KSP.
- Basic navigation test to verify route transitions.

### Manual Verification
- App launch -> Splash (1.5s delay) -> Home.
- Clicking Bottom Navigation items switches screens correctly.
- Verify mock data renders in the Home feed cards.
