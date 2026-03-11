# PizzaShop Android App

PizzaShop is a Java Android application built in Android Studio that allows users to create an account, browse a pizza menu, manage a cart, place orders, and view order history. The app uses Room for local data storage and includes encrypted local password handling for authentication.

## Features

- User registration and login
- Encrypted local password storage
- Searchable pizza menu
- Cart management with quantity updates
- Checkout flow with order history
- Order details and reorder support
- Local persistence with Room

## Tech Stack

- Java
- Android Studio
- Room Database
- RecyclerView
- View Binding
- Material Components

## Project Structure

- UI layer with Activities and RecyclerView adapters
- Repository layer for application data operations
- DAO layer for Room database access
- SQLite persistence through Room

## Screenshots

| Login | Account Creation |
|------|------|
| ![Login](screenshots/login.png) | ![Account](screenshots/account_creation.png) |

| Menu | Cart |
|------|------|
| ![Menu](screenshots/menu.png) | ![Cart](screenshots/cart.png) |

| Order History | Order Details |
|------|------|
| ![History](screenshots/order_history.png) | ![Details](screenshots/order_details.png) |

## Running the Project

1. Open the project in Android Studio.
2. Let Gradle sync complete.
3. Build and run on an emulator or Android device.