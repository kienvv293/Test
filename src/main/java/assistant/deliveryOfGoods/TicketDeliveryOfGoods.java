package assistant.deliveryOfGoods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.JournalDetail;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import assistant.CommandExecute;
import common.RedmineCommon;
import common.StringCommon;
import constant.BotConst;
import constant.RedmineConst;

/**
 * 
 * <PRE>
 * Check noi dung ticket nop hang。
 * 
 * 2019/01/01 phamxuantung 新規作成
 * </PRE>
 * 
 * @author phamxuantung
 */
public class TicketDeliveryOfGoods {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateFormatFull = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static final String OK_STR = "✔";
	public static final String NG_STR = "✗";

	/**
	 * 
	 * <PRE>
	 * Get list ticket nop hang 。
	 * </PRE>
	 * 
	 * @param inputDate
	 * @param ticketID
	 * @param issueManagerVN
	 * @return
	 * @throws RedmineException
	 */
	public static List<Issue> getListTicketDeliveryOfGoodsVN(String inputDate, String ticketID, IssueManager issueManagerVN) throws RedmineException {
		List<Issue> lstTicketSubmitGoodVN = new ArrayList<Issue>();
		List<Issue> lstTicketCodeHopeDate;
		// Check list ticket la sysdate hoac 1 ngay cu the duoc nhap
		if (StringCommon.isNull(ticketID)) {
			// Set param de get list ticket dua vao ngay du dinh nop chinh sua
			List<Issue> lstTicketHopeDate = new ArrayList<Issue>();
			Params paramsHopeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_ANY);
			paramsHopeDate.add("f[]", "cf_1");
			paramsHopeDate.add("op[cf_1]", "=");
			paramsHopeDate.add("v[cf_1][]", inputDate);
			paramsHopeDate.add("f[]", "status_id");
			paramsHopeDate.add("op[status_id]", "*");
			
			// Set param de get list ticket dua vao ngay du dinh nop code
			lstTicketCodeHopeDate = new ArrayList<Issue>();
			Params paramsCodeHopeDate = RedmineCommon.getCommonRedmineParams(RedmineConst.REDMINE_STATUS_ANY);
			paramsCodeHopeDate.add("f[]", "cf_2");
			paramsCodeHopeDate.add("op[cf_2]", "=");
			paramsCodeHopeDate.add("v[cf_2][]", inputDate);
			paramsCodeHopeDate.add("f[]", "status_id");
			paramsCodeHopeDate.add("op[status_id]", "*");

			lstTicketHopeDate = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY, paramsHopeDate);
			lstTicketSubmitGoodVN.addAll(lstTicketHopeDate);

