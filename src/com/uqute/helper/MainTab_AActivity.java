package com.uqute.helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uqute.XListView.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainTab_AActivity extends Activity implements XListView.IXListViewListener{

    private HashMap<String, String> session;//Session信息

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");//设置时间格式
    private String TAG = "test";

    private XListView mListView;//创建控件
    private ArrayAdapter<String> mAdapter;//创建adapter
    private ArrayList<String> items = new ArrayList<String>();//用于存放列表内容

    private int start = 0;
    private static int refreshCnt = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_activity_a);

//		tv.setGravity(Gravity.CENTER);
//		setContentView(tv);

        TextView tt = (TextView)findViewById(R.id.TabA_topText);
        tt.setText("用户名：" + getIntent().getStringExtra("username_info"));

        TextView bt = (TextView)findViewById(R.id.TabA_bottomText);
        bt.setText("用户信息：" + getIntent().getStringExtra("userid_info"));

        geneItems();//添加初始的items
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
	}

    //添加初始的items
    private void geneItems() {
        for (int i = 0; i != 10; ++i) {
            items.add("refresh cnt " + (++start));
        }
    }

    /**
     *重载上拉刷新
     */
    @Override
    public void onRefresh() {
        new GetDataTask().execute(0);//采用异步任务
    }

    /**
     *重载下拉加载
     */
    @Override
    public void onLoadMore() {
        new GetDataTask().execute(1);//采用异步任务
    }

    /**异步任务*/
    private class GetDataTask extends AsyncTask<Integer, Void, ArrayList<String> > {
        //doInBackground为异步任务内容 用于执行耗时操作
        @Override
        protected ArrayList<String> doInBackground(Integer... params) {
            try {
                //此处为耗时操作
                if(params[0]==0){
                    Log.d(TAG, "sleep 1s");
                    Thread.sleep(1000);
                    start = --refreshCnt;
                    items.clear();
                    geneItems();
                }
                if (params[0] == 1){
                    Thread.sleep(1000);
                    geneItems();
                }
            } catch (InterruptedException e) {
                Log.w(TAG, "0InterruptedException" + e.toString());
            }
            return items;//doInBackground的返回值会向onPostExecute传递
        }

        //onPostExecute为异步线程执行完后回到主线程后执行的内容
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            mAdapter.notifyDataSetChanged();//通知adapter列表更新
            //显示上次更新时间
            mListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
            mListView.stopRefresh();//通知更新完毕
            mListView.stopLoadMore();
            super.onPostExecute(result);
        }
    }

}
