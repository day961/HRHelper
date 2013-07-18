package com.uqute.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
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
	
	public static final int MENU_PWD_BACK = 1;
	public static final int MENU_HELP = 2;
	public static final int MENU_EXIT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题窗体
		setContentView(R.layout.login);
		/**启动窗体设置*/
		initView();
	}
	
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
                if ((edit_usr.getText().toString().isEmpty()) || ("".equals(edit_usr.getText().toString()))) {
                    Toast.makeText(getBaseContext(), "请输入正确用户名以便登陆", Toast.LENGTH_SHORT).show();

                } else {
                    if (edit_pass.getText().toString().isEmpty() || ("".equals(edit_pass.getText().toString()))) {
                        Toast.makeText(getBaseContext(), "请输入您的密码", Toast.LENGTH_SHORT).show();
                    } else {
                        //连网并验证成功，转入登陆
                        //相关函数
                        boolean result = login(edit_usr.getText().toString(), edit_pass.getText().toString());
                        Toast.makeText(getBaseContext(), "示例：登陆返回信息"+result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**
         * 记住密码选项----复选框
         *
         */
        checkbox_rp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox_rp.isChecked()) {
                    if ("".equals(edit_usr.getText().toString())) {
                        Toast.makeText(getBaseContext(), "请输入正确用户名以便登陆", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(edit_pass.getText().toString())) {
                            Toast.makeText(getBaseContext(), "请输入您的密码", Toast.LENGTH_SHORT).show();
                        } else {
                            //检测用户及密码入框正确后进行用户密码记录
                            save_rm(edit_usr.getText().toString(), edit_pass.getText().toString());
                            checkbox_rp.setChecked(true);
                        }
                    }
                } else {
                    if (!("".equals(edit_usr.getText().toString())) && ("".equals(edit_pass.getText().toString()))) {
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
                    if((!"".equals(edit_usr.getText().toString()))||(!edit_usr.getText().toString().isEmpty())){
                        edit_pass.setText(getpass(edit_usr.getText().toString()));
                        checkbox_rp.setChecked(true);
                    }
                }
            }
        });
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_more:
			showMoreMenu(isShowMenu);
			break;
		case R.id.btn_login_regist:
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
		// TODO Auto-generated method stub
		menu.add(0, MENU_PWD_BACK, 1, "密码找回").setIcon(R.drawable.menu_findkey);
		menu.add(0,MENU_HELP,2,"帮助").setIcon(R.drawable.menu_setting);
		menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_PWD_BACK:
			break;
		case MENU_HELP:
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

    public String getpass(String name) {
        SharedPreferences sp;
        String passwd, user;
        sp = getSharedPreferences(name, Context.MODE_PRIVATE);
        user = sp.getString("name", "");
        passwd = sp.getString("pswd", "");
        return passwd;
    }

    /**
     * 登陆验证函数
     *（rewrote by 嘉明）
     */
    protected boolean login(String strUID,String strUPW) {
       /*Demo登陆
          * 账号:david
          * 密码:1234
          */
        /*创建新进程用于连接网络进行登陆验证*/
       checkingThread checkingThread = new checkingThread(strUID, strUPW);
       checkingThread.start();

        /*阻塞主线程等待登陆验证结束（严重的界面进程卡死风险！！！！）*/
       while (checkingThread.isAlive()) {
           try {
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       System.out.println("在主函数 Thread NO." + Thread.currentThread().getId() + ".连接状态:" + checkingThread.status);
       return checkingThread.status;

//       // pc pc登录验证是否成功
//       String result = null;
//       // 发送http请求,传递参数
//       // 获取http返回状态,根据返回状态在界面提示
//       // web服务器封装并返回一定格式数据对象
//       // http状态返回正常,取出并解析数据
//       // 解析服务器返回的数据显示或存储在本地
//       String queryUrl = "http://day961.uqute.com/API/Login/index.php?username="
//               + user + "&pwd=" + pass;
//       System.out.println("url==>" + queryUrl);
//       HttpPost request = new HttpPost(queryUrl);
//       try {
//           HttpResponse response = new DefaultHttpClient()
//                   .execute(request);
//
//           if (response.getStatusLine().getStatusCode() == 200) {
//               result = EntityUtils.toString(response.getEntity());
//           }
//       } catch (ClientProtocolException e) {
//           // TODO Auto-generated catch block
//           e.printStackTrace();
//       } catch (IOException e) {
//           // TODO Auto-generated catch block
//           e.printStackTrace();
//       }
//       return result; //返回页面信息,成功success,失败fail
    }

    class checkingThread extends Thread {
        private String TAG = "HTTP_DEBUG";
        private String strUID, strUPW;

        public checkingThread(String strUID, String strUPW) {
            this.strUID = strUID;
            this.strUPW = strUPW;
        }

        private boolean status = false;//储存登陆状态

        @Override
        public void run() {
            String uriAPI = "https://day961.uqute.com/API/Login/index1.php";//登陆验证网页
            String strRet = "";

            try {
                Looper.prepare();
                System.out.println("进入判断函数 Thread NO." + Thread.currentThread().getId());

                DefaultHttpClient httpclient = (DefaultHttpClient) getNewHttpClient();
                //DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                HttpPost httpPost = new HttpPost(uriAPI);

                List<NameValuePair> nvpl = new ArrayList<NameValuePair>();
                nvpl.add(new BasicNameValuePair("uid", strUID));
                nvpl.add(new BasicNameValuePair("upw", strUPW));

                HttpEntity httpentity = new UrlEncodedFormEntity(nvpl, HTTP.UTF_8);
                httpPost.setEntity(httpentity);

                response = httpclient.execute(httpPost);//执行登陆信息传递，将结果返回

                /*判断连接是否成功，HttpStatus.SC_OK表示连接成功*/
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    System.out.println("请求成功 Thread NO." + Thread.currentThread().getId());
                } else {
                    System.out.println("请求错误 Thread NO." + Thread.currentThread().getId());
                }

                HttpEntity entity = response.getEntity();

                Log.d(TAG, "HTTP POST getStatusLine:" + response.getStatusLine());
                strRet = EntityUtils.toString(entity);
                Log.i(TAG, strRet);
                strRet = strRet.trim().toLowerCase();//strRet存放验证结果(trim清除空格,toLowerCase变小写)

                /*取得Cookie内容*/
                List<Cookie> cookies = httpclient.getCookieStore().getCookies();

                if (entity != null) {
                    entity.consumeContent();
                }

                Log.d(TAG, "HTTP POST Initialize of cookies.");
                cookies = httpclient.getCookieStore().getCookies();
                if (cookies.isEmpty()) {
                    Log.d(TAG, "HTTP POST Cookie not found.");
                    Log.i(TAG, entity.toString());
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        Log.d(TAG, "HTTP POST Found Cookie:" + cookies.get(i).toString());
                    }
                }
                if (strRet.equals("y")) {
                    Log.i("TEST", "YES");
                    status = true;
                    System.out.println("验证通过   Thread NO." + Thread.currentThread().getId());
                } else {
                    Log.i("TEST", "NO");
                    status = false;
                    System.out.println("验证错误   Thread NO." + Thread.currentThread().getId());
                }
                stop();//强制线程退出
                Looper.loop();
                //super.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("进入异常   Thread NO." + Thread.currentThread().getId() + e.getMessage());
            }
            super.run();
        }
    }

    /*信任所有证书以通过服务器的https*/
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
