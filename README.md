# Team 2984 Robot Code

This is team 2984's code for the 2017 robotics season, FIRST Steamworks.

## Project Configuration for Mac

### Eclipse

Full instructions can be found at:

[Installing Eclipse (C++/Java)](https://wpilib.screenstepslive.com/s/4485/m/13503/l/599679-installing-eclipse-c-java)

Site:

http://first.wpi.edu/FRC/roborio/release/eclipse/

Add libraries to project with using Properties > Java Build Path > Add External Jars

Download and install Talon libraries ([page](http://www.ctr-electronics.com/hro.html#product_tabs_technical_resources), [direct link](http://www.ctr-electronics.com//downloads/lib/CTRE_FRCLibs_NON-WINDOWS_v4.4.1.10.zip)).

After unzipping, add java/lib directory contents to:

`$HOME/wpilib/user/java/`

### OpenCV

http://www.rmnd.net/install-and-use-opencv-3-0-on-mac-os-x-with-eclipse-java/

```bash
$ brew update
$ brew install ant
$ brew tap homebrew/science
$ brew install opencv3 --with-java
$ sudo cp /usr/local/Cellar/opencv3/3.2.0/share/OpenCV/java/libopencv_java320.dylib /Library/Java/Extensions/
```

### Test for OpenCV

Create a new project. Add `Hello.java`:

```java
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
 
public class Hello
{
   public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
      System.out.println( "mat = " + mat.dump() );
   }
}
```

In project, right-click then properties. Add Library>External Jar. Point to:

`/usr/local/Cellar/opencv3/3.2.0/share/OpenCV/java/opencv-320.jar`

**It should be unnecessary to link native code when the jarfile is included in this way.**