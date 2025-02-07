
# Features  <br>
View Article: Displays articles fetched from a news API or passed through a saved fragment.  <br>
Save Article: Users can save articles by clicking the floating action button (FAB). Articles are stored in the local database.  <br>
Article Saved State: Once an article is saved, the FAB disappears to avoid multiple saves.  <br>
Swipe to Delete: In the saved fragment, users can swipe to delete articles from the database.  <br>
Image Loading: Articles include images that are loaded asynchronously using Glide.  <br>
# Technologies Used
Kotlin for Android development.  <br>
Room Database for local storage.  <br>
ViewModel and LiveData for managing UI-related data lifecycle-consciously.  <br>
Glide for loading images.  <br>
FloatingActionButton (FAB) for saving articles.  <br>
Retrofit for making network requests to fetch articles.   <br>
Swipe to Delete for removing saved articles.  <br>
# Architecture
The app follows the MVVM architecture: <br>

Model: Represents data (Article, News, etc.).  <br>
View: The UI elements (fragments, views).  <br>
ViewModel: Handles the business logic and manages data from the Model and updates the View.  <br>
Repository: Acts as an abstraction layer for accessing data sources (local Room DB and network APIs).  <br>
DAO (Data Access Object): Interface for database operations in Room.  <br>
