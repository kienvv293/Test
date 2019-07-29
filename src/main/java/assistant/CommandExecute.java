/**
 *
 */
package assistant;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.TimeEntry;
import com.taskadapter.redmineapi.bean.TimeEntryFactory;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import assistant.deliveryOfGoods.TicketDeliveryOfGoods;
import assistant.productivityStatistics.ProductivityStatistics;
import common.RedmineCommon;
import common.StringCommon;
import constant.ActionEnum;
import constant.BotConst;
import constant.MessageConst;
import constant.RedmineConst;
import constant.SpentTimeActivityEnum;

/**
 * @author nguyenhuytan
 *
 */
public class CommandExecute {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static StringBuilder runHelpCommand(StringBuilder messReply) {
		messReply.append("Hi các anh đẹp zai :D List command:" + BotConst.LINE_BREAK);
		for (ActionEnum actionEnum : ActionEnum.getActionListSort()) {
			if (BotConst.ROLE_ADMIN.equals(actionEnum.getRoleId())) {
				continue;
			}
			messReply.append(actionEnum.name() + BotConst.HALF_SPACE + "_____" + actionEnum.getActionName()
					+ BotConst.LINE_BREAK);
		}
		return messReply;
	}

	public static StringBuilder runDeadlineCommand(String messSend, StringBuilder messReply) throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI,
				RedmineConst.API_ACCESS_KEY);
		IssueManager issueVNManager = mgr.getIssueManager();

		System.out.println(new Date() + " : " + messSend);
		String[] msgParam = messSend.split(BotConst.HALF_SPACE);
		String dateStr = "";
		if (msgParam.length > 1) {
			dateStr = StringCommon.formatDate(msgParam[1]);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sysdate = new Date();
		String sysdateStr = dateFormat.format(sysdate);
		if (StringCommon.isNull(dateStr)) {
			dateStr = sysdateStr;
		}
		System.out.println(new Date() + " : " + "converted Date to String: " + dateStr);

		// skip in est code issue list
		HashSet<Integer> hopeDateIssues = new HashSet<>();
		Params paramsHopeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// Dự kiến nộp chỉnh sửa/trả lời
		paramsHopeDate.add("f[]", "cf_1");
		paramsHopeDate.add("op[cf_1]", "=");
		paramsHopeDate.add("v[cf_1][]", dateStr);
		// issueManager.getIssues(null, 52, Include.values());
		ResultsWrapper<Issue> issue = issueVNManager.getIssues(paramsHopeDate);
		messReply.append("「VN」Dự kiến nộp hàng ngày: " + dateStr);
		if (issue == null || issue.getResults().isEmpty()) {
			messReply.append(BotConst.LINE_BREAK);
			messReply.append("Không có");
		} else {
			for (Issue issue2 : issue.getResults()) {
				RedmineCommon.printIssueInfo(messReply, issue2, issueVNManager, false);
				hopeDateIssues.add(issue2.getId());
			}
		}
		messReply.append(BotConst.LINE_BREAK);
		// sysdate show issue in pass
		if (sysdateStr.equals(dateStr)) {
			Params paramsHopeDatePass = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
			// Dự kiến nộp chỉnh sửa/trả lời
			// T/H sysdate: search ra cả ticket chưa close trong quá khứ
			paramsHopeDatePass.add("f[]", "cf_1");
			paramsHopeDatePass.add("op[cf_1]", "<=");
			paramsHopeDatePass.add("v[cf_1][]", dateStr);
			ResultsWrapper<Issue> issuePass = issueVNManager.getIssues(paramsHopeDatePass);
			List<Issue> issueListOutofDate = new ArrayList<>();
			messReply.append("Dự kiến nộp hàng trong quá khứ (bị trả lại hoặc update miss -> cần check lại)");
			if (issuePass != null && !issuePass.getResults().isEmpty()) {
				for (Issue issue2 : issuePass.getResults()) {
					if (hopeDateIssues.contains(issue2.getId())) {
						continue;
					}
					issueListOutofDate.add(issue2);
				}
			}
			Params paramsNGPass = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_NG);
			// Dự kiến nộp chỉnh sửa/trả lời			
			paramsNGPass.add("f[]", "cf_1"); // Kỳ vọng nộp/chỉnh sửa
			paramsNGPass.add("op[cf_1]", "!*"); // Chưa thiết định
			paramsNGPass.add("f[]", ""); // blank
			ResultsWrapper<Issue> issueNGPass = issueVNManager.getIssues(paramsNGPass);
			if (issueNGPass != null && !issueNGPass.getResults().isEmpty()) {
				for (Issue issue2 : issueNGPass.getResults()) {
					if (hopeDateIssues.contains(issue2.getId())) {
						continue;
					}
					issueListOutofDate.add(issue2);
				}
			}
			if (issueListOutofDate.isEmpty()) {
				messReply.append(BotConst.LINE_BREAK);
				messReply.append("Không có");
			} else {
				for (Issue issue2 : issueListOutofDate) {
					RedmineCommon.printIssueInfo(messReply, issue2, issueVNManager, false);
				}
			}
			messReply.append(BotConst.LINE_BREAK);
		}

		Params paramsEstCodeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// Dự kiến nộp chỉnh sửa/trả lời
		paramsEstCodeDate.add("f[]", "cf_2");
		paramsEstCodeDate.add("op[cf_2]", "=");
		paramsEstCodeDate.add("v[cf_2][]", dateStr);
		ResultsWrapper<Issue> issueCode = issueVNManager.getIssues(paramsEstCodeDate);
		messReply.append(BotConst.LINE_BREAK);
		messReply.append("Dự kiến nộp code ngày: " + dateStr);
		if (issueCode == null || issueCode.getResults().isEmpty()) {
			messReply.append(BotConst.LINE_BREAK);
			messReply.append("Không có");
		} else {
			for (Issue issue2 : issueCode.getResults()) {
				RedmineCommon.printIssueInfo(messReply, issue2, issueVNManager, false);
			}
			messReply.append(BotConst.LINE_BREAK);
		}

		// sysdate: Get JP deadline
		if (sysdateStr.equals(dateStr)) {
			RedmineManager mgrJP = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI_JP,
					RedmineConst.API_ACCESS_KEY_JP);
			IssueManager issueManagerJP = mgrJP.getIssueManager();

			getRedmineJpIssues(messReply, issueManagerJP, issueVNManager);
		}
		return messReply;
	}

	private static void getRedmineJpIssues(StringBuilder messReply, IssueManager issueManagerJP, IssueManager issueManagerVN)
			throws RedmineException {
		try {
			messReply.append(BotConst.LINE_BREAK);
			messReply.append(BotConst.LINE_BREAK);
			messReply.append("「JP」 Ticket cần lên schedule hoặc add QA ko kịp xử lý:");
			messReply.append(BotConst.LINE_BREAK);
			// List ticket
			// + Ưu tiên: Ngay lập tức/Gấp/Cao
			// + không có ngày kỳ vọng
			// URL tham khảo:
			// https://ws-redm.chip1stop.com/redmine/projects/j-t/issues?set_filter=1&f%5B%5D=status_id&op%5Bstatus_id%5D=o&v%5Bstatus_id%5D%5B%5D=1&f%5B%5D=priority_id&op%5Bpriority_id%5D=%3D&v%5Bpriority_id%5D%5B%5D=5&v%5Bpriority_id%5D%5B%5D=6&v%5Bpriority_id%5D%5B%5D=7&f%5B%5D=assigned_to_id&op%5Bassigned_to_id%5D=%3D&v%5Bassigned_to_id%5D%5B%5D=me&f%5B%5D=cf_40&op%5Bcf_40%5D=!*&v%5Bcf_40%5D%5B%5D=&f%5B%5D=&c%5B%5D=status&c%5B%5D=subject&c%5B%5D=cf_60&c%5B%5D=cf_24&c%5B%5D=priority&c%5B%5D=cf_40&c%5B%5D=cf_41&c%5B%5D=cf_25&group_by=project
			Params paramByPriority = RedmineCommon.getCommonRedmineParamsJP();
			paramByPriority.add("f[]", "priority_id");
			paramByPriority.add("op[priority_id]", "=");
			paramByPriority.add("v[priority_id][]", "7"); // Priority: Ngay lập tức
			paramByPriority.add("v[priority_id][]", "6"); // Priority: Gấp
			paramByPriority.add("v[priority_id][]", "5"); // Priority: Cao
			
			// tannh 2018/12/15 add start
			paramByPriority.add("f[]", "cf_40"); // Kỳ vọng nộp/chỉnh sửa
			paramByPriority.add("op[cf_40]", "!*"); // Chưa thiết định
			paramByPriority.add("f[]", ""); // blank

			paramByPriority.add("f[]", "cf_41"); // Dự định nộp/chỉnh sửa
			paramByPriority.add("op[cf_41]", "!*"); // Chưa thiết định
			paramByPriority.add("f[]", ""); // blank

			// List hiển thị
			List<Issue> dispList = new ArrayList<>();
			// Lần get data 1
			ResultsWrapper<Issue> issueJpPriorityList = RedmineCommon.getRedmineIssueRetry(issueManagerJP, paramByPriority);
			if (issueJpPriorityList != null && issueJpPriorityList.getResults() != null
					&& !issueJpPriorityList.getResults().isEmpty()) {
				Collections.sort(issueJpPriorityList.getResults(), new Comparator<Issue>() {
					@Override
					public int compare(Issue o1, Issue o2) {
						return o2.getPriorityId().compareTo(o1.getPriorityId());
					}
				});
				for (Issue issue : issueJpPriorityList.getResults()) {
					// tannh 2019/01/09 add start
					if (!isMaintainTeam(issueManagerVN, issue)) {
						continue;
					}
					// tannh 2019/01/09 add end
					// Kỳ vọng nộp/chỉnh sửa=blank
					if (BotConst.HALF_MINUS.equals(getCustomFieldVal(issue.getCustomFieldById(40)))
							&& BotConst.HALF_MINUS.equals(getCustomFieldVal(issue.getCustomFieldById(41)))) {
						dispList.add(issue);
					}
				}
			}
			int breakLine = dispList.size();

			Params paramByDate = RedmineCommon.getCommonRedmineParamsJP();
			// Ky vong nộp chỉnh sửa/trả lời + 7 day
			paramByDate.add("f[]", "cf_40");
			paramByDate.add("op[cf_40]", "<t+");
			paramByDate.add("v[cf_40][]", "7");

			paramByDate.add("f[]", "cf_41"); // Dự định nộp/chỉnh sửa
			paramByDate.add("op[cf_41]", "!*"); // Chưa thiết định
			paramByDate.add("f[]", ""); // blank

			ResultsWrapper<Issue> issueJpByDateList = RedmineCommon.getRedmineIssueRetry(issueManagerJP, paramByDate);
			if (issueJpByDateList != null && issueJpByDateList.getResults() != null
					&& !issueJpByDateList.getResults().isEmpty()) {
				for (Issue issue : issueJpByDateList.getResults()) {
					if (issue.getPriorityId() == null) {
						issue.setPriorityId(4); // default: normal
					}
				}
				Collections.sort(issueJpByDateList.getResults(), new Comparator<Issue>() {
					@Override
					public int compare(Issue o1, Issue o2) {
						if (o2.getPriorityId().compareTo(o1.getPriorityId()) == 0) {
							// Get cf_40:納品希望日(改修・回答)
							String o2Cf40 = getCustomFieldVal(o2.getCustomFieldById(40));
							String o1Cf40 = getCustomFieldVal(o1.getCustomFieldById(40));
							return o2Cf40.compareTo(o1Cf40);
						} else {
							return o2.getPriorityId().compareTo(o1.getPriorityId());
						}
					}
				});
				for (Issue issue : issueJpByDateList.getResults()) {
					// tannh 2019/01/09 add start
					if (!isMaintainTeam(issueManagerVN, issue)) {
						continue;
					}
					// tannh 2019/01/09 add end
					// Kỳ vọng nộp/chỉnh sửa=blank
					if (BotConst.HALF_MINUS.equals(getCustomFieldVal(issue.getCustomFieldById(41)))) {
						dispList.add(issue);
					}
				}
			}

			// Disp all data
			if (dispList.isEmpty()) {
				messReply.append("Không có");
			} else {
				for (int i = 0; i < dispList.size(); i++) {
					Issue issue = dispList.get(i);
					if (breakLine > 0 && breakLine == i) {
						messReply.append("----------" + BotConst.LINE_BREAK);
					}
					messReply.append(issue.getId());
					messReply.append("|");
					messReply.append("Ưu tiên:" + getPriorityVal(issue.getPriorityId()));
					messReply.append("|");
					messReply.append("Kỳ vọng:" + getCustomFieldVal(issue.getCustomFieldById(40)));
					messReply.append("|");
					messReply.append("Branch:" + getBranchInfo(getCustomFieldVal(issue.getCustomFieldById(44))));
					messReply.append("|");
					messReply.append("System:" + getSystemInfo(getCustomFieldVal(issue.getCustomFieldById(1))));
					messReply.append(BotConst.LINE_BREAK);
				}
			}
			// tannh 2018/12/15 add end
			// tannh 2018/12/15 del start
//			// Ky vong nộp chỉnh sửa/trả lời + 7 day
//			params.add("f[]", "cf_40");
//			params.add("op[cf_40]", "<t+");
//			params.add("v[cf_40][]", "7");
//			// issueManager.getIssues(null, 53, Include.values());
//			ResultsWrapper<Issue> issueJPList = issueManagerJP.getIssues(params);
//			// tannh 2018/12/15 mod start
////			messReply.append("[info][title]「Redmine JP」 Ticket Ngay lập tức/Gấp/Cao/Trung bình có dealine hoặc có kỳ vọng nộp hàng gần chưa xử lý (lên schedule hoặc add QA ko kịp xử lý):[/title]");
//			messReply.append(BotConst.LINE_BREAK);
//			messReply.append("「JP」 Ticket cần lên schedule hoặc add QA ko kịp xử lý:");
//			// tannh 2018/12/15 mod end
//			if (issueJPList == null || issueJPList.getResults().isEmpty()) {
//				messReply.append(BotConst.LINE_BREAK);
//				messReply.append("Không có");
//			} else {
//				messReply.append(BotConst.LINE_BREAK);
//				for (Issue issue : issueJPList.getResults()) {
//					if (issue.getPriorityId() == null) {
//						issue.setPriorityId(4); // default: normal
//					}
//				}
//				Collections.sort(issueJPList.getResults(), new Comparator<Issue>() {
//					@Override
//					public int compare(Issue o1, Issue o2) {
//						return o2.getPriorityId().compareTo(o1.getPriorityId());
//					}
//				});
//				
//				for (Issue issue : issueJPList.getResults()) {
//					messReply.append(issue.getId());
//					messReply.append(" | ");
//					messReply.append("Ưu tiên:" + getPriorityVal(issue.getPriorityId()));
//					messReply.append(" | ");
//					messReply.append("Kỳ vọng:" + getCustomFieldVal(issue.getCustomFieldById(40)));
//					messReply.append(" | ");
//					messReply.append("Branch:" + getBranchInfo(getCustomFieldVal(issue.getCustomFieldById(44))));
//					messReply.append(" | ");
//					messReply.append("System:" + getSystemInfo(getCustomFieldVal(issue.getCustomFieldById(1))));
//					messReply.append(BotConst.LINE_BREAK);
//				}
//			    int countPriorityRightAway = 0;
//			    int countPriorityFolding = 0;
//			    int countPriorityHight = 0;
//			    int countPriorityNormal = 0;
//			    int countEstLuvinaDate = 0;
//			    
//			    // Mess cho ticket ngay lập tức
//			    StringBuilder messPriorityRightAway = new StringBuilder();
//			    // Mess cho ticket Gấp
//			    StringBuilder messPriorityFolding = new StringBuilder();
//			    // Mess cho ticket Cao
//                StringBuilder messPriorityHight = new StringBuilder();
//                // Mess cho ticket Bình thường có deadline
//                StringBuilder messPriorityNormal = new StringBuilder();
//                // Mess cho ticket có ngày kỳ vọng nộp hàng gần chưa xử lý
//                StringBuilder messEstLuvinaDate = new StringBuilder();
//			    
//				for (Issue issue2 : issueJPList.getResults()) {
//				    int ticketID = issue2.getId();
//                    String branch = getCustomFieldVal(issue2.getCustomFieldById(44));
//				    String systemName = getSystemInfo(getCustomFieldVal(issue2.getCustomFieldById(1)));
//                    
//                    // List ticket by priority
//                    if (issue2.getPriorityId()  != null) {
//                        int priorityId = issue2.getPriorityId() ;
//                        
//                        if (priorityId == 7) {
//                            countPriorityRightAway++;
//                            if (countPriorityRightAway == 1) {
//                                messPriorityRightAway.append("(*)(*)(*) List ticket NGAY LẬP TỨC (*)(*)(*)");
//                            }
//                            messPriorityRightAway = printJpIssueInfo(messPriorityRightAway, ticketID, branch, systemName, "", "", "");
//                        } else if (priorityId == 6) {
//                            countPriorityFolding++;
//                            if (countPriorityFolding == 1) {
//                                messPriorityFolding.append("\n--- List ticket GẤP ---");
//                            }
//                            messPriorityFolding = printJpIssueInfo(messPriorityFolding, ticketID, branch, systemName, "", "", "");
//                        } else if (priorityId == 5) {
//                            countPriorityHight++;
//                            if (countPriorityHight == 1) {
//                                messPriorityHight.append("\n--- List ticket CAO ---");
//                            }
//                            messPriorityHight = printJpIssueInfo(messPriorityHight, ticketID, branch, systemName, "", "", "");
//                        } else if (priorityId == 4 && !StringCommon.isNull(getCustomFieldVal(issue2.getCustomFieldById(40)))) {
//                            countPriorityNormal++;
//                            if (countPriorityNormal == 1) {
//                                messPriorityNormal.append("\n--- List ticket TRUNG BÌNH đã có deadline ---");
//                            }
//                            messPriorityNormal = printJpIssueInfo(messPriorityNormal, ticketID, branch, systemName, "", getCustomFieldVal(issue2.getCustomFieldById(40)), "");
//                        }
//                    }
//					// "納品予定日(改修・回答)"
//					CustomField estLuvinaDate = issue2.getCustomFieldById(41);
//					if (estLuvinaDate == null || StringCommon.isNull(estLuvinaDate.getValue())) {
//					    countEstLuvinaDate++;
//					    if (countEstLuvinaDate == 1) {
//					        messEstLuvinaDate.append("\n--- List ticket có kỳ vọng nộp hàng gần chưa xử lý (lên schedule hoặc add QA ko kịp xử lý) ---");
//                        }
//					    messEstLuvinaDate = printJpIssueInfo(messEstLuvinaDate, ticketID, branch, systemName, "estLuvinaDate", "", getCustomFieldVal(issue2.getCustomFieldById(40)));
//					}
//				}
//				// Joint String
//                messReply.append(messPriorityRightAway).append(messPriorityFolding).append(messPriorityHight).append(messPriorityNormal).append(messEstLuvinaDate).append("[/info]");
//				messReply.append(BotConst.LINE_BREAK);
				// tannh 2018/12/15 del end
		} catch (Exception e) {
			String msg = StringCommon.trimSpace(e.getMessage()).replace("c1s_redm:Red_mine_c1s", "*****");
			throw new RedmineException(msg);
		}
	}

	// tannh 2019/01/09 mod start
