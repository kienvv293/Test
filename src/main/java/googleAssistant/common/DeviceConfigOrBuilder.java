// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/assistant/embedded/v1alpha2/embedded_assistant.proto

package googleAssistant.common;

public interface DeviceConfigOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.assistant.embedded.v1alpha2.DeviceConfig)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * *Required* Unique identifier for the device. The id length must be 128
   * characters or less. Example: DBCDW098234. This MUST match the device_id
   * returned from device registration. This device_id is used to match against
   * the user's registered devices to lookup the supported traits and
   * capabilities of this device. This information should not change across
   * device reboots. However, it should not be saved across
   * factory-default resets.
   * </pre>
   *
   * <code>string device_id = 1;</code>
   */
  java.lang.String getDeviceId();
  /**
   * <pre>
   * *Required* Unique identifier for the device. The id length must be 128
   * characters or less. Example: DBCDW098234. This MUST match the device_id
   * returned from device registration. This device_id is used to match against
   * the user's registered devices to lookup the supported traits and
   * capabilities of this device. This information should not change across
   * device reboots. However, it should not be saved across
   * factory-default resets.
   * </pre>
   *
   * <code>string device_id = 1;</code>
   */
  com.google.protobuf.ByteString
      getDeviceIdBytes();

  /**
   * <pre>
   * *Required* Unique identifier for the device model. The combination of
   * device_model_id and device_id must have been previously associated through
   * device registration.
   * </pre>
   *
   * <code>string device_model_id = 3;</code>
   */
  java.lang.String getDeviceModelId();
  /**
   * <pre>
   * *Required* Unique identifier for the device model. The combination of
   * device_model_id and device_id must have been previously associated through
   * device registration.
   * </pre>
   *
   * <code>string device_model_id = 3;</code>
   */
  com.google.protobuf.ByteString
      getDeviceModelIdBytes();
}