			lstTicketCodeHopeDate = RedmineCommon.getListIssues(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY, paramsCodeHopeDate);
			lstTicketSubmitGoodVN.addAll(lstTicketCodeHopeDate);
		} else {
			// Check ticket khi nhap id ticket
			ResultsWrapper<Issue> result = CommandExecute.getIssueByC1sNo(issueManagerVN, ticketID);
			if ((result != null) && (result.getResults() != null) && (result.getResults().size() > 0)) {
				for (Issue ticket : result.getResults()) {
					lstTicketSubmitGoodVN.add(ticket);
				}
			}
		}

		return lstTicketSubmitGoodVN;
	}

	/**
	 * 
	 * <PRE>
	 * Get list ticket nộp hàng JP.
	 * </PRE>
	 * 
	 * @param lstTicketSubmitGoodVN
	 * @return
	 * @throws RedmineException
	 */
	public static List<Issue> getListTicketDeliveryOfGoodsJP(List<Issue> lstTicketSubmitGoodVN) throws RedmineException {
		RedmineManager mgrJP = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI_JP, RedmineConst.API_ACCESS_KEY_JP);
		IssueManager issueManagerJP = mgrJP.getIssueManager();
		List<Issue> lstTicketSubmitGoodJP = new ArrayList<Issue>();
		List<String> idTicketList = new ArrayList<String>();
		for (Issue issueVN : lstTicketSubmitGoodVN) {
			idTicketList.add(CommandExecute.getCustomFieldVal(issueVN.getCustomFieldById(8)).trim().replace("#", ""));
		}

		for (String idTicket : idTicketList) {
			Issue issue = issueManagerJP.getIssueById(Integer.valueOf(Integer.parseInt(idTicket)), new Include[] { Include.journals });
			if (issue != null) {
				lstTicketSubmitGoodJP.add(issue);
			}
		}
		return lstTicketSubmitGoodJP;
	}

	/**
	 * 
	 * <PRE>
	 * Check chi tiết các nội dung nộp hàng。
	 * </PRE>
	 * 
	 * @param messReply
	 * @param lstTicketVN
	 * @param lstTicketJP
	 * @param inputDate
	 * @param idTicket
	 * @return
	 * @throws RedmineException
	 */
	public static StringBuilder checkContentTicketDeliveryOfGoods(StringBuilder messReply, List<Issue> lstTicketVN, List<Issue> lstTicketJP, String inputDate, String idTicket) throws RedmineException {
		int count = 0;
		RedmineManager mgrJP = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI_JP, RedmineConst.API_ACCESS_KEY_JP);
		RedmineManager mgrVN = RedmineManagerFactory.createWithApiKey(RedmineConst.REDMINE_URI, RedmineConst.API_ACCESS_KEY);
		IssueManager issueManagerJP = mgrJP.getIssueManager();
		IssueManager issueManagerVN = mgrVN.getIssueManager();
		Issue issueVNMapping = new Issue();
		Date sysDate = new Date();

		String titleHeader = "";
		if (StringCommon.isNull(idTicket)) {
			titleHeader = "Các ticket nộp hàng ngày: " + inputDate;
		} else {
			titleHeader = "Nộp hàng của ticket #" + idTicket;
		}
		messReply.append("[info][title]Check list " + titleHeader + "[/title]");
		for (Issue issueJP : lstTicketJP) {
			count++;
			for (Issue issueVN : lstTicketVN) {
				if (issueJP.getId().toString().equals(issueVN.getCustomFieldById(8).getValue().trim().replace("#", ""))) {
					issueVNMapping = issueVN;
					break;
				}
			}
			
			String branchVN = CommandExecute.getCustomFieldVal(issueVNMapping.getCustomFieldById(6));
			Integer issueVNId = issueVNMapping.getId();
			boolean hasTicketResearch = issueVNMapping.getTracker().getId().intValue() == 11;

			messReply.append("[hr]");
			messReply.append("Ticket #");
			messReply.append(issueJP.getId());
			messReply.append(" 「");
			messReply.append(count);
			messReply.append("」");
			messReply.append("[hr]");

			String statusCheckStr = NG_STR;
			String statusName = issueJP.getStatusName().trim();

			if ((!hasTicketResearch) && ("改修完了".equals(statusName))) {
				statusCheckStr = OK_STR;
			} else if ((hasTicketResearch) && (("調査完了".equals(statusName)) || ("終了".equals(statusName)))) {
				statusCheckStr = OK_STR;
			}
			messReply.append("●  Trạng thái:  " + statusCheckStr + "  " + issueJP.getStatusName() + BotConst.LINE_BREAK);

			String assigneeCheckStr = NG_STR;
			String assigneeName = issueJP.getAssigneeName();
			if ((!"34".equals(issueJP.getAssigneeId())) && (!"Luvi na".equals(issueJP.getAssigneeName()))) {
				assigneeCheckStr = OK_STR;
				String cusName = CommandExecute.getCustomFieldVal(issueJP.getCustomFieldById(24)).trim();
				if ((!StringCommon.isNull(cusName)) && (!assigneeName.contains(cusName.substring(cusName.length() - 1)))) {
					System.out.println("Assignee nhầm khách hàng !!!" + assigneeName + ":" + cusName);
				}
			}
			messReply.append("●  Assignee:  " + assigneeCheckStr + "  " + assigneeName + BotConst.LINE_BREAK);

			String branchJP = CommandExecute.getBranchInfo(CommandExecute.getCustomFieldVal(issueJP.getCustomFieldById(44)));
			String branchCheckStr = NG_STR;
			String branchMap = branchJP + " : " + branchVN;
			if ((!StringCommon.isNull(branchJP)) && (branchJP.replace("チケット", "Ticket").toLowerCase().equals(branchVN.toLowerCase()))) {
				branchCheckStr = OK_STR;
				branchMap = branchJP;
			}

			messReply.append("●  Branch:  " + branchCheckStr + "  " + branchMap + BotConst.LINE_BREAK);

			String contentCheckStr = NG_STR;
			StringBuilder reasonContent = new StringBuilder();

			Issue issueJPIncludeJournals = issueManagerJP.getIssueById(issueJP.getId(), new Include[] { Include.journals });
			Collection<Journal> journalsJP = issueJPIncludeJournals.getJournals();
			String conntentDeliveryOfGoods = "";

			String hopeDate = "";
			if (!StringCommon.isNull(idTicket)) {
				String codeHopeDate = CommandExecute.getCustomFieldVal(issueVNMapping.getCustomFieldById(1));
				hopeDate = CommandExecute.getCustomFieldVal(issueVNMapping.getCustomFieldById(2));
				if (!StringCommon.isNull(hopeDate)) {
					try {
						if (!dateFormat.parse(codeHopeDate).before(sysDate)) {
							break;
						}
						inputDate = codeHopeDate;

					} catch (ParseException localParseException1) {
						// do nothing
					}
				} else if (!StringCommon.isNull(codeHopeDate)) {
					inputDate = codeHopeDate;
				}
			}
			
			// Noi dung nop hang
			for (Journal journalJP : journalsJP) {
				if (dateFormat.format(journalJP.getCreatedOn()).equals(inputDate)) {
					List<JournalDetail> journalDetailsJP = journalJP.getDetails();
					for (JournalDetail detail : journalDetailsJP) {
						if ("22".equals(detail.getName())) {
							contentCheckStr = OK_STR;
							conntentDeliveryOfGoods = detail.getNewValue();
							break;
						}
					}
				}
			}
			if (NG_STR.equals(contentCheckStr)) {
				reasonContent.append("Thiếu nội dung nộp hàng !");
			}
			messReply.append("●  Nội dung nộp hàng:  " + contentCheckStr + "  " + reasonContent + BotConst.LINE_BREAK);
			if (!hasTicketResearch) {
				String logCommitCheckStr = NG_STR;
				String revisionCheckStr = NG_STR;
				String dateCheckStr = NG_STR;
				String commentCheckStr = NG_STR;
				String reasonRevision = "";
				String reasonDate = "";
				String reasonComment = "";

				String revisionJP = CommandExecute.getCustomFieldVal(issueJP.getCustomFieldById(57)); // ID = 57
				String dateJP = CommandExecute.getCustomFieldVal(issueJP.getCustomFieldById(58)); // ID = 58
				String commentJP = CommandExecute.getCustomFieldVal(issueJP.getCustomFieldById(56)); // ID = 56

				if ((StringCommon.isNull(revisionJP)) && (StringCommon.isNull(dateJP)) && (StringCommon.isNull(commentJP))) {
					messReply.append("Chưa có log commit !");
				} else {
					if ((StringCommon.isNull(revisionJP)) || (StringCommon.isNull(dateJP)) || (StringCommon.isNull(commentJP))) {
						if (StringCommon.isNull(revisionJP)) {
							reasonRevision = "Thiếu revision !";
						}
						if (StringCommon.isNull(dateJP)) {
							reasonDate = "Thiếu date !";
						}
						if (StringCommon.isNull(commentJP)) {
							reasonComment = "Thiếu comment !";
						}
					} else {
						Issue issueVNIncludeJournals = issueManagerVN.getIssueById(issueVNId, new Include[] { Include.journals });
						Collection<Journal> journalsVN = issueVNIncludeJournals.getJournals();

						for (Journal journalVN : journalsVN) {
							if (dateFormat.format(journalVN.getCreatedOn()).equals(inputDate)) {
								String notesVN = journalVN.getNotes();
								if ((!StringCommon.isNull(notesVN)) && ((notesVN.contains("の修正対応")) || (notesVN.contains("Revision")))) {
									String revisionVN = "";
									String dateVN = "";
									String commentVN = "";
									String[] notesVNArr = notesVN.split(BotConst.LINE_BREAK);
									boolean isComment = false;
									for (String lineStr : notesVNArr) {
										if (lineStr.startsWith("Revision")) {
											revisionVN = lineStr.replace("Revision:", "").trim();
										}
										if (lineStr.startsWith("Date")) {
											dateVN = lineStr.replace("Date:", "").trim();
										}
										if (isComment) {
											commentVN = lineStr.trim();
											isComment = false;
										}
										if (lineStr.startsWith("Message")) {
											isComment = true;
										}
									}

									if ((!StringCommon.isNull(revisionVN)) && (revisionJP.contains(revisionVN))) {
										revisionCheckStr = OK_STR;
										reasonRevision = revisionVN;
									} else {
										reasonRevision = "Revision không mapping";
									}

									boolean dateFormatFlg = true;
									String lastDateJP = "";
									try {
										String[] dateJPArr = dateJP.split(BotConst.LINE_BREAK);
										lastDateJP = dateJPArr[(dateJPArr.length - 1)];
										dateFormatFull.parse(lastDateJP);
									} catch (ParseException e) {
										dateFormatFlg = false;
										reasonDate = "Date không đúng định dạng";
									}
									if ((dateFormatFlg) && (dateVN.equals(lastDateJP))) {
										dateCheckStr = OK_STR;
										reasonDate = lastDateJP;
									} else {
										reasonDate = "Date không mapping";
									}

									String[] commentJPArr = commentJP.split(BotConst.LINE_BREAK);
									String lastCommentJP = commentJPArr[(commentJPArr.length - 1)];
									if (commentVN.equals(lastCommentJP)) {
										commentCheckStr = OK_STR;
										reasonComment = lastCommentJP;
									} else {
										reasonComment = "Comment không mapping";
									}
									if ((!OK_STR.equals(revisionCheckStr)) || (!OK_STR.equals(dateCheckStr)) || (!OK_STR.equals(commentCheckStr))) {
										break;
									} else {
										logCommitCheckStr = OK_STR;
									}
								}
							}
						}
					}
					messReply.append("●  Log commit code:  " + logCommitCheckStr + "  " + BotConst.LINE_BREAK);
					messReply.append("   - Revision:  " + revisionCheckStr + "  " + reasonRevision + BotConst.LINE_BREAK);
					messReply.append("   - Date:  " + dateCheckStr + "  " + reasonDate + BotConst.LINE_BREAK);
					messReply.append("   - Comment:  " + commentCheckStr + "  " + reasonComment + BotConst.LINE_BREAK);
				}
				
			}
			
			// Attach file
			List<String> attachmentsJP = new ArrayList<String>();
			for (Journal journalJP : journalsJP) {
				if (dateFormat.format(journalJP.getCreatedOn()).equals(inputDate)) {
					List<JournalDetail> journalDetails = journalJP.getDetails();
					for (JournalDetail detail : journalDetails) {
						if ("attachment".equals(detail.getProperty())) {
							attachmentsJP.add(detail.getNewValue());
						}
					}
				}
			}

			String attacCheckStr = NG_STR;
			String resonAttachFile = "";

			if (attachmentsJP.size() == 0) {
				resonAttachFile = "File attach redmine JP đang bị miss !";
				messReply.append("●  Attatch:  " + attacCheckStr + "  " + resonAttachFile + BotConst.LINE_BREAK);
			} else {
				String extendCheckStr = NG_STR;
				String testEvidenceCheckStr = NG_STR;
				String testCaseCheckStr = NG_STR;
				String fileReportCheckStr = NG_STR;
				for (String attachmentName : attachmentsJP) {
					if ("rar".equals(attachmentName.split("\\.")[1])) {
						resonAttachFile = "Đuôi mở rộng bắt buộc phải là .zip";
					} else {
						extendCheckStr = OK_STR;
					}

					if ((attachmentName.contains("エビデンス")) || (attachmentName.contains("ｴﾋﾞﾃﾞﾝｽ"))) {
						testEvidenceCheckStr = OK_STR;
					}

					if (attachmentName.contains("【UT】")) {
						testCaseCheckStr = OK_STR;
					}

					if (issueVNMapping.getTracker().getId().intValue() == 11) {
						if ((".xlxs".contains(conntentDeliveryOfGoods)) || (".xls".contains(conntentDeliveryOfGoods))) {
							fileReportCheckStr = OK_STR;
						} else {
							fileReportCheckStr = NG_STR;
						}
					}
				}

				if (((OK_STR.equals(extendCheckStr)) && (OK_STR.equals(testEvidenceCheckStr)) && (OK_STR.equals(testCaseCheckStr)) && (OK_STR.equals(extendCheckStr))) || (OK_STR.equals(fileReportCheckStr))) {
					attacCheckStr = OK_STR;
				}
				
				messReply.append("●  Attach file:  " + attacCheckStr + "  " + BotConst.LINE_BREAK);
				if (issueVNMapping.getTracker().getId().intValue() != 11) {
					messReply.append("   - B/c test:  " + testEvidenceCheckStr + "  " + BotConst.LINE_BREAK);
					messReply.append("   - Test case:  " + testCaseCheckStr + "  " + BotConst.LINE_BREAK);
				} else {
					messReply.append("  - Báo cáo điều tra    :   " + fileReportCheckStr + "  " + BotConst.LINE_BREAK);
				}
			}

			messReply.append("[hr]");
		}
		messReply.append("[/info]");

		return messReply;
	}
}