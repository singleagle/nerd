package com.enjoy.nerd.remoterequest.xmpp;

import org.jivesoftware.smack.BOSHConfiguration;
import org.jivesoftware.smack.BOSHConnection;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class XMPPClient {
	private static final String XMPPSERVER = "192.168.1.101";//"120.24.208.105";
	private static final boolean USE_BOSH = false;
	
	Connection mConnection;
	Context mContext;
	HandlerThread mTransferThread;
	Handler mTransferHandler;
	ProviderManager mSmackProvider;
	
	public XMPPClient(Context context){
		mContext = context;
		mTransferThread = new HandlerThread("XMPPTransfer");
		mTransferThread.start();
		mTransferHandler = new Handler(mTransferThread.getLooper());
		try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
	    } catch (Exception e) {
	            e.printStackTrace();
	    }
		//ReconnectionManager reconnectionManager =  new ReconnectionManager(mConnection);
		ConnectionConfiguration config;
		if(USE_BOSH){
			config = new BOSHConfiguration(XMPPSERVER);
		}else{
			config = new ConnectionConfiguration(XMPPSERVER, 5222, "enjoy.com");
		}
		
		config.setSendPresence(true);
		config.setReconnectionAllowed(true);
		config.setDebuggerEnabled(true);
		config.setSASLAuthenticationEnabled(false);
		SmackConfiguration.setPacketReplyTimeout(10*60*1000);
		// 允许登陆成功后更新在线状态
		
		if(USE_BOSH){
			mConnection = new BOSHConnection((BOSHConfiguration)config);
		}else{
			mConnection = new XMPPConnection(config);		
		}
		// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
		
		mConnection.getChatManager();
		Connection.DEBUG_ENABLED = true;
		initPacketParser();
	}
	
	public void release(){
		mConnection.disconnect();
	}
	
	/**package**/Connection getConnection(){
		return mConnection;
	}
	
	private void initPacketParser(){

		mSmackProvider = ProviderManager.getInstance();
		// Private Data Storage
		mSmackProvider.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			mSmackProvider.addIQProvider("query", "jabber:iq:time", Class
					.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		mSmackProvider.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		mSmackProvider.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		mSmackProvider.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		mSmackProvider.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		mSmackProvider.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		mSmackProvider.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		mSmackProvider.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		mSmackProvider.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		mSmackProvider.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		mSmackProvider.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		mSmackProvider.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		mSmackProvider.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		mSmackProvider.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		mSmackProvider.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		mSmackProvider.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		mSmackProvider.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			mSmackProvider.addIQProvider("query", "jabber:iq:version", Class
					.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		mSmackProvider.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		mSmackProvider.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		mSmackProvider.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		mSmackProvider.addIQProvider("query", "jabber:iq:last",
						new LastActivity.Provider());

		// User Search
		mSmackProvider.addIQProvider("query", "jabber:iq:search",
						new UserSearch.Provider());

		// SharedGroupsInfo
		mSmackProvider.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		mSmackProvider.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		mSmackProvider.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		mSmackProvider.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// mSmackProvider.addIQProvider("open", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Open());
		//
		// mSmackProvider.addIQProvider("close", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Close());
		//
		// mSmackProvider.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Data());

		// Privacy
		mSmackProvider.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		mSmackProvider.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		mSmackProvider.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		mSmackProvider.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		mSmackProvider.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		mSmackProvider.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		mSmackProvider.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
	
	
	 private int translateToErrorCode(Throwable throwable) {
		int errCode = FailResponseListner.ERR_UNKNOWN;
		if (throwable != null) {
			if (throwable instanceof XMPPException){
				errCode = FailResponseListner.ERR_ERROR_MESSAGE;
			}else if(throwable instanceof IllegalStateException){
				
			}
		}
		return errCode;
	 }
	
	private void handleXMPPRequest(XMPPRequest<?> request){
		try {
			Object xmppResp = request.handleSendAction(mConnection);
			if(xmppResp != null){
				request.dispatchSuccessResult(xmppResp);
			}else{
				request.dispatchFailureResult(FailResponseListner.ERR_INVALID_FORMAT_MESSAGE, 0, "null response");
			}
			
		} catch (XMPPException e) {
			XMPPError error = e.getXMPPError();
			if(error != null){
				request.dispatchFailureResult(FailResponseListner.ERR_ERROR_MESSAGE, error.getCode(), error.getMessage());
			}else{
				request.dispatchFailureResult(FailResponseListner.ERR_UNKNOWN, 0, e.getMessage());
			}
			
		}
	}
	
	void excuteRequest(final XMPPRequest<?> request){
		mTransferHandler.post(new Runnable() {
			@Override
			public void run() {
				handleXMPPRequest(request);
			}
		});
	}
}
