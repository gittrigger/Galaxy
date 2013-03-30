package com.havenskys.galaxy.activity;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.havenskys.galaxy.Custom;
import com.havenskys.galaxy.DataProvider;
import com.havenskys.galaxy.R;
import com.havenskys.galaxy.SqliteWrapper;
import com.havenskys.galaxy.SyncProcessing;

public class Dash extends Activity {

	private final static String TAG = "Galaxy";
	private SharedPreferences mSharedPreferences;
	private Editor mPreferencesEditor;
	private Context mContext;
	private TextView mTitle, mMail;
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		
        
    	dispatchTextView("Connection Status Enabled",100,2100,"onCreate() 44");
    	//remoteConnection("onCreate() 45");
    	dispatchRemoteConnection("onStart()",2000);
    	
    	Button findnow = (Button) findViewById(R.id.main_searchbutton);
    	findnow.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				dispatchButton("onDown 55","disable",100,0,R.id.main_searchbutton);
				mPreferencesEditor.putLong("findnow", System.currentTimeMillis());
				mPreferencesEditor.commit();
			}
				return false;
			}
    		
    	});
    	findnow.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if( arg1 ){
					
				}
			}
    		
    	});
    	
    	Button getnow = (Button) findViewById(R.id.getnow);
    	
    	getnow.setOnTouchListener(new OnTouchListener(){

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					dispatchButton("onClick 51","disable",100,0, R.id.getnow);
					//arg0.setVisibility(View.INVISIBLE);
					dispatchTextView("Get Now",100,2100,"onCreate() 44");
					//arg0.requestFocusFromTouch();
					mPreferencesEditor.putLong("getnow", System.currentTimeMillis() );
					mPreferencesEditor.commit();
					return true;
				}
				return false;
			}
    		
    	});
    	
	}
	private SyncProcessing mSyncProcessing;
	private final static int ACTIVITYCODE_CONFLOGIN = 99;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dash);
		
		mSharedPreferences = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
    	mPreferencesEditor = mSharedPreferences.edit();
    	mContext = this.getApplicationContext();
    	//mTitle = (TextView) findViewById(R.id.shell_title);
    	//mMail = (TextView) findViewById(R.id.mail_text);
    	//Intent datafold = new Intent();
    	//datafold.putExtra("com.havenskys.galaxy.who", "Dash 68");
    	//datafold.setClass(this, com.havenskys.galaxy.DataFold.class);
    	
    	//startService(datafold);
    	
    	String webaddress = mSharedPreferences.contains("webaddress") ? mSharedPreferences.getString("webaddress", "") : "";
    	String login = mSharedPreferences.contains("login") ? mSharedPreferences.getString("login", "") : "";
    	//boolean validlogin = mSharedPreferences.contains("validlogin") ? mSharedPreferences.getBoolean("validlogin", false) : false;
    	String password = mSharedPreferences.contains("password") ? mSharedPreferences.getString("password", "") : "";
    	
    	if( webaddress == "" || login == "" || password == "" ){
        	Intent configureLogin = new Intent();
        		configureLogin.setClass(this, ConfigureLogin.class);
        		
        	startActivityForResult(configureLogin,ACTIVITYCODE_CONFLOGIN);
		}
    	
	}
	private long mTextViewLast = 0;
	private void dispatchTextView(String whattext, long dl, long le, String who){
		Message m = new Message();
		Bundle b = new Bundle();
		b.putString("text", whattext);
		b.putString("who", "dispatchConnectionStatus() 51 for " + who);
		m.setData(b);long rt = System.currentTimeMillis();
		if( mTextViewLast > rt+dl ){
			mDispatchTextView.sendMessageDelayed(m, mTextViewLast - System.currentTimeMillis() + dl );
			mTextViewLast = rt + mTextViewLast - rt + dl + le;return;
		}else{
		mDispatchTextView.sendMessageDelayed(m, dl);}
		mTextViewLast = System.currentTimeMillis()+dl + le;
		
	}
	private long mButtonLast = 0;
	private void dispatchButton(String who, String action, long dl, long le, int viewid){
		Message m = new Message();
		Bundle b = new Bundle();
		b.putString("who", "dispatchGetnowButton() for "+who);
		b.putString("action", action);
		b.putInt("viewid", viewid);
		m.setData(b);
		long rt = System.currentTimeMillis(); if(mButtonLast > rt+dl){
			mDispatchButton.sendMessageDelayed(m, mButtonLast - rt + dl);
			mButtonLast = rt + mButtonLast - rt + dl + le;
		}else{
			mDispatchButton.sendMessageDelayed(m,dl);
			mButtonLast = rt + dl + le;
		}
	}
	
	private Handler mDispatchButton = new Handler() {
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			//String whattext = b.containsKey("text") ? b.getString("text"): "";
			String who = b.containsKey("who") ? b.getString("who"): "";
			String action = b.containsKey("action") ? b.getString("action") : "";
			int viewid = b.containsKey("viewid") ? b.getInt("viewid") : R.id.getnow;
			//dispatchConnectionStatus("Connection Engaged","mDRC 105");
			//remoteConnection("mDispatchGetnowButton for " +who);
			
			Button getnow = (Button) findViewById(viewid);//R.id.getnow);
			
			if( action == "visible" ){
				getnow.setVisibility(View.VISIBLE);
			}else if( action == "hidden" ){
				getnow.setVisibility(View.INVISIBLE);
			}else if( action == "disable" ){
				//getnow.clearFocus
				getnow.setEnabled(false);
			}else if( action == "enable" ){
				getnow.setEnabled(true);
			}else if( action.length() > 0 ){
				if( getnow.isEnabled() ){
					getnow.setText(action);
				}else{
					getnow.setEnabled(true);
					getnow.setText(action);
					getnow.setEnabled(false);
				}
			}
		}
	};
	
	private void dispatchRemoteConnection(String who, long dl){
		
		Message m = new Message();
		Bundle b = new Bundle();
		b.putString("who", "dispatchRemoteConnection() for "+who);
		m.setData(b);
		mDispatchRemoteConnection.sendMessageDelayed(m,dl);
	}
	
	private Handler mDispatchRemoteConnection = new Handler() {
		public void handleMessage(Message msg){
			
			dispatchButton("mDispatchMail 265","disable",100,0, R.id.getnow);
			dispatchButton("mDispatchMail 255","Login",100,2100, R.id.getnow);
			Bundle b = msg.getData();
			//String whattext = b.containsKey("text") ? b.getString("text"): "";
			String who = b.containsKey("who") ? b.getString("who"): "";
			dispatchTextView("Connection Engaged",100,2100,"mDRC 105");
			remoteConnection("mDispatchRemoteConnection for " +who);
		}
	};
	private void remoteConnection(final String who) {
		dispatchTextView("Connecting",100,2100,"onCreate() 44");
		
		// Background Query
		Thread remoteConnectionThread = new Thread(){

			
			//private EditText tSearch;
			//private String tSearchSaved;
			//private SharedPreferences tSharedPreferences;
			//private Editor tPreferencesEditor;
			
        	public void run(){
        		

        		//tSearch = (EditText) findViewById(R.id.main_search);
    			mSharedPreferences = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
    			mPreferencesEditor = mSharedPreferences.edit();

        		mSyncProcessing = new SyncProcessing(Dash.this);
                mSyncProcessing.setSharedPreferences(mSharedPreferences);
    			
        		//Log.w(TAG,"TypeWatcher Background Query() ========================");
        		
        		//SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        		
        		//String login = sharedPreferences.contains("login") ? sharedPreferences.getString("login", "") : "";
		        String search = mSharedPreferences.contains("search") ? mSharedPreferences.getString("search", "") : "";
		        String lastSearch = mSharedPreferences.contains("search") ? mSharedPreferences.getString("search", "") : "";
		        String lastSearchProcessed = "none";
		        
		        long lastTime = System.currentTimeMillis() - 900;
		        //DefaultHttpClient httpClient = null;
		        //mSyncProcessing = new SyncProcessing(Dash.this);
		        //mSyncProcessing.setSharedPreferences(sharedPreferences);
		        //long httpClientBirthday = System.currentTimeMillis() - (4*60*1000);
		        String webaddress = mSharedPreferences.contains("webaddress") ? mSharedPreferences.getString("webaddress", "") : "";
		    	String login = mSharedPreferences.contains("login") ? mSharedPreferences.getString("login", "") : "";
		    	String password = mSharedPreferences.contains("password") ? mSharedPreferences.getString("password", "") : "";
		    	int syncReview = mSharedPreferences.contains("syncreview") ? mSharedPreferences.getInt("syncreview",5) : 5;
		    	
		    	dispatchTextView("Login " + login,100,2100, "remoteConnection() 91 for "+who);
		    	int loginCode = mSyncProcessing.owaLogin("remoteConnection() 76 for "+who, webaddress, login, password);
		    	if( loginCode == -9 ){
		        	dispatchTextView("Service appears busy (replied with an empty page).",100,4100, "remoteConnection() 96 for "+who);
		        }else if( loginCode == -8 ){
		        	dispatchTextView("Service appears disabled by the System Administrators.",100,4100, "remoteConnection() 98 for "+who);
		        }else if( loginCode == -7 ){
		        	dispatchTextView("Generic HTTP client failure, failure could be due to network connectivity locally or between here and there.",100,4100, "remoteConnection() 100 for "+who);
		        }else if( loginCode == -6 ){
		        	dispatchTextView("Internet DNS unavailable.",100,4100, "remoteConnection() 102 for "+who);
		        }else if( loginCode == -5 ){
		        	dispatchTextView("Webmail version was determined correct but I wasn't able to understand the login reply.",100,4100, "remoteConnection() 104 for "+who);//, I'm looking for 
		        }else if( loginCode == -4 ){
		        	dispatchTextView("Webmail version isn't Microsoft Outlook Webmail (OWA 8), email support havenskys@gmail.com, mention 'Galaxy' in the subject.",100,4100, "remoteConnection() 106 for "+who);
		        }else if( loginCode == -3 ){
		        	dispatchTextView("One of the configuration values is empty.",100,4100, "remoteConnection() 108 for "+who);
		        }else if( loginCode == -2 ){
		        	dispatchTextView("Login/Password incorrect.",100,4100, "remoteConnection() 110 for "+who);
		        }else if( loginCode == -1 ){
		        	dispatchTextView("Login failed, missing expected login result.",100,4100, "remoteConnection() 112 for "+who);
		        }else if( loginCode > 0 ){
		        	dispatchTextView("Login " + loginCode,10,4100, "remoteConnection() 92 for "+who);
		        	//dispatchConnectionStatus("Login Good", "remoteConnection() 92 for "+who);
		        	//dispatchMail("remoteConnection() 116 for " + who);
		        	
		        		//dispatchConnectionStatus("Inbox "+httpPage, "rCT 109 for "+who);
		        	
		        }else{
		        	dispatchTextView("Login " + loginCode,100,4100, "remoteConnection() 94 for "+who);
		        }
		        
		    	
		    	workloop();
		    	
        	}
        	
        	public void workloop(){
        		long getnowlast = 0;
        		long getnow = 0;
        		long findnowlast = 0;
        		long findnow = 0;
        		for(;;){
        			SystemClock.sleep(50);
        			getnow = mSharedPreferences.getLong("getnow", 0);
        			if( getnow > getnowlast ){
        				dispatchMail("workloop() 193");
        				getnowlast = System.currentTimeMillis();
        				SystemClock.sleep(100);
        			}
        			findnow = mSharedPreferences.getLong("findnow", 0);
        			if( findnow > findnowlast ){
        				findnowlast = System.currentTimeMillis();
        			}
        			SystemClock.sleep(100);
        		}
        	}

		};
		//t.setDaemon(true);
		remoteConnectionThread.start();
	}
	
	private void dispatchMail(String who){
		//dispatchConnectionStatus("Get Mail","mDispatchMail 153 for "+who);
		dispatchButton("mDispatchMail 265","disable",100,0, R.id.getnow);
		dispatchButton("mDispatchMail 255","Mail",100,2100,R.id.getnow);
		Thread dispatchMail = new Thread(){
			public void run(){
				
			
				Message m = new Message();
				Bundle b = new Bundle();
				//b.putString("text", whattext);
				b.putString("who", "dispatchMail() 41");
				m.setData(b);
				mDispatchMail.sendMessageDelayed(m, 1900);
			}
		};
		dispatchMail.start();
	}
	
	private Handler mDispatchMail = new Handler() {
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			//String whattext = b.containsKey("text") ? b.getString("text"): "";
			String who = b.containsKey("who") ? b.getString("who"): "";
			
			//SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			String webaddress = mSharedPreferences.contains("webaddress") ? mSharedPreferences.getString("webaddress", "") : "";
	    	//String login = sharedPreferences.contains("login") ? sharedPreferences.getString("login", "") : "";
	    	//String password = sharedPreferences.contains("password") ? sharedPreferences.getString("password", "") : "";
	    	
	        
			String httpStatus = "";
			dispatchTextView("Checking Mail",100,2100,"mDispatchMail 153 for "+who);
        	httpStatus = mSyncProcessing.safeHttpGet("remoteConnectionThread 97 for"+who, new HttpGet(webaddress + "/OWA"));
        	dispatchButton("mDispatchMail 265","Get",100,2100, R.id.getnow);
        	dispatchButton("mDispatchMail 265","enable",100,0, R.id.getnow);
        	if( httpStatus.contains("440") ){
        		dispatchTextView("Mail Server Error\n"+httpStatus,100,4100, "mDispatchMail 156 for "+who);
        		
        	}else{
        	if( httpStatus.contains("200") ){
        		dispatchTextView("Mail Server Replied\n" + httpStatus,100,2100, "rCT 102 for "+who);
        		String httpPage = "";
        		httpPage = mSyncProcessing.getHttpPage().replaceAll("\n", "").replaceAll("\r","");
        		
        		if(httpPage.contains("<td class=\"cntnttp\">") ){
        		
        		dispatchTextView("Inbox in hand " + httpPage.length() + " bytes.",100,4100, "rCT 108 for "+who);
        		//httpPage = httpPage.replaceAll("\n", "");
        		//dispatchConnectionStatus("Inbox in hand " + httpPage.length() + " bytes. Mining.", "rCT 108 for "+who);
        		httpPage = httpPage.split("<td class=\"cntnttp\">")[1];
        		//httpPage = httpPage.replaceAll("</td></tr><tr><td","\n<td");
        		String[] ilist = httpPage.split("<tr>");
        		String item = "";String display = "";
        		int num = 0;//int max = (ilist.length > 10) ? 10 : ilist.length;
        		for(int i = 0; i < ilist.length; i++){
        			
        			if( ilist[i].contains("checkbox") ){
        				
        				item = ilist[i];
        				
        				//item = item.replaceAll("<.*?>", " ")
        				item = item.replaceAll("&.*?;"," ");
		        		//item = item.replaceAll(" +"," ");

        				String[] parts = item.split("</td>");
        				if( parts.length > 6 ){
        				display = "";
        				if( parts[0].contains("Priority") ){
        					display += "Priority\n";
        				}else{
        					if( parts[0].contains("class") ){
        						display = "[0] " + parts[0] + "\n";
	        				}
        				}
        				num++;
        				display +=  parts[1].replaceAll(".*alt=.","").replaceAll(".>.$","") + "\n";
        				if( parts[2].contains("Attachment") ){
        					display += "Has Attachment\n";
        				}
        				//display += "[2] " + parts[2] + "\n";
        				//display += "[3] id: " + parts[3] + "\n";
        				display +=  parts[4].replaceAll("<.*?>", "").replaceAll("&.*?;", "") + "\n";
        				display +=  parts[5].replaceAll("<.*?>", "").replaceAll("&.*?;", "") + "\n";
        				display +=  parts[6].replaceAll("<.*?>", "").replaceAll("&.*?;", "") + "\n";
        				display +=  parts[7].replaceAll("<.*?>", "").replaceAll("&.*?;", "") + "\n";
        				//dispatchConnectionStatus("Inbox " + num + "\n" +ilist[i], "rCT 109 for "+who);
        				//if( num < 10 )
        				{Message m2 = new Message();
        				Bundle b2 = new Bundle();
        				b2.putString("text", "Inbox " + num + "\n" + display);
        				b2.putString("who", "dispatchConnectionStatus() 51 for " + who);
        				m2.setData(b2);
        				mDispatchTextView.sendMessageDelayed(m2, 1100 * num);}
        				}
        				
        			}
        			
        			
        		
        			
        		}
        		}else{
        			dispatchTextView("Inbox Error\n" + httpPage,100,4100, "mDispatchMail 226 for "+who);
        			dispatchRemoteConnection("mDispatchMail 396",100);
        		}
        	}else{
        		dispatchTextView("Mail Server " + httpStatus,100,4100, "rCT 107 for "+who);
        	}
        	}
			
		}
	};
    
