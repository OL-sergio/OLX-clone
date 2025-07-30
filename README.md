# OLX Clone Application

**OLX Clone** - A simplified version of the OLX platform for buying and selling items.

---

## 📝 Project Description
The **OLX Clone** application is designed to simulate the core functionalities of the OLX platform. Users can create, view, and manage advertisements for products or services. The app provides a user-friendly interface for browsing ads, viewing details, and contacting sellers.

---

## 🎯 Project Objectives
- Provide a platform for users to post and manage advertisements.
- Enable users to browse and view detailed information about ads.
- Allow users to contact sellers directly via phone.
- Implement a smooth and responsive user experience with image sliders and navigation.

---

## 🛠️ Components and Technologies

### **Technologies Used**
 ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
 ![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
 ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
 ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
 ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

### **Libraries and Frameworks**
- **Picasso**: Used for loading and displaying images efficiently.
- **TabLayoutMediator**: Provides smooth tab navigation for the image slider.
- **View Binding**: Simplifies UI interactions by binding views directly to code.
- **Firebase**: Used for backend services like authentication and data storage.

---

## 📂 Project Structure

````
app/src/main/java/exemple/udemy/java/olx/

├── activity/
│   ├── `AdvertDetailsActivity.java` \- Displays details of a selected advertisement.
│   ├── `AdvertsActivity.java` \- Main activity showing a list of advertisements.
│   ├── `CreateAdvertActivity.java` \- Allows users to create new advertisements.
│   ├── `LoginRegisterActivity.java` \- Handles user login and registration.
│   ├── `MyAdvertsActivity.java` \- Displays advertisements created by the user.
├── adapter/
│   ├── `AdapterAdverts.java` \- Adapter for displaying a list of advertisements.
│   ├── `ImageSliderAdapter.java` \- Adapter for managing the image slider in ad details.
├── helper/
│   ├── `RecyclerItemClickListener.java` \- Handles item click events in RecyclerView.
│   ├── `SettingsFirebase.java` \- Configures Firebase settings.
├── model/
│   ├── `Advert.java` \- Model class representing an advertisement.
├── utilities/
│   ├── `CustomHorizontalProgressDialog.java` \- Custom progress dialog for loading states.

````

---

## ⚙️ Application Functionality

### **1. AdvertDetailsActivity**
- Displays detailed information about a selected advertisement, including title, price, location, and description.
- Implements an auto-scrolling image slider using `ViewPager2` and `TabLayoutMediator` to showcase product photos.
- Allows users to contact the seller directly by initiating a phone call.

### **2. AdvertsActivity**
- The main screen of the application, displaying a list of all available advertisements in a `RecyclerView`.
- Provides navigation to create new ads or view the details of existing ones.
- Implements filtering options for different regions and categories.

### **3. CreateAdvertActivity**
- Provides a form for users to create and publish new advertisements.
- Includes fields for title, price, description, category, and image uploads from the device's gallery.
- Saves the new advertisement data to Firebase.

### **4. LoginRegisterActivity**
- Handles user authentication, allowing users to either log in with existing credentials or register for a new account.
- Integrates with Firebase Authentication for secure user management.

### **5. MyAdvertsActivity**
- Displays a list of advertisements created by the currently logged-in user.
- Allows users to manage their ads, including the option to delete them.

---

## 🚀 How to Execute and Configure

### **1. Clone the Repository**
```bash


git clone https://github.com/OL-sergio/OLX-clone.git

```

### 2. Open in Android Studio
- Open Android Studio.
- Select File > Open and navigate to the cloned project directory.
- Wait for Gradle to sync all project dependencies.
### 3. Configure Firebase
- Go to the Firebase Console and create a new project.
- Add an Android app to your Firebase project with the package name exemple.udemy.java.olx.
- Download the google-services.json file and place it in the app/ directory of your project.
- Enable Authentication (Email/Password) and Firestore Database in the Firebase console.
### 4. Run the Application
- Connect an Android device or start an emulator.
- Click the Run button in Android Studio to build and install the application.

<!--
## 📸 Screenshots
### Home Screen
<img src="https://via.placeholder.com/300x600" alt="Home Screen"></img>

### Ad Details
<img src="https://via.placeholder.com/300x600" alt="Ad Details"></img>
-->

## 📚 Libraries and Frameworks Explanation
### Picasso
Picasso is a powerful image downloading and caching library for Android. It simplifies the process of loading images from URLs into ImageViews, handling caching, and transformations with minimal code.

### TabLayoutMediator
This component from the Material Design library is used to link a TabLayout with a ViewPager2. It's essential for creating the dot indicators for the image slider, providing visual feedback for the current image position.


### Firebase
Firebase is a comprehensive mobile and web application development platform. In this project, it's used for:

- Authentication: To manage user sign-up and login.
- Firestore: As a NoSQL database to store advertisement data.
- Storage: To upload and store user-provided images for the ads.

### View Binding
View Binding is a feature that generates a binding class for each XML layout file. This allows for safer and more concise code to reference views, eliminating the need for findViewById and preventing null pointer exceptions from invalid view IDs.




