package assistant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import assistant.reminder.review.PersonalIssueReminderExecute;
import assistant.reminder.todayTask.TodayTaskReminderExecute;
import bean.Message;
import common.BotProperty;
import common.StringCommon;
import common.UserCommon;
import common.chatwork.ChatworkCommon;
import constant.ActionEnum;
import constant.BotConst;
import constant.MessageConst;
import constant.RedmineConst;

/**
 * Created by QuangDn
 */
public class ChatworkClient implements Runnable {

    private String bot_id = "";
    private String cw_api_token = "";

    // private boolean breakFlag = false;
    private boolean aimode = false;
    // private boolean meetingReminder = false;
    // private boolean reviewReminder = false;
    private HttpClient httpClient = null;
    private ObjectMapper mapper = null;
    private String roomId;

    public ChatworkClient(String roomId) {
        this.roomId = roomId;
        cw_api_token = BotProperty.getProperty(BotProperty.CW_API_TOKEN);
        bot_id = BotProperty.getProperty(BotProperty.BOT_ID);
        httpClient = new HttpClient();

        HttpClientParams httpClientParams = new HttpClientParams();
        httpClientParams.setConnectionManagerTimeout(60 * 1000);
        httpClientParams.setSoTimeout(60 * 1000);
        httpClient.setParams(httpClientParams);
        System.out.println(new Date() + ": Init ChatworkClient done");
        mapper = new ObjectMapper();
    }

