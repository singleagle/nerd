package com.enjoy.nerd.home;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.R;
import com.enjoy.nerd.R.layout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class HomeActivity extends FragmentActivity{
	
	static private final int MSG_SHOW_TAB_CONTENT = 1000;
	static private final String WELCOME_TAG = "welcomefragement";
	
	private FragmentManager mFragmentManager;
	private TabHost mTabHost;
	private TabManager mTabManger;
	
	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_SHOW_TAB_CONTENT:
				hideWelcomeFragement();
				showTabContent();
				break;
				
			default:
				break;
				
			}
		}
		
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar();
        setContentView(R.layout.activity_home);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabManger = new TabManager(this, mTabHost, R.id.content);
        mFragmentManager = getSupportFragmentManager();
        showWelcomeFragement();
        mUIHandler.sendEmptyMessageDelayed(MSG_SHOW_TAB_CONTENT, 2000);
    }
    
    

    @Override
	protected void onDestroy() {
		NerdApp app = (NerdApp) getApplicationContext();
		app.getXMPPClient().release();
		
		super.onDestroy();
	}



	void showWelcomeFragement(){
    	getActionBar().hide();
    	FragmentTransaction transaction = mFragmentManager.beginTransaction();
    	Fragment fragment = new WelcomeFragment();
    	transaction.add(R.id.welcome_container, fragment, WELCOME_TAG).commit();
    }
    
    void hideWelcomeFragement(){
    	Fragment fragment = mFragmentManager.findFragmentByTag(WELCOME_TAG);
    	if(fragment != null){
    		FragmentTransaction transaction = mFragmentManager.beginTransaction();
    		transaction.remove(fragment).commit();
    	}
    	getActionBar().show();
    }
    
    void showTabContent(){
    	mTabManger.addTab(RecommandFeedFragment.class, R.string.recommend, R.drawable.home_normal);
    	mTabManger.addTab(NearByFragment.class, R.string.nearby, R.drawable.nearby_normal);
    	mTabManger.addTab(UserCenterFragment.class, R.string.user_center, R.drawable.user_center_normal);
    	mTabHost.setCurrentTab(0);
    }


	public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private String mLastTabTag;
	    
	    static private class DummyTabFactory implements TabHost.TabContentFactory {
	        private final Context mContext;
	
	        public DummyTabFactory(Context context) {
	            mContext = context;
	        }
	
	        @Override
	        public View createTabContent(String tag) {
	            View v = new View(mContext);
	            v.setMinimumWidth(0);
	            v.setMinimumHeight(0);
	            return v;
	        }
	    }
	    
	    
        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(Class<?> clss, int    nameResId, int  drawableResId){
    		TabSpec tabSpec = mTabHost.newTabSpec(clss.getName());
    		View indicator = LayoutInflater.from(mActivity).inflate(R.layout.home_tab_indicator, null, false);
    		TextView tx = (TextView)indicator.findViewById(R.id.title);
    		tx.setText(nameResId);
    		Drawable top = mActivity.getResources().getDrawable(drawableResId);
    		top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
    		tx.setCompoundDrawables(null, top , null, null);
    		tabSpec.setIndicator(indicator);
    		tabSpec.setContent(new DummyTabFactory(mActivity));
    		mTabHost.addTab(tabSpec);
        }
        
	    
		@Override
		public void onTabChanged(String tabId) {
			if(mLastTabTag == tabId){
				return;
			}
			
			FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (mLastTabTag != null) {
            	Fragment fg = fm.findFragmentByTag(mLastTabTag);
            	if(fg != null){
            		ft.detach(fg);
            	}
            }
            
        	Fragment fg = fm.findFragmentByTag(tabId);
        	if(fg == null){
        		fg = Fragment.instantiate(mActivity, tabId);
        		ft.add(mContainerId, fg, tabId);
        	}else{
        		ft.attach(fg);
        	}
            
            mLastTabTag = tabId;
            ft.commit();
            fm.executePendingTransactions();
		}
    
	}
	
}
