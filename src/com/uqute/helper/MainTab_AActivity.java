package com.uqute.helper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.uqute.ImageLoader.BaseListViewActivity;
import com.uqute.ImageLoader.Constants;
import com.uqute.ImageLoader.ImagePagerActivity;
import com.uqute.XListView.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainTab_AActivity extends BaseListViewActivity implements XListView.IXListViewListener{
    DisplayImageOptions options;//图片加载器选项
    String[] imageUrls;

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

        Bundle bundle = getIntent().getExtras();
        imageUrls = getIntent().getExtras().getStringArray(Constants.Extra.IMAGES);//获取url

        // 配置图片加载及显示选项
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)//设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)//设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))//设置图片的显示方式 (设置圆角图片)
                .build();


        TextView tt = (TextView)findViewById(R.id.TabA_topText);
        tt.setText("用户名：" + getIntent().getStringExtra("username_info"));

        TextView bt = (TextView)findViewById(R.id.TabA_bottomText);
        bt.setText("用户信息：" + getIntent().getStringExtra("userid_info"));

        geneItems();//添加初始的items
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

//        mListView.setAdapter(mAdapter);//原来的mlistview适配器
        mListView.setAdapter(new ItemAdapter());
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position - 1);//因为mlistview从1开始
            }
        });
	}
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_POSITION, pager.getCurrentItem());
//    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(Constants.Extra.IMAGES, imageUrls);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        startActivity(intent);
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

    //imageloader adapter
    class ItemAdapter extends BaseAdapter {

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private class ViewHolder {
            public TextView text;
            public ImageView image;
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            //当该item的图片和文字第一次加载时
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.image = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);//设置数据??
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.text.setText("Item " + (position + 1));

            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */
            imageLoader.displayImage(imageUrls[position], holder.image, options, animateFirstListener);
            return view;
        }
    }

    //当图片第一次加载的监听事件(渐变显示动画)??
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