    /**
     * BOT (API SIMSIMI)
     *
     * @param roomId
     * @throws Exception
     */
    // tannh 2019/01/09 mod start
    @Override
    public void run() {
        // tannh 2019/01/09 mod end
        System.out.println(new Date() + " : " + "********** X Assistant started **********");

        // Notify to room
        // sendMessage(roomId, "TO ALL >>> BOT STARTED!");

        // tannh 2019/01/09 mod start
        // while (!breakFlag) {
        // int mentionRoomNum = 0;
        // int unreadRoomNum = 0;
        // try {
        // // Check the number of messages mention BOT
        // Date startTime = new Date();
        // System.out.println(startTime + ": Check new message: Start");
        // BotStatus status =
        // mapper.readValue(get(BotConst.CW_API_URL.concat("/rooms/").concat(roomId),
        // BotConst.CW_HEADER_NAME, cw_api_token), new
        // TypeReference<BotStatus>() {
        // });
        //
        // mentionRoomNum = status.mentionRoomNum;
        // unreadRoomNum = status.unread_num;
        // calculateTime(startTime, new Date());
        // System.out.println(new Date() + ": Check new message: Done. Number: "
        // + unreadRoomNum);
        // } catch (Exception e) {
        // unreadRoomNum = 0;
        // mentionRoomNum = 0;
        // }
        // if (unreadRoomNum == 0 || mentionRoomNum == 0) {
        // Thread.sleep(Integer.valueOf(BotProperty.getProperty(BotProperty.BOT_DELAY_TIME)));
        // continue;
        // }
        // Date currentTime = new Date();
        // Calendar calendar = Calendar.getInstance();
        // calendar.setTime(currentTime);
        // int dayOfW = calendar.get(Calendar.DAY_OF_WEEK);
        //
        // if (dayOfW >= 2 && dayOfW <= 6) {
        // int hourOfD = calendar.get(Calendar.HOUR_OF_DAY);
        // int minute = calendar.get(Calendar.MINUTE);
        // // 0h~7h: Reset flag
        // if (hourOfD >= 0 && hourOfD <= 7) {
        // meetingReminder = true;
        // }
        // if (hourOfD == 8 && (minute == 30 || minute == 31) && meetingReminder
        // && "124043200".equals(roomId)) {
        // meetingReminder = false;
        // // Send message of Bot to all
        // sendMessage(roomId, "[toall] mọi người qua họp nhé (blush) !!!");
        // }
        // // 0h~7h + 12h~14h: Reset flag
        // if ((hourOfD >= 0 && hourOfD <= 7) || (hourOfD >= 12 && hourOfD <=
        // 14)) {
        // reviewReminder = true;
        // }
        // // Remind at 9h + 14h
        // if (((hourOfD == 9 && (minute == 0 || minute == 1)) || (hourOfD == 14
        // && (minute == 0 || minute == 1)))
        // && reviewReminder && "124043200".equals(roomId)) {
        // reviewReminder = false;
        // ReviewReminderExecute reviewReminder = new
        // ReviewReminderExecute(cw_api_token, httpClient, roomId);
        // reviewReminder.remind();
        // }
        // }
        while (true) {
            try {
                // tannh 2019/01/09 mod end
                List<Message> msgLst = getMessages(roomId);
                if (msgLst == null || msgLst.size() == 0) {
                    Thread.sleep(Integer.valueOf(BotProperty.getProperty(BotProperty.BOT_DELAY_TIME)));
                    continue;
                }

                // [To:id]
                // [rp aid=3373936
                for (Message message : msgLst) {
                    if (!message.body.startsWith("[To:".concat(bot_id).concat("]")) && !message.body.startsWith("[rp aid=".concat(bot_id))) {
                        continue;
                    }

                    // Get message from room of ChatWork
                    String messSend = message.body.substring(message.body.indexOf(BotConst.LINE_BREAK) + 1);
                    String command = getCommand(messSend);
                    if (StringCommon.isNull(command)) {
                        continue;
                    }
                    StringBuilder messReply = new StringBuilder();
                    try {
                        if (isAdminCommand(command, message)) {
                            System.out.println(new Date() + " : " + "Admin command accepted !!!");
                            setToUserInfo(message, messReply);
                            // lucall
                            if (ActionEnum.lucall.toString().equals(command)) {
                                messReply.append("A hihi.. em (sẽ) login cho anh lúc 8h31' nhé (dance)");
                            } else
                            // stop
                            if (ActionEnum.stop.toString().equals(command)) {
                                // Notify to room
                                // tannh 2019/01/09 mod start
                                messReply.append("Sorry, this command has been disable. Because of i'm multithread app :)");
                                // breakFlag = true;
                                // tannh 2019/01/09 mod end
                            } else
                            // aimode
                            if (ActionEnum.aimode.toString().equals(command)) {
                                String[] aimodeParams = messSend.split(BotConst.HALF_SPACE);
                                if (aimodeParams.length > 1 && BotConst.OF_ON.equals(StringCommon.trimSpace(aimodeParams[1]).toLowerCase())) {
                                    aimode = true;
                                } else {
                                    aimode = false;
                                }
                                // Notify to room
                                messReply.append(">>> AI mode is: " + aimode);
                            } else
                            // estfulldatejp
                            if (ActionEnum.estfulldatejp.toString().equals(command)) {
                                // messReply =
                                // CommandExecute.runEstFullDateJpCommand(messSend,
                                // messReply);
                                messReply.append("Under Construction !!! Chức năng này đang có lỗi.");
                            }
                            // tannh 2019/01/09 add start
                            else if (ActionEnum.remind.toString().equals(command)) {
                                Thread taskReminderThread = new Thread(new TodayTaskReminderExecute());
                                taskReminderThread.start();
                                Thread reminderThread = new Thread(new PersonalIssueReminderExecute());
                                reminderThread.start();
                                messReply.append("done nhé anh ;)");
                            }
                            // tannh 2019/01/09 add end
                            // command's not recognized
                            else {
                                messReply.append(MessageConst.COMMAND_NOT_FOUND + command);
                            }
                            ChatworkCommon.sendMessage(roomId, messReply.toString());
                        } else {
                            // Token
                            String token = "";
                            try {
                                token = UserCommon.checkUserRole(message.account.accountId, command);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            // Check permission
                            if (StringCommon.isNull(token)) {
                                setToUserInfo(message, messReply);
                                if (aimode) {
                                    messReply.append(GoogleAssistantClient.executeGoogleAssistant(messSend, false));
                                } else {
                                    messReply.append("Sorry, AI mode is not available");
                                }
                                // messReply.append(SimsimiExecute.getMessage(messSend));
                            } else {
                                setToUserInfo(message, messReply);
                                // help
                                if (ActionEnum.help.toString().equals(command)) {
                                    messReply = CommandExecute.runHelpCommand(messReply);
                                } else
                                // deadline
                                if (ActionEnum.deadline.toString().equals(command)) {
                                    messReply = CommandExecute.runDeadlineCommand(messSend, messReply);
                                } else
                                // status
                                if (ActionEnum.status.toString().equals(command)) {
                                    messReply = CommandExecute.runStatusCommand(messSend, messReply);
                                } else
                                // assignee
                                if (ActionEnum.assignee.toString().equals(command)) {
                                    messReply = CommandExecute.runAssigneeCommand(messSend, token, messReply);
                                } else
                                // note
                                if (ActionEnum.note.toString().equals(command)) {
                                    messReply = CommandExecute.runUpdateNoteCommand(messSend, token, messReply);
                                } else
                                // hopedate
                                if (ActionEnum.hopedate.toString().equals(command)) {
                                    messReply = CommandExecute.runUpdateDateCommand(messSend, token, messReply, RedmineConst.ITM_ID_HOPE_DATE);
                                } else
                                // estCodedate
                                if (ActionEnum.estcodedate.toString().equals(command)) {
                                    messReply = CommandExecute.runUpdateDateCommand(messSend, token, messReply, RedmineConst.ITM_ID_EST_CODE_DATE);
                                } else
                                // estFulldate
                                if (ActionEnum.estfulldate.toString().equals(command)) {
                                    messReply = CommandExecute.runUpdateDateCommand(messSend, token, messReply, RedmineConst.ITM_ID_EST_FULL_DATE);
                                } else
                                // releasedate
                                if (ActionEnum.releasedate.toString().equals(command)) {
                                    messReply = CommandExecute.runUpdateDateCommand(messSend, token, messReply, RedmineConst.ITM_ID_RELEASE_DATE);
                                } else
                                // spent time
                                if (ActionEnum.spent.toString().equals(command)) {
                                    messReply = CommandExecute.runSpentTimeUpdCommand(messSend, token, messReply);
                                } else if (ActionEnum.jpmiss.toString().equals(command)) {
                                    messReply = CommandExecute.runJPMissCommand(messReply);
                                } else if (ActionEnum.vnupdatemiss.toString().equals(command)) {
                                    messReply = CommandExecute.runVNUpdateMissCommand(messReply);
                                    // check qa
                                } else if (ActionEnum.checkqa.toString().equals(command)) {
                                    messReply = CommandExecute.runManagerQA(messReply);
                                    // check nop hang
                                } else if (ActionEnum.checkreply.toString().equals(command)) {
                                    messReply = CommandExecute.runCheckInfoReplyTicketDeliveryOfGoods(messSend, messReply);
                                } else if (ActionEnum.thongke.toString().equals(command)) {
                                    messReply = CommandExecute.runProductivityStatisticss(messSend, messReply);
                                } else {
                                    // command's not recognized
                                    messReply.append(MessageConst.COMMAND_NOT_FOUND + command);
                                }
                            }
                            if (messReply != null && messReply.length() > 0) {
                                // Send message of Bot to the sender
                                ChatworkCommon.sendMessage(roomId, messReply.toString());
                            }
                        }
                    } catch (Exception e) {
                        if (messReply == null) {
                            messReply = new StringBuilder();
                        }
                        messReply.append("[To:3185755]").append("[To:3210750]").append(BotConst.LINE_BREAK);
                        messReply.append(MessageConst.COMMON_ERROR + e.getMessage());
                        ChatworkCommon.sendMessage(roomId, messReply.toString());
                        e.printStackTrace();
                    }
                }
                // tannh 2019/01/09 mod start
                // if (!breakFlag) {
                Thread.sleep(Integer.valueOf(BotProperty.getProperty(BotProperty.BOT_DELAY_TIME)));
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // sendMessage(roomId, ">>> X Assistant has been shutdown !");
        // System.out.println(new Date() + " : " + "********** X Assistant
        // stopped **********");
        // tannh 2019/01/09 mod end
    }

    private boolean isAdminCommand(String command, Message message) {
        for (String keyVal : ActionEnum.getActionList().keySet()) {
            if (keyVal.equals(command)) {
                if (ActionEnum.getActionList().get(keyVal).getRoleId().equals(BotConst.ROLE_ADMIN)) {
                    // TanNH // QuangDN
                    if (message.account.accountId.equals("3185755") || message.account.accountId.equals("3210750")) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void setToUserInfo(Message message, StringBuilder messReply) {
        messReply.append("[To:").append(message.account.accountId).append("] ").append(message.account.name).append(BotConst.LINE_BREAK);
    }

    private String getCommand(String messSend) {
        String actionId = "";
        if (StringCommon.isNull(messSend)) {
            actionId = "";
        } else {
            String message = messSend.trim();
            if (message.contains(BotConst.LINE_BREAK)) {
                message = message.split(BotConst.LINE_BREAK)[0];
            }
            if (message.contains(BotConst.HALF_SPACE)) {
                message = message.substring(0, message.indexOf(BotConst.HALF_SPACE)).trim();
            }
            actionId = message.trim();
        }
        return actionId.toLowerCase();
    }

    /**
     * Get message from room.
     *
     * @param roomId
     * @return the list of Message
     * @throws IOException
     */
    private List<Message> getMessages(String roomId) {
        try {
            // System.out.println(new Date() + ": Get new message: Start");
            Date startTime = new Date();
            String json = get(BotConst.CW_API_URL.concat("/rooms/").concat(roomId).concat("/messages"), BotConst.CW_HEADER_NAME, cw_api_token);
            calculateTime(roomId, startTime, new Date());
            // System.out.println(new Date() + ": Get new message: Done. Val: "
            // + json);
            if (StringCommon.isNull(json)) {
                return null;
            }

            return mapper.readValue(json, new TypeReference<List<Message>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // tannh 2019/01/09 del start
    // /**
    // * Send message to room.
    // *
    // * @param roomId
    // * @param message
    // * @throws IOException
    // */
    // private void sendMessage(String roomId, String message) {
    // PostMethod method = null;
    // try {
    // method = new
    // PostMethod(BotConst.CW_API_URL.concat("/rooms/").concat(roomId).concat("/messages"));
    // method.addRequestHeader("X-ChatWorkToken", cw_api_token);
    // method.addRequestHeader("Content-type",
    // "application/x-www-form-urlencoded; charset=UTF-8");
    // method.setParameter("body", message);
    //
    // int statusCode = httpClient.executeMethod(method);
    // if (statusCode != HttpStatus.SC_OK) {
    // throw new Exception("Response is not valid. Check your API Key or
    // ChatWork API status. response_code = "
    // + statusCode + ", message =" + method.getResponseBodyAsString());
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (method != null) {
    // method.releaseConnection();
    // }
    // }
    // }
    // tannh 2019/01/09 del end

    /**
     * Get method of APIs
     *
     * @param path
     * @return json result
     * @throws IOException
     */
    private String get(String path, String headerName, String apiKey) {
        GetMethod method = null;
        try {
            method = new GetMethod(path);
            method.addRequestHeader(headerName, apiKey);
            int statusCode = httpClient.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                // System.out.println(new Date() + " : " + "Không có message mới
                // hoặc Token sai. " + method.getResponseBodyAsString());
                return BotConst.BLANK;
            } else {
                return method.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BotConst.BLANK;
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    /**
     * <PRE>
     * calculateTime Method。
     * </PRE>
     * 
     */
    public static void calculateTime(String roomId, Date t1, Date t2) {
        long diffInMilis = Math.abs(t1.getTime() - t2.getTime());
        long diffInSeconds = 0;
        long diffInMinutes = 0;

        if (diffInMilis > 1000) {
            diffInSeconds = diffInMilis / 1000;
            diffInMilis = diffInMilis % 1000;
            if (diffInSeconds > 60) {
                diffInMinutes = diffInSeconds / 60;
                diffInSeconds = diffInSeconds % 60;
            }
        }
        // tannh 2019/01/09 mod start
        System.out.println("_____" + new Date() + "____roomId: " + roomId + "_Spent time: " + diffInMinutes + " minute(s) " + diffInSeconds + " second(s) "
                + diffInMilis + " milisecond(s)");
        // tannh 2019/01/09 mod end
    }
}
