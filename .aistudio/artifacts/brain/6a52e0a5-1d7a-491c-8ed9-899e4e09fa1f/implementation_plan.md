# Glass UI Online Music & Video Player (Android Jetpack Compose)

A native Android streaming audio and video player featuring a modern **Dark Cosmic Glassmorphism** visual theme built with Jetpack Compose. Supports seamless switching between online music tracks and high-definition music videos, comprehensive playback controls (Play/Pause, Seek, Next/Prev, Volume, **Shuffle**, and **Repeat** modes), a slide-up playlist drawer, favorite tracks storage, and dynamic ambient background lighting.

## User Review & Critical Decisions

> [!IMPORTANT]
> Key feature choices and design decisions for your Online Music & Video Player:

- **Shuffle & Repeat Navigation**:
  - **Shuffle Mode**: Toggles random track ordering when progressing (`OFF` -> `ON`). When active, selecting "Next" picks a random track from the current queue.
  - **Repeat Modes**: Cycle through 3 states: `REPEAT_OFF` (stop at end of playlist), `REPEAT_ALL` (loop playlist indefinitely), and `REPEAT_ONE` (loop current track continuously).
- **Glass UI Theme & Aesthetic**:
  - Translucent frosted glass cards (`backdrop-filter` effect using semi-transparent surfaces + backdrop blurs / gradient brushes).
  - Ambient glowing background orbs with smooth subtle scaling animation.
  - Custom styled progress scrubber slider, volume controls, and glowing playback state indicators.
- **Audio & Video Mode Dual Engine**:
  - **Audio Mode**: High-definition online MP3 streams with dynamic glowing album art, title, artist, seek bar, time stamps, and queue list.
  - **Video Mode**: Integrated video player canvas with fullscreen toggle, media controls, and seamless audio-video switching.
- **Local Persistence (Room Database)**:
  - Saves favorite tracks, recently played history, and custom playlist queue across app restarts.

---

## 1. Overview & Core Concept

- **What It Does**: Provides an online music and video streaming application with a modern Glassmorphism UI. Users can browse curated online songs and music videos, play audio or video media, control playback with shuffle and repeat modes, manage volume and seek position, view live song lyrics/details, and save favorite tracks locally.
- **Target Audience**: Music and video enthusiasts who appreciate modern, high-polish visual design and fluid mobile playback features.
- **Key Value**: Merges high-fidelity Glassmorphic Compose UI design with complete media playback features—including song/video switching, slide-out queue drawer, and full shuffle/repeat controls.

---

## 2. User Experience & Visual Design

- **Key User Flows**:
  1. **Player Home Screen**: Visualized with ambient neon light orbs and a main frosted glass card showing album art / video screen, track title, artist name, and live time slider.
  2. **Playback Controls**: Tap Play/Pause, Skip Back/Forward, **Shuffle** (random order toggle), **Repeat** (Off -> All -> One toggle), or adjust volume.
  3. **Audio / Video Switcher**: Toggle between "Songs" (Audio album art display) and "Music Video" (video viewport player) seamlessly.
  4. **Playlist Queue Drawer**: Tap top-right drawer icon to slide up the frosted glass song list. Tap any track to play instantly.
  5. **Favorites & Search**: Quick heart toggle to mark songs as favorites, stored in local Room database.

- **Visual Identity & Theme**:
  - *Aesthetic Direction*: Dark Cosmic Glass (deep slate/midnight background `#090D16`, glowing purple `#7C3AED` and cyan `#0284C7` ambient orbs, translucent glass cards with subtle white borders).
  - *Typography*: Clean bold title headers, semi-transparent body subtext, and clear time-stamp labels (`sp` scaled).
  - *Component Styling*: Layered Compose `Box` surfaces with rounded corners (`32.dp`), semi-transparent overlays (`Color.White.copy(alpha = 0.08f)`), custom blur or gradient backgrounds, and M3 ripple feedback.

---

## 3. Key Product Decisions & Trade-Offs

- **Media Player Engine**:
  - *Chosen Approach*: Android `ExoPlayer` / `Media3` (or Compose `AndroidView` video holder + `MediaPlayer` engine) for seamless background & online streaming playback.
  - *Why*: Reliable online media streaming support for both MP3 and MP4 streams with accurate time updates, buffer status, and completion listeners.
