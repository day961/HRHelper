package com.uqute.helper;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import java.util.HashMap;

public class MainTab_MainActivity extends TabActivity implements OnCheckedChangeListener{
//    private MenuDrawer mDrawer;

    private HashMap<String, String>session;

	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
//	private Intent mEIntent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maintab_activity);

        //获取intent
        session =  (HashMap<String, String>)
                this.getIntent().getBundleExtra("session").getSerializable("sessionid");
        //读取session的基本信息，并显示相应的控件
        String flag = session.get("s_flag");
        String userid_info=session.get("s_userid");
        String username_info=session.get("s_username");
        String session_id=session.get("s_sessionid");

        this.mAIntent = new Intent(this,MainTab_AActivity.class);
        this.mBIntent = new Intent(this,MainTab_BActivity.class);
        this.mCIntent = new Intent(this,MainTab_CActivity.class);
        this.mDIntent = new Intent(this,MainTab_DActivity.class);
//        this.mEIntent = new Intent(this,MainTab_EActivity.class);

        mAIntent.putExtra("username_info",username_info);
        mAIntent.putExtra("userid_info",userid_info);

        //设置监听器
		((RadioButton) findViewById(R.id.radio_button0))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3))
		.setOnCheckedChangeListener(this);
//        ((RadioButton) findViewById(R.id.radio_button4))
//		.setOnCheckedChangeListener(this);
        
        setupIntent();
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
//			case R.id.radio_button4:
//				this.mTabHost.setCurrentTabByTag("MORE_TAB");
//				break;
			}
		}
	}
	
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", R.string.main_home,
				R.drawable.home_icon_1_n, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.main_news,
				R.drawable.home_icon_2_n, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", R.string.main_manage_date,
                R.drawable.home_icon_4_n, this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_friends,
				R.drawable.home_icon_5_n, this.mDIntent));

//		localTabHost.addTab(buildTabSpec("MORE_TAB", R.string.more,
//				R.drawable.home_icon_5_n, this.mEIntent));

	}
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}
}