//	private static ResultsWrapper<Issue> getRedmineJpIssueRetry(IssueManager issueManagerJP, Params params) throws RedmineException {
//		ResultsWrapper<Issue> result = null;
//		for (int i = 0; i < 5; i++) {
//			try {
//				result = issueManagerJP.getIssues(params);
//				break;
//			} catch (RedmineException e) {
//				System.out.println(e.getMessage());
//				if(!e.getMessage().contains("ConnectException")) {
//					throw e;
//				}
//			}
//		}
//		return result;
//	}

	private static boolean isMaintainTeam(IssueManager issueManagerVN, Issue issueJP) throws RedmineException {
		String issueId = issueJP.getId().toString();
		ResultsWrapper<Issue> result = getIssueByC1sNo(issueManagerVN, issueId);

		if (result != null && result.getResults() != null && result.getResults().size() > 0) {
			for (Issue issueVN : result.getResults()) {
				String teamName = getCustomFieldVal(issueVN.getCustomFieldById(130));
				if (BotConst.HALF_MINUS.equals(teamName) || "Team Bảo Trì".equals(teamName)
						|| "Team Bảo Trì 2".equals(teamName)) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	// tannh 2019/01/09 mod end

	public static String getBranchInfo(String branchJp) {
		branchJp = branchJp.replace("指定お願いします", "");
		branchJp = branchJp.replace("*", "");
		if (StringCommon.isNull(branchJp)) {
			branchJp = "-";
		}
		return branchJp;
	}

	/**
	 * Get priority name from int val
	 * 
	 * @param priorityId
	 * @return
	 */
	private static String getPriorityVal(Integer priorityId) {
		if (priorityId == null) {
			return "-";
		}
		String val = "-";
		// Ngay lập tức
		if (priorityId.intValue() == 7) {
			val = "NOW (*)";
		} else
		// Gấp
		if (priorityId.intValue() == 6) {
			val = "Gấp (*)";
		} else
		// Cao
		if (priorityId.intValue() == 5) {
			val = "Cao (*)";
		} else
		// Thường
		if (priorityId.intValue() == 4) {
			val = "Thường";
		} else
		// Thấp
		if (priorityId.intValue() == 3) {
			val = "Thấp";
		} else {
			// Khác
			val = "Khác";
		}
		return val;
	}

	private static String getSystemInfo(String systemJp) {
		systemJp = systemJp.replace("販売管理", "QLBH");
		systemJp = systemJp.replace("複数", "Nhiều HT");
		systemJp = systemJp.replace("ユーザWEB", "Userweb");
		systemJp = systemJp.replace("ユーザーWEB", "Userweb");
		systemJp = systemJp.replace("バッチ", "Batch");
		systemJp = systemJp.replace("物流", "TTPP");
		systemJp = systemJp.replace("その他", "Khác");
		systemJp = systemJp.replace("センター", "");
		systemJp = systemJp.replace("システム", "");
		return systemJp;
	}

	public static StringBuilder runStatusCommand(String message, StringBuilder messReply) throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI,
				RedmineConst.API_ACCESS_KEY);
		IssueManager issueManager = mgr.getIssueManager();

		String[] multiRowMsg = StringCommon.trimSpace(message).split(BotConst.LINE_BREAK);
		if (multiRowMsg == null || multiRowMsg.length <= 1) {
			messReply.append(MessageConst.DO_NOTHING);
			return messReply;
		}
		// Skip command row
		for (int i = 1; i < multiRowMsg.length; i++) {
			String issueId = multiRowMsg[i];
			if (StringCommon.isNull(issueId)) {
				continue;
			}
			ResultsWrapper<Issue> result = getIssueByC1sNo(issueManager, issueId);

			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				for (Issue issue : result.getResults()) {
					setStatusMsg(messReply, issue);
				}
			} else {
				ResultsWrapper<Issue> qaResult = getIssueByC1sQa(issueManager, issueId);
				if (qaResult != null && qaResult.getResults() != null && qaResult.getResults().size() > 0) {
					for (Issue issue : qaResult.getResults()) {
						setStatusMsg(messReply, issue);
					}
				} else {
					messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
				}
			}
		}
		return messReply;
	}

	private static StringBuilder setStatusMsg(StringBuilder messReply, Issue issue) {
		if (issue.getCustomFieldById(RedmineConst.ITM_ID_C1SNO) != null) {
			// C1S ticket
			messReply.append(getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_C1SNO)));
		} else if (issue.getCustomFieldById(RedmineConst.ITM_ID_C1SQA) != null) {
			// C1S QA
			messReply.append(getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_C1SQA)));
		}
		// tannh 2019/01/09 del start
