# ChurchHub

**ChurchHub** is a lightweight, offline-first Android app designed to serve as a digital hub for a local church community.  
It provides essential information, announcements, Bible reading plans, and sermon access in a simple, maintainable architecture.

> 🎯 **Design goal:** small scope, production-ready quality, and future scalability.

---

## ✨ Features

- 🏠 **Home**
  - Church information (location, contact, links)
  - Quick access to website, giving, and YouTube
- 📢 **Announcements**
  - Categorized announcements
  - Pinned items for important updates
- 📖 **Bible Reading Plan**
  - Daily readings
  - Local completion tracking (offline)
- 🎥 **Sermons**
  - YouTube sermon list with previews
  - Launches directly into the YouTube app or browser

---

## 🧱 Architecture Overview

ChurchHub uses a **clean, pragmatic Android architecture** focused on reliability and clarity.

```
GitHub-hosted JSON
        ↓
Retrofit + kotlinx.serialization
        ↓
Room (local cache)
        ↓
Repositories
        ↓
ViewModels
        ↓
Jetpack Compose UI
```

### Key principles
- **Offline-first:** cached data always displays
- **Single source of truth:** Room
- **No over-engineering:** entities are used directly by the UI
- **Easy content updates:** JSON edited directly in the repository

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Navigation:** Navigation Compose + Bottom Navigation
- **Architecture:** MVVM
- **Dependency Injection:** Hilt
- **Networking:** Retrofit
- **Serialization:** kotlinx.serialization
- **Local Storage:** Room
- **Image Loading:** Coil
- **Build System:** Gradle (KTS)

---

## 📂 Project Structure

```
com.dangle.churchhub
├── core
│   ├── di
│   └── util
├── data
│   ├── local        // Room entities & DAOs
│   ├── remote       // DTOs & APIs
│   └── repository   // Repositories
├── ui
│   ├── home
│   ├── announcements
│   ├── readingplan
│   ├── sermons
│   └── nav
└── MainActivity.kt
```

---

## 🌐 Data Source

All content is hosted as **static JSON** and fetched at runtime.

Example endpoints:
```
/content/v1/church_info.json
/content/v1/announcements.json
/content/v1/reading_plan.json
/content/v1/sermons_youtube.json
```

This approach allows:
- Content updates without app releases
- Simple “CMS” via GitHub
- Transparent version control of church content

---

## 📡 Networking & Caching

- Network requests populate Room
- UI observes Room via `Flow`
- If refresh fails, cached data remains visible
- Errors are surfaced to the user with retry options

---

## 🚀 Future Roadmap

Planned (not yet implemented):

- 🙏 Prayer board
- 📆 Ministry calendar
- 🔔 Push notifications
- 👥 Authenticated admin posting
- 🌍 Multi-church support

The navigation structure is already designed to support these additions without refactoring.

---

## 🧪 Testing (Planned / Minimal)

- Repository refresh logic
- ViewModel state combination (reading plan completion)

Testing is intentionally scoped to critical logic.

---

## 📱 Screenshots

_(Add screenshots or a short GIF here for flagship polish.)_

---

## 📝 Why This Project Exists

ChurchHub is intentionally **small but serious**.

It demonstrates:
- Thoughtful Android architecture choices
- Real-world tradeoffs (simplicity over abstraction)
- Clean Compose UI patterns
- Offline-first design
- A scalable foundation without premature complexity

---

## 📄 License

MIT License  
Feel free to use, modify, and adapt for your own church or community.

