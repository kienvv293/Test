/**
 * 
 */
package assistant.reminder;

import java.util.Calendar;
import java.util.Date;

import assistant.reminder.review.PersonalIssueReminderExecute;
import assistant.reminder.todayTask.TodayTaskReminderExecute;
import common.chatwork.ChatworkCommon;

/**
 * @author nguyenhuytan
 *
 */
public class ReminderDaemonTask implements Runnable {

	private boolean meetingReminder = false;
	private boolean reviewReminder = false;
	private boolean taskReminder = false;
	private String roomId;

	public ReminderDaemonTask(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public void run() {
		while (true) {
			Date currentTime = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentTime);
			int dayOfW = calendar.get(Calendar.DAY_OF_WEEK);

			if (dayOfW >= 2 && dayOfW <= 6) {
				int hourOfD = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				// Daily Meeting
				meetingRemind(hourOfD, minute);
				// Personal issue
				personalIssueRemind(hourOfD, minute);
				// Daily ticket issue
				todayTaskRemind(hourOfD, minute);
			}
			try {
				// Sleep 10s
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void todayTaskRemind(int hourOfD, int minute) {
		// 0h~7h + 12h~14h: Reset flag
		if ((hourOfD >= 0 && hourOfD <= 7) || (hourOfD >= 10 && hourOfD <= 12) || (hourOfD >= 14 && hourOfD <= 15)) {
			taskReminder = true;
		}
		// Remind at 9h + 13h
		if (((hourOfD == 8 && minute == 30) || (hourOfD == 13 && minute == 0) || (hourOfD == 16 && minute == 0)) && taskReminder) {
			taskReminder = false;
			Thread taskReminderThread = new Thread(new TodayTaskReminderExecute());
			taskReminderThread.start();
		}
	}

	private void personalIssueRemind(int hourOfD, int minute) {
		// 0h~7h + 12h~14h: Reset flag
		if ((hourOfD >= 0 && hourOfD <= 7) || (hourOfD >= 10 && hourOfD <= 12)) {
			reviewReminder = true;
		}
		// Remind at 9h + 13h
		if (((hourOfD == 8 && minute == 30) || (hourOfD == 13 && minute == 0)) && reviewReminder) {
			reviewReminder = false;
			Thread reviewReminderThread = new Thread(new PersonalIssueReminderExecute());
			reviewReminderThread.start();
		}
	}

	private void meetingRemind(int hourOfD, int minute) {
		// 0h~7h: Reset flag
		if (hourOfD >= 0 && hourOfD <= 7) {
			meetingReminder = true;
		}
		// 8h30: remind
		if (hourOfD == 8 && minute == 30 && meetingReminder) {
			meetingReminder = false;
			// Send message of Bot to all
			ChatworkCommon.sendMessage(roomId, "[toall] mọi người qua họp nhé (blush) !!!");
		}
	}

}
