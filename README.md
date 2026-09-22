# Nairobi Hidden Gems 💎

Nairobi Hidden Gems is a "Dark Mode first" discovery platform designed to help urban explorers find, save, and share aesthetic spots in Nairobi. From quiet study cafes to vibrant rooftop lounges and sunset viewpoints, the app curates the city's best-kept secrets.

## 🚀 Vision & Aesthetic
The app follows a sleek, translucent, dark-themed aesthetic with vibrant purple and pink accents. It’s built for the modern explorer who values "vibes," aesthetic photography, and community-driven recommendations.

## ✨ Features (Sprint 1, 2 & 3 Completed)
- **Splash Entry:** Smooth dark-themed transition into the app.
- **Home Feed:** 
    - **Trending Now 🔥:** Real-time trending spots based on community saves.
    - **Search & Filters:** Instant search by name/location and filtering by "Vibe" (Cafe, Sunset, Study, Night).
    - **Gem Grid:** Two-column visual layout with functional **Save/Love** icons on cards.
- **Authentication:** Fully integrated Firebase Authentication (Email/Password) with persistent sessions and secure password validation.
- **Cloud Data:** Live Firestore integration for gem discovery with reactive UI updates using `addSnapshotListener`.
- **Drop a Hidden Gem 🚀:** 
    - Functional contribution form to add new spots.
    - Real image selection via system photo picker.
    - Integrated Unsplash search simulation for high-quality demo photography.
- **Interactive Details ✨:** 
    - **Save:** Persist spots to your personal collection.
    - **Rate:** Interactive rating system for Aesthetic, Chill, and Crowd vibes.
    - **Share:** Native Android share sheet integration.
- **Profiles 👤:** 
    - Explorer profiles with live stats (Drops, Saved).
    - Functional **Edit Profile** feature to customize your display name.
- **Saved Collections:** A dedicated space to organize and view your real-time saved gems.

## 🛠️ Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Modern, Declarative UI)
- **Dependency Injection:** Hilt (Dagger)
- **Asynchronous Flow:** Kotlin Coroutines & StateFlow
- **Backend:** Firebase (Authentication, Cloud Firestore)
- **Image Loading:** Coil
- **Navigation:** Jetpack Navigation Compose
- **Architecture:** Clean Architecture (Domain, Data, UI layers) with MVVM pattern.

## 📂 Project Structure
```text
nairobihiddengems/
├── core/               # Theme, navigation definitions, and shared components
├── data/               # Repository implementations (Firebase, Mock)
├── domain/             # Business logic, models, and repository interfaces
├── ui/                 # UI components and ViewModels grouped by feature
│   ├── auth/           # Login & Registration
│   ├── home/           # Feed & Discovery
│   ├── details/        # Place details
│   ├── profile/        # User stats & settings
│   └── saved/          # Collections & Folder management
└── di/                 # Hilt Dependency Injection modules
```

## 🏗️ Getting Started
1. **Clone the repo:**
   ```bash
   git clone https://github.com/Elly739/nairobi-hidden-gems-.git
   ```
2. **Firebase Setup:**
   - Add your `google-services.json` to the `app/` directory.
   - Enable Email/Password Auth in the Firebase Console.
   - Set up Cloud Firestore in "Test Mode" or update security rules to allow authenticated access.
3. **Run the App:**
   - Open in Android Studio (Ladybug or newer).
   - Sync Gradle and run on a physical device (API 24+).
   - Tap "↺ Refresh" in the Home Feed to seed initial data.

## 📅 Roadmap (Next Steps)
- [ ] **Firebase Storage:** Full integration for persistent user-uploaded photo hosting.
- [ ] **Google Maps:** Direct integration for "Get Directions" and map-view exploration.
- [ ] **Social Features:** Comments, photo reviews, and following other gem hunters.
- [ ] **Reposts:** Allow users to share other hunters' gems to their own feed.

---
*Created by Elly739 - Discover the Vibe of Nairobi.*
