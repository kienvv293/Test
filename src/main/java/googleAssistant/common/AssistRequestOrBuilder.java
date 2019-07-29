// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/assistant/embedded/v1alpha2/embedded_assistant.proto

package googleAssistant.common;

public interface AssistRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.assistant.embedded.v1alpha2.AssistRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The `config` message provides information to the recognizer that
   * specifies how to process the request.
   * The first `AssistRequest` message must contain a `config` message.
   * </pre>
   *
   * <code>.google.assistant.embedded.v1alpha2.AssistConfig config = 1;</code>
   */
  boolean hasConfig();
  /**
   * <pre>
   * The `config` message provides information to the recognizer that
   * specifies how to process the request.
   * The first `AssistRequest` message must contain a `config` message.
   * </pre>
   *
   * <code>.google.assistant.embedded.v1alpha2.AssistConfig config = 1;</code>
   */
  googleAssistant.common.AssistConfig getConfig();
  /**
   * <pre>
   * The `config` message provides information to the recognizer that
   * specifies how to process the request.
   * The first `AssistRequest` message must contain a `config` message.
   * </pre>
   *
   * <code>.google.assistant.embedded.v1alpha2.AssistConfig config = 1;</code>
   */
  googleAssistant.common.AssistConfigOrBuilder getConfigOrBuilder();

  /**
   * <pre>
   * The audio data to be recognized. Sequential chunks of audio data are sent
   * in sequential `AssistRequest` messages. The first `AssistRequest`
   * message must not contain `audio_in` data and all subsequent
   * `AssistRequest` messages must contain `audio_in` data. The audio bytes
   * must be encoded as specified in `AudioInConfig`.
   * Audio must be sent at approximately real-time (16000 samples per second).
   * An error will be returned if audio is sent significantly faster or
   * slower.
   * </pre>
   *
   * <code>bytes audio_in = 2;</code>
   */
  com.google.protobuf.ByteString getAudioIn();

  public googleAssistant.common.AssistRequest.TypeCase getTypeCase();
}
