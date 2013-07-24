package com.uqute.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;

import java.net.CookieManager;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suoday on 13-7-19.
 * 登陆成功后页面
 */
public class MainActivity extends Activity {
    private HashMap<String, String>session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
