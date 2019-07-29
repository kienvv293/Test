/**
 * 
 */
package assistant.reminder.review;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import common.BotProperty;
import common.RedmineCommon;
import common.StringCommon;
import common.mail.MailCommon;
import common.mail.bean.MailBean;
import common.user.UserInfoBean;
import common.user.UserManager;
import constant.BotConst;
import constant.RedmineConst;

/**
 * @author nguyenhuytan
 *
 */
public class PersonalIssueReminderExecute implements Runnable {

	public PersonalIssueReminderExecute() {
	}

	@Override
	public void run() {
		System.out.println(new Date() + " : review reminder");

		Date sysDate = new Date();
		System.out.println(sysDate + " : qa reminder");

		try {
			// QA có kỳ vọng <= SYSDATE
			List<Issue> qaAlertIssues = getQaIssues();
			HashSet<String> userList = new HashSet<>();

			HashMap<String, List<Issue>> alertQALst = new HashMap<>();
			for (Issue issue : qaAlertIssues) {
				UserInfoBean userInfoBean = UserManager.getUserInfoByName(issue.getAssigneeName());
				if (!userInfoBean.getIsOk()) {
					continue;
				}
				List<Issue> issueLst = alertQALst.get(userInfoBean.getUserName());
				if (issueLst == null) {
					issueLst = new ArrayList<>();
				}
				issueLst.add(issue);
				alertQALst.put(userInfoBean.getUserName(), issueLst);
				userList.add(userInfoBean.getUserName());
			}

			List<Issue> issues = getReviewIssues();
			HashMap<String, List<Issue>> alertReviewLst = new HashMap<>();
			for (Issue issue : issues) {
				UserInfoBean userInfoBean = UserManager.getUserInfoByName(issue.getAssigneeName());
				if (!userInfoBean.getIsOk()) {
					continue;
				}
				List<Issue> issueLst = alertReviewLst.get(userInfoBean.getUserName());
				if (issueLst == null) {
					issueLst = new ArrayList<>();
				}
				issueLst.add(issue);
				alertReviewLst.put(userInfoBean.getUserName(), issueLst);
				userList.add(userInfoBean.getUserName());
			}

			if (userList.isEmpty()) {
				return;
			}

			StringBuilder alertLeader = new StringBuilder();
			// Alert
			for (String userName : userList) {
				UserInfoBean userInfoBean = UserManager.getUserInfoByName(userName);
				if ((alertReviewLst.get(userName) == null || alertReviewLst.get(userName).isEmpty())
						&& (alertQALst.get(userName) == null || alertQALst.get(userName).isEmpty())) {
					continue;
				}
				// Alert by mail
				if (!StringCommon.isNull(userInfoBean.getEmail())) {
					alertLeader.append(BotConst.LINE_BREAK_MAIL);
					alertLeader.append("-------------------------------------");
					alertLeader.append(BotConst.LINE_BREAK_MAIL);
					// send mail
					String mailBody = alertByMail(userInfoBean, alertReviewLst.get(userName), alertQALst.get(userName));
					if (userInfoBean.getMustcc() && !StringCommon.isNull(mailBody)) {
						alertLeader.append(mailBody);
					}
				}
			}
			if (!StringCommon.isNull(alertLeader.toString())) {
				alertLeader(alertLeader.toString());
			}
		} catch (Exception e) {
			System.out.println(PersonalIssueReminderExecute.class.getName());
			e.printStackTrace();
		}
	}

	private void alertLeader(String mailBody) {
		// Send message
		MailBean mailBean = new MailBean();
		mailBean.setToAddress(BotProperty.getProperty(BotProperty.MAIL_LEADER));
		mailBean.setSubject("[ODC-C1S] X-Assistant: Tổng hợp issue của members");
		mailBean.setBody("Thông báo tổng hợp issue của members cho leader" + BotConst.LINE_BREAK_MAIL + mailBody);
		MailCommon.sendMail(mailBean);
	}

	private List<Issue> getReviewIssues() {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI,
				RedmineConst.API_ACCESS_KEY);
		IssueManager issueVNManager = mgr.getIssueManager();
		// URL
		// http://192.168.0.136/redmine/projects/c1s-jp/issues?utf8=✓&set_filter=1&f%5B%5D=tracker_id&op%5Btracker_id%5D=%3D&v%5Btracker_id%5D%5B%5D=10&f%5B%5D=status_id&op%5Bstatus_id%5D=o&f%5B%5D=&c%5B%5D=cf_9&c%5B%5D=author&c%5B%5D=status&c%5B%5D=priority&c%5B%5D=subject&c%5B%5D=cf_27&c%5B%5D=assigned_to&c%5B%5D=cf_25&c%5B%5D=updated_on&group_by=status
		Params params = new Params();
		params.add("utf8", "✓");
		params.add("set_filter", "1");

		// Status
		params.add("f[]", "status_id");
		params.add("op[status_id]", "o");

