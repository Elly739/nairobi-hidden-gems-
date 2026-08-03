# Implementation Plan - Nairobi Hidden Gems UI Refinement (Vision Alignment)

This plan focuses on refining the Android app's UI to align with the "Gen Z aesthetic" vision provided in the reference images. We will move beyond the basic skeletons to implement rich, functional screens for discovery, collections, and contributions.

## User Review Required

> [!IMPORTANT]
> **Central "+" Button**: The reference images show a prominent "+" button in the center of the bottom navigation. I will implement this as a primary action that navigates to the "Drop a Hidden Gem" screen.
> **Glassmorphism/Blur**: Some elements (like the bottom bar) appear to have a soft blur/translucency. I will use `Modifier.background` with alpha and `Modifier.blur` (Android 12+) where possible, but prioritize performance on older devices.
> **Unsplash Integration**: The "Add Gem" screen mentions searching Unsplash. For this sprint, I will implement the UI for this, but the actual API integration will be part of the "Services" phase (Sprint 2).

## Proposed Changes

### 1. UI Refinement: Home Screen
- **Header**: "Nairobi Hidden Gems" with a notification bell.
- **Trending Now**: Horizontal scroll section with "Most saved this week" subtitle.
- **Search & Filters**:
    - "Search gems..." bar with icon.
    - "Filter by Vibe" horizontal list of `FilterChip`s (Cafe, Sunset, Study, etc.).
- **Gems Grid**: Two-column `LazyVerticalGrid` of `PlaceCard`s.
- **PlaceCard Updates**:
    - Image overlays for category badges (e.g., "🌙 night").
    - Translucent overlays for area and rating.
    - Bold, high-contrast typography.

### 2. NEW: Saved / Collections Screen
- **Header**: "My Collections" with a "+ New" button.
- **Collections Grid**: Grid showing named collections (e.g., "Weekend Date Spots") with a preview image and spot count.

### 3. NEW: Add Gem Screen
- **Form UI**:
    - "Choose Image" toggle (Search vs Upload).
    - Input fields with placeholder text (e.g., "e.g. Java House Westlands Roof").
    - Dropdowns for Area and Category.
    - Custom rating inputs for "Aesthetic", "Chill", and "Crowd".
- **Action**: "Post Gem" gradient button.

### 4. NEW: Profile Screen
- **Hero Section**: Large avatar with a gradient background/cover photo area.
- **Stats Row**: "Drops", "Saved", "Followers" counts.
- **Content Tabs**: "Posted" and "Saved" toggle buttons with a grid view below.

### 5. Navigation Shell Refinement
- **Custom BottomBar**: Implement a `NavigationBar` that accommodates the large central "+" button.
- **Themes & Styling**:
    - Ensure a "Dark Mode first" aesthetic as per the images.
    - Refine `Color.kt` to include the specific purples and dark greys shown.

---

## Verification Plan

### Automated Tests
- Snapshot/Screenshot tests (if infrastructure exists) to compare UI against mocks.
- Navigation tests to ensure the "+" button correctly opens the "Add Gem" screen.

### Manual Verification
- Verify the scrolling behavior of the "Trending Now" and "Vibe Filters" sections.
- Test form validation on the "Add Gem" screen.
- Check the visual layout on different screen sizes (phone/foldable).
