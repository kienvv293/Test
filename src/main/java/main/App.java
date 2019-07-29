package main;

import java.io.File;

import assistant.ChatworkClient;
import assistant.reminder.ReminderDaemonTask;
import common.BotProperty;
import common.SqliteCommon;
import common.StringCommon;
import common.UserCommon;
import common.user.UserManager;
import constant.BotConst;
import constant.MessageConst;

/**
 * Created by QuangDN
 */
public class App {

	public static void main(String[] args) {
		try {
			if (args.length < 1) {
				System.out.println("Chưa thiết định thư mục chứa file config");
				return;
			}
			File listTokenFile = new File(args[0] + BotConst.FILE_TOKEN);
			if (listTokenFile == null || !listTokenFile.exists()) {
				System.out.println(MessageConst.FILE_NOT_FOUND + listTokenFile);
				return;
			}
			File configFile = new File(args[0] + BotConst.FILE_BOT_CONFIG);
			if (configFile == null || !configFile.exists()) {
				System.out.println(MessageConst.FILE_NOT_FOUND + configFile);
				return;
			}

			initConfig(args[0]);

			String roomId1 = BotProperty.getProperty(BotProperty.ROOM_ID_1);
			// tannh 2019/01/09 mod start
			if (!StringCommon.isNull(roomId1)) {
				Thread threadReminder = new Thread(new ReminderDaemonTask(roomId1));
				threadReminder.start();

				Thread threadRoom1 = new Thread(new ChatworkClient(roomId1));
				threadRoom1.start();
			}

			String roomId2 = BotProperty.getProperty(BotProperty.ROOM_ID_2);
			if (!StringCommon.isNull(roomId2)) {
				Thread threadRoom2 = new Thread(new ChatworkClient(roomId2));
				threadRoom2.start();
			}

			String roomId3 = BotProperty.getProperty(BotProperty.ROOM_ID_3);
			if (!StringCommon.isNull(roomId3)) {
				Thread threadRoom3 = new Thread(new ChatworkClient(roomId3));
				threadRoom3.start();
			}
			// tannh 2019/01/09 mod end

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initConfig(String path) {
		// Init Role
		UserCommon.init(path + BotConst.FILE_TOKEN);
		// Init bot chat property
		BotProperty.init(path + BotConst.FILE_BOT_CONFIG);
		// tannh 2019/01/09 add start
		// Init user
		UserManager.initUserManager(path + BotConst.USER_CONFIG);
		// tannh 2019/01/09 add end
		// Init Sqlite
		SqliteCommon.initDatabase(path + BotConst.FILE_DB_SQLITE);
	}
}
