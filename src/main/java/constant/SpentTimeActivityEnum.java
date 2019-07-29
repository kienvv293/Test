/**
 * 
 */
package constant;

import java.util.HashMap;

/**
 * @author nguyenhuytan
 *
 */
public enum SpentTimeActivityEnum {
	code		("9", 0),
	test		("14", 0),
	fixbug		("10", 0),
	req			("15", 0),
	dieutra		("16", 0),
	review		("21", 0),
	other		("20", 0)
	;

	private String actionId;
	private int actionParam;

	private SpentTimeActivityEnum(String actionId, int actionParam) {
		this.actionId = actionId;
		this.actionParam = actionParam;
	}

	public String getActionId() {
		return actionId;
	}

	public int getActionParam() {
		return actionParam;
	}

	public static HashMap<String, SpentTimeActivityEnum> getActionList() {
		HashMap<String, SpentTimeActivityEnum> actionList = new HashMap<>();
		for (int i = 0; i < SpentTimeActivityEnum.values().length; i++) {
			actionList.put(SpentTimeActivityEnum.values()[i].toString(), SpentTimeActivityEnum.values()[i]);
		}
		return actionList;
	}
}
