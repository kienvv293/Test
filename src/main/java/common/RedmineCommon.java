/**
 *
 */
package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import constant.BotConst;
import constant.RedmineConst;

/**
 * @author nguyenhuytan
 *
 */
public class RedmineCommon {

	private static HashMap<String, Integer> userNameMap = null;
	// tannh 2019/01/09 mod start
	private static int reviewTrackerId = 10;
	private static int doneStatusId = 16;

	public static Params getCommonRedmineParams(String status) {
		// tannh 2019/01/09 mod end
		Params params = new Params();
		params.add("utf8", "✓");
		params.add("set_filter", "1");

		// tannh 2019/01/09 mod start
		// Status
		if (RedmineConst.REDMINE_STATUS_OPEN.equals(status)) {
			params.add("f[]", "status_id");
			params.add("op[status_id]", "o");
		} else if (RedmineConst.REDMINE_STATUS_NG.equals(status)) {
			params.add("f[]", "status_id");
			params.add("op[status_id]", "=");
			params.add("v[status_id][]", "14");
		}
		// tannh 2019/01/09 mod end
		// tungpx 2019/01/23 add start
		else if (RedmineConst.REDMINE_STATUS_ANY.equals(status)) {
			params.add("f[]", "status_id");
			params.add("op[status_id]", "*");
		}
		// tungpx 2019/01/23 add end

		// Tracker
		params.add("f[]", "tracker_id");
		params.add("op[tracker_id]", "=");
		params.add("v[tracker_id][]", "7");
		params.add("v[tracker_id][]", "8");
		params.add("v[tracker_id][]", "11");
		params.add("v[tracker_id][]", "18");
		params.add("v[tracker_id][]", "19");
		params.add("v[tracker_id][]", "1");
		// tannh 2019/01/09 del start
//		params.add("v[tracker_id][]", "3");
		// tannh 2019/01/09 del end
		params.add("v[tracker_id][]", "26"); // Tiket thiết kế
		// tannh 2019/01/09 add start
		params.add("v[tracker_id][]", "33"); // Request merge code
		// tannh 2019/01/09 add end
		// tannh 2019/01/31 add start
		params.add("v[tracker_id][]", "34"); // QA từ khách hàng
		// tannh 2019/01/31 add end
		return params;
	}

	// tannh 2019/01/09 add start
	public static String getCustomFieldVal(CustomField customFieldById) {
		if (customFieldById == null || StringCommon.isNull(customFieldById.getValue())) {
			return "-";
		}
		return customFieldById.getValue();
	}
	// tannh 2019/01/09 add end

	public static Integer getUserId(String userName) {
		if (userNameMap == null) {
			initUserMap();
		}
		String userNameProcess = StringCommon.trimSpace(userName).replace(BotConst.HALF_SPACE, BotConst.BLANK)
				.toLowerCase();
		return userNameMap.get(userNameProcess);
	}

