<div align="right">
<img align="right" src="https://github.com/mrousavy/react-native-vision-camera/blob/main/docs/static/img/frame-processors.gif?raw=true">
</div>

# vision-camera-object-detection

A [VisionCamera](https://github.com/mrousavy/react-native-vision-camera) Frame Processor Plugin to label images using [**MLKit Vision** Object Detection](https://developers.google.com/ml-kit/vision/object-detection).

## Installation

```sh
npm install vision-camera-object-detection
cd ios && pod install
```

Add the plugin to your `babel.config.js`:

```js
module.exports = {
  plugins: [
    [
      'react-native-reanimated/plugin',
      {
        globals: ['detectObject'],
      },
    ],

    // ...
```

> Note: You have to restart metro-bundler for changes in the `babel.config.js` file to take effect.

## Usage

```js
import { detectObject } from "vision-camera-object-detection";

// ...

const frameProcessor = useFrameProcessor((frame) => {
  'worklet';
  const labels = detectObject(frame);
}, []);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
