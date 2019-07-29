/**
 * 
 */
package assistant.reminder.todayTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import assistant.CommandExecute;
import common.BotProperty;
import common.RedmineCommon;
import common.StringCommon;
import common.mail.MailCommon;
import common.mail.bean.MailBean;
import constant.BotConst;
import constant.RedmineConst;

/**
 * @author nguyenhuytan
 *
 */
public class TodayTaskReminderExecute implements Runnable {

	private RedmineManager mgr;
	private IssueManager issueVNManager;

	public TodayTaskReminderExecute() {
		mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY);
		issueVNManager = mgr.getIssueManager();
	}

	@Override
	public void run() {
		Date sysDate = new Date();
		System.out.println(sysDate + " : issues reminder");

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String sysdateStr = dateFormat.format(sysDate);

			// Nộp hàng hôm nay
			List<Issue> delifullIssues = getIssuesByDate(sysdateStr);
			// Nộp code hôm nay
			List<Issue> delicodeIssues = getCodeDeliIssuesByDate(sysdateStr);

			String nextDate = StringCommon.getNextDate(sysDate);
			// Nộp hàng hôm sau
			List<Issue> nextDateDelifullIssues = getIssuesByDate(nextDate);
			// Nộp code hôm sau
			List<Issue> nextDateDelicodeIssues = getCodeDeliIssuesByDate(nextDate);

			HashSet<Integer> hideIssueList = new HashSet<>();
			if (!delifullIssues.isEmpty()) {
				for (Issue issue : delifullIssues) {
					hideIssueList.add(issue.getId());
				}
			}
			if (!delicodeIssues.isEmpty()) {
				for (Issue issue : delicodeIssues) {
					hideIssueList.add(issue.getId());
				}
			}
			List<Issue> outOfDateIssues = getIssuesOutOfDate(sysdateStr, hideIssueList);

			if (delifullIssues.isEmpty() && delicodeIssues.isEmpty() && nextDateDelifullIssues.isEmpty()
					&& nextDateDelicodeIssues.isEmpty() && outOfDateIssues.isEmpty()) {
				return;
			}

			StringBuilder mailBody = new StringBuilder();
			mailBody.append("Hi all," + BotConst.LINE_BREAK_MAIL);
			mailBody.append("Mọi người check list ticket dự định nộp hàng nhé." + BotConst.LINE_BREAK_MAIL);

			HashSet<Integer> todayIssues = new HashSet<>();
			if (!delifullIssues.isEmpty()) {
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("★ Nộp hàng hôm nay (" + sysdateStr + "):");
				for (Issue issue : delifullIssues) {
					try {
						RedmineCommon.printIssueInfo(mailBody, issue, issueVNManager, true);
					} catch (RedmineException e) {
						e.printStackTrace();
					}
					todayIssues.add(issue.getId());
				}
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("----------------------");
			}
			if (!delicodeIssues.isEmpty()) {
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("★ Nộp code hôm nay (" + sysdateStr + "):");
				for (Issue issue : delicodeIssues) {
					if (todayIssues.contains(issue.getId())) {
						continue;
					}
					try {
						RedmineCommon.printIssueInfo(mailBody, issue, issueVNManager, true);
					} catch (RedmineException e) {
						e.printStackTrace();
					}
				}
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("----------------------");
			}

			HashSet<Integer> nextDateIssues = new HashSet<>();
			if (!nextDateDelifullIssues.isEmpty()) {
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("★ Nộp hàng hôm sau (" + nextDate + "):");
				for (Issue issue : nextDateDelifullIssues) {
					try {
						RedmineCommon.printIssueInfo(mailBody, issue, issueVNManager, true);
					} catch (RedmineException e) {
						e.printStackTrace();
					}
					nextDateIssues.add(issue.getId());
				}
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("----------------------");
			}
			if (!nextDateDelicodeIssues.isEmpty()) {
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("★ Nộp code hôm sau (" + nextDate + "):");
				for (Issue issue : nextDateDelicodeIssues) {
					if (nextDateIssues.contains(issue.getId())) {
						continue;
					}
					try {
						RedmineCommon.printIssueInfo(mailBody, issue, issueVNManager, true);
					} catch (RedmineException e) {
						e.printStackTrace();
					}
				}
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("----------------------");
			}
			if (!outOfDateIssues.isEmpty()) {
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("★ Bị trả lại hoặc deadline trong quá khứ:");
				for (Issue issue : outOfDateIssues) {
					try {
						RedmineCommon.printIssueInfo(mailBody, issue, issueVNManager, true);
					} catch (RedmineException e) {
						e.printStackTrace();
					}
				}
				mailBody.append(BotConst.LINE_BREAK_MAIL);
				mailBody.append("----------------------");
			}
			mailBody.append(BotConst.LINE_BREAK_MAIL + "★");
			StringBuilder jpMissCmd = CommandExecute.runJPMissCommand(new StringBuilder());
			mailBody.append(jpMissCmd.toString());
			mailBody.append(BotConst.LINE_BREAK_MAIL);

			// compose the message
			String toAddress = BotProperty.getProperty(BotProperty.MAIL_DAILY_TO);
			// Send message
			MailBean mailBean = new MailBean();
			mailBean.setToAddress(toAddress);
			mailBean.setSubject("[ODC-C1S] X-Assistant: Nhắc nhở ticket nộp hàng " + sysdateStr);
			mailBean.setBody(refactorMessage(mailBody));
			MailCommon.sendMail(mailBean);
		} catch (Exception e) {
			System.out.println(TodayTaskReminderExecute.class.getName());
			e.printStackTrace();
		}
	}

	private String refactorMessage(StringBuilder mailBody) {
		String mailBodyText = mailBody.toString();
		mailBodyText = mailBodyText.replace("Đã hoàn thành điều tra", "Đã hoàn thành");
		mailBodyText = mailBodyText.replace("Team Bảo Trì", "Bảo Trì");
		mailBodyText = mailBodyText.replace("Team Kỹ Thuật", "Kỹ Thuật");
		mailBodyText = mailBodyText.replace("Bổ sung request", "Bổ sung");
		mailBodyText = mailBodyText.replace("Request điều tra", "Điều tra");
		return mailBodyText;
	}

	private List<Issue> getCodeDeliIssuesByDate(String sysdateStr) {
		Params paramsEstCodeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// Dự kiến nộp chỉnh sửa/trả lời
		paramsEstCodeDate.add("f[]", "cf_2");
		paramsEstCodeDate.add("op[cf_2]", "=");
		paramsEstCodeDate.add("v[cf_2][]", sysdateStr);

		List<Issue> issues = new ArrayList<>();
		try {
			ResultsWrapper<Issue> issueCode = issueVNManager.getIssues(paramsEstCodeDate);
			if (issueCode != null && issueCode.getResults() != null) {
				issues = issueCode.getResults();
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}
		return issues;
	}

	private List<Issue> getIssuesByDate(String processDate) {
		Params paramsHopeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// Dự kiến nộp chỉnh sửa/trả lời
		paramsHopeDate.add("f[]", "cf_1");
		paramsHopeDate.add("op[cf_1]", "=");
		paramsHopeDate.add("v[cf_1][]", processDate);

		paramsHopeDate.add("offset", "0");
		paramsHopeDate.add("limit", "100");

		List<Issue> issues = new ArrayList<>();
		try {
			ResultsWrapper<Issue> issueRs = issueVNManager.getIssues(paramsHopeDate);
			if (issueRs != null && issueRs.getResults() != null) {
				issues = issueRs.getResults();
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}
		return issues;
	}
	
	private List<Issue> getIssuesOutOfDate(String processDate, HashSet<Integer> hideIssueList) {
		Params paramsHopeDatePass = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_OPEN);
		// Dự kiến nộp chỉnh sửa/trả lời
		// T/H sysdate: search ra cả ticket chưa close trong quá khứ
		paramsHopeDatePass.add("f[]", "cf_1");
		paramsHopeDatePass.add("op[cf_1]", "<=");
		paramsHopeDatePass.add("v[cf_1][]", processDate);
		ResultsWrapper<Issue> issuePass;
		List<Issue> issueListOutofDate = new ArrayList<>();
		try {
			issuePass = issueVNManager.getIssues(paramsHopeDatePass);
			if (issuePass != null && !issuePass.getResults().isEmpty()) {
				for (Issue issue2 : issuePass.getResults()) {
					if (hideIssueList.contains(issue2.getId())) {
						continue;
					}
					issueListOutofDate.add(issue2);
				}
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}

		Params paramsNGPass = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_NG);
		// Dự kiến nộp chỉnh sửa/trả lời
		paramsNGPass.add("f[]", "cf_1"); // Kỳ vọng nộp/chỉnh sửa
		paramsNGPass.add("op[cf_1]", "!*"); // Chưa thiết định
		paramsNGPass.add("f[]", ""); // blank
		ResultsWrapper<Issue> issueNGPass;
		try {
			issueNGPass = issueVNManager.getIssues(paramsNGPass);
			if (issueNGPass != null && !issueNGPass.getResults().isEmpty()) {
				for (Issue issue2 : issueNGPass.getResults()) {
					if (hideIssueList.contains(issue2.getId())) {
						continue;
					}
					issueListOutofDate.add(issue2);
				}
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}
		return issueListOutofDate;
	}

}
