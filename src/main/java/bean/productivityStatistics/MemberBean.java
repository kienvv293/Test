/*
 * MemberBean.java
 *
 * Copyright (c) 2009 Chip One Stop, Inc. All right reserved. 
 * This software is the confidential and proprietary information of Chip One Stop, Inc.
 *
 */
package bean.productivityStatistics;

import java.util.List;

/**
 * <PRE>
 * 。
 * 
 * 2019/01/16 phamxuantung 新規作成
 * </PRE>
 * @author phamxuantung
 */
public class MemberBean {
	
	/* ID member */
	private Integer memberId;
	
	/* Tên member */
	private String memberNm;
	
	private String memberFullName;
	
	/* Phân loại member */
	private String memberType; // Coder: 1, Tester: 2, Điều tra: 3, Review: 4
	
	/* List các ticket xử lý */
	private List<TicketBean> ticketLst;
	
	/* Tổng số giờ tham gia sản xuất */
	private float totalHoursHandling = 0;
	
	/* Số ticket dễ */
	int numOfTicketEasy = 0;
	
	/* Số ticket trung bình */
	int numOfTicketMedium = 0;
	
	/* Số ticket khó */
	int numOfTicketDifficult = 0;
	
	List<String> notes;
	
	
	/* Số bug test bắt được */
	private int numOfBugsTest;
	
	/* Số bug khách hàng bắt được */
	private int numOfBugsCus;
	
	/* Số bug là sai rule, quy trình */
	private int numOfBugsProcess;
	
	/* Có báo cáo kịp thời hay không ? */
	private boolean reportFlg; // Kịp thời: 1 Không kịp thời: 0
	
	private int numberOfDaysNotWorking;
	/**
	 * <PRE>
	 * memberNmを取得する。<br>
	 * </PRE>
	 * @return memberNm memberNm
	 */
	public String getMemberNm() {
		return memberNm;
	}

