package com.uqute.helper;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;

import java.net.CookieManager;
import java.util.HashMap;
import java.util.List;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by suoday on 13-7-19.
 * 登陆成功后页面
 */
public class MainActivity extends TabActivity implements OnCheckedChangeListener {
    private HashMap<String, String>session;

    private TabHost mTabHost;
    private Intent mAIntent;
    private Intent mBIntent;
    private Intent mCIntent;
    private Intent mDIntent;
    private Intent mEIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        //获取intent
        session =  (HashMap<String, String>)
                this.getIntent().getBundleExtra("session").getSerializable("sessionid");
        //读取session的基本信息，并显示相应的控件
        String flag = session.get("s_flag");
        String userid_info=session.get("s_userid");
        String username_info=session.get("s_username");
        String session_id=session.get("s_sessionid");
        //显示相应的内容到控件

        TextView userid_show=(TextView)findViewById(R.id.userid_show);
        userid_show.setText(userid_info);
        TextView username_show=(TextView)findViewById(R.id.username_show);
        username_show.setText(username_info);
        TextView sessionid_show=(TextView)findViewById(R.id.sessionid_show);
        sessionid_show.setText(session_id);

        //根据本次session再次获取用户信息
        Button getInfo=(Button)findViewById(R.id.getinfo);
//        getInfo.setOnClickListener(getInfoClick);
*/


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maintab_activity);

        this.mAIntent = new Intent(MainActivity.this,MainTab_AActivity.class);
        this.mBIntent = new Intent(MainActivity.this,MainTab_BActivity.class);
        this.mCIntent = new Intent(MainActivity.this,MainTab_CActivity.class);
        this.mDIntent = new Intent(MainActivity.this,MainTab_DActivity.class);
        this.mEIntent = new Intent(MainActivity.this,MainTab_EActivity.class);

        //设置监听器
        ((RadioButton) findViewById(R.id.radio_button0))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button4))
                .setOnCheckedChangeListener(this);

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
                case R.id.radio_button4:
                    this.mTabHost.setCurrentTabByTag("MORE_TAB");
                    break;
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
                R.drawable.home_icon_3_n, this.mCIntent));

        localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_friends,
                R.drawable.home_icon_4_n, this.mDIntent));

        localTabHost.addTab(buildTabSpec("MORE_TAB", R.string.more,
                R.drawable.home_icon_5_n, this.mEIntent));

    }

    private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
                                         final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
                getResources().getDrawable(resIcon)).setContent(content);
    }

    /**登陆保持重点：利用SESSION*/
//    View.OnClickListener getInfoClick=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            if(getUserInfo()){
//                Context context = v.getContext();
//                Intent intent = new Intent(context,
//                        GetUserInfoActivity.class);
//                //传递session参数,在用户登录成功后为session初始化赋值,即传递HashMap的值
//                Bundle map = new Bundle();
//                map.putSerializable("sessionid", session);
//                intent.putExtra("session", map);
//                context.startActivity(intent); // 跳转到成功页面
//
//            }else{
//                Toast.makeText(v.getContext(), "数据为空！", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    };
}
