package com.uqute.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uqute.util.SSLSocketFactoryEx; //自定义ssl用于信任网站证书连接https

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**登陆界面activity*/
public class LoginActivity extends Activity implements OnClickListener{
	/**更多登陆项的显示View*/
	private View view_more;
	/**更多菜单，默认收起，点击后展开，再点击收起*/
	private View menu_more;
	private ImageView img_more_up;//更多登陆项箭头图片
	private Button btn_login_regist,btn_login;//注册按钮 及登陆按钮
    private CheckBox checkbox_rp;
    private EditText edit_usr,edit_pass;//登陆用户及密码输入框
	/**更所登陆项的菜单是否展开，默认收起*/
	private boolean isShowMenu = false;
    /**信息处理及cookies*/
    Handler loginMsgHandler;//消息处理Handler --嘉明
    private HashMap<String, String> MainSession =new HashMap<String, String>();//登陆保持重点！SESSION--嘉明
    /** 登录loading提示框 */
    private ProgressDialog proDialog;

	public static final int MENU_PWD_BACK = 1;
	public static final int MENU_HELP = 2;
	public static final int MENU_EXIT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题窗体
		setContentView(R.layout.login);

        /**实现handler消息处理及更新UI -90%
         * 判断认证
         * created by 嘉明
         * */
        loginMsgHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String chk =msg.getData().getString("s_flag");
                System.out.println("FLAG:" + chk);
                proDialog.dismiss();//“请稍后”对话框消失
                if(chk.equals("success")){
                    Toast.makeText(getBaseContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    //登陆成功后进入下一个页面
                    Intent intent = new Intent(LoginActivity.this, MainTab_MainActivity.class);//TODO MainActivity暂时没用
                    //传递session参数,在用户登录成功后为session初始化赋值,即传递HashMap的值
                    Bundle map = new Bundle();
                    MainSession.put("s_flag", msg.getData().getString("s_flag"));
                    MainSession.put("s_userid", msg.getData().getString("s_userid"));
                    MainSession.put("s_username", msg.getData().getString("s_username"));
                    MainSession.put("s_sessionid", msg.getData().getString("s_sessionid"));
                    map.putSerializable("sessionid", MainSession);
                    intent.putExtra("session", map);
                    startActivity(intent); // 跳转到成功页面
                    //页面切换效果
                    overridePendingTransition(R.anim.translucent_enter, R.anim.translucent_exit);
//                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
		/**启动窗体设置*/
		initView();
	}

    /**窗体设置*/
	private void initView(){
        edit_usr=(EditText)this.findViewById(R.id.et_Num);
        edit_pass=(EditText)this.findViewById(R.id.et_Pwd);
		img_more_up = (ImageView) findViewById(R.id.img_more_up);
		btn_login_regist = (Button) findViewById(R.id.btn_login_regist);
        btn_login=(Button)this.findViewById(R.id.btn_login);
        checkbox_rp=(CheckBox)this.findViewById(R.id.checkbox_rp);
		btn_login_regist.setOnClickListener(this);
		
		menu_more = findViewById(R.id.menu_more);
		view_more =  findViewById(R.id.view_more);
		view_more.setOnClickListener(this);
        /**
         * 按钮-登陆按钮
         *
         */
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                proDialog = ProgressDialog.show(LoginActivity.this, "连接中..",
                        "登陆中..请稍后....", true, true);
                if ((edit_usr.getText().toString().isEmpty()) ||
                        ("".equals(edit_usr.getText().toString()))) {
                    proDialog.dismiss();//“请稍后”对话框消失
                    Toast.makeText(getBaseContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                } else {
                    if (edit_pass.getText().toString().isEmpty() ||
                            ("".equals(edit_pass.getText().toString()))) {
                        proDialog.dismiss();//“请稍后”对话框消失
                        Toast.makeText(getBaseContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        //开始转入登陆
                        //由此处进入认证处理（耗时操作）--嘉明
                        login(edit_usr.getText().toString(), edit_pass.getText().toString());
                    }
                }
            }
        });

        /**
         * 记住密码选项----复选框
         *TODO:记住账号密码（也要记住账号）
         */
        checkbox_rp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox_rp.isChecked()) {
                    if ("".equals(edit_usr.getText().toString())) {
                        Toast.makeText(getBaseContext(),"请输入用户名", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(edit_pass.getText().toString())) {
                            Toast.makeText(getBaseContext(),"请输入密码", Toast.LENGTH_SHORT).show();
                        } else {
                            //检测用户及密码入框正确后进行用户密码记录
                            save_rm(edit_usr.getText().toString(), edit_pass.getText().toString());
                            checkbox_rp.setChecked(true);
                        }
                    }
                } else {
                    if (!("".equals(edit_usr.getText().toString())) &&
                            ("".equals(edit_pass.getText().toString()))) {
                        if (!checkbox_rp.isChecked()) {
                            clean_rm(edit_usr.getText().toString());
                            checkbox_rp.setChecked(false);
                        }
                    }
                }
            }
        });

        /**
         * 输入密码框-判断当有用户记录时自动获取密码
         *
         */
        edit_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edit_pass.isFocused()){
                    if((!"".equals(edit_usr.getText().toString()))||
                            (!edit_usr.getText().toString().isEmpty())){
                        edit_pass.setText(getpass(edit_usr.getText().toString(),1));
                        checkbox_rp.setChecked(true);
                    }
                }
            }
        });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_more:
            //TODO:更多登陆选项
			showMoreMenu(isShowMenu);
			break;
		case R.id.btn_login_regist:
            //TODO:注册账号页面
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            break;
        }
    }

    /**
	 * 展示更多菜单的方法,boolean的默认值是false
	 * @param show
	 */
	public void showMoreMenu(boolean show){
		if(show){//如果菜单不展开的时候
			menu_more.setVisibility(View.GONE);
			img_more_up.setImageResource(R.drawable.login_more_up);
			isShowMenu = false;
		}else{//菜单展开的时候
			menu_more.setVisibility(View.VISIBLE);
			img_more_up.setImageResource(R.drawable.login_more);
			isShowMenu = true;
		}
	}

    /**创建系统功能菜单*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_PWD_BACK, 1, "密码找回").setIcon(R.drawable.menu_findkey);
		menu.add(0,MENU_HELP,2,"帮助").setIcon(R.drawable.menu_setting);
		menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case MENU_PWD_BACK:
            //TODO：密码找回
			break;
		case MENU_HELP:
            //TODO：帮助
			break;
		case MENU_EXIT:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    /**
     * 记住密码选项的相关函数
     *
     */
    public boolean save_rm(String name, String pswd) {
        boolean flag = false;
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//对数据进行编辑
        editor.putString("name", name);
        editor.putString("pswd", pswd);
        flag = editor.commit();//将数据持久化到存储介质中
        return flag;
    }

    public boolean clean_rm(String name) {
        boolean flag = false;
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//对数据进行编辑
        editor.clear();
        editor.commit();
        return flag;
    }

    public String getpass(String name,int i) {
        SharedPreferences sp;
        String passwd;
        sp = getSharedPreferences(name, Context.MODE_PRIVATE);
        passwd = sp.getString("pswd", "");
        return passwd;
    }

    /**
     * 登陆验证函数（入口）
     *（rewrote by 嘉明）
     *
     */
    protected void login(String strUID,String strUPW) {
        /**创建新进程用于连接网络进行登陆验证*/
        checkingThread checkingThread = new checkingThread(strUID, strUPW);
        checkingThread.start();//线程启动
        System.out.println("进入认证处理函数");
    }

    /**
     * 登陆验证线程 (更新成session认证)
     *（created by 嘉明）80%
     *
     */
    class checkingThread extends Thread {
        private String strUID, strUPW;
        private List<Cookie> cookies;
        private Message m = loginMsgHandler.obtainMessage();//传递登陆认证的返回消息
        private Bundle bundle = new Bundle();//消息内容

        public checkingThread(String strUID, String strUPW) {
            this.strUID = strUID;//参数传入
            this.strUPW = strUPW;
        }
        @Override
        public void run() {
            DefaultHttpClient mHttpClient = (DefaultHttpClient) getNewHttpClient();//带SSLHttpClient
//            DefaultHttpClient mHttpClient = new DefaultHttpClient(); //不带SSL HttpClient
            HttpPost mPost = new HttpPost("https://day961.uqute.com/API/Login/login.php");

            //设置post参数
            List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            pairs.add(new BasicNameValuePair("username", strUID));
            pairs.add(new BasicNameValuePair("password", strUPW));

            try {
                mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                Looper.prepare();
                System.out.println("进入判断函数 Thread NO." + Thread.currentThread().getId());
                HttpResponse response = mHttpClient.execute(mPost);//HttpClient执行Post请求
                int res = response.getStatusLine().getStatusCode();
                if (res == 200) {//200表示请求成功
                    HttpEntity entity = response.getEntity();//得到请求实体

                    if (entity != null) {
                        String info = EntityUtils.toString(entity);
                        System.out.println("反馈内容:"+info);

                        cookies = mHttpClient.getCookieStore().getCookies();//得到cookies
                        if (cookies.isEmpty()) {
                            System.out.println("HTTP POST Cookie not found.");
                        } else {
                            for (int i = 0; i < cookies.size(); i++) {
                                System.out.println("HTTP POST Found Cookie:" + cookies.get(i).toString());
                            }
                        }

                        //以下主要是对服务器端返回的数据进行解析
                        JSONObject jsonObject=null;
                        //flag为登录成功与否的标记,从服务器端返回的数据
                        String flag="";
                        String name="";
                        String userid="";
                        String sessionid="";
                        try {
                            jsonObject = new JSONObject(info);
                            flag = jsonObject.getString("flag");
                            name = jsonObject.getString("name");
                            userid = jsonObject.getString("userid");
                            sessionid = jsonObject.getString("sessionid");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //把从服务器返回的内容打包（装入bundle）
                        bundle.putString("s_flag",flag);
                        bundle.putString("s_userid", userid);
                        bundle.putString("s_username", name);
                        bundle.putString("s_sessionid", sessionid);
                        m.setData(bundle);
                        loginMsgHandler.sendMessage(m);//发送到主线程
                    }
                }
                stop();//强制线程退出
                Looper.loop();
                //super.run();
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("进入异常   Thread NO." + Thread.currentThread().getId() + e.getMessage());
            }
            super.run();
        }
    }

    /**
     * 初始化自定义HttpClient
     *（created by 嘉明）
     *
     * 作用：信任所有证书以通过服务器的https*/
    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
