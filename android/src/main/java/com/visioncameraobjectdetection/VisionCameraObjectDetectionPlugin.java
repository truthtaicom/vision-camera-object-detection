package com.visioncameraobjectdetection;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisionCameraObjectDetectionPlugin extends FrameProcessorPlugin {
  // Live detection and tracking
  ObjectDetectorOptions options =
    new ObjectDetectorOptions.Builder()
      .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//      .enableClassification()  // Optional
      .build();

  ObjectDetector objectDetector = ObjectDetection.getClient(options);

  @Override
  public Object callback(ImageProxy frame, @NotNull Object[] params) {
    @SuppressLint("UnsafeOptInUsageError")
    Image mediaImage = frame.getImage();
    if (mediaImage != null) {
      InputImage image = InputImage.fromMediaImage(mediaImage, frame.getImageInfo().getRotationDegrees());
      Task<List<DetectedObject>> task = objectDetector.process(image);

      try {
        List<DetectedObject> objects = Tasks.await(task);

        WritableNativeArray array = new WritableNativeArray();
        Log.d("TAG", String.valueOf(objects.size()));

        for (DetectedObject object : objects) {
          Rect boundingBox = object.getBoundingBox();
          Integer trackingId = object.getTrackingId();
          WritableNativeMap map = new WritableNativeMap();
          map.putInt("left", boundingBox.left);
          map.putInt("top", boundingBox.top);
          map.putInt("right", boundingBox.right);
          map.putInt("bottom", boundingBox.bottom);
          map.putInt("trackingID", trackingId);

          Log.d("TAG", " boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})");
          Log.d("TAG", " trackingId: ${trackingId}");

          array.pushMap(map);
        }
        return array;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  VisionCameraObjectDetectionPlugin() {
    super("detectObject");
  }
}