	/**
	 * <PRE>
	 * memberNmを設定する。<br>
	 * </PRE>
	 * @param memberNm memberNm
	 */
	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}
	
	/**
	 * <PRE>
	 * memberFullNameを取得する。<br>
	 * </PRE>
	 * @return memberFullName memberFullName
	 */
	public String getMemberFullName() {
		return memberFullName;
	}

	/**
	 * <PRE>
	 * memberFullNameを設定する。<br>
	 * </PRE>
	 * @param memberFullName memberFullName
	 */
	public void setMemberFullName(String memberFullName) {
		this.memberFullName = memberFullName;
	}

	/**
	 * <PRE>
	 * memberTypeを取得する。<br>
	 * </PRE>
	 * @return memberType memberType
	 */
	public String getMemberType() {
		return memberType;
	}

	/**
	 * <PRE>
	 * memberTypeを設定する。<br>
	 * </PRE>
	 * @param memberType memberType
	 */
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	/**
	 * <PRE>
	 * ticketLstを取得する。<br>
	 * </PRE>
	 * @return ticketLst ticketLst
	 */
	public List<TicketBean> getTicketLst() {
		return ticketLst;
	}

	/**
	 * <PRE>
	 * ticketLstを設定する。<br>
	 * </PRE>
	 * @param ticketLst ticketLst
	 */
	public void setTicketLst(List<TicketBean> ticketLst) {
		this.ticketLst = ticketLst;
	}

	/**
	 * <PRE>
	 * memberIdを取得する。<br>
	 * </PRE>
	 * @return memberId memberId
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * <PRE>
	 * memberIdを設定する。<br>
	 * </PRE>
	 * @param memberId memberId
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * <PRE>
	 * totalHoursHandlingを取得する。<br>
	 * </PRE>
	 * @return totalHoursHandling totalHoursHandling
	 */
	public float  getTotalHoursHandling() {
		return totalHoursHandling;
	}

	/**
	 * <PRE>
	 * totalHoursHandlingを設定する。<br>
	 * </PRE>
	 * @param totalHoursHandling totalHoursHandling
	 */
	public void setTotalHoursHandling(float totalHoursHandling) {
		this.totalHoursHandling = totalHoursHandling;
	}

	/**
	 * <PRE>
	 * numOfTicketEasyを取得する。<br>
	 * </PRE>
	 * @return numOfTicketEasy numOfTicketEasy
	 */
	public int getNumOfTicketEasy() {
		return numOfTicketEasy;
	}

	/**
	 * <PRE>
	 * numOfTicketEasyを設定する。<br>
	 * </PRE>
	 * @param numOfTicketEasy numOfTicketEasy
	 */
	public void setNumOfTicketEasy(int numOfTicketEasy) {
		this.numOfTicketEasy = numOfTicketEasy;
	}

	/**
	 * <PRE>
	 * numOfTicketMediumを取得する。<br>
	 * </PRE>
	 * @return numOfTicketMedium numOfTicketMedium
	 */
	public int getNumOfTicketMedium() {
		return numOfTicketMedium;
	}

	/**
	 * <PRE>
	 * numOfTicketMediumを設定する。<br>
	 * </PRE>
	 * @param numOfTicketMedium numOfTicketMedium
	 */
	public void setNumOfTicketMedium(int numOfTicketMedium) {
		this.numOfTicketMedium = numOfTicketMedium;
	}

	/**
	 * <PRE>
	 * numOfTicketDifficultを取得する。<br>
	 * </PRE>
	 * @return numOfTicketDifficult numOfTicketDifficult
	 */
	public int getNumOfTicketDifficult() {
		return numOfTicketDifficult;
	}

	/**
	 * <PRE>
	 * numOfTicketDifficultを設定する。<br>
	 * </PRE>
	 * @param numOfTicketDifficult numOfTicketDifficult
	 */
	public void setNumOfTicketDifficult(int numOfTicketDifficult) {
		this.numOfTicketDifficult = numOfTicketDifficult;
	}

	/**
	 * <PRE>
	 * noteを取得する。<br>
	 * </PRE>
	 * @return note note
	 */
	public List<String> getNotes() {
		return notes;
	}

	/**
	 * <PRE>
	 * noteを設定する。<br>
	 * </PRE>
	 * @param note note
	 */
	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	/**
	 * <PRE>
	 * numOfBugsTestを取得する。<br>
	 * </PRE>
	 * @return numOfBugsTest numOfBugsTest
	 */
	public int getNumOfBugsTest() {
		return numOfBugsTest;
	}

	/**
	 * <PRE>
	 * numOfBugsTestを設定する。<br>
	 * </PRE>
	 * @param numOfBugsTest numOfBugsTest
	 */
	public void setNumOfBugsTest(int numOfBugsTest) {
		this.numOfBugsTest = numOfBugsTest;
	}

	/**
	 * <PRE>
	 * numOfBugsCusを取得する。<br>
	 * </PRE>
	 * @return numOfBugsCus numOfBugsCus
	 */
	public int getNumOfBugsCus() {
		return numOfBugsCus;
	}

	/**
	 * <PRE>
	 * numOfBugsCusを設定する。<br>
	 * </PRE>
	 * @param numOfBugsCus numOfBugsCus
	 */
	public void setNumOfBugsCus(int numOfBugsCus) {
		this.numOfBugsCus = numOfBugsCus;
	}

	/**
	 * <PRE>
	 * numOfBugsProcessを取得する。<br>
	 * </PRE>
	 * @return numOfBugsProcess numOfBugsProcess
	 */
	public int getNumOfBugsProcess() {
		return numOfBugsProcess;
	}

	/**
	 * <PRE>
	 * numOfBugsProcessを設定する。<br>
	 * </PRE>
	 * @param numOfBugsProcess numOfBugsProcess
	 */
	public void setNumOfBugsProcess(int numOfBugsProcess) {
		this.numOfBugsProcess = numOfBugsProcess;
	}

	/**
	 * <PRE>
	 * reportFlgを取得する。<br>
	 * </PRE>
	 * @return reportFlg reportFlg
	 */
	public boolean isReportFlg() {
		return reportFlg;
	}

	/**
	 * <PRE>
	 * reportFlgを設定する。<br>
	 * </PRE>
	 * @param reportFlg reportFlg
	 */
	public void setReportFlg(boolean reportFlg) {
		this.reportFlg = reportFlg;
	}

	/**
	 * <PRE>
	 * numberOfDaysNotWorkingを取得する。<br>
	 * </PRE>
	 * @return numberOfDaysNotWorking numberOfDaysNotWorking
	 */
	public int getNumberOfDaysNotWorking() {
		return numberOfDaysNotWorking;
	}

	/**
	 * <PRE>
	 * numberOfDaysNotWorkingを設定する。<br>
	 * </PRE>
	 * @param numberOfDaysNotWorking numberOfDaysNotWorking
	 */
	public void setNumberOfDaysNotWorking(int numberOfDaysNotWorking) {
		this.numberOfDaysNotWorking = numberOfDaysNotWorking;
	}
	
}
	
