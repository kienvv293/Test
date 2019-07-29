package bean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import bean.Account;

/**
 * Created by QuangDn
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Message {

    @JsonProperty("message_id")
    public String message_id;

    @JsonProperty("account")
    public Account account;

    @JsonProperty("body")
    public String body;
}
