# 🩺 Halli Santhe (ಹಳ್ಳಿ ಸಂತೆ)

<div align="center">
  <img src="https://img.icons8.com/color/120/000000/leaf.png" width="100" />
  <p><strong>A rural-to-urban marketplace connecting local farmers directly to urban kitchens.</strong></p>
</div>

---

## 🌿 Overview
**Halli Santhe** is a "Nature-Fresh" marketplace application built with **Jetpack Compose**. It facilitates a direct connection between rural producers (Sellers) and urban consumers (Buyers). The application emphasizes high-quality imagery, a clean "Earth-White" aesthetic, and a robust local data architecture.

## 🛠️ Technology Stack
- **Language:** [Kotlin 2.1.0](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose (Material 3)](https://developer.android.com/jetpack/compose)
- **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Local Database:** Room Database / Preferences DataStore
- **Navigation:** Jetpack Navigation Compose
- **State Management:** Kotlin StateFlow & ViewModel

## 📂 Project Structure
Following a modularized clean architecture approach:

```text
HalliSantheApp/
├── app/
│   ├── src/main/java/com/example/hallisanthe/
│   │   ├── ui/
│   │   │   ├── theme/          # Color.kt, Type.kt, Theme.kt (Nature-Fresh Palette)
│   │   │   ├── components/     # Reusable UI: TickerTape, ProductCard, UserPill
│   │   │   └── screens/        # Dashboard, Login, Start, Profile Screens
│   │   ├── data/               # Room Models, Entity definitions, Repository
│   │   └── MainActivity.kt     # App Entry point & NavHost setup
│   └── res/
│       ├── drawable/           # SVG icons for Veggies, Fruits, Grains, Flowers
│       └── values/             # Localized strings.xml

✨ Key Features
1. Dual-Role Experience
The app dynamically adapts based on the user's role (selected during login):

Buyer Dashboard: Features an auto-scrolling TickerTape for live market trends, category grids, and a 2-column product catalog.

Seller Panel: Provides an intuitive inventory management system with "Add Product" forms and real-time list management.

2. Nature-Fresh UI
Primary Palette: Leaf Green (#2e7d32) & Earth White (#f9fbf9).

Typography: Professional Inter/Roboto font pairing.

Components: Rounded 16dp corner cards for a soft, modern feel.

3. Integrated AI & Data
Local-First: Robust data management ensuring zero-latency browsing.

Smart Ticker: Marquee-style market news updates for real-time price awareness.

🚀 Setup & Installation
Clone the repository:

Bash
git clone [https://github.com/Krishnaumarani1066/HalliSantheApp.git](https://github.com/Krishnaumarani1066/HalliSantheApp.git)
Open in Android Studio: Ensure you are using Android Studio Koala (2024.1.2) or newer.

Build Requirements:

JDK 17

Gradle 9.0.0+

Android SDK 34+

👨‍💻 Author
Krishnaumarani1066 Android Development Intern | Computer Science Engineering student
