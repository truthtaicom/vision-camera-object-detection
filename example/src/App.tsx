import React, { useEffect, useState } from 'react';
import { StyleSheet, View, ActivityIndicator } from 'react-native';
import { useAnimatedStyle, useSharedValue } from 'react-native-reanimated';
import {
  Camera,
  useCameraDevices,
  useFrameProcessor,
} from 'react-native-vision-camera';
import { DetectedObject, detectObject } from 'vision-camera-object-detection';


export default function App() {
  const [hasPermission, setHasPermission] = useState(false);
  // const currentLabel = useSharedValue('');

  const devices = useCameraDevices();
  const device = devices.back;

  useEffect(() => {
    (async () => {
      const status = await Camera.requestCameraPermission();
      setHasPermission(status === 'authorized');
    })();
  }, []);

  const objectBounds = useSharedValue({ top: 0, left: 0, right: 0, bottom: 0 })

  const frameProcessor = useFrameProcessor(
    (frame) => {
      'worklet';
      const objects: DetectedObject[] = detectObject(frame);
      // const labels = detectObject(frame);

      console.log('Object:', objects[0]);
      // console.log('Labels:', labels);
      // currentLabel.value = labels[0];
    },
    // [currentLabel]
    []
  );

  // uses 'objectBounds' to position the rectangle on screen.
  // smoothly updates on UI thread whenever 'objectBounds' is changed
  const boxOverlayStyle = useAnimatedStyle(() => ({
    position: 'absolute',
    borderWidth: 2,
    borderColor: 'white',
    borderRadius: 10,
    ...objectBounds.value
  }), [objectBounds])

  return (
    <View style={styles.container}>
      {device != null && hasPermission ? (
        <>
          <Camera
            style={styles.camera}
            device={device}
            isActive={true}
            frameProcessor={frameProcessor}
            frameProcessorFps={3}
          />
          {/* <Label sharedValue={currentLabel} /> */}
        </>
      ) : (
        <ActivityIndicator size="large" color="white" />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  camera: {
    flex: 1,
    width: '100%',
  },
});
