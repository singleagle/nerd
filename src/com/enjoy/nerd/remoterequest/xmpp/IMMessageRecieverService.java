package com.enjoy.nerd.remoterequest.xmpp;

import java.util.Calendar;
import java.util.Iterator;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.packet.DelayInfo;

import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.usercenter.AccountManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


/**
 * 
 * 聊天服务.
 * 
 * @author tangwh
 */
public class IMMessageRecieverService extends Service implements ChatManagerListener {
	private NotificationManager notificationManager;
	private ChatManager mChatManager;
	private OfflineMessageManager mOfflineMessageManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NerdApp app = (NerdApp) getApplication();
		XMPPClient client = app.getXMPPClient();
		mChatManager = client.getConnection().getChatManager();
		mChatManager.addChatListener(this);
		mOfflineMessageManager = new OfflineMessageManager(client.getConnection());
	}

	private void recieveOfflineMessage(){
		try {
			Iterator<Message> it = mOfflineMessageManager.getMessages();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		mChatManager.removeChatListener(this);
		super.onDestroy();
	}


	private void saveRecivedChatMsg(Message message, boolean fromGroup){
		IMMessage imMsg = new IMMessage(message, AccountManager.getInstance(this).getLoginUIN(), fromGroup);
		ContentValues content = imMsg.createContentValues();
		getContentResolver().insert(IMMessage.Columns.CONTENT_URI, content);
	}
	

	@Override
	public void chatCreated(Chat chat, final  boolean createdLocally) {
		
		chat.addMessageListener(new MessageListener(){
			boolean mFromOther = !createdLocally;
			
			@Override
			public void processMessage(Chat chat, Message message) {
				if(mFromOther){
					saveRecivedChatMsg(message, false);
				}
			}
			
		});

	}
}
