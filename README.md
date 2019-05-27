<p align="center">
  <img src="https://user-images.githubusercontent.com/41321155/58410744-72640100-807b-11e9-8639-47c537784c1a.gif"><br>
</p>


# DialogCameraView
An Android library to display a camera view inside an alert dialog (popup) based on CameraKit (SDK 21+)



# Installation
Step 1. Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency

```groovy
dependencies {
        implementation 'com.github.JagarYousef:DialogCameraView:1.0.0'
}
```

# Permissions
This library needs the following permissions:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.hardware.camera2.full" />
```

Refer to [this article](<https://jagar.me/post/opencamera2window/>) to find out how to ask for this permission for marshmallow and above. 

# Usage
1. Create and object of `CameraViewDialog` and init it:
```java
CameraViewDialog cameraViewDialog = new CameraViewDialog();
        cameraViewDialog.init(context, image_path);
```
2. Handle results:
```java
cameraViewDialog.setOnCaptureDoneListener(new OnCaptureDone() {
    @Override
    public void onDone(String imagePath) {
       
    }
});
cameraViewDialog.setOnCaptureCancelledListener(new OnCaptureCancelled() {
    @Override
    public void OnCancelled() {
      
    }
});
cameraViewDialog.setOnRecapturingListener(new OnRecapturing() {
    @Override
    public void onRecapturing(int times) {
       
    }
});
```

3. Start the camera dialog view:

```java
cameraViewDialog.startDialog();
```



# License 
```
MIT License

Copyright (c) 2019 Jagar Yousef

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