	private static void initUserMap() {
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI,
				RedmineConst.API_ACCESS_KEY);
		UserManager userManager = mgr.getUserManager();
		try {
			userNameMap = new HashMap<>();
			List<User> users = userManager.getUsers();
			if (users != null && users.size() > 0) {
				for (User user : users) {
					userNameMap.put(user.getFullName().replace(BotConst.HALF_SPACE, BotConst.BLANK).toLowerCase(),
							user.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Params getCommonRedmineParamsJP() {
		Params params = new Params();
		// Status
		params.add("f[]", "status_id"); // Status
		params.add("op[status_id]", "o"); // Open
		params.add("v[status_id][]", "40");

		// Tracker
		params.add("f[]", "tracker_id");
		params.add("op[tracker_id]", "=");
		params.add("v[tracker_id][]", "7");
		params.add("v[tracker_id][]", "16");
		params.add("v[tracker_id][]", "10");
		// tannh 2019/01/15 add start
		params.add("v[tracker_id][]", "23"); // Request merge code
		// tannh 2019/01/15 add end
		// tannh 2018/12/15 del start
//		 params.add("v[tracker_id][]", "14"); // QA
		// tannh 2018/12/15 del end

		// Assign
		params.add("f[]", "assigned_to_id");
		params.add("op[assigned_to_id]", "=");
		params.add("v[assigned_to_id][]", "me");

		return params;
	}
	
	/**
	 * Lấy list ticket redmine. Get data theo parameter.
	 * 
	 * @param redmineURI
	 *            uri redmine
	 * @param apiAccessKey
	 *            token key để sử dụng api của redmine
	 * @params parameter để Get data
	 * 
	 *         <pre>
	 *         Params params = new Params().add("set_filter", "1").add("f[]", "summary").add("op[summary]", "~")
	 *         		.add("v[summary]", "another").add("f[]", "description").add("op[description]", "~")
	 *         		.add("v[description][]", "abc");
	 *         </pre>
	 * 
	 * @return List ticket
	 * @throws RedmineException
	 * @author DangNV
	 */
	public static List<Issue> getListIssues(String redmineURI, String apiAccessKey, Params params)
			throws RedmineException {
		RedmineManager redmineMng = RedmineManagerFactory.createWithApiKey(redmineURI, apiAccessKey);
		IssueManager issueMng = redmineMng.getIssueManager();
		List<Issue> lstIssue = new ArrayList<>();
		Integer totalObjectsFound;
		Integer offset = 0;
		for (BasicNameValuePair pair : params.getList()) {
			//Nếu param có offset và có value hợp lệ
			if ("offset".equals(pair.getName()) && !StringCommon.isNull(pair.getValue())) {
				offset = IntegerCommon.parseInt(pair.getValue(), 0);
			}
		}
		do {
			params.add("offset", String.valueOf(offset));
			// tannh 2018/12/15 mod start
//			ResultsWrapper<Issue> resultWrap = issueMng.getIssues(params);
			ResultsWrapper<Issue> resultWrap = getRedmineIssueRetry(issueMng, params);
			// tannh 2018/12/15 mod end
			lstIssue.addAll(resultWrap.getResults());
			totalObjectsFound = resultWrap.getTotalFoundOnServer();
			if (totalObjectsFound == null || !resultWrap.hasSomeResults()) {
				break;
			}
			offset += resultWrap.getResultsNumber();
		} while (offset < totalObjectsFound);
		return lstIssue;
	}
	
	// tannh 2019/01/09 add start
	/**
	 * Get Redmine JP (Retry)
	 * @param issueManagerJP
	 * @param params
	 * @return
	 * @throws RedmineException
	 */
	public static ResultsWrapper<Issue> getRedmineIssueRetry(IssueManager issueManagerJP, Params params) throws RedmineException {
		ResultsWrapper<Issue> result = null;
		for (int i = 0; i < 5; i++) {
			try {
				result = issueManagerJP.getIssues(params);
				break;
			} catch (RedmineException e) {
				System.out.println(e.getMessage());
				if(!e.getMessage().contains("ConnectException")) {
					throw e;
				}
			}
		}
		return result;
	}
	// tannh 2019/01/09 add end
	/**
	 * Remove parameter on uri query by name and value of param
	 * @param params Object contains pairs of param
	 * @param name name param
	 * @param value value param
	 */
	public static void removeParam(Params params, String name, String value) {
		BasicNameValuePair pair = new BasicNameValuePair(name, value);
		params.getList().remove(pair);
	}
	// tannh 2019/01/09 add start
	public static void printIssueInfo(StringBuilder messReply, Issue issue2, IssueManager issueManager, boolean isMail)
			throws RedmineException {
		messReply.append(BotConst.LINE_BREAK_MAIL);
		messReply.append(StringCommon.trimSpace(getCustomFieldVal(issue2.getCustomFieldById(8))).replace("#", ""));
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append(getCustomFieldVal(issue2.getCustomFieldById(130)));
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append(issue2.getTracker().getName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append(issue2.getStatusName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append(issue2.getAssigneeName());
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("Branch:" + getCustomFieldVal(issue2.getCustomFieldById(153)));
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("|");
		messReply.append(BotConst.HALF_SPACE);
		messReply.append("http://192.168.0.136/redmine/issues/" + issue2.getId());

		StringBuilder reviewStr = new StringBuilder();
		Issue issueWithChild = issueManager.getIssueById(issue2.getId(), Include.children);
		Collection<Issue> childIssues = issueWithChild.getChildren();
		if (childIssues != null && childIssues.size() > 0) {
			if (isMail) {
				printReviewForMail(issueManager, reviewStr, issue2, childIssues);
			} else {
				printReviewForChat(issueManager, reviewStr, childIssues);
			}
		}
		messReply.append(reviewStr);
	}

	private static void printReviewForChat(IssueManager issueManager, StringBuilder reviewStr,
			Collection<Issue> childIssues) throws RedmineException {

		boolean reviewCode = false;
		boolean reviewTest = false;
		boolean reviewOther = false;

		for (Issue issue : childIssues) {
			issue = issueManager.getIssueById(issue.getId());
			if (issue == null || issue.getTracker() == null || issue.getTracker().getId() == null
					|| issue.getStatusId() == null) {
				continue;
			}
			// Tracker != Biên bản review
			if (issue.getTracker().getId().intValue() != reviewTrackerId) {
				continue;
			}
			// Status = Test xong (bug nội bộ)
			if (issue.getStatusId() == doneStatusId) {
				continue;
			}
			String reviewType = getCustomFieldVal(issue.getCustomFieldById(44)).toLowerCase();
			if (reviewType.contains("code")) {
				reviewCode = true;
			}
			if (reviewType.contains("test")) {
				reviewTest = true;
			}
			if (reviewType.contains("hiểu")) {
				reviewOther = true;
			}
		}

		if (reviewCode || reviewTest || reviewOther) {
			reviewStr.append(BotConst.HALF_SPACE + "|");
			reviewStr.append(BotConst.HALF_SPACE + "Cần review ");
			if (reviewCode) {
				reviewStr.append("code");
			}
			if (reviewTest) {
				if (reviewCode) {
					reviewStr.append(",");
				}
				reviewStr.append("test");
			}
			if (reviewOther) {
				if (reviewCode || reviewTest) {
					reviewStr.append(",");
				}
				reviewStr.append("ý hiểu");
			}
		}
	}

	private static void printReviewForMail(IssueManager issueManager, StringBuilder reviewStr, Issue parentIssue,
			Collection<Issue> childIssues) throws RedmineException {

		Issue reviewCode = null;
		Issue reviewTest = null;
		Issue reviewOther = null;

		boolean hasReviewCode = false;
		boolean hasReviewTest = false;
		boolean hasReviewOther = false;
		boolean checkReviewFlg = false;
		// Change require
		if (parentIssue.getTracker().getId().intValue() == 7) {
			checkReviewFlg = true;
		}

		for (Issue issue : childIssues) {
			issue = issueManager.getIssueById(issue.getId());
			if (issue == null || issue.getTracker() == null || issue.getTracker().getId() == null
					|| issue.getStatusId() == null) {
				continue;
			}
			// Tracker != Biên bản review
			if (issue.getTracker().getId().intValue() != reviewTrackerId) {
				continue;
			}
			String reviewType = getCustomFieldVal(issue.getCustomFieldById(44)).toLowerCase();

			if (reviewType.contains("code")) {
				hasReviewCode = true;
			}
			if (reviewType.contains("test")) {
				hasReviewTest = true;
			}
			if (reviewType.contains("hiểu")) {
				hasReviewOther = true;
			}
			// Status = Test xong (bug nội bộ)
			if (issue.getStatusId() == doneStatusId) {
				continue;
			}
			if (reviewType.contains("code")) {
				reviewCode = issue;
			}
			if (reviewType.contains("test")) {
				reviewTest = issue;
			}
			if (reviewType.contains("hiểu")) {
				reviewOther = issue;
			}
		}

		if (reviewCode != null) {
			reviewStr.append(BotConst.HALF_SPACE + "|");
			// Status: Đã review - chờ sửa lại
			if (reviewCode.getStatusId().intValue() == 17) {
				reviewStr.append("ReviewCode:Đã review(chờ sửa)");
			} else
			// Status: Đã sửa - chờ review lại
			if (reviewCode.getStatusId().intValue() == 18) {
				reviewStr.append("ReviewCode:Đã sửa(chờ review)");
			} else
			// Status: Code xong
			if (reviewCode.getStatusId().intValue() == 3) {
				reviewStr.append("ReviewCode:Chờ test");
			} else {
				reviewStr.append("Cần review code");
			}
		}
		if (reviewTest != null) {
			reviewStr.append(BotConst.HALF_SPACE + "|");
			// Status: Đã review - chờ sửa lại
			if (reviewTest.getStatusId().intValue() == 17) {
				reviewStr.append("ReviewTest:Đã review(chờ sửa)");
			} else
			// Status: Đã sửa - chờ review lại
			if (reviewTest.getStatusId().intValue() == 18) {
				reviewStr.append("ReviewTest:Đã sửa(chờ review)");
			} else {
				reviewStr.append("Cần review test");
			}
		}
		if (reviewOther != null) {
			reviewStr.append(BotConst.HALF_SPACE + "|");
			// Status: Đã review - chờ sửa lại
			if (reviewOther.getStatusId().intValue() == 17) {
				reviewStr.append("Ý hiểu:Đã review(chờ sửa)");
			} else
			// Status: Đã sửa - chờ review lại
			if (reviewOther.getStatusId().intValue() == 18) {
				reviewStr.append("Ý hiểu:Đã sửa(chờ review)");
			} else
			// Status: Code xong
			if (reviewOther.getStatusId().intValue() == 3) {
				reviewStr.append("Ý hiểu:Chờ xử lý code");
			} else {
				reviewStr.append("Cần review ý hiểu");
			}
		}
		if (checkReviewFlg) {
			if (!hasReviewCode) {
				reviewStr.append(BotConst.HALF_SPACE + "|");
				reviewStr.append("ReviewCode:Chưa add");
			}
			if (!hasReviewTest) {
				reviewStr.append(BotConst.HALF_SPACE + "|");
				reviewStr.append("ReviewTest:Chưa add");
			}
			if (!hasReviewOther) {
				reviewStr.append(BotConst.HALF_SPACE + "|");
				reviewStr.append("Ý hiểu:Chưa add");
			}
		}
	}
	// tannh 2019/01/09 add end
}