private void safeUpdateTitle(String who) {
    	
    	SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
    	boolean deepsearch = sharedPreferences.contains("deepsearch") ? sharedPreferences.getBoolean("deepsearch",false) : false;
    	String httpstatus = sharedPreferences.contains("httpstatus") ? sharedPreferences.getString("httpstatus", "") : "";
    	boolean validlogin = sharedPreferences.contains("validlogin") ? sharedPreferences.getBoolean("validlogin",false) : false;

		int count = sharedPreferences.contains("count") ? sharedPreferences.getInt("count", 0) : 0;
		String httpspeed = sharedPreferences.contains("httpspeed") ? sharedPreferences.getString("httpspeed", "") : "";
		try {
			Message msg = new Message();
			Bundle b = new Bundle();
				b.putBoolean("deepsearch", deepsearch);
				b.putInt("count", count);
				if( validlogin ){
					b.putString("httpstatus", httpstatus);
					b.putString("httpspeed", httpspeed);
				}else{
					b.putString("httpstatus", "");
					b.putString("httpspeed", "(login-failure)");
				}
				b.putString("who", TAG + " safeUpdateTitle() 600 for " + who);
				
			msg.setData(b);
			mTitleHandler.sendMessage(msg);
		} catch (android.util.AndroidRuntimeException e) {
			Log.w(TAG,"safeUpdateTitle() 605 Message Already taken. for " + who);
			e.printStackTrace();
		}

	}

