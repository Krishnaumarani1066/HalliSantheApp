This is a comprehensive Master Specification and Functional Structure for building the "Halli Santhe" application in Android Studio using Kotlin and Jetpack Compose.
To recreate the exact experience you see in the preview, you can use these prompts and structure descriptions.
Phase 1: The App Identity (The "Prompt")
Overall App Vision:
"Build a rural-to-urban marketplace app called 'Halli Santhe' using Jetpack Compose. The design should feel 'Nature-Fresh' using a palette of Leaf Green (#2e7d32), Earth White (#f9fbf9), and Soft Gray. The app must handle two distinct user roles: Buyers and Sellers. Use professional typography (Inter or Roboto) and emphasize high-quality imagery of fresh produce. All data should be managed locally using a shared state or Room database."
Phase 2: File Structure (Project Setup)
Follow this structure in your Android Studio project:
code
Text
HalliSantheApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/hallisanthe/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/          (Color.kt, Type.kt, Theme.kt)
│   │   │   │   │   ├── components/     (TickerTape, ProductCard, UserPill)
│   │   │   │   │   └── screens/        (StartScreen, LoginScreen, BuyerDashboard, SellerDashboard, ProfileScreen)
│   │   │   │   ├── data/               (Models.kt, Repository.kt)
│   │   │   │   └── MainActivity.kt     (Navigation Host and Entry Point)
│   │   │   ├── res/
│   │   │   │   ├── drawable/           (SVG icons for Veggies, Fruits, Grains, Flowers)
│   │   │   │   └── values/             (Strings.xml)
Phase 3: Screen-by-Screen Logic (Detailed Prompts)
1. Entry Point: MainActivity.kt
Logic: Use NavHost to manage screen transitions.
Prompt: "Create a Navigation Controller that starts at StartScreen. Pass a UserViewModel between screens to track if the user is a 'Buyer' or 'Seller'."
2. Start Screen: StartScreen.kt
UI: Large central logo, animated floating leaves (optional), and a 'Get Started' button.
Prompt: "A full-screen welcome page with a background gradient of light green. Include a centered 120dp logo, a bold title 'Halli Santhe', and a subtext 'Connecting Farmers to your Kitchen'. A large rounded button at the bottom should navigate to the Login page."
3. Login/Registration: LoginScreen.kt
UI: Role toggle (Buyer/Seller), Form (Name, Phone, Location).
Prompt: "A clean login form. At the top, a 'Toggle Switch' to choose between Buyer and Seller role. Form fields: Full Name, Phone Number (keyboard type: phone), and City/Location. On click 'Login', save the user object and navigate to the respective dashboard."
4. The Buyer Dashboard: BuyerHome.kt
UI Components:
Header: Logo on left, UserPill (Profile icon + Name) on right.
TickerTape: An auto-scrolling row showing 'Live Trends' and 'Tomato Prices'.
Category Grid: Horizontal scroll with icons: Vegetables, Fruits, Grains, Flowers.
Product List: A 2-column vertical grid of cards showing product images, Price per KG, and Seller name.
Prompt: "Implement a scrolling dashboard. The top section is a StickyHeader. Below it, a MarqueeText showing market news. Use a LazyVerticalGrid to show products. Each card has an 'Order' button that triggers a simple 'Order Success' snackbar."
5. The Seller Dashboard: SellerPanel.kt
UI Components:
Add Product Form: Inputs for 'Item Name', 'Price', and a Dropdown Select for Category (Vegetables, Fruits, Grains, Flowers).
Inventory List: A list of items the seller has already posted.
Prompt: "A dashboard for sellers. The top half is an 'Add Product' card with text fields and a ExposedDropdownMenu for categories. The bottom half is a LazyColumn showing 'My Listed Items' with a delete icon on each row."
6. User Profile: ProfileScreen.kt
UI: Huge avatar circle, User Details (Role, Phone, Location), and a red 'Logout' button.
Prompt: "A profile view. Show a circular avatar with user initials. Display the user's role in a high-contrast badge. Below, list the metadata (Email, Phone, Location) in a card with subtle icons. A 'Logout' button at the bottom clears the session and returns to the Start screen."
Phase 4: Data Linking & State
Shared State: Create a class UserSession that stores: String name, Enum role, String location.
Inventory Data: A List<Product> where each product has a category property matching the selection from the Seller Dashboard.
Navigation Links:
Profile Button (on any dashboard) -> ProfileScreen.
Back Arrow (on Profile) -> returns to the dashboard based on the user.role.
Logout -> StartScreen.
Key UI commands for your Kotlin Styles:
Colors: Color(0xFF2E7D32) (Primary), Color(0xFFF1F8E9) (Background).
Shapes: Use RoundedCornerShape(16.dp) for all cards to get the soft look.
Icons: Use ImageVector from Icons.Rounded for things like Person, Agriculture, Search.