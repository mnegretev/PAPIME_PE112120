package mx.mnegretev.stargazer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    //AutoFitTextureView mTextureView;
    ImageView mImageView;
    ImageReader mImageReader;
    CaptureRequest.Builder mPreviewRequestBuilder;
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEGHTH = 480;
    private static final int MY_CAMERA_PERMISSION_CODE = 314;

    private PoseCalculator poseCalculator = new PoseCalculator();
    private Filter filterMoonX;
    private Filter filterMoonY;
    private Filter filterSunX;
    private Filter filterSunY;
    private Filter filterMercuryX;
    private Filter filterMercuryY;
    private Filter filterVenusX;
    private Filter filterVenusY;
    private Filter filterMarsX;
    private Filter filterMarsY;
    private Filter filterJupyterX;
    private Filter filterJupyterY;
    private Filter filterSaturnX;
    private Filter filterSaturnY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        filterMoonX = new Filter();
        filterMoonY = new Filter();
        filterSunX = new Filter();
        filterSunY = new Filter();
        filterMercuryX  = new Filter();
        filterMercuryY = new Filter();
        filterVenusX  = new Filter();
        filterVenusY = new Filter();
        filterMarsX  = new Filter();
        filterMarsY = new Filter();
        filterJupyterX = new Filter();
        filterJupyterY = new Filter();
        filterSaturnX  = new Filter();
        filterSaturnY = new Filter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.main_image);
        poseCalculator.setupSensorServices((LocationManager)getSystemService(Context.LOCATION_SERVICE),
                (SensorManager)getSystemService(Context.SENSOR_SERVICE));
        OrbitalParameters.InitializePlanets();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mBaseLoaderCallback);
        else
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        poseCalculator.registerListener();

        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            initCamera();
        else
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
    }

    @Override
    protected void onPause() {
        if(mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
        poseCalculator.unregisterListener();
        super.onPause();
    }

    private void initCamera()
    {
        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIds = null;
        String cameraId = "";
        CameraCharacteristics characteristics = null;
        Size largest = new Size(640,480);
        try{
            cameraIds = cameraManager.getCameraIdList();
            cameraId = cameraIds[0];
            characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Log.d("MAIN ACTIVITY: ", Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)).toString());
            largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            Log.d("MainActivity","Size: " + largest.toString());
        }catch (CameraAccessException e)
        {
            showToast("Cannot get camera Id");
            finish();
        }
        //mImageReader = ImageReader.newInstance(largest.getWidth()/4, largest.getHeight()/4, ImageFormat.JPEG, 2);
        //mImageReader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 2);
        //mImageReader = ImageReader.newInstance(1280, 640, ImageFormat.JPEG, 2);
        mImageReader = ImageReader.newInstance(720, 480, ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(onImageAvailableListener, null);
        try{
            cameraManager.openCamera(cameraId, camera_state_callback, null);
        }catch (SecurityException e){
            showToast("Cannot open camera: security exception");
            finish();
        }catch (CameraAccessException e) {
            showToast("Cannot open camera: camera acceses exception");
            finish();
        }

    }

    ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Image img = reader.acquireLatestImage();
            if(img == null)
                return;
            ByteBuffer buffer = img.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmapImage, mat);


            double lon = poseCalculator.getLongitude();
            double lat = poseCalculator.getLatitude();
            double timestamp = ((double)System.currentTimeMillis())/1000;
            double phoneRoll = poseCalculator.getRoll();
            double phonePitch = poseCalculator.getPitch();
            double phoneAz = poseCalculator.getAzimuth();
            AllHorizontalPoses allPoses = ObjectPositions.CalculateAllPositions(lon, lat, timestamp);

            Cartesian moonPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Moon, phoneRoll, phonePitch, phoneAz);
            Cartesian sunPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Sun, phoneRoll, phonePitch, phoneAz);
            Cartesian mercuryPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Mercury, phoneRoll, phonePitch, phoneAz);
            Cartesian venusPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Venus, phoneRoll, phonePitch, phoneAz);
            Cartesian marsPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Mars, phoneRoll, phonePitch, phoneAz);
            Cartesian jupyterPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Jupyter, phoneRoll, phonePitch, phoneAz);
            Cartesian saturnPhonePos = Transform.HorizontalToPhoneXYZ(allPoses.Saturn, phoneRoll, phonePitch, phoneAz);

            moonPhonePos.X = filterMoonX.Filter(moonPhonePos.X);
            moonPhonePos.Y = filterMoonY.Filter(moonPhonePos.Y);
            sunPhonePos.X = filterSunX.Filter(sunPhonePos.X);
            sunPhonePos.Y = filterSunY.Filter(sunPhonePos.Y);
            mercuryPhonePos.X = filterMercuryX.Filter(mercuryPhonePos.X);
            mercuryPhonePos.Y = filterMercuryY.Filter(mercuryPhonePos.Y);
            venusPhonePos.X = filterVenusX.Filter(venusPhonePos.X);
            venusPhonePos.Y = filterVenusY.Filter(venusPhonePos.Y);
            marsPhonePos.X = filterMarsX.Filter(marsPhonePos.X);
            marsPhonePos.Y = filterMarsY.Filter(marsPhonePos.Y);
            jupyterPhonePos.X = filterJupyterX.Filter(jupyterPhonePos.X);
            jupyterPhonePos.Y = filterJupyterY.Filter(jupyterPhonePos.Y);
            saturnPhonePos.X = filterSaturnX.Filter(saturnPhonePos.X);
            saturnPhonePos.Y = filterSaturnY.Filter(saturnPhonePos.Y);
            int x = 360 + (int)(moonPhonePos.X/0.43*360);
            int y = 240 + (int)(moonPhonePos.Y/0.36*240);
            if(moonPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 25, new Scalar(200,200,255), -1);

            x = 360 + (int)(sunPhonePos.X/0.43*360);
            y = 240 + (int)(sunPhonePos.Y/0.36*240);
            if(sunPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 25, new Scalar(255,255,0), -1);

            x = 360 + (int)(mercuryPhonePos.X/0.43*360);
            y = 240 + (int)(mercuryPhonePos.Y/0.36*240);
            if(mercuryPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 15, new Scalar(255,128,0), -1);

            x = 360 + (int)(venusPhonePos.X/0.43*360);
            y = 240 + (int)(venusPhonePos.Y/0.36*240);
            if(venusPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 15, new Scalar(162,255,255), -1);

            x = 360 + (int)(marsPhonePos.X/0.43*360);
            y = 240 + (int)(marsPhonePos.Y/0.36*240);
            if(marsPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 15, new Scalar(255,0,0), -1);

            x = 360 + (int)(jupyterPhonePos.X/0.43*360);
            y = 240 + (int)(jupyterPhonePos.Y/0.36*240);
            if(jupyterPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 20, new Scalar(255,178,110), -1);

            x = 360 + (int)(saturnPhonePos.X/0.43*360);
            y = 240 + (int)(saturnPhonePos.Y/0.36*240);
            if(saturnPhonePos.Z > 0.6)
                Imgproc.circle(mat, new Point(x, y), 20, new Scalar(255,115,55), -1);

            String text;
            text = "Moon Az: " + allPoses.Moon.Azimuth*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 25), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            text = "Moon El: " + allPoses.Moon.Elevation*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 45), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            /*
            text = "Moon Px: " + moonPhonePos.X;
            Imgproc.putText(mat, text, new Point(5, 65), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255), 1);
            text = "Moon Py: " + moonPhonePos.Y;
            Imgproc.putText(mat, text, new Point(5, 85), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255), 1);
            text = "Moon Pz: " + moonPhonePos.Z;
            Imgproc.putText(mat, text, new Point(5, 105), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255), 1);
*/
            Imgproc.putText(mat, "Phone Status: ", new Point(5, 370), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255), 1);
            text = "Lat: " + poseCalculator.getLatitude()*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 390), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            text = "Lon: " + poseCalculator.getLongitude()*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 410), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            text = "Azim:  " + poseCalculator.getAzimuth()*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 430), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            text = "Pitch: " + poseCalculator.getPitch()*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 450), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
            text = "Roll:  " + poseCalculator.getRoll()*180/Math.PI;
            Imgproc.putText(mat, text, new Point(5, 470), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );

            Utils.matToBitmap(mat, bitmapImage);
            mImageView.setImageBitmap(bitmapImage);
            img.close();

        }
    };

    CameraDevice.StateCallback camera_state_callback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //SurfaceTexture texture = mTextureView.getSurfaceTexture();
            try {
                Log.d("MainActivity", "Entering onOpened");
                mPreviewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
                camera.createCaptureSession(Arrays.asList(mImageReader.getSurface()), capture_state_callback, null);
            }catch (CameraAccessException e) {
                showToast("Cannot create capture session: camera acceses exception");
                finish();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            showToast("Camera error: " + error);
            finish();
        }
    };

    CameraCaptureSession.StateCallback capture_state_callback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            try {
                session.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                showToast("Capture session configured correctly");
            }catch (CameraAccessException e) {
                showToast("Error while configuring capture session");
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            showToast("ERROR while configuring capture session");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        }
    }

    private BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
        }
    };

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
