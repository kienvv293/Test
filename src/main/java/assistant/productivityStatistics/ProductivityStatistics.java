/*
 * ProductivityStatistics.java
 *
 * Copyright (c) 2009 Chip One Stop, Inc. All right reserved. 
 * This software is the confidential and proprietary information of Chip One Stop, Inc.
 *
 */
package assistant.productivityStatistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.TimeEntry;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import assistant.CommandExecute;
import bean.productivityStatistics.MemberBean;
import common.SqliteCommon;
import common.StringCommon;
import common.excel.AbstractReportExport;
import common.excel.ProductivityStatisticsExportFactory;
import common.excel.bean.ReportInfo;
import constant.BotConst;
import constant.RedmineConst;

/**
 * <PRE>
 * 。
 * 
 * 2019/01/16 phamxuantung 新規作成
 * </PRE>
 * @author phamxuantung
 */
public class ProductivityStatistics {
	
	private static final String DB_NAME = "chatbot.db";
	
	private static final String TEMPLATE_EXPORT_PATH = "templateExcel\\ProductivityStatistics_template.xlsx";
	
	private static final String EXPORT_PATH = "\\\\LUV-TUNGPX\\";
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String MEMBER_TYPE_CODER = "1";
	
	public static final String MEMBER_TYPE_TESTER = "2";
	
	public static final String MEMBER_TYPE_INVESTIGATE = "3";
	
	public static final String MEMBER_TYPE_REVIEW = "4";
	
	/**
	 * <PRE>
	 * Thông kê năng suất của member trong team。
	 * </PRE>
	 * @throws RedmineException 
	 */
	public static StringBuilder doExportProductivityStaistics(StringBuilder messReply, int month) throws RedmineException {
		ReportInfo info = new ReportInfo();
		info.setInportFileName(TEMPLATE_EXPORT_PATH);
		info.setExportFileName("exportExcel\\" + "Thống kê năng suất tháng "+ month + ".xlsx");
		Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
		Map<String, Object> detail = new HashMap<String, Object>();

		detail.put("creatorNm", "Bot Chatworks"); // TODO add member list mapping with ac chatwork
		detail.put("createDate", dateFormat.format(new Date()));
		detail.put("month", month);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// get data productivity staistics
		List<MemberBean> memberBeanLst = getDataProductivityStaistics(month);
		for (MemberBean memberBean : memberBeanLst) {
			Map<String, Object> resultInfo = new HashMap<String, Object>();
			// Tên hiển thị
			resultInfo.put("memberFullName", memberBean.getMemberFullName());
			// Số ticket dễ
			resultInfo.put("numOfTicketEasy", memberBean.getNumOfTicketEasy());
			// Số ticket trung bình
			resultInfo.put("numOfTicketMedium", memberBean.getNumOfTicketMedium());
			// Số ticket khó
			resultInfo.put("numOfTicketDifficult", memberBean.getNumOfTicketDifficult());
			// Số bug test
			resultInfo.put("numOfBugsTest", memberBean.getNumOfBugsTest());
			// Tổng thời gian xử lý
			resultInfo.put("totalHoursHandling", memberBean.getTotalHoursHandling()); 
			// Số ngày nghỉ trong tháng
			resultInfo.put("numberOfDaysNotWorking", memberBean.getNumberOfDaysNotWorking());
			// Set type cho member
			resultInfo.put("memberType", memberBean.getMemberType());
			
			result.add(resultInfo);
		}
		detail.put("list", result);
		data.put("refined 1", detail);
		
		info.setReportData(data);
		info.setTemplateName("export");
		AbstractReportExport report = ProductivityStatisticsExportFactory.getInstance().createReportExport(info);
		if (report.reportExport()) {
			messReply = messReply.append("(*) Export thành công !" + BotConst.LINE_BREAK + EXPORT_PATH + info.getExportFileName());
			System.out.println("Export thành công !");
		} else {
			messReply = messReply.append("Export data không thành công !");
			System.out.println("Export không thành công !");
		}
		
		 return messReply;
	}
	
	
	/**
	 * <PRE>
	 * 
	 * </PRE>
	 * @throws RedmineException 
	 */
	private static List<MemberBean> getDataProductivityStaistics(int month) throws RedmineException {
		// Tao list member theo id
		List<MemberBean> memberLst = getListMemberFromSqlLite();
		if (memberLst.size() == 0) {
			return null;
		}
		
		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY);
		TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();
		IssueManager issueManager = mgr.getIssueManager();
		Map<String, String> parameters;
		parameters = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		String monthStr = String.valueOf(month);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}
		String startDay = String.valueOf(cal.get(Calendar.YEAR))+ "-" + monthStr + "-" + "01";
		String lastDay = getLastDayOfMonth(cal, month);