private Handler mDispatchTextView = new Handler() {
	public void handleMessage(Message msg){
		Bundle b = msg.getData();
		String whattext = b.containsKey("text") ? b.getString("text"): "";
		String who = b.containsKey("who") ? b.getString("who"): "";
		TextView textv = (TextView) findViewById(R.id.connectionstatus_text);
		if(whattext == null){}else{textv.setText(whattext);}
		
	}
};
	
	private Handler mTitleHandler = new Handler() {
	    public void handleMessage(Message msg) {
	    	Bundle b = msg.getData();
	    	String httpstatus = b.containsKey("httpstatus") ? b.getString("httpstatus") : "";
	    	String httpspeed = b.containsKey("httpspeed") ? b.getString("httpspeed") : "";
	    	String who       = b.containsKey("who") ? b.getString("who") : "";
	    	//int count = sharedPreferences.contains("count") ? sharedPreferences.getInt("count", 0) : 0;
	    	boolean deepsearch = b.containsKey("deepsearch") ? b.getBoolean("deepsearch") : false;
	    	int count = b.containsKey("count") ? b.getInt("count") : 0;
	    	
			String title = "Galaxy";
			
			if( deepsearch ){
				title += " DeepSearch";
			}else{
				if( count > 0 ){
					title += " NameSearch";
				}
			}
			if( count > 0 ){
				title += " (" + count + ")";
			}
			
			if( httpstatus != "" ){
				title += " " + httpstatus;
			}else{
				if( count > 0 ){
					title += " Local";
				}
			}
			
			if( httpspeed != "" ){
				title += " " + httpspeed;
			}else{
				title += "";
			}
			
			Dash.this.setTitle(title);
			Log.w(TAG,"mTitleHandler 652 title("+title+") for " + who);
			//Toast.makeText(mContext, title, Toast.LENGTH_LONG).show();
	    }
    };

	public Dash() {
		// TODO Auto-generated constructor stub
	}

}
