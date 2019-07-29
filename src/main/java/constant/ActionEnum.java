/**
 *
 */
package constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author nguyenhuytan
 *
 */
public enum ActionEnum {
	stop		(BotConst.ROLE_ADMIN, "Dừng bot", 1),
	lucall		(BotConst.ROLE_ADMIN, "Lucall", 2),
	aimode		(BotConst.ROLE_ADMIN, "Set AI mode", 2),
	remind		(BotConst.ROLE_ADMIN, "Send daily mail (manually)", 2),

	help		(BotConst.ROLE_PUBLIC, "List command", 3),
	deadline	(BotConst.ROLE_PUBLIC, "Get list ticket nộp hàng hôm nay", 4),
	status		(BotConst.ROLE_PUBLIC, "Get thông tin ticket", 5),

	assignee	(BotConst.ROLE_ADVANCED, "Update assignee", 6),
	note		(BotConst.ROLE_ADVANCED, "Update ghi chú", 7),
	hopedate	(BotConst.ROLE_ADVANCED, "Update ngày kỳ vọng nộp hàng", 8),
	estcodedate	(BotConst.ROLE_ADVANCED, "Update ngày dự định nộp code", 9),
	estfulldate	(BotConst.ROLE_ADVANCED, "Update ngày dự định nộp full", 10),
	releasedate	(BotConst.ROLE_ADVANCED, "Update ngày release", 11),
	spent		(BotConst.ROLE_ADVANCED, "Add spent time", 12),

	jpmiss		(BotConst.ROLE_PUBLIC, "List ticket redmineJP bị miss", 13),
	vnupdatemiss(BotConst.ROLE_PUBLIC, "Get status ticket update lệch trạng thái", 14),

	estfulldatejp(BotConst.ROLE_ADMIN, "Update dự định nộp hàng bên JP", 101),
	
	checkqa(BotConst.ROLE_PUBLIC, "Quản lý QA", 102),
	
	checkreply(BotConst.ROLE_PUBLIC, "Check nội dung nộp hàng", 103),
	
	thongke(BotConst.ROLE_PUBLIC, "Thống kê năng suất của member trong 1 tháng", 104),
	;

	private String roleId;
	private String actionName;
	private int actionParam;

	private ActionEnum(String roleId, String actionName, int actionParam) {
		this.roleId = roleId;
		this.actionName = actionName;
		this.actionParam = actionParam;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getActionName() {
		return actionName;
	}

	public int getActionParam() {
		return actionParam;
	}

	public static HashMap<String, ActionEnum> getActionList() {
		HashMap<String, ActionEnum> actionList = new HashMap<>();
		for (int i = 0; i < ActionEnum.values().length; i++) {
			actionList.put(ActionEnum.values()[i].toString(), ActionEnum.values()[i]);
		}
		return actionList;
	}

	public static List<ActionEnum> getActionListSort() {
		List<ActionEnum> actionList = new ArrayList<>();
		for (int i = 0; i < ActionEnum.values().length; i++) {
			actionList.add(ActionEnum.values()[i]);
		}
		Collections.sort(actionList, new Comparator<ActionEnum>() {
			@Override
			public int compare(ActionEnum arg0, ActionEnum arg1) {
				if (arg0.getActionParam() > arg1.getActionParam()) {
					return 1;
				}
				return 0;
			}
		});
		return actionList;
	}
}