//		parameters.put("user_id", "9");
		parameters.put("limit", "1000");
		parameters.put("from", startDay); // GOOD
		parameters.put("to", lastDay);
		ResultsWrapper<TimeEntry> timeEntriesResult = timeEntryManager.getTimeEntries(parameters);
		for (MemberBean memberBean : memberLst) {
			boolean flg = false;
			float countHours = 0;
			int numOfTicketEasy = 0;
			int numOfTicketMedium = 0;
			int numOfTicketDifficult = 0;
			List<Integer> issueIdLst = new ArrayList<Integer>();
			List<String> notes = new ArrayList<String>();
			int numbeOfBugTest = 0; // Số bug nội bộ test được
			Issue issue;
			HashSet<String> dayWorkList = new HashSet<>();
			for (TimeEntry timeEntry : timeEntriesResult.getResults()) {
				if (timeEntry.getUserId() == memberBean.getMemberId()) {
					flg = true;
					// set type for member
					if (!StringCommon.isNull(timeEntry.getActivityName())) {
						if ("Coding".equals(timeEntry.getActivityName()) || "Bug fixing".equals(timeEntry.getActivityName())) {
							memberBean.setMemberType(MEMBER_TYPE_CODER);
						} else if ("Testing".equals(timeEntry.getActivityName())) {
							memberBean.setMemberType(MEMBER_TYPE_TESTER);
						} else if ("Review".equals(timeEntry.getActivityName())) {
							memberBean.setMemberType(MEMBER_TYPE_REVIEW);
						} else if ("Điều tra".equals(timeEntry.getActivityName())) {
							memberBean.setMemberType(MEMBER_TYPE_INVESTIGATE);
						}
					}
					
					// calculator point for ticket
					countHours += timeEntry.getHours();
					Integer issueId = timeEntry.getIssueId();
					if (issueId == null) {
						continue;
					}
					// Ticket khac thi moi can get lai cac thong tin lien quan den ticket do
					if (!issueIdLst.contains(issueId)) {
						issueIdLst.add(issueId);
						issue = issueManager.getIssueById(issueId, Include.journals);
						String classify = CommandExecute.getCustomFieldVal(issue.getCustomFieldById(127));
						String idTicket = CommandExecute.getCustomFieldVal(issue.getCustomFieldById(8)).trim().replace("#", "");
						if ("Dễ".equals(classify)) {
							numOfTicketEasy += 1;
						} else if ("Trung Bình".equals(classify)) {
							numOfTicketMedium += 1;
						} else if ("Khó".equals(classify)) {
							numOfTicketDifficult += 1;
						} else {
							notes.add("Ticket #" + idTicket + "Chưa phân loại độ khó !");
						}
						
						// cal number of bug
						if (MEMBER_TYPE_CODER.equals(memberBean.getMemberType()) || MEMBER_TYPE_INVESTIGATE.equals(memberBean.getMemberType())) {
							Issue issueChild = issueManager.getIssueById(issueId, Include.children);
							Collection<Issue> issuesChildrenCol = issueChild.getChildren();
							for (Issue issuesChildren : issuesChildrenCol) {
								if ("Bug nội bộ".equals(issuesChildren.getTracker().getName())) { // 9: Bug nội bộ
									numbeOfBugTest++;
								}
							}
						}
					}
					
					// Tinh toan so ngay nghi
					dayWorkList.add(dateFormat.format(timeEntry.getSpentOn()));
				}
			}
			if (flg) {
				memberBean.setTotalHoursHandling(countHours);
				memberBean.setNumOfTicketEasy(numOfTicketEasy);
				memberBean.setNumOfTicketMedium(numOfTicketMedium);
				memberBean.setNumOfTicketDifficult(numOfTicketDifficult);
				memberBean.setNotes(notes);
				memberBean.setNumOfBugsTest(numbeOfBugTest);
				// Số ngày nghỉ
				int numOfDay = getNumOfDayWorkOnMonth(month) - dayWorkList.size();
				memberBean.setNumberOfDaysNotWorking(numOfDay);
			}
		
		}
		
		return memberLst;	
	}
	
	/*
	 * Get listt member from sql lite 
	 */
	private static List<MemberBean> getListMemberFromSqlLite() {
        Connection conn = SqliteCommon.initDatabase(DB_NAME);
        List<MemberBean> memberBeanLst = new ArrayList<MemberBean>();
        try {
        	MemberBean memberBean;
            Statement stmt  = conn.createStatement();  
            conn.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM MEMBER WHERE DEL_FLG != 1;" );
            while ( rs.next() ) {
            	memberBean = new MemberBean();
                int memberId = rs.getInt("member_id");
                String memberNm = rs.getString("member_name");
                String memberFullName = rs.getString("member_fullname");
                
                memberBean.setMemberId(memberId);
                memberBean.setMemberNm(memberNm);
                memberBean.setMemberFullName(memberFullName);
                
                memberBeanLst.add(memberBean);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return memberBeanLst;
    }
	
	/**
	 * 
	 * <PRE>
	 * Get list ngay lam viec trong 1 thang。
	 * </PRE>
	 * @param month
	 * @return
	 */
	public static int getNumOfDayWorkOnMonth(int month) {
		Calendar cal = Calendar.getInstance();
		int monthCurrent = cal.get(Calendar.MONTH) + 1;
		int dayCurrent = cal.get(Calendar.DAY_OF_MONTH);
	    cal.set(Calendar.MONTH, month - 1); // month on calendar start from 0
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    int maxDay = 0;
	    if (monthCurrent == month) {
	    	maxDay = dayCurrent;
	    } else {
	    	maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    }
	    int num = 0;
	    for (int i = 1; i <= maxDay; i++) {
	       cal.set(Calendar.DAY_OF_MONTH, i);
	       if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//	    	   System.out.println(dateFormat.format(cal.getTime()));
	    	  num++;
	       }
	    }
	    
	    return num;
	}
	
	/**
	 * 
	 * <PRE>
	 * Get ngay cuoi cung trong 1 thang。
	 * </PRE>
	 * @param month
	 * @return
	 */
	private static String getLastDayOfMonth(Calendar cal, int month) {
		cal.set(Calendar.MONTH, month - 1); // month on calendar start from 0
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, maxDay);
		
		return dateFormat.format(cal.getTime());
	}
}