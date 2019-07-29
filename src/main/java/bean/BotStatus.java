package bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by QuangDN
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BotStatus {

	@JsonProperty("mention_num")
	public int mentionRoomNum;

	@JsonProperty("unread_num")
	public int unread_num;
}
