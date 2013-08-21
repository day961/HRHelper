package com.uqute.helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.uqute.ImageLoader.Constants;
import com.uqute.menudrawer.MenuDrawer;

import java.util.HashMap;

//这是我使sherlockactivity继承了tabactivity
public class MainTab_MainActivity extends /*TabActivity*/ SherlockActivity implements OnCheckedChangeListener,View.OnClickListener{
    //滑动菜单变量
    private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
    private MenuDrawer mDrawer;
//    private MenuAdapter mAdapter;
    private ListView mList;
    private int mActivePosition = -1;
    private String mContentText;
    private TextView mContentTextView;
    private int mActiveViewId;

    //Session登陆保持变量
    private HashMap<String, String>session;

    //tabhost变量
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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.maintab_activity);

        //设置左滑菜单
        if (savedInstanceState != null) {
            mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
        }
        mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
        mDrawer.setContentView(R.layout.maintab_activity);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mDrawer.setMenuView(R.layout.maintabmenu_left);
//        mDrawer.peekDrawer();//第一次运行时滑动菜单会飘出来提示用户

        findViewById(R.id.item1).setOnClickListener(this);
        findViewById(R.id.item2).setOnClickListener(this);
        findViewById(R.id.item3).setOnClickListener(this);
        findViewById(R.id.item4).setOnClickListener(this);
        findViewById(R.id.item5).setOnClickListener(this);
        findViewById(R.id.item6).setOnClickListener(this);


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
        mAIntent.putExtra("username_info",username_info);//用户名
        mAIntent.putExtra("userid_info",userid_info);//用户ID
        mAIntent.putExtra(Constants.Extra.IMAGES, Constants.IMAGES);//图片url

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
        
        setupIntent();//初始化底部的TAB
        setupActionBar(username_info);//初始化ActionBar
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

    private void setupActionBar(String title){
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//显示自带的tab
//        String[] arrStrings = getResources().getStringArray(R.array.sections);
//        COUNT = arrStrings.length;
//        for (int i = 0; i < COUNT; i++) {
//            ActionBar.Tab tab =  actionBar.newTab();
//            tab.setCustomView(getTabView(arrStrings[i]));
//            tab.setTabListener(this);
//            actionBar.addTab(tab);
//        }
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setLogo(R.drawable.biz_pics_main_back_normal);
        //设置actionBar标题
        actionBar.setTitle("用户:" + title);
        //设置下拉菜单
        // 生成一个SpinnerAdapter
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.MORE, android.R.layout.simple_spinner_dropdown_item);
        // 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为ActionBar设置下拉菜单和监听器
        actionBar.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            /**
             * This method is called whenever a navigation item in your action bar
             * is selected.
             *
             * @param itemPosition Position of the item clicked.
             * @param itemId       ID of the item clicked.
             * @return True if the event was handled, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                return false;
            }
        });
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

    /**当actionbar上的项目被点击时*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击actionbar图标时
            case android.R.id.home:
                mDrawer.toggleMenu();
                return true;
            //点击more图标
            case R.id.menu_more:
                Toast.makeText(getBaseContext(), "点击更多按钮", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**在创建Optionsmenu填充actionbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_topmenu, menu);
        return true;

    }

    /**在tabActivity中拦截子activity的按键事件*/
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

    /**点击左滑动菜单时*/
    @Override
    public void onClick(View v) {
        mDrawer.setActiveView(v);
//        mContentTextView.setText("Active item: " + ((TextView) v).getText());
        Toast.makeText(getBaseContext(),
                "点击左滑菜单"+((TextView) v).getText()+((TextView) v).getId(), Toast.LENGTH_SHORT).show();
        mDrawer.closeMenu();
        mActiveViewId = v.getId();
    }
}