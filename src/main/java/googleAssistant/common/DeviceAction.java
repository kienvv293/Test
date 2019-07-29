// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/assistant/embedded/v1alpha2/embedded_assistant.proto

package googleAssistant.common;

/**
 * <pre>
 * The response returned to the device if the user has triggered a Device
 * Action. For example, a device which supports the query *Turn on the light*
 * would receive a `DeviceAction` with a JSON payload containing the semantics
 * of the request.
 * </pre>
 *
 * Protobuf type {@code google.assistant.embedded.v1alpha2.DeviceAction}
 */
public  final class DeviceAction extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.assistant.embedded.v1alpha2.DeviceAction)
    DeviceActionOrBuilder {
private static final long serialVersionUID = 0L;
  // Use DeviceAction.newBuilder() to construct.
  private DeviceAction(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DeviceAction() {
    deviceRequestJson_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private DeviceAction(
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

            deviceRequestJson_ = s;
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
    return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceAction_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceAction_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            googleAssistant.common.DeviceAction.class, googleAssistant.common.DeviceAction.Builder.class);
  }

  public static final int DEVICE_REQUEST_JSON_FIELD_NUMBER = 1;
  private volatile java.lang.Object deviceRequestJson_;
  /**
   * <pre>
   * JSON containing the device command response generated from the triggered
   * Device Action grammar. The format is given by the
   * `action.devices.EXECUTE` intent for a given
   * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
   * </pre>
   *
   * <code>string device_request_json = 1;</code>
   */
  public java.lang.String getDeviceRequestJson() {
    java.lang.Object ref = deviceRequestJson_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      deviceRequestJson_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * JSON containing the device command response generated from the triggered
   * Device Action grammar. The format is given by the
   * `action.devices.EXECUTE` intent for a given
   * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
   * </pre>
   *
   * <code>string device_request_json = 1;</code>
   */
  public com.google.protobuf.ByteString
      getDeviceRequestJsonBytes() {
    java.lang.Object ref = deviceRequestJson_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      deviceRequestJson_ = b;
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
    if (!getDeviceRequestJsonBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, deviceRequestJson_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getDeviceRequestJsonBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, deviceRequestJson_);
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
    if (!(obj instanceof googleAssistant.common.DeviceAction)) {
      return super.equals(obj);
    }
    googleAssistant.common.DeviceAction other = (googleAssistant.common.DeviceAction) obj;

    boolean result = true;
    result = result && getDeviceRequestJson()
        .equals(other.getDeviceRequestJson());
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
    hash = (37 * hash) + DEVICE_REQUEST_JSON_FIELD_NUMBER;
    hash = (53 * hash) + getDeviceRequestJson().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static googleAssistant.common.DeviceAction parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceAction parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static googleAssistant.common.DeviceAction parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static googleAssistant.common.DeviceAction parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceAction parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static googleAssistant.common.DeviceAction parseFrom(
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
  public static Builder newBuilder(googleAssistant.common.DeviceAction prototype) {
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
   * The response returned to the device if the user has triggered a Device
   * Action. For example, a device which supports the query *Turn on the light*
   * would receive a `DeviceAction` with a JSON payload containing the semantics
   * of the request.
   * </pre>
   *
   * Protobuf type {@code google.assistant.embedded.v1alpha2.DeviceAction}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.assistant.embedded.v1alpha2.DeviceAction)
      googleAssistant.common.DeviceActionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceAction_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceAction_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              googleAssistant.common.DeviceAction.class, googleAssistant.common.DeviceAction.Builder.class);
    }

    // Construct using com.google.assistant.embedded.v1alpha2.DeviceAction.newBuilder()
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
      deviceRequestJson_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return googleAssistant.common.AssistantProto.internal_static_google_assistant_embedded_v1alpha2_DeviceAction_descriptor;
    }

    public googleAssistant.common.DeviceAction getDefaultInstanceForType() {
      return googleAssistant.common.DeviceAction.getDefaultInstance();
    }

    public googleAssistant.common.DeviceAction build() {
      googleAssistant.common.DeviceAction result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public googleAssistant.common.DeviceAction buildPartial() {
      googleAssistant.common.DeviceAction result = new googleAssistant.common.DeviceAction(this);
      result.deviceRequestJson_ = deviceRequestJson_;
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
      if (other instanceof googleAssistant.common.DeviceAction) {
        return mergeFrom((googleAssistant.common.DeviceAction)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(googleAssistant.common.DeviceAction other) {
      if (other == googleAssistant.common.DeviceAction.getDefaultInstance()) return this;
      if (!other.getDeviceRequestJson().isEmpty()) {
        deviceRequestJson_ = other.deviceRequestJson_;
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
      googleAssistant.common.DeviceAction parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (googleAssistant.common.DeviceAction) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object deviceRequestJson_ = "";
    /**
     * <pre>
     * JSON containing the device command response generated from the triggered
     * Device Action grammar. The format is given by the
     * `action.devices.EXECUTE` intent for a given
     * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
     * </pre>
     *
     * <code>string device_request_json = 1;</code>
     */
    public java.lang.String getDeviceRequestJson() {
      java.lang.Object ref = deviceRequestJson_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        deviceRequestJson_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * JSON containing the device command response generated from the triggered
     * Device Action grammar. The format is given by the
     * `action.devices.EXECUTE` intent for a given
     * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
     * </pre>
     *
     * <code>string device_request_json = 1;</code>
     */
    public com.google.protobuf.ByteString
        getDeviceRequestJsonBytes() {
      java.lang.Object ref = deviceRequestJson_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        deviceRequestJson_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * JSON containing the device command response generated from the triggered
     * Device Action grammar. The format is given by the
     * `action.devices.EXECUTE` intent for a given
     * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
     * </pre>
     *
     * <code>string device_request_json = 1;</code>
     */
    public Builder setDeviceRequestJson(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      deviceRequestJson_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * JSON containing the device command response generated from the triggered
     * Device Action grammar. The format is given by the
     * `action.devices.EXECUTE` intent for a given
     * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
     * </pre>
     *
     * <code>string device_request_json = 1;</code>
     */
    public Builder clearDeviceRequestJson() {
      
      deviceRequestJson_ = getDefaultInstance().getDeviceRequestJson();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * JSON containing the device command response generated from the triggered
     * Device Action grammar. The format is given by the
     * `action.devices.EXECUTE` intent for a given
     * [trait](https://developers.google.com/assistant/sdk/reference/traits/).
     * </pre>
     *
     * <code>string device_request_json = 1;</code>
     */
    public Builder setDeviceRequestJsonBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      deviceRequestJson_ = value;
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


    // @@protoc_insertion_point(builder_scope:google.assistant.embedded.v1alpha2.DeviceAction)
  }

  // @@protoc_insertion_point(class_scope:google.assistant.embedded.v1alpha2.DeviceAction)
  private static final googleAssistant.common.DeviceAction DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new googleAssistant.common.DeviceAction();
  }

  public static googleAssistant.common.DeviceAction getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DeviceAction>
      PARSER = new com.google.protobuf.AbstractParser<DeviceAction>() {
    public DeviceAction parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new DeviceAction(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<DeviceAction> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DeviceAction> getParserForType() {
    return PARSER;
  }

  public googleAssistant.common.DeviceAction getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

