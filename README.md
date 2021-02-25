# Asteroid Radar App

This is the 2st project for the Udacity Android Kotlin Developer Nanodegree Program.

Asteroid Radar is an app to view data provided by NASA via the NeoWs API.
NeoWs (Near Earth Object Web Service) is a RESTful web service for near earth Asteroid information.

The app consists of two screens: A Main screen with a list of all the detected asteroids and
a Details screen which displays the data of a single asteroid selected in the Main screen list.
The main screen also shows the NASA image of the day which is fetched using NASA APOD API.

This app implements several key features of real world applications:

1. Fetching and parsing data from remote APIs
2. Data persistance using Room database
3. Caching of data from remote APIs
4. Image fetching using Picasso library
5. Use of RecyclerView
6. WorkManager for background work


## NASA API key

The NASA API portal https://api.nasa.gov/ requires an API key for access and use.
The build of this app is configured in a way that it uses a build configuration field for
providing the necessary API key in the source code. It requires the definition of a string
with name `NASA_API_KEY` in a local `gradle.properties` file (e.g. in `~/.gradle/gradle.properties`).


## Screenshots

<img src="https://raw.githubusercontent.com/jploz/android-nd-asteroid-radar/main/screenshots/Screenshot_1614194624.png" width="240"/> <img src="https://raw.githubusercontent.com/jploz/android-nd-asteroid-radar/main/screenshots/Screenshot_1614194682.png" width="240"/>
