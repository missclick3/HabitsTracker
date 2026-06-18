
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
│   │   ├── HabitsTheme.kt                      # Material3 design system entry point
│   │   ├── HabitsColors.kt
│   │   ├── HabitsDimensions.kt
│   │   └── HabitsTextStyles.kt
│   ├── model/
│   │   ├── HabitId.kt
│   │   ├── HabitKind.kt
│   │   └── Mood.kt
│   ├── navigation/
│   │   └── Navigator.kt                        # Navigator interface + FeatureEntryBuilder typealias + CoreNavigator
│   ├── uiutils/
│   │   └── UseDebounce.kt                      # <T> T.useDebounce() composable extension
│   └── utils/
│       └── StringUtils.kt
│
├── core/database/src/commonMain/.../database/  # separate Gradle module :database
│   ├── HabitsDatabase.kt                       # Room DB, version 2, BundledSQLiteDriver
│   ├── dao/
│   │   ├── HabitDao.kt
│   │   ├── HabitDailyRecordDao.kt
│   │   ├── DailyReflectionDao.kt
│   │   ├── UserDao.kt
│   │   └── QuoteDao.kt
│   ├── entity/
│   │   ├── HabitEntity.kt
│   │   ├── HabitDailyRecordEntity.kt
│   │   ├── DailyReflectionEntity.kt
│   │   ├── UserEntity.kt
│   │   └── QuoteEntity.kt                      # PK = ISO date string
│   └── di/
│       ├── DatabaseModule.kt                   # commonMain: exposes all DAOs
│       ├── DatabaseModule.android.kt
│       ├── DatabaseModule.ios.kt
│       └── DatabaseModule.jvm.kt
│
├── feature/
│   ├── home-api/src/commonMain/.../home/api/
│   │   └── HomeScreenRoute.kt                  # @Serializable sealed interface : NavKey
│   │
│   ├── home-impl/src/
│   │   ├── commonMain/.../home/impl/
│   │   │   ├── data/
│   │   │   │   ├── mapper/EntityMapper.kt
│   │   │   │   ├── network/
│   │   │   │   │   ├── QuoteApiDataSource.kt   # Ktor GET /v2/quoteoftheday
│   │   │   │   │   └── dto/QuoteOfDayResponse.kt
│   │   │   │   └── repository/
│   │   │   │       ├── RoomHomeRepository.kt   # primary home data source
│   │   │   │       ├── RoomUserRepository.kt
│   │   │   │       ├── QuoteRepository.kt      # cache-first (Room → Ktor)
│   │   │   │       └── InMemoryHomeRepository.kt  # dead code, superseded by Room
│   │   │   ├── di/
│   │   │   │   └── HomeFeatureModule.kt        # Koin module — all singletons + viewModels
│   │   │   ├── domain/
│   │   │   │   ├── mapper/HomeStateMapper.kt
│   │   │   │   ├── model/
│   │   │   │   │   ├── Habit.kt
│   │   │   │   │   ├── Quote.kt
│   │   │   │   │   └── User.kt
│   │   │   │   ├── repository/
│   │   │   │   │   ├── IHomeRepository.kt
│   │   │   │   │   └── IUserRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── DateProvider.kt         # interface + SystemDateProvider impl
│   │   │   │       ├── LocaleDateFormatter.kt  # platform impl via per-source-set Koin binding
│   │   │   │       ├── GetTodayDateLabelUseCase.kt
│   │   │   │       ├── GetQuoteOfDayUseCase.kt # injects DateProvider; no date param from VM
│   │   │   │       ├── ObserveHomeUseCase.kt
│   │   │   │       ├── ObserveUserUseCase.kt
│   │   │   │       ├── SeedDatabaseUseCase.kt
│   │   │   │       ├── ToggleHabitUseCase.kt
│   │   │   │       ├── IncrementHabitUseCase.kt
│   │   │   │       ├── DecrementHabitUseCase.kt
│   │   │   │       ├── UpdateReflectionMoodUseCase.kt
│   │   │   │       ├── UpdateReflectionNoteUseCase.kt
│   │   │   │       ├── CreateHabitUseCase.kt
│   │   │   │       ├── UpdateHabitUseCase.kt
│   │   │   │       ├── DeleteHabitUseCase.kt
│   │   │   │       ├── GetHabitByIdUseCase.kt
│   │   │   │       └── UpdateUserNameUseCase.kt
│   │   │   ├── navigation/
│   │   │   │   └── HomeEntryBuilder.kt         # registers home + editHabit nav entries
│   │   │   ├── presenter/
│   │   │   │   ├── mainScreen/
│   │   │   │   │   ├── HomeContract.kt         # HomeIntent / HomeState / HomeEffect / QuoteUiState
│   │   │   │   │   ├── HomeNavigation.kt
│   │   │   │   │   └── HomeViewModel.kt
│   │   │   │   └── editHabit/
│   │   │   │       ├── EditHabitContract.kt
│   │   │   │       ├── EditHabitNavigation.kt
│   │   │   │       └── EditHabitViewModel.kt
│   │   │   └── ui/
│   │   │       ├── HomeScreen.kt
│   │   │       ├── EditHabitScreen.kt
│   │   │       └── components/
│   │   │           ├── HeaderBlock.kt
│   │   │           ├── QuoteCard.kt
│   │   │           ├── ReflectionBlock.kt
│   │   │           └── HabitsBlock.kt          # dead code (TODO #7)
│   │   ├── androidMain/.../usecase/
│   │   │   └── LocaleDateFormatter.android.kt
│   │   ├── iosMain/.../usecase/
│   │   │   └── LocaleDateFormatter.ios.kt
│   │   └── jvmMain/.../usecase/
│   │       └── LocaleDateFormatter.jvm.kt
│   │
│   ├── journal-api/src/commonMain/.../journal/api/
│   │   └── JournalScreenRoute.kt               # @Serializable sealed interface : NavKey
│   │
│   └── journal-impl/src/commonMain/.../journal/impl/
│       ├── data/repository/RoomJournalRepository.kt
│       ├── di/JournalFeatureModule.kt
│       ├── domain/
│       │   ├── model/JournalEntry.kt
│       │   ├── repository/IJournalRepository.kt
│       │   └── usecase/
│       │       ├── ObserveJournalUseCase.kt
│       │       └── SeedJournalUseCase.kt
│       └── ui/ (journal screen + components)
│
└── composeApp/src/
    ├── commonMain/.../habitstracker/
    │   ├── App.kt                              # Koin setup, Scaffold, bottom bar
    │   ├── AppNavHost.kt                       # expect — platform nav host
    │   └── BottomTab.kt                        # HOME / JOURNAL enum
    ├── androidMain/.../
    │   ├── AppNavHost.android.kt               # actual — Navigation3 back stack handler
    │   └── MainActivity.kt                     # passes platformModule (Context + quoteApiKey)
    ├── iosMain/.../
    │   ├── AppNavHost.ios.kt
    │   └── MainViewController.kt               # passes platformModule (quoteApiKey)
    └── jvmMain/.../
        ├── AppNavHost.jvm.kt
        └── main.kt                             # reads local.properties → platformModule