- **Shuffle & Repeat Logic**:
  - *Shuffle*: Maintained via a shuffled index queue map in `PlayerViewModel`.
  - *Repeat*: State enum (`OFF`, `ALL`, `ONE`). When `ONE`, `ended` listener re-seeks to 0 and plays. When `ALL`, reaching playlist end wraps to track 0.
- **Data Persistence**:
  - *Chosen Approach*: **Room Database** for local favorite tracks and persistent media playback history.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          Jetpack Compose UI                            │
│  ┌──────────────────────────────┐    ┌──────────────────────────────┐  │
│  │    GlassPlayerScreen         │    │    PlaylistDrawerSheet       │  │
│  │  - Top Bar & Mode Pill       │    │  - Song Queue List           │  │
│  │  - Media Stage (Cover/Video) │    │  - Favorites Filter          │  │
│  │  - Title & Artist Info       │    └──────────────────────────────┘  │
│  │  - Progress Slider & Time    │                                      │
│  │  - Controls Row (Shuffle,    │                                      │
│  │    Prev, Play/Pause, Next,   │                                      │
│  │    Repeat)                   │                                      │
│  │  - Volume Control Slider     │                                      │
│  └──────────────┬───────────────┘                                      │
└─────────────────┼──────────────────────────────────────────────────────┘
                  │ Observes State Flow & Emits Actions
                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         PlayerViewModel                                │
│  - PlayerState Flow (Current Track, Play/Pause, Shuffle, Repeat)       │
│  - Playback Queue Management & Next/Prev Index Calculation             │
│  - Time / Position Progress Timer Flow                                 │
└─────────────────┬──────────────────────────────────────────────────────┘
                  │ Interacts with Data & Media
                  ▼
┌───────────────────────────────┐        ┌───────────────────────────────┐
│     ExoPlayer / Media Engine  │        │       Room Repository         │
│  - Online Audio/Video Player  │        │  - Favorites Dao              │
│  - Stream Buffering & Events  │        │  - History/Queue Persistence  │
└───────────────────────────────┘        └───────────────────────────────┘
```

### Data Model & State

- **Track Model**: `id: String`, `title: String`, `artist: String`, `type: MediaType` (`AUDIO` / `VIDEO`), `mediaUrl: String`, `coverUrl: String`, `durationMs: Long`, `isFavorite: Boolean`.
- **RepeatMode**: `OFF`, `ALL`, `ONE`.
- **PlayerUiState**:
  - `currentTrack: Track?`
  - `isPlaying: Boolean`
  - `currentPositionMs: Long`
  - `durationMs: Long`
  - `isShuffleEnabled: Boolean`
  - `repeatMode: RepeatMode`
  - `volume: Float`
  - `activeMediaType: MediaType`
  - `playlist: List<Track>`
  - `currentIndex: Int`
  - `isPlaylistOpen: Boolean`

### Interactive Component & State Mapping

1. **Shuffle Button (`IconButton`)**:
   - Icon toggles active color (Neon cyan/purple when enabled vs muted white when disabled).
   - Tap calls `viewModel.toggleShuffle()`. Updates `isShuffleEnabled`.
2. **Repeat Button (`IconButton`)**:
   - Icon toggles icon state (`Repeat`, `RepeatOne`, `RepeatOff`).
   - Tap calls `viewModel.cycleRepeatMode()`. Updates `repeatMode`.
3. **Play / Pause Button (`FloatingActionButton` / Glass Circle)**:
   - Toggles media playback state.
4. **Seek Slider (`Slider`)**:
   - User scrubbing updates position state real-time; releasing seeks media player.
5. **Mode Toggle Pill ("Songs" vs "Music Video")**:
   - Switches media view container and media stream source seamlessly.

---

## Step-by-Step Implementation Steps

1. **Dependencies & Architecture Setup**: Add Media3 / ExoPlayer dependencies if needed, establish Room database entities (`TrackEntity`, `FavoritesDao`), and set up `PlayerViewModel`.
2. **Core Playback Engine & ViewModel Logic**: Implement complete shuffle and repeat navigation logic, track index calculations, timer flows, and media control functions.
3. **Glassmorphism Compose UI Styling**: Build custom glass cards, glowing ambient background canvas, translucent sliders, and dynamic icons.
4. **Controls Row Enhancements**: Add Shuffle & Repeat buttons with active state styling and proper tooltips/content descriptions.
5. **Slide-Out Playlist Queue Drawer**: Implement modal bottom sheet or animated drawer for track selection.
6. **Compilation & Verification**: Run `compile_applet` to ensure smooth build and execution on Android.
