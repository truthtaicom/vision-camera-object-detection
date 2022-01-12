package com.visioncameraobjectdetection;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisionCameraObjectDetectionPlugin extends FrameProcessorPlugin {

  private final ObjectDetector objectDetector = ObjectDetection.getClient(ObjectDetectorOptions.DEFAULT_OPTIONS);

  @Override
  public Object callback(ImageProxy frame, @NotNull Object[] params) {
    @SuppressLint("UnsafeOptInUsageError")
    Image mediaImage = frame.getImage();
    if (mediaImage != null) {
      InputImage image = InputImage.fromMediaImage(mediaImage, frame.getImageInfo().getRotationDegrees());
      Task<List<DetectedObject>> task = objectDetector.process(image);

      try {
        List<DetectedObject> objects = Tasks.await(task);
        Log.d("XXX", String.valueOf(objects.isEmpty()));


        WritableNativeArray array = new WritableNativeArray();

        return array;
      } catch (Exception e) {
        Log.d("XXX", "ERROR");
        e.printStackTrace();
      }
    }
    return null;
  }

  VisionCameraObjectDetectionPlugin() {
    super("detectObject");
  }
}
