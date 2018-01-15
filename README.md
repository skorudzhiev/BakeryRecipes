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

### Tests
> * Tested on 
>   * Nexus 5X API 24(Android 7.0, API 24) 
>   * 10.1 WXGA (Tablet) API 24
> * The app makes use of [Espresso](https://developer.android.com/training/testing/espresso/index.html) to test aspects of the UI

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
* The app allows navigation between individual recipes and recipe steps
* Play video content 
* Widget companion on your home screen with recipe ingredients upon selected item from the available list

### Gradle Dependencies

```Gradle
dependencies {

    ext {
        supportLibVersion = '26.+'
    }
    
    compile "com.android.support:recyclerview-v7:${supportLibVersion}"
    compile 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    compile 'com.google.code.gson:gson:2.7'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.google.android.exoplayer:exoplayer:r2.0.4'
    }
```

## Features
* Content POJO classes are implementing *Parcelable*
* The app uses *Master Detail Flow* to display recipe steps and navigation between them
* *UI* was built with two Actvities and three *Fragments* all of which are compatible with different *Screen Sizes and Device orientation*
* [Exoplayer](https://github.com/google/ExoPlayer) is utilized in the app to display videos
* The app properly initializes and releases video assets when appropriate
* Utilization of [Lambda Expression](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) to populate the ingredients list 
* Wigdet companion composed of the following classes - 
  * WidgetProvider
  * WidgetService
  * UpdateService
```Java
public class WidgetProvider extends AppWidgetProvider {
...
}
```   
```Java
public class WidgetService extends RemoteViewsService {
...
}
```
```Java
public class UpdateService extends IntentService {
...
}
```

