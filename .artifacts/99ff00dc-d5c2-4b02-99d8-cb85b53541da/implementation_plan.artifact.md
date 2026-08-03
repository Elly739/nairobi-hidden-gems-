# Implementation Plan - UI Layout Fix & Interaction Logic

This plan addresses the visibility issues with the bottom navigation bar and defines the interaction flow for "Hidden Gems" cards.

## User Review Required

> [!IMPORTANT]
> **Details Screen**: When you click a gem, I will implement a transition to a **Place Details** screen. This screen will show larger images, the full description, and options to **Rate**, **Comment**, and **Get Directions**.
> **Navigation Bar Fix**: I will add `navigationBarsPadding()` to the custom bottom bar to ensure it sits above the system navigation pill/buttons and is fully visible on all devices.

## Proposed Changes

### 1. Fix: Bottom Navigation Visibility
- **File**: [`MainScreen.kt`](file:///C:/Users/Okatho/AndroidStudioProjects/NairobiHiddenGems/app/src/main/java/com/example/nairobihiddengems/ui/MainScreen.kt)
- **Change**: Add `Modifier.navigationBarsPadding()` to the `CustomBottomBar` container. This will push the entire navigation bar up so it's not hidden by the Android system navigation area.

### 2. Feature: Place Details Interaction
- **Interaction**: Clicking any `PlaceCard` in the Home feed or Trending section will navigate to a new `DetailsScreen`.
- **New Screen**: `PlaceDetailScreen.kt` [NEW]
    - **Header**: Large image with a "back" button and "save" toggle.
    - **Info**: Name, category, location, and rating.
    - **Community Section**:
        - A "Rate this Gem" interactive star section.
        - A "Reviews" section with placeholders for comments.
    - **Actions**: "Get Directions" (opens Google Maps) and "Share" buttons.

### 3. Navigation Updates
- **Routes**: Add `object Details : Screen("details/{placeId}")` to `Screen.kt`.
- **Graph**: Update `AppNavGraph.kt` to include the details route and pass the `placeId`.

---

## Verification Plan

### Manual Verification
1. **Nav Visibility**: Verify that Home, Saved, and Profile icons are fully visible on the emulator.
2. **Interaction**: Click on "The Alchemist" card and confirm it opens the new Details screen.
3. **Back Stack**: Use the back button on the Details screen to return to the Home feed.
