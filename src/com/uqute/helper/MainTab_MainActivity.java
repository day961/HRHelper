package com.uqute.helper;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.uqute.ImageLoader.Constants;
import com.uqute.menudrawer.MenuDrawer;

import java.util.HashMap;

public class MainTab_MainActivity extends TabActivity implements OnCheckedChangeListener,View.OnClickListener{

    private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
    private MenuDrawer mDrawer;
//    private MenuAdapter mAdapter;
    private ListView mList;
    private int mActivePosition = -1;
    private String mContentText;
    private TextView mContentTextView;
    private int mActiveViewId;


    private HashMap<String, String>session;

	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
//	private Intent mEIntent;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        ActionBar actionBar = this.getActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
//
//    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.maintab_activity);

        if (savedInstanceState != null) {
            mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
        }
        mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
        mDrawer.setContentView(R.layout.maintab_activity);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mDrawer.setMenuView(R.layout.maintabmenu_left);

        findViewById(R.id.item1).setOnClickListener(this);
        findViewById(R.id.item2).setOnClickListener(this);
        findViewById(R.id.item3).setOnClickListener(this);
        findViewById(R.id.item4).setOnClickListener(this);
        findViewById(R.id.item5).setOnClickListener(this);
        findViewById(R.id.item6).setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        mDrawer.peekDrawer();//第一次运行时滑动菜单会飘出来提示用户

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

        //放置intent内容
        mAIntent.putExtra("username_info",username_info);
        mAIntent.putExtra("userid_info",userid_info);
        mAIntent.putExtra(Constants.Extra.IMAGES, Constants.IMAGES);

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


    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.toggleMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //在tabActivity中拦截子activity的按键事件
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            final int drawerState = mDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                mDrawer.closeMenu();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
//    @Override
//    public void onBackPressed() {
//        final int drawerState = mDrawer.getDrawerState();
//        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
//            mDrawer.closeMenu();
//            return;
//        }
//        Toast.makeText(getBaseContext(), "BackPressed", Toast.LENGTH_SHORT).show();
//        super.onBackPressed();
//    }

    @Override
    public void onClick(View v) {
        mDrawer.setActiveView(v);
//        mContentTextView.setText("Active item: " + ((TextView) v).getText());
        mDrawer.closeMenu();
        mActiveViewId = v.getId();
    }
}