//		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
//		messReply.append(BotConst.HALF_SPACE);
		// tannh 2019/01/09 del end
		messReply.append(issue.getTracker().getName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append(issue.getStatusName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("Assign:" + issue.getAssigneeName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);

		String coderVal = getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_CODER));
		if (!"-".equals(coderVal)) {
			messReply.append("Coder:" + getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_CODER)));
		} else {
			messReply.append("-");
		}

		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);

		String testerVal = getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_TESTER));
		if (!"-".equals(testerVal)) {
			messReply.append("Tester:" + testerVal);
		} else {
			messReply.append("-");
		}

		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);

		String estCodeVal = getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_EST_CODE_DATE));
		if (!"-".equals(estCodeVal)) {
			messReply.append("Nộp code:" + estCodeVal);
		} else {
			messReply.append("-");
		}

		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("Nộp full:" + getCustomFieldVal(issue.getCustomFieldById(RedmineConst.ITM_ID_EST_FULL_DATE)));
		messReply.append(BotConst.LINE_BREAK);
		return messReply;
	}

	public static String getCustomFieldVal(CustomField customFieldById) {
		if (customFieldById == null || StringCommon.isNull(customFieldById.getValue())) {
			return "-";
		}
		return customFieldById.getValue();
	}

	public static StringBuilder runAssigneeCommand(String message, String token, StringBuilder messReply)
			throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, token);
		IssueManager issueManager = mgr.getIssueManager();

		HashMap<String, String> issueIdLst = createIssueValueMap(message);
		if (issueIdLst.isEmpty()) {
			messReply.append(MessageConst.DO_NOTHING);
			return messReply;
		}

		for (String issueId : issueIdLst.keySet()) {
			Integer userid = RedmineCommon.getUserId(issueIdLst.get(issueId));
			if (userid == null) {
				messReply.append(MessageConst.USER_NOT_FOUND + issueId + BotConst.LINE_BREAK);
				continue;
			}
			ResultsWrapper<Issue> result = getIssueByC1sNo(issueManager, issueId);

			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				Issue issue = result.getResults().get(0);
				issue.setAssigneeId(userid); // Username
				issueManager.update(issue);
				messReply.append(MessageConst.UPDATED + issueId + BotConst.LINE_BREAK);
			} else {
				messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
			}
		}
		return messReply;
	}

	public static StringBuilder runUpdateNoteCommand(String message, String token, StringBuilder messReply)
			throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, token);
		IssueManager issueManager = mgr.getIssueManager();

		HashMap<String, String> issueIdLst = createIssueValueMap(message);
		if (issueIdLst.isEmpty()) {
			messReply.append(MessageConst.DO_NOTHING);
			return messReply;
		}
		for (String issueId : issueIdLst.keySet()) {
			ResultsWrapper<Issue> result = getIssueByC1sNo(issueManager, issueId);

			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				Issue issue = result.getResults().get(0);
				issue.getCustomFields();
				CustomField customField = issue.getCustomFieldById(RedmineConst.ITM_ID_NOTE);
				if (customField != null) {
					customField.setValue(issueIdLst.get(issueId));
					issue.addCustomField(customField);
					issueManager.update(issue);
					messReply.append(MessageConst.UPDATED + issueId + BotConst.LINE_BREAK);
				}
			} else {
				messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
			}
		}
		return messReply;
	}

	public static StringBuilder runUpdateDateCommand(String message, String token, StringBuilder messReply, int itemId)
			throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, token);
		IssueManager issueManager = mgr.getIssueManager();

		HashMap<String, String> issueIdLst = createIssueValueMap(message);
		if (issueIdLst.isEmpty()) {
			messReply.append(MessageConst.DO_NOTHING);
			return messReply;
		}
		for (String issueId : issueIdLst.keySet()) {
			String dateStr = StringCommon.formatDate(issueIdLst.get(issueId));
			if (StringCommon.isNull(dateStr)) {
				messReply.append(MessageConst.DATE_ERROR + issueId + BotConst.LINE_BREAK);
				continue;
			}

			ResultsWrapper<Issue> result = getIssueByC1sNo(issueManager, issueId);

			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				Issue issue = result.getResults().get(0);
				issue.getCustomFields();
				// Kỳ vọng nộp chỉnh sửa/trả lời
				CustomField customField = issue.getCustomFieldById(itemId);
				if (customField != null) {
					customField.setValue(dateStr);
					issue.addCustomField(customField);
					issueManager.update(issue);
					messReply.append(MessageConst.UPDATED + issueId + BotConst.LINE_BREAK);
				}
			} else {
				messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
			}
		}
		return messReply;
	}

	public static StringBuilder runSpentTimeUpdCommand(String message, String token, StringBuilder messReply)
			throws Exception {
		HashMap<String, List<String>> issueIdLst = new HashMap<>();
		String[] multiRowMsg = StringCommon.trimSpace(message).split(BotConst.LINE_BREAK);

		if (multiRowMsg.length == 1) {
			String[] rowValList = multiRowMsg[0].split(BotConst.HALF_SPACE);
			if (rowValList.length < 1) {
				messReply.append(MessageConst.DO_NOTHING);
				return messReply;
			} else {
				issueIdLst.put(rowValList[0], getSpendTimeParam(rowValList));
			}
		} else {
			for (int i = 0; i < multiRowMsg.length; i++) {
				String[] rowValList = multiRowMsg[i].split(BotConst.HALF_SPACE);
				if (rowValList.length > 1) {
					issueIdLst.put(rowValList[0], getSpendTimeParam(rowValList));
				}
			}
		}

		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, token);
		IssueManager issueManager = mgr.getIssueManager();
		TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();

		for (String issueId : issueIdLst.keySet()) {
			List<String> paramLst = issueIdLst.get(issueId);
			if (paramLst == null) {
				messReply.append(MessageConst.PARAM_ERROR + issueId + BotConst.LINE_BREAK);
				continue;
			}
			ResultsWrapper<Issue> result = getIssueByC1sNo(issueManager, issueId);
			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				Issue issue = result.getResults().get(0);

				TimeEntry timeEntry = TimeEntryFactory.create();
				if (!StringCommon.isNull(paramLst.get(2))) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dateResult = df.parse(paramLst.get(2));
					timeEntry.setSpentOn(dateResult);
				}
				timeEntry.setActivityId(new Integer(paramLst.get(1)));
				timeEntry.setActivityId(new Integer(paramLst.get(1)));
				timeEntry.setHours(new Float(paramLst.get(0)));
				timeEntry.setIssueId(issue.getId());

				timeEntryManager.createTimeEntry(timeEntry);
				messReply.append(MessageConst.UPDATED + issueId + BotConst.LINE_BREAK);
			} else {
				messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
			}
		}
		return messReply;
	}

	/**
	 * ===================================================================
	 * Private method
	 * ===================================================================
	 **/

	private static HashMap<String, String> createIssueValueMap(String message) {
		HashMap<String, String> issueIdLst = new HashMap<>();
		String[] multiRowMsg = StringCommon.trimSpace(message).split(BotConst.LINE_BREAK);

		if (multiRowMsg.length >= 1) {
			for (int i = 0; i < multiRowMsg.length; i++) {
				String[] rowValList = multiRowMsg[i].split(BotConst.HALF_SPACE);
				if (rowValList.length > 1) {
					String value = StringCommon
							.trimSpace(multiRowMsg[i].substring(multiRowMsg[i].indexOf(BotConst.HALF_SPACE) + 1));
					issueIdLst.put(rowValList[0], value);
				}
			}
		}
		return issueIdLst;
	}

	public static ResultsWrapper<Issue> getIssueByC1sNo(IssueManager issueManager, String issueId)
			throws RedmineException {
		Params searchParam = new Params();
		// Replace #
		String issueIdForSearch = issueId.replace(BotConst.NO_SHARP, "");
		// Status: default is open, cannot get closed ticket
		searchParam.add(RedmineConst.ITM_STATUS, "*");
		// Set serach parameter
		searchParam.add(RedmineConst.ITM_C1S_TICKETNO, issueIdForSearch);
		// Search C1S #
		ResultsWrapper<Issue> result = issueManager.getIssues(searchParam);
		if (result == null || result.getResults() == null || result.getResults().size() == 0) {
			searchParam = new Params();
			// Status: default is open, cannot get closed ticket
			searchParam.add(RedmineConst.ITM_STATUS, "*");
			// Replace #
			issueIdForSearch = BotConst.NO_SHARP.concat(issueIdForSearch);
			// Set serach parameter
			searchParam.add(RedmineConst.ITM_C1S_TICKETNO, issueIdForSearch);
			// Search C1S #
			result = issueManager.getIssues(searchParam);
		}
		return result;
	}

	private static ResultsWrapper<Issue> getIssueByC1sQa(IssueManager issueManager, String issueId)
			throws RedmineException {
		Params searchParam = new Params();
		// Replace #
		String issueIdForSearch = issueId.replace(BotConst.NO_SHARP, "");
		// Status: default is open, cannot get closed ticket
		searchParam.add(RedmineConst.ITM_STATUS, "*");
		// Set serach parameter
		searchParam.add(RedmineConst.ITM_C1S_QA, issueIdForSearch);
		// Search C1S #
		ResultsWrapper<Issue> result = issueManager.getIssues(searchParam);
		if (result == null || result.getResults() == null || result.getResults().size() == 0) {
			searchParam = new Params();
			// Replace #
			issueIdForSearch = BotConst.NO_SHARP.concat(issueIdForSearch);
			// Set serach parameter
			searchParam.add(RedmineConst.ITM_C1S_QA, issueIdForSearch);
			// Search C1S #
			result = issueManager.getIssues(searchParam);
		}
		return result;
	}

	private static List<String> getSpendTimeParam(String[] paramLst) {
		List<String> paramList = getParamList(paramLst);

		String param1 = paramList.get(0);
		String param2 = paramList.get(1);
		String param3 = paramList.get(2);
		if (StringCommon.isNull(param1)) {
			param1 = BotConst.DEFAULT_ST_TIME;
		} else {
			try {
				Float time = new Float(param1);
				System.out.println("SpendTime:" + time);
			} catch (Exception e) {
				param1 = BotConst.DEFAULT_ST_TIME;
				e.printStackTrace();
			}
		}
		if (StringCommon.isNull(param2)) {
			param2 = SpentTimeActivityEnum.other.getActionId();
		} else {
			if (SpentTimeActivityEnum.getActionList().containsKey(param2)) {
				param2 = SpentTimeActivityEnum.getActionList().get(param2).getActionId();
			} else {
				param2 = SpentTimeActivityEnum.other.getActionId();
			}
		}
		if (!StringCommon.isNull(param3)) {
			param3 = StringCommon.formatDate(param3);
		}
		List<String> paramResult = new ArrayList<>();
		paramResult.add(param1);
		paramResult.add(param2);
		paramResult.add(param3);
		return paramResult;
	}

	private static List<String> getParamList(String[] paramLst) {
		List<String> paramList = new ArrayList<>();
		if (paramLst.length < 1) {
			return null;
		}
		String param1 = "";
		String param2 = "";
		String param3 = "";
		for (int i = 1; i < paramLst.length; i++) {
			if (i == 1) {
				param1 = paramLst[i];
			}
			if (i == 2) {
				param2 = paramLst[i];
			}
			if (i == 3) {
				param3 = paramLst[i];
			}
		}
		paramList.add(param1);
		paramList.add(param2);
		paramList.add(param3);
		return paramList;
	}

	// phan quyen
	public static StringBuilder runEstFullDateJpCommand(String message, StringBuilder messReply) throws Exception {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI_JP,
				RedmineConst.API_ACCESS_KEY_JP);
		IssueManager issueManager = mgr.getIssueManager();

		HashMap<String, String> issueIdLst = createIssueValueMap(message);
		if (issueIdLst.isEmpty()) {
			messReply.append(MessageConst.DO_NOTHING);
			return messReply;
		}
		for (String issueId : issueIdLst.keySet()) {
			String dateStr = StringCommon.formatDate(issueIdLst.get(issueId));
			if (StringCommon.isNull(dateStr)) {
				messReply.append(MessageConst.DATE_ERROR + issueId + BotConst.LINE_BREAK);
				continue;
			}

			Issue result = mgr.getIssueManager().getIssueById(Integer.valueOf(issueId), Include.values());

			if (result != null) {
				// "納品予定日(改修・回答)"
				CustomField cusfield = result.getCustomFieldById(41);
				if (cusfield != null) {
					cusfield.setValue(dateStr);
					issueManager.update(result);
					messReply.append(MessageConst.UPDATED + issueId + BotConst.LINE_BREAK);
				}
			} else {
				messReply.append(MessageConst.ISSUE_NOT_FOUND + issueId + BotConst.LINE_BREAK);
			}
		}
		return messReply;
	}
	
	/**
	 * Get list các ticket JP bị miss so với các ticket trên redmine VN.
	 * 
	 * @param lstTicketJP list ticket JP
	 * @param lstTicketVN list ticket VN
	 * @return List<Issue> list các ticket JP bị miss
	 * @author DangNV
	 */
	private static List<Issue> getListTicketMiss(List<Issue> lstTicketJP, List<Issue> lstTicketVN) {
		List<Issue> listTicketMiss = new ArrayList<>();
		
		//Convert list issue VN to a Map
		Map<String, String> mapTicketVN = new HashMap<>();
		for (Issue ticketVN : lstTicketVN) {
			String idTicketVN = String.valueOf(ticketVN.getId());
			String idTicketJPRoot = getCustomFieldVal(ticketVN.getCustomFieldById(8)).trim().replace("#", "");
			if (!idTicketJPRoot.matches("[0-9]+")) {
                continue;
            }
			mapTicketVN.put(idTicketJPRoot, idTicketVN);
		}
		//Loop add list ticket miss
		for (Issue ticketJP : lstTicketJP) {
			String idTicketJP = String.valueOf(ticketJP.getId());
			if (!mapTicketVN.containsKey(idTicketJP)) {
				listTicketMiss.add(ticketJP);
			}
		}
		return listTicketMiss;
	}
	
	
	/**
	 * Run jpmiss command. Liệt kê ticket jp bị miss
	 * 
	 * @param lstIssueJP
	 * @param lstIssueVN
	 * @param messReply
	 * @return messReply
	 * @author DangNV
	 * @throws UnsupportedEncodingException
	 * @throws RedmineException
	 */
	public static StringBuilder runJPMissCommand(StringBuilder messReply) throws UnsupportedEncodingException, RedmineException {
		//Params JP
		Params paramsTicketJP = RedmineCommon.getCommonRedmineParamsJP();
		paramsTicketJP.add("sort","id");
		// tannh 2019/01/09 mod start
//		paramsTicketJP.add("limit","100");
		paramsTicketJP.add("limit","200");
		// tannh 2019/01/09 mod end
		//Params VN
		// tannh 2019/01/09 mod start
		Params paramsTicketVN = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// tannh 2019/01/09 mod end
		paramsTicketVN.add("limit","100");
		paramsTicketVN.add("sort","id");
		paramsTicketVN.add("f[]","status_id");
		paramsTicketVN.add("op[status_id]","o");
//		paramsTicketVN.add("f[]","created_on");
//		paramsTicketVN.add("op[created_on]","y"); // this year
		RedmineCommon.removeParam(paramsTicketVN, "v[tracker_id][]", "3"); //Bỏ filter tracker QA
		
		//List ticket JP, VN
		List<Issue> lstTicketJP = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI_JP, RedmineConst.API_ACCESS_KEY_JP,
				paramsTicketJP);
		List<Issue> lstTicketVN = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY, paramsTicketVN);

		messReply.append("Danh sách ticket miss:");
		List<Issue> listTicketMiss = getListTicketMiss(lstTicketJP, lstTicketVN);
		if (listTicketMiss == null || listTicketMiss.size() == 0) {
			messReply.append("Không có");
		} else {
			int ticketSize = listTicketMiss.size();
			boolean isAutoTransOk = false;

			for (int i = 0; i < ticketSize; i++) {
				Issue ticketJP = listTicketMiss.get(i);
				messReply.append(BotConst.LINE_BREAK);
				String idTicketJP = ticketJP.getId().toString();
				String subject = autoTrans(ticketJP.getSubject());
				if (!StringCommon.isNull(subject)) {
					isAutoTransOk = true;
				} else {
					subject = ticketJP.getSubject() + "(autotrans fail)";
				}

				String startDate = new SimpleDateFormat("yyyy-MM-dd").format(ticketJP.getCreatedOn());
				messReply.append(idTicketJP);
				messReply.append("|");
				messReply.append("Add:" + startDate);
				messReply.append("|");
				messReply.append("Ưu tiên:" + getPriorityVal(ticketJP.getPriorityId()));
				messReply.append("|");
				messReply.append("Title:" + subject);
			}
			if (isAutoTransOk) {
				messReply.append(BotConst.LINE_BREAK);
				messReply.append("  (title được dịch tự động bởi X Assistant)");
			}
		}
		return messReply;
	}

	private static String autoTrans(String subject) {
		StringBuilder googleTransCmd = new StringBuilder();
		googleTransCmd.append("translate \"" + subject + "\" to english");
		String gAssistantResult = "";
		try {
			gAssistantResult = GoogleAssistantClient.executeGoogleAssistant(googleTransCmd.toString(), false);
		} catch (Exception e) {
			System.out.println("--- Cannot translate");
			e.printStackTrace();
		}
		if (StringCommon.isNull(gAssistantResult) || MessageConst.GA_ERROR.equals(gAssistantResult)) {
			gAssistantResult = "";
		}
		return gAssistantResult;
	}
	
	/**
	 * Run vnupdatemiss command. Liệt kê ticket VN đang update lệch trạng thái so với ticket tương ứng bên JP
	 * 
	 * @param messReply
	 * @return messReply
	 * @author DangNV
	 * @throws RedmineException
	 * @throws UnsupportedEncodingException
	 */
	public static StringBuilder runVNUpdateMissCommand(StringBuilder messReply) throws RedmineException, UnsupportedEncodingException {
		// ----Params JP----
		Params paramsTicketJP = RedmineCommon.getCommonRedmineParamsJP();
		// Xóa param filter status open
		RedmineCommon.removeParam(paramsTicketJP, "op[status_id]", "o");
		RedmineCommon.removeParam(paramsTicketJP, "v[status_id][]", "40");

		paramsTicketJP.add("limit", "100");
		paramsTicketJP.add("sort", "id");
		paramsTicketJP.add("op[status_id]", "=");
		paramsTicketJP.add("v[status_id][]", "3"); // Ticket status: close

		// ----Params VN----
		// tannh 2019/01/09 mod start
		Params paramsTicketVN = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// tannh 2019/01/09 mod end
		paramsTicketVN.add("limit", "100");
		paramsTicketVN.add("sort", "id");
		paramsTicketVN.add("f[]", "status_id");
		paramsTicketVN.add("op[status_id]", "o");
		paramsTicketVN.add("f[]", "cf_3");
		paramsTicketVN.add("op[cf_3]", "=");
		paramsTicketVN.add("v[cf_3][]", "13");
		paramsTicketVN.add("v[cf_3][]", "94");
		paramsTicketVN.add("v[cf_3][]", "3");
		RedmineCommon.removeParam(paramsTicketVN, "v[tracker_id][]", "3");

		// ----List ticket JP, VN----
		List<Issue> lstTicketJP = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI_JP,
				RedmineConst.API_ACCESS_KEY_JP, paramsTicketJP);
		List<Issue> lstTicketVN = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY,
				paramsTicketVN);

		// ----list ticketJP-VN lech trang thai----
		Map<Issue, Issue> lstMissUpdate = getListTicketMapping(lstTicketVN, lstTicketJP);

		messReply.append("List ticket chưa update status: ");
		if (lstMissUpdate.isEmpty()) {
			messReply.append("Không có");
		} else {
			for (Issue ticketVN : lstMissUpdate.keySet()) {
				Issue issueJP = lstMissUpdate.get(ticketVN);
				messReply.append(BotConst.LINE_BREAK);
				messReply.append(ticketVN.getId() + BotConst.HALF_SPACE);
				messReply.append(BotConst.HALF_SPACE);
				messReply.append(ticketVN.getStatusName() + BotConst.HALF_SPACE);
				messReply.append(BotConst.HALF_MINUS + BotConst.HALF_SPACE);
				messReply.append(issueJP.getId() + BotConst.HALF_SPACE);
				messReply.append(BotConst.HALF_SPACE);
				messReply.append(issueJP.getStatusName());
			}
		}
		return messReply;
	}
	
	/**
	 * Lay ra nhung ticket redmine VN mapping với ticket redmine JP
	 * @param lstTicketVN List ticket VN
	 * @param lstTicketJP List ticket JP
	 * @return List Ticket VN-JP mapping voi nhau
	 */
	private static Map<Issue, Issue> getListTicketMapping(List<Issue> lstTicketVN, List<Issue> lstTicketJP) {
		Map<Issue, Issue> lstTicketMapping = new HashMap<>();
		//Convert list ticket B to a map with key is ticketID
		Map<String, Issue> mapTicketJP = new HashMap<>(); 
		for (Issue ticketJP : lstTicketJP) {
			String idTicketJP = String.valueOf(ticketJP.getId());
			mapTicketJP.put(idTicketJP, ticketJP);
		}
		
		for (Issue ticketVN : lstTicketVN) {
			String idTicketJPRoot = getCustomFieldVal(ticketVN.getCustomFieldById(8)).trim();
			Issue ticketJP = mapTicketJP.get(idTicketJPRoot);
			if (ticketJP != null) {
					lstTicketMapping.put(ticketVN, ticketJP);
			}
		}
		return lstTicketMapping;
	}
	
