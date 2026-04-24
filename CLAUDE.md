# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Android APK
./gradlew :composeApp:assembleDebug

# Desktop JVM
./gradlew :composeApp:run

# All tests
./gradlew test

# Android instrumented tests
./gradlew :composeApp:connectedAndroidTest

# Run all quality checks (Detekt + Ktlint + Android Lint + custom checks)
./gradlew qualityCheck

# Auto-format code
./gradlew qualityFormat
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Project Structure

```
HabitsTracker/
├── build-logic/conventions/src/main/kotlin/
│   ├── AndroidApplicationConventionPlugin.kt   # compileSdk 36, minSdk 24, targetSdk 36
│   ├── AndroidLibraryConventionPlugin.kt
│   ├── ComposeMultiplatformConventionPlugin.kt
│   ├── FeatureConventionPlugin.kt              # common feature module deps
│   ├── QualityConventionPlugin.kt              # Detekt + Ktlint + Lint wiring
│   └── ProjectExtensions.kt
│
├── core/src/commonMain/.../core/
│   ├── design/
│   │   └── HabitsTheme.kt                      # Material3 design system entry point
│   ├── model/
│   │   ├── HabitId.kt
│   │   ├── HabitKind.kt
│   │   └── Mood.kt
│   ├── navigation/
│   │   ├── AppScreen.kt                        # sealed interface for all routes
│   │   ├── AppComposeNavigator.kt              # wraps Navigation3 back stack
│   │   ├── ComposeNavigator.kt
│   │   ├── NavigationModule.kt                 # Koin module
│   │   ├── NavigationResultStore.kt
│   │   ├── NavigationResults.kt
│   │   ├── NavCommand.kt
│   │   ├── NavResultKeys.kt
│   │   └── ResultFlow.kt
│   └── utils/
│       └── StringUtils.kt
│
├── feature/
│   ├── home-api/src/commonMain/.../home/api/
│   │   └── HomeFeatureApi.kt                   # public contract (HomeRoute, HomeCallbacks)
│   │
│   └── home-impl/src/
│       ├── commonMain/.../home/impl/
│       │   ├── data/repository/
│       │   │   └── InMemoryHomeRepository.kt
│       │   ├── di/
│       │   │   └── HomeFeatureModule.kt        # Koin module
│       │   ├── domain/
│       │   │   ├── mapper/HomeStateMapper.kt
│       │   │   ├── repository/IHomeRepository.kt
│       │   │   └── usecase/
│       │   │       ├── LocaleDateFormatter.kt  # expect interface
│       │   │       ├── GetTodayDateLabelUseCase.kt
│       │   │       ├── ObserveHomeUseCase.kt
│       │   │       ├── ToggleHabitUseCase.kt
│       │   │       ├── IncrementHabitUseCase.kt
│       │   │       ├── DecrementHabitUseCase.kt
│       │   │       ├── UpdateReflectionMoodUseCase.kt
│       │   │       └── UpdateReflectionNoteUseCase.kt
│       │   ├── navigation/
│       │   │   └── HomeFeatureImpl.kt
│       │   ├── presenter/
│       │   │   ├── HomeContract.kt             # HomeIntent / HomeState / HomeEffect
│       │   │   └── HomeViewModel.kt
│       │   └── ui/
│       │       ├── HomeScreen.kt
│       │       └── components/
│       │           ├── HabitsBlock.kt
│       │           ├── HeaderBlock.kt
│       │           └── ReflectionBlock.kt
│       ├── androidMain/.../usecase/
│       │   └── LocaleDateFormatter.android.kt
│       ├── iosMain/.../usecase/
│       │   └── LocaleDateFormatter.ios.kt
│       └── jvmMain/.../usecase/
│           └── LocaleDateFormatter.jvm.kt
│
└── composeApp/src/
    ├── commonMain/.../habitstracker/
    │   ├── App.kt                              # root Composable, nav host setup
    │   └── BottomTab.kt                        # HOME / JOURNAL tab enum
    ├── androidMain/.../
    │   └── MainActivity.kt
    ├── iosMain/.../
    │   └── MainViewController.kt
    └── jvmMain/.../
        └── main.kt
```

## Architecture

**Kotlin Multiplatform** (Android, iOS, Desktop JVM) with feature-based modular MVI.

