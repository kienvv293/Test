// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/assistant/embedded/v1alpha2/embedded_assistant.proto

package googleAssistant.common;

/**
 * <pre>
 * *Required* Fields that identify the device to the Assistant.
 * See also:
 * *   [Register a Device - REST API](https://developers.google.com/assistant/sdk/reference/device-registration/register-device-manual)
 * *   [Device Model and Instance Schemas](https://developers.google.com/assistant/sdk/reference/device-registration/model-and-instance-schemas)
 * *   [Device Proto](https://developers.google.com/assistant/sdk/reference/rpc/google.assistant.devices.v1alpha2#device)
 * </pre>
 *
 * Protobuf type {@code google.assistant.embedded.v1alpha2.DeviceConfig}
 */
public  final class DeviceConfig extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.assistant.embedded.v1alpha2.DeviceConfig)
    DeviceConfigOrBuilder {
private static final long serialVersionUID = 0L;
  // Use DeviceConfig.newBuilder() to construct.
  private DeviceConfig(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DeviceConfig() {
    deviceId_ = "";
    deviceModelId_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private DeviceConfig(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            deviceId_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            deviceModelId_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceConfig_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceConfig_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            googleAssistant.common.DeviceConfig.class, googleAssistant.common.DeviceConfig.Builder.class);
  }

  public static final int DEVICE_ID_FIELD_NUMBER = 1;
  private volatile java.lang.Object deviceId_;
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
  public java.lang.String getDeviceId() {
    java.lang.Object ref = deviceId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      deviceId_ = s;
      return s;
    }
  }
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
  public com.google.protobuf.ByteString
      getDeviceIdBytes() {
    java.lang.Object ref = deviceId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      deviceId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int DEVICE_MODEL_ID_FIELD_NUMBER = 3;
  private volatile java.lang.Object deviceModelId_;
  /**
   * <pre>
   * *Required* Unique identifier for the device model. The combination of
   * device_model_id and device_id must have been previously associated through
   * device registration.
   * </pre>
   *
   * <code>string device_model_id = 3;</code>
   */
  public java.lang.String getDeviceModelId() {
    java.lang.Object ref = deviceModelId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      deviceModelId_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * *Required* Unique identifier for the device model. The combination of
   * device_model_id and device_id must have been previously associated through
   * device registration.
   * </pre>
   *
   * <code>string device_model_id = 3;</code>
   */
  public com.google.protobuf.ByteString
      getDeviceModelIdBytes() {
    java.lang.Object ref = deviceModelId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      deviceModelId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getDeviceIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, deviceId_);
    }
    if (!getDeviceModelIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, deviceModelId_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getDeviceIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, deviceId_);
    }
    if (!getDeviceModelIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, deviceModelId_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof googleAssistant.common.DeviceConfig)) {
      return super.equals(obj);
    }
    googleAssistant.common.DeviceConfig other = (googleAssistant.common.DeviceConfig) obj;

    boolean result = true;
    result = result && getDeviceId()
        .equals(other.getDeviceId());
    result = result && getDeviceModelId()
        .equals(other.getDeviceModelId());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + DEVICE_ID_FIELD_NUMBER;
    hash = (53 * hash) + getDeviceId().hashCode();
    hash = (37 * hash) + DEVICE_MODEL_ID_FIELD_NUMBER;
    hash = (53 * hash) + getDeviceModelId().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static googleAssistant.common.DeviceConfig parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static googleAssistant.common.DeviceConfig parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceConfig parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceConfig parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(googleAssistant.common.DeviceConfig prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * *Required* Fields that identify the device to the Assistant.
   * See also:
   * *   [Register a Device - REST API](https://developers.google.com/assistant/sdk/reference/device-registration/register-device-manual)
   * *   [Device Model and Instance Schemas](https://developers.google.com/assistant/sdk/reference/device-registration/model-and-instance-schemas)
   * *   [Device Proto](https://developers.google.com/assistant/sdk/reference/rpc/google.assistant.devices.v1alpha2#device)
   * </pre>
   *
   * Protobuf type {@code google.assistant.embedded.v1alpha2.DeviceConfig}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.assistant.embedded.v1alpha2.DeviceConfig)
      googleAssistant.common.DeviceConfigOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceConfig_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceConfig_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              googleAssistant.common.DeviceConfig.class, googleAssistant.common.DeviceConfig.Builder.class);
    }

    // Construct using com.google.assistant.embedded.v1alpha2.DeviceConfig.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      deviceId_ = "";

      deviceModelId_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceConfig_descriptor;
    }

    public googleAssistant.common.DeviceConfig getDefaultInstanceForType() {
      return googleAssistant.common.DeviceConfig.getDefaultInstance();
    }

    public googleAssistant.common.DeviceConfig build() {
      googleAssistant.common.DeviceConfig result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public googleAssistant.common.DeviceConfig buildPartial() {
      googleAssistant.common.DeviceConfig result = new googleAssistant.common.DeviceConfig(this);
      result.deviceId_ = deviceId_;
      result.deviceModelId_ = deviceModelId_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof googleAssistant.common.DeviceConfig) {
        return mergeFrom((googleAssistant.common.DeviceConfig)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(googleAssistant.common.DeviceConfig other) {
      if (other == googleAssistant.common.DeviceConfig.getDefaultInstance()) return this;
      if (!other.getDeviceId().isEmpty()) {
        deviceId_ = other.deviceId_;
        onChanged();
      }
      if (!other.getDeviceModelId().isEmpty()) {
        deviceModelId_ = other.deviceModelId_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      googleAssistant.common.DeviceConfig parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (googleAssistant.common.DeviceConfig) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object deviceId_ = "";
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
    public java.lang.String getDeviceId() {
      java.lang.Object ref = deviceId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        deviceId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
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
    public com.google.protobuf.ByteString
        getDeviceIdBytes() {
      java.lang.Object ref = deviceId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
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
    public Builder setDeviceId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      deviceId_ = value;
      onChanged();
      return this;
    }
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
    public Builder clearDeviceId() {
      
      deviceId_ = getDefaultInstance().getDeviceId();
      onChanged();
      return this;
    }
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
    public Builder setDeviceIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      deviceId_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object deviceModelId_ = "";
    /**
     * <pre>
     * *Required* Unique identifier for the device model. The combination of
     * device_model_id and device_id must have been previously associated through
     * device registration.
     * </pre>
     *
     * <code>string device_model_id = 3;</code>
     */
    public java.lang.String getDeviceModelId() {
      java.lang.Object ref = deviceModelId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        deviceModelId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * *Required* Unique identifier for the device model. The combination of
     * device_model_id and device_id must have been previously associated through
     * device registration.
     * </pre>
     *
     * <code>string device_model_id = 3;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceModelIdBytes() {
      java.lang.Object ref = deviceModelId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceModelId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * *Required* Unique identifier for the device model. The combination of
     * device_model_id and device_id must have been previously associated through
     * device registration.
     * </pre>
     *
     * <code>string device_model_id = 3;</code>
     */
    public Builder setDeviceModelId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      deviceModelId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * *Required* Unique identifier for the device model. The combination of
     * device_model_id and device_id must have been previously associated through
     * device registration.
     * </pre>
     *
     * <code>string device_model_id = 3;</code>
     */
    public Builder clearDeviceModelId() {
      
      deviceModelId_ = getDefaultInstance().getDeviceModelId();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * *Required* Unique identifier for the device model. The combination of
     * device_model_id and device_id must have been previously associated through
     * device registration.
     * </pre>
     *
     * <code>string device_model_id = 3;</code>
     */
    public Builder setDeviceModelIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      deviceModelId_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.assistant.embedded.v1alpha2.DeviceConfig)
  }

  // @@protoc_insertion_point(class_scope:google.assistant.embedded.v1alpha2.DeviceConfig)
  private static final googleAssistant.common.DeviceConfig DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new googleAssistant.common.DeviceConfig();
  }

  public static googleAssistant.common.DeviceConfig getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DeviceConfig>
      PARSER = new com.google.protobuf.AbstractParser<DeviceConfig>() {
    public DeviceConfig parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new DeviceConfig(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<DeviceConfig> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DeviceConfig> getParserForType() {
    return PARSER;
  }

  public googleAssistant.common.DeviceConfig getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

