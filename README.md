# BakeryRecipes
**Version 1.0 2017/11/20** 

Created an app to view video recipes that incorporated media loading, verifying user interfaces with UI tests, integrated third-party libraries and provided a complete UX with home screen widget.

![alt text](https://github.com/skorudzhiev/BakeryRecipes/blob/master/readme_photos/BakeryRecipes%20-%20recipes2%20-%20Nexus_5X_API_24_5554.png) ![alt text](https://github.com/skorudzhiev/BakeryRecipes/blob/master/readme_photos/BakeryRecipes%20-%20step%20-%20Nexus_5X_API_24_5554.png) 

![alt text](https://github.com/skorudzhiev/BakeryRecipes/blob/master/readme_photos/BakeryRecipes%20-%20steps2%20-%20Nexus_5X_API_24_5554.png)

### Device permissions
*App needs the following user's permissions to provide the featured functionality*
* Request user's permission to access the internet
```XML
<uses-permission android:name="android.permission.INTERNET"/>
```
* Request user's permission to access device network state
```XML
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## General Usage Notes

```Gradle
defaultConfig {
        minSdkVersion 15
        targetSdkVersion 26
    }
```
* The app displays a list of recipes from provided network resource 
* Every recipe contains the following information: 
  * ingredients
  * recipe steps - including detailed information regarding every step with additional video content
* app allows navigation between individual recipes and recipe steps

## Components and Libraries 
- application uses Master Detail Flow to display recipe steps and navigation between them
- application uses Exoplayer to display videos
- application properly initializes and releases video assets when appropriate
- application has a companion homescreen widget
- application makes use of Espresso to test aspects of the UI
