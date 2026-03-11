# PizzaShop Android App

PizzaShop is an Android mobile ordering application built in Java using Android Studio. The project allows users to sign in, browse a pizza menu, add items to a cart, place orders, and review order history through a clean mobile interface.

## Features

- User login and account creation
- Salted PBKDF2 password hashing for local sign-in
- Browse available pizzas
- Search pizzas by name or ingredient
- Add items to cart
- Update cart quantities
- Clear cart and checkout
- View order history
- Reorder from past orders

## Tech Stack

- Java
- Android Studio
- Room Database
- DAO architecture
- RecyclerView
- View Binding

## Architecture

The app uses a layered structure to separate UI and data concerns:

- **UI layer** for activities and adapters
- **Repository layer** for coordinating data operations
- **DAO layer** for database access
- **Room database** for local persistence

## Project Purpose

This project began as a software engineering capstone and was later refined into a portfolio project. The UI, navigation, launcher icon, and overall presentation were improved beyond the original submission to better reflect a polished Android application.

## Screenshots

Screenshots are available in the repository root `screenshots/` folder and the main project README.

## Potential Future Improvements

- Move database work off the main thread for a more production-style data layer
- Add account/profile management
- Expand automated UI coverage for end-to-end flows
- Add a small backend or sync layer if the project ever grows beyond local storage