### Feature Layer Structure

Each feature is split into `-api` and `-impl` modules:

```
Data      InMemoryXxxRepository : IXxxRepository
Domain    XxxUseCase (one per operation) + XxxStateMapper
Presenter XxxViewModel — state: StateFlow<XxxState>, effects: SharedFlow<XxxEffect>, onIntent(XxxIntent)
UI        XxxScreen + components/
API       XxxFeatureApi (interface consumed by composeApp, implemented in -impl)
```

Contracts (`XxxIntent`, `XxxState`, `XxxEffect`) live in `XxxContract.kt` alongside the ViewModel.

### Navigation

Type-safe via `androidx.navigation3`. Routes are `@Serializable` objects implementing `AppScreen`. `AppComposeNavigator` manages the back stack. Bottom tabs: `HOME`, `JOURNAL`.

### Dependency Injection

Koin 4.x. Each feature registers its own Koin module (`homeFeatureModule`). Platform-specific bindings (e.g., `LocaleDateFormatter`) are registered per source set, not via `expect`/`actual`.

## Code Quality Rules

- **No hardcoded colors** outside `core/design/`. The `checkNoHardcodedColors` Gradle task scans for `Color(0x...)` and fails the build on violations.
- Detekt `maxIssues: 0` — every issue is a build failure.
- Ktlint Android style; run `qualityFormat` to auto-fix.
- Android Lint strict mode — warnings are errors (version-check lints suppressed).

Run `./gradlew qualityCheck` before committing.

## TODO — Architecture Fixes

| # | Severity | Area | Fix effort |
|---|----------|------|------------|
| ~~1~~ | ~~Critical~~ | ~~DI — `HomeFeatureModule` is nearly empty; use cases and ViewModel are created manually with `remember()` instead of being registered in Koin~~ | ~~Done~~ |
| ~~2~~ | ~~Critical~~ | ~~ViewModel coroutine scope leaks — manual `CoroutineScope` + `DisposableEffect(clear)` is fragile; switch to `viewModelScope` after fixing #1~~ | ~~Done~~ |
| 3 | Critical | MVI race condition — optimistic state update in `MoodSelected` runs before async repository save; drive state only from the `observeHome` flow | Small |
| 4 | Critical | Hardcoded username `"Alex Sterling"` in `HomeScreen.kt:72` — must come from a `UserRepository` and flow through `HomeState` | Medium |
| 5 | Major | `LocaleDateFormatter` not injected — `GetTodayDateLabelUseCase` constructs it via default parameter, bypassing Koin; inject as singleton | Small |
| 6 | Major | No error handling in ViewModel — `launch { toggleHabit(...) }` silently drops exceptions; add try/catch and emit `HomeEffect.ShowError` | Medium |
| 7 | Major | Dead code — `HabitsBlock.kt` contains empty `HabitCard`/`EmptyHabitCard` placeholders that are never used; delete or implement | Small |
| 8 | Major | Dead code — `HomeHeader` composable in `HomeScreen.kt:118–168` is defined but never called; remove | Small |
| 9 | Major | Type-unsafe navigation results — results are `Any` keyed by raw strings; replace with a sealed `NavigationResult` interface | Medium |
| 10 | Major | `App.kt` god-composable — handles Koin setup, navigation, bottom bar, and dialogs; extract into `AppNavigation` / `AppDialogs` | Medium |
| 11 | Minor | `MutableSharedFlow` for effects has no buffer (capacity = 0); add `extraBufferCapacity = 16` | Small |
| 12 | Minor | Date label string transformation `replace(".", "")` in `HomeScreen.kt:73` is a no-op for the current format and will silently break on format changes; remove | Small |
| 13 | Minor | Bottom tab selection falls back to HOME for non-tab screens (`App.kt:98`); track `selectedTab` as explicit state | Small |
| 14 | Minor | `DATE_PATTERN` duplicated across all three `LocaleDateFormatter` platform files; move to a `commonMain` constant | Small |
| 15 | Minor | Interactive elements missing `contentDescription` for accessibility throughout UI components | Medium |

## SDK & JVM Targets

- `compileSdk` / `targetSdk`: 36 — `minSdk`: 24
- JVM target: 11
- Kotlin: 2.3.20 — Compose Multiplatform: 1.10.3