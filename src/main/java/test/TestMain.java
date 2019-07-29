/**
 *
 */
package test;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

import assistant.CommandExecute;
import common.BotProperty;
import common.StringCommon;
import common.UserCommon;
import constant.ActionEnum;
import constant.BotConst;
import constant.MessageConst;

/**
 * @author nguyenhuytan
 *
 */
public class TestMain {
	public static void main(String[] args) throws RedmineException, UnsupportedEncodingException {
		// System.out.println(UserCommon.checkUserRole("xxx", "yyy"));
		// System.out.println(UserCommon.checkUserRole("3185755", "deadline"));
		// System.out.println(UserCommon.checkUserRole("3185755", "update"));
		// String xxx = " deadline xxx yyy".trim();
		// System.out.println(xxx.substring(0, xxx.indexOf(" ")));
//		System.out.println(StringCommon.formatDate("2019/1/1"));
//		System.out.println(StringCommon.formatDate("2019/01/01"));
//
//		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI_JP,
//				RedmineConst.API_ACCESS_KEY_JP);
//
//		IssueManager issueManager = mgr.getIssueManager();
//		List<Issue> lstIssue = issueManager.getIssues(null, 106, Include.values());
//		for (Issue issue : lstIssue) {
//			System.err.println(issue.getId());
//		}
		
		
		String command=ActionEnum.jpmiss.name();
//		String command=ActionEnum.vnupdatemiss.name();
		
		StringBuilder messReply = new StringBuilder();

		if (ActionEnum.jpmiss.toString().equals(command)) {
			messReply = CommandExecute.runJPMissCommand(messReply);
		} else if (ActionEnum.vnupdatemiss.toString().equals(command)) {
			messReply = CommandExecute.runVNUpdateMissCommand(messReply);
		} else {
			// command's not recognized
			messReply.append(MessageConst.COMMAND_NOT_FOUND + command);
		}
		System.out.println(messReply);
	}
}