//    private static StringBuilder printJpIssueInfo(StringBuilder mess, int ticketID, String branch, String systemName,String type, String deadline, String estLuvinaDate) {
//        mess.append(BotConst.LINE_BREAK);
//        mess.append(ticketID);
//        mess.append(BotConst.HALF_SPACE);
//        mess.append("|");
//        if (!StringCommon.isNull(deadline)) {
//            mess.append(BotConst.HALF_SPACE);
//            mess.append("Deadline:" + deadline);
//            mess.append(BotConst.HALF_SPACE);
//            mess.append("|");
//        } 
//        if ("estLuvinaDate".equals(type)) {
//                mess.append(BotConst.HALF_SPACE);
//                mess.append("Kỳ vọng:" + estLuvinaDate);
//                mess.append(BotConst.HALF_SPACE);
//                mess.append("|");
//        }
//        mess.append(BotConst.HALF_SPACE);
//        mess.append("Branch:" + branch);
//        mess.append(BotConst.HALF_SPACE);
//        mess.append("|");
//        mess.append(BotConst.HALF_SPACE);
//        mess.append("Hệ thống:" + systemName);
//
//        return mess;
//    }
	
	/**
	 * 
	 * <PRE>
	 * 。
	 * </PRE>
	 * @param messReply
	 * @return
	 * @throws RedmineException 
	 * @throws ParseException 
	 */
	public static StringBuilder runManagerQA(StringBuilder messReply) throws RedmineException, ParseException {
		Params params = new Params();
		params.add("utf8", "✓");
		params.add("set_filter", "1");
		params.add("sort", "cf_25,id:desc"); // sort=cf_25,id:desc
		// status
		params.add("f[]", "status_id");
		params.add("op[status_id]", "=");
		params.add("v[status_id][]", "15"); // Da gui QA cho KH
		
		// tracker
		params.add("f[]", "tracker_id");
		params.add("op[tracker_id]", "=");
		params.add("v[tracker_id][]", "3"); // QA
		
		// team leader
		params.add("f[]", "cf_3");
		params.add("op[cf_3]", "=");
		params.add("v[cf_3][]", "13"); // Team leader QuangDN
		// Get all QA
		List<Issue> lstAllQA = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY, params);
		// list QA is due to answer
		Map<String, Issue> lstQAMap = new LinkedHashMap<>();
		Date sysDate = new Date();
		long getDaysDiff = 0;
		Date hopeDate;
		for (Issue issueQA : lstAllQA) {
			if (StringCommon.isNull(issueQA.getCustomFieldById(25).getValue())) {
				continue;
			}
			hopeDate = dateFormat.parse(issueQA.getCustomFieldById(25).getValue()); // id = 25 is field hope date
			if (sysDate.after(hopeDate)) {
				getDaysDiff = TimeUnit.MILLISECONDS.toDays(sysDate.getTime() - hopeDate.getTime());
				// list QA is due to answer
				lstQAMap.put(getDaysDiff + "_" + issueQA.getId(), issueQA);
			}
		}
		
		messReply.append("List QA đến kỳ vọng hoặc vượt quá kỳ vọng trả lời:");
		if (lstQAMap.size() == 0) {
			messReply.append("Không có");
		} else {
			Issue issueTarget = new Issue();
			for (String key : lstQAMap.keySet()) {
				issueTarget = lstQAMap.get(key);
				messReply.append(BotConst.LINE_BREAK);
				// ID QA
				messReply.append(issueTarget.getId());
				messReply.append(BotConst.HALF_SPACE);
				messReply.append("|");
				messReply.append(BotConst.HALF_SPACE);
				// ID Ticket
				messReply.append(StringCommon.trimSpace(getCustomFieldVal(issueTarget.getCustomFieldById(9))).replace("#", ""));
				messReply.append(BotConst.HALF_SPACE);
				messReply.append("|");
				messReply.append(BotConst.HALF_SPACE);
				// Assignee
				messReply.append(issueTarget.getAssigneeName());
				messReply.append(BotConst.HALF_SPACE);
				messReply.append("|");
				messReply.append(BotConst.HALF_SPACE);
				// So ngay vuot qua ky vong
				String dayDiff = key.split("_")[0];
				if ("0".equals(dayDiff)) {
					messReply.append("QA đến hạn KH phải trả lời, chú ý theo dõi (*)");
				} else {
					messReply.append("Vượt quá ");
					messReply.append(dayDiff);
					messReply.append(" ngày");
				}
				// Hope date
				messReply.append(" (Ngày kỳ vọng của phía Luvina ");
				messReply.append(issueTarget.getCustomFieldById(25).getValue());
				messReply.append(") ");
				// Chu y cua leader
				if (!StringCommon.isNull(issueTarget.getCustomFieldById(27).getValue())) {
					messReply.append(BotConst.HALF_SPACE);
					messReply.append("|");
					messReply.append(BotConst.HALF_SPACE);
					messReply.append("「");
					messReply.append(issueTarget.getCustomFieldById(27).getValue());
					messReply.append("」");
				}
			}
		}
		
		return messReply;
	}
	
	/**
	 * 
	 * <PRE>
	 * Check nội dung nộp hàng.
	 * </PRE>
	 * @param messSend
	 * @param messReply
	 * @return
	 * @throws RedmineException
	 */
	public static StringBuilder runCheckInfoReplyTicketDeliveryOfGoods(String messSend, StringBuilder messReply) throws RedmineException{
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY);
		IssueManager issueManagerVN = mgr.getIssueManager();
		
		// Check type command line
		String inputDate = "";
		String ticketID = "";
		String[] msgParam = messSend.split(BotConst.HALF_SPACE);
		if (msgParam.length > 1) {
			if (msgParam[1].contains("#")) {
				ticketID = msgParam[1];
			} else {
				inputDate = StringCommon.formatDate(msgParam[1]);
			}
		}
		
		if (StringCommon.isNull(inputDate)) {
			Date sysdate = new Date();
			inputDate = dateFormat.format(sysdate);
		}
		// Get list ticket nộp hàng VN
		List<Issue> lstTicketVN = TicketDeliveryOfGoods.getListTicketDeliveryOfGoodsVN(inputDate, ticketID, issueManagerVN);
		if (lstTicketVN.size() == 0) {
			return messReply.append("Không có ticket nào nộp hàng trong ngày" + (inputDate != "" ? ":" + inputDate : "") + ".");
		} else {
			// Get list ticket nộp hàng JP
			List<Issue> lstTicketJP = TicketDeliveryOfGoods.getListTicketDeliveryOfGoodsJP(lstTicketVN);
			// Start check
			TicketDeliveryOfGoods.checkContentTicketDeliveryOfGoods(messReply, lstTicketVN, lstTicketJP, inputDate, ticketID);
		}
		
		return messReply;
	}
	
	/**
	 * 
	 * <PRE>
	 * Thống kê năng suất member trong 1 tháng.
	 * </PRE>
	 * @param messSend
	 * @param messReply
	 * @return
	 * @throws RedmineException
	 */
	public static StringBuilder runProductivityStatisticss(String messSend, StringBuilder messReply) throws RedmineException {
		// Check type command line
		int month = Calendar.getInstance().get(Calendar.MONTH);
		String[] msgParam = messSend.split(BotConst.HALF_SPACE);
		if (msgParam.length > 1) {
			try {
				month = Integer.parseInt(msgParam[1]);
			} catch (Exception e) {
				return messReply.append("Tháng thống kê nhập không đúng !");
			}
		}
		messReply = ProductivityStatistics.doExportProductivityStaistics(messReply, month);

		return messReply;
	}
}