```

## Architecture

**Kotlin Multiplatform** (Android, iOS, Desktop JVM) with feature-based modular MVI.

### Feature Layer Structure

Each feature is split into `-api` (public contract consumed by `composeApp`) and `-impl` (internal):

```
Data      XxxRepository : IXxxRepository  — returns domain models only, never UI state
Domain    XxxUseCase (one per operation) + XxxStateMapper
Presenter XxxViewModel — state: StateFlow<XxxState>, effects: SharedFlow<XxxEffect>, onIntent(XxxIntent)
UI        XxxScreen + components/
API       XxxScreenRoute (@Serializable sealed interface : NavKey)
```

Contracts (`XxxIntent`, `XxxState`, `XxxEffect`) live in `XxxContract.kt` alongside the ViewModel.

### Navigation

Type-safe via `androidx.navigation3`. Routes are `@Serializable` objects inside sealed interfaces that implement `NavKey`. `Navigator` (in `core/navigation/`) wraps a `SnapshotStateList<NavKey>` back stack. The `FeatureEntryBuilder` typealias lets each feature register its own nav entries. Bottom tabs: `HOME`, `JOURNAL`.

### Dependency Injection

Koin 4.x. Each feature registers its own Koin module (`homeFeatureModule`, `journalFeatureModule`). Platform-specific bindings (Android `Context`, API keys, Ktor engine) are passed in via `platformModule` from each entry point (`MainActivity`, `main.kt`, `MainViewController`). Use `named("quoteApiKey")` qualifier for the API key.

`DateProvider` / `SystemDateProvider` — use cases that need today's date inject `DateProvider` rather than reading the date inside the ViewModel.

### Database

Room 2.8 KMP with `BundledSQLiteDriver`. The `:database` Gradle module (`core/database/`) owns all entities, DAOs, and `HabitsDatabase`. Features import specific DAOs; they never import `HabitsDatabase` directly. `fallbackToDestructiveMigration(dropAllTables = true)` is in place (dev stage).

### Networking

Ktor 3.1. `HttpClient` with `ContentNegotiation` + `kotlinx-serialization-json` is registered as a Koin singleton in `homeFeatureModule`. Platform engines: OkHttp (Android), Darwin (iOS), Java (JVM).

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
| ~~4~~ | ~~Critical~~ | ~~Hardcoded username `"Alex Sterling"` — must come from a `UserRepository` and flow through `HomeState`~~ | ~~Done~~ |
| ~~5~~ | ~~Major~~ | ~~`LocaleDateFormatter` not injected — `GetTodayDateLabelUseCase` constructs it via default parameter, bypassing Koin; inject as singleton~~ | ~~Done~~ |
| 6 | Major | No error handling in ViewModel — `launch { toggleHabit(...) }` silently drops exceptions; add try/catch and emit `HomeEffect.ShowError` | Medium |
| 7 | Major | Dead code — `HabitsBlock.kt` contains a `HabitCard` with `TODO()` that crashes at runtime; delete or implement | Small |
| ~~8~~ | ~~Major~~ | ~~Dead code — `HomeHeader` composable defined but never called; remove~~ | ~~Done~~ |
| 9 | Major | Type-unsafe navigation results — results are `Any` keyed by raw strings; replace with a sealed `NavigationResult` interface | Medium |
| 10 | Major | `App.kt` still handles Koin setup + bottom bar + nav host; extract bottom bar into its own composable | Small |
| 11 | Minor | `MutableSharedFlow` for effects has no buffer (capacity = 0); add `extraBufferCapacity = 16` | Small |
| 12 | Minor | Date label `replace(".", "")` in `HomeScreen.kt:74` is a no-op for the current locale format and will silently break on format changes; remove | Small |
| 13 | Minor | Bottom tab selection falls back to HOME for non-tab screens (`App.kt:68`); consider tracking `selectedTab` as explicit state | Small |
| 14 | Minor | `DATE_PATTERN` duplicated across all three `LocaleDateFormatter` platform files; move to a `commonMain` constant | Small |
| 15 | Minor | Interactive elements missing `contentDescription` for accessibility throughout UI components | Medium |

## SDK & JVM Targets

- `compileSdk` / `targetSdk`: 36 — `minSdk`: 24
- JVM target: 11
- Kotlin: 2.3.20 — Compose Multiplatform: 1.10.3