		// Tracker
		params.add("f[]", "tracker_id");
		params.add("op[tracker_id]", "=");
		params.add("v[tracker_id][]", "10");

		// Date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sysdate = new Date();
		String sysdateStr = dateFormat.format(sysdate);
		params.add("f[]", "cf_25");
		params.add("op[cf_25]", "<=");
		params.add("v[cf_25][]", sysdateStr);

		params.add("offset", "0");
		params.add("limit", "100");

		List<Issue> issues = new ArrayList<>();
		try {
			ResultsWrapper<Issue> issueRs = issueVNManager.getIssues(params);
			if (issueRs != null && issueRs.getResults() != null) {
				issues = issueRs.getResults();
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}
		return issues;
	}

	private void createMsg(List<Issue> issueList, StringBuilder messReply) {
		for (Issue issue : issueList) {
			messReply.append(issue.getSubject());
			messReply.append("|");
			messReply.append(issue.getStatusName());
			messReply.append("|Kỳ vọng xử lý:");
			// Kỳ vọng nộp chỉnh sửa/trả lời
			messReply.append(RedmineCommon.getCustomFieldVal(issue.getCustomFieldById(25)));
			messReply.append("| ");
			messReply.append("http://192.168.0.136/redmine/issues/" + issue.getId());
			messReply.append(BotConst.LINE_BREAK_MAIL);
		}
	}

	private String alertByMail(UserInfoBean userInfoBean, List<Issue> reviewList, List<Issue> qaList) {
		String toAddress = userInfoBean.getEmail();
		String userName = "";
		if (reviewList != null && !reviewList.isEmpty()) {
			userName = reviewList.get(0).getAssigneeName();
		} else if (qaList != null && !qaList.isEmpty()) {
			userName = qaList.get(0).getAssigneeName();
		}

		// compose the message
		StringBuilder mailBody = new StringBuilder();
		String gender = "";
		if (userInfoBean.getIsMale()) {
			gender = " anh ";
		} else {
			gender = " chị ";
		}
		mailBody.append("Hi" + gender + userName + BotConst.LINE_BREAK_MAIL);

		if (reviewList != null && !reviewList.isEmpty()) {
			mailBody.append(BotConst.LINE_BREAK_MAIL + "Biên bản review cần check:" + BotConst.LINE_BREAK_MAIL);
			createMsg(reviewList, mailBody);
		}

		if (qaList != null && !qaList.isEmpty()) {
			mailBody.append(BotConst.LINE_BREAK_MAIL + "QA cần check:" + BotConst.LINE_BREAK_MAIL);
			createMsg(qaList, mailBody);
		}

		// Send message
		MailBean mailBean = new MailBean();
		mailBean.setMustCc(userInfoBean.getMustcc());
		mailBean.setToAddress(toAddress);
		mailBean.setSubject("[ODC-C1S] X-Assistant: Nhắc nhở " + userName + " check issue");
		mailBean.setBody(mailBody.toString());
		MailCommon.sendMail(mailBean);

		return mailBody.toString();
	}

	private List<Issue> getQaIssues() {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI,
				RedmineConst.API_ACCESS_KEY);
		IssueManager issueVNManager = mgr.getIssueManager();
		// URL
		// http://192.168.0.136/redmine/issues?utf8=✓&set_filter=1&f%5B%5D=status_id&op%5Bstatus_id%5D=o&f%5B%5D=tracker_id&op%5Btracker_id%5D=%3D&v%5Btracker_id%5D%5B%5D=3&f%5B%5D=cf_25&op%5Bcf_25%5D=<%3D&v%5Bcf_25%5D%5B%5D=2019-01-06&f%5B%5D=&c%5B%5D=tracker&c%5B%5D=cf_8&c%5B%5D=status&c%5B%5D=priority&c%5B%5D=subject&c%5B%5D=author&c%5B%5D=cf_25&c%5B%5D=cf_1&c%5B%5D=cf_2&group_by=priority
		Params params = new Params();
		params.add("utf8", "✓");
		params.add("set_filter", "1");

		// Status
		params.add("f[]", "status_id");
		params.add("op[status_id]", "o");

		// Tracker
		params.add("f[]", "tracker_id");
		params.add("op[tracker_id]", "=");
		params.add("v[tracker_id][]", "3");

		// Date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sysdate = new Date();
		String sysdateStr = dateFormat.format(sysdate);
		params.add("f[]", "cf_25");
		params.add("op[cf_25]", "<=");
		params.add("v[cf_25][]", sysdateStr);

		params.add("offset", "0");
		params.add("limit", "200");

		List<Issue> issues = new ArrayList<>();
		try {
			ResultsWrapper<Issue> issueRs = issueVNManager.getIssues(params);
			if (issueRs != null && issueRs.getResults() != null) {
				issues = issueRs.getResults();
			}
		} catch (RedmineException e) {
			e.printStackTrace();
		}
		return issues;
	}

}
