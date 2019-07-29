/**
 *
 */
package constant;

/**
 * @author nguyenhuytan
 *
 */
public interface RedmineConst {

	String REDMINE_URI = "http://192.168.0.136/redmine";
	String API_ACCESS_KEY = "5c26c573df39bf138526510d01533f7e5560b86f";

	String REDMINE_URI_JP = "https://c1s_redm:Red_mine_c1s@ws-redm.chip1stop.com/redmine";
	String API_ACCESS_KEY_JP = "235ac04ba2ac4a1236630177c4cea3a9224469ac";

	String ITM_STATUS = "status_id";
	String ITM_TRACKER = "tracker_id";
	String ITM_HOPE_DATE = "cf_1";
	String ITM_C1S_TICKETNO = "cf_8";
	String ITM_C1S_QA = "cf_9";
	String ITM_C1S_ASSIGNTO = "assigned_to_id";
	String LIMIT = "1000";
	
	int ITM_ID_NOTE = 54;
	int ITM_ID_HOPE_DATE = 25;
	int ITM_ID_EST_FULL_DATE = 1;
	int ITM_ID_EST_CODE_DATE = 2;
	int ITM_ID_C1SNO = 8;
	int ITM_ID_C1SQA = 9;
	int ITM_ID_CODER = 4;
	int ITM_ID_TESTER = 5;
	int ITM_ID_RELEASE_DATE = 93;
	// tannh 2019/01/09 add start
	String REDMINE_STATUS_OPEN = "o";
	String REDMINE_STATUS_NG = "ng";
	// tannh 2019/01/09 add end
	// tungpx 2019/01/23 add start
	String REDMINE_STATUS_ANY = "*";
	// tannh 2019/01/23 add end
}
