/* globals __labelImage */
import type { Frame } from 'react-native-vision-camera';

interface DetectedObject {
  /**
   * A label describing the image, in english.
   */
  location: {
    top: number,
    left: number,
    right: number,
    bottom: number
  };
  /**
   * A floating point number from 0 to 1, describing the confidence (percentage).
   */
  confidence: number;
}

/**
 * Returns an array of matching `DetectedObject`s for the given frame.
 *
 */
export function detectObject(frame: Frame): DetectedObject[] {
  'worklet';
  // @ts-expect-error Frame Processors are not typed.
  return __detectObject(frame);
}
