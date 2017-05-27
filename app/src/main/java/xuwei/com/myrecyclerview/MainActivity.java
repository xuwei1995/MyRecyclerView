package xuwei.com.myrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.view.CommonHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xuwei.com.myrecyclerview.adapter.CommonAdapter;
import xuwei.com.myrecyclerview.bean.Info;
import xuwei.com.myrecyclerview.bean.Person;
import xuwei.com.myrecyclerview.util.Helper;
import xuwei.com.myrecyclerview.util.NetworkUtils;
import xuwei.com.myrecyclerview.view.MyRefreshView;
import xuwei.com.myrecyclerview.view.SampleHeader;
import xuwei.com.myrecyclerview.view.SwipeMenuView;
import xuwei.com.myrecyclerview.view.ViewHolder;

public class MainActivity extends AppCompatActivity {
    static  List<Integer> noRead=new ArrayList<>();
    List<Info> list=new ArrayList<>();
    LRecyclerView lRecyclerView;
    CommonAdapter commonAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 26;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;
    boolean isRefresh=false;
    String[] imageUrl;

 //   MyRefreshView myRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageUrl= Helper.IMAGEURL.split(",");
        for (int i = 0; i < 10; i++) {
            Info info=new Info();
            info.title="ITEM  "+i;
            info.ImageUrl=imageUrl[i];
            list.add(info);
        }
        mCurrentCounter=10;
        lRecyclerView= (LRecyclerView) findViewById(R.id.list);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));

      /*  DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();*/
     commonAdapter=    new CommonAdapter<Info>(this,R.layout.item,list) {



         @Override
            public void convert(ViewHolder holder, final Info info, final int position) {

                View contentView = holder.getView(R.id.swipe_content);
                TextView tv = holder.getView(R.id.title);

                //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
                ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);
                ((SwipeMenuView)holder.itemView).setIos(false);
                tv.setText(info.title + (position % 2 == 0 ? "我只能向左滑动" : "我只能向右滑动"));
                TextView title2= holder.getView(R.id.title2);
                 title2.setText(info.ImageUrl);
                Button btnDelete = holder.getView(R.id.btnDelete);
                Button btnUnRead = holder.getView(R.id.btnUnRead);
                Button btnTop = holder.getView(R.id.btnTop);
                 ImageView icon=holder.getView(R.id.icon);
             Glide.with(getApplicationContext()).load(info.ImageUrl)
                     .asBitmap()
                     .centerCrop()
                     .error(R.drawable.error)
                     .placeholder(R.drawable.timg)
                     .into(icon);
             /*   for (int i = 0; i <noRead.size() ; i++) {
                    if(position==noRead.get(i))
                        isReadImg.setVisibility(View.VISIBLE);

                }*/
                //隐藏控件
                btnUnRead.setVisibility(position % 3 == 0 ? View.GONE : View.VISIBLE);
              //  btnUnRead.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    list.remove(position);
                        commonAdapter.notifyItemRemoved(position);

                            //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                            //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                            //((CstSwipeDelMenu) holder.itemView).quickClose();
                          //  mOnSwipeListener.onDel(position);
                        if(position != (list.size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                            commonAdapter.notifyItemRangeChanged(position,list.size() - position);
                        }
                    }
                });
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Toast.makeText(mContext, info.title,Toast.LENGTH_SHORT).show();
                        //   AppToast.makeShortToast(mContext,"我在Adapter中执行");
                        Log.d("TAG", "onClick() called with: v = [" + v + "]");
                    }
                });
                btnTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       list.remove(position);
                        commonAdapter.notifyItemRemoved(position);
                        list.add(0, info);
                        commonAdapter.notifyItemInserted(0);


                        if(position != (list.size())){ // 如果移除的是最后一个，忽略
                            commonAdapter.notifyItemRangeChanged(0, list.size() - 1,"jdsjlzx");
                        }

                        lRecyclerView.scrollToPosition(0);
                    }
                });
                btnUnRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     /*   noRead.add(position);
                    commonAdapter.notifyDataSetChanged();*/
                    }
                });
            }


        };
        lRecyclerViewAdapter=new LRecyclerViewAdapter(commonAdapter);

  /*      // setRefreshHeader(mRefreshHeader);
        myRefreshView=new MyRefreshView(this);
        lRecyclerView.setRefreshHeader(myRefreshView);*/
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        lRecyclerView.addItemDecoration(divider);

       // CommonHeader headerView = new CommonHeader(this, R.layout.layout_home_header);
        lRecyclerViewAdapter.addHeaderView(new SampleHeader(this));//加自定义头部
        lRecyclerView.setRefreshProgressStyle(ProgressStyle.BallTrianglePath);//设置刷新样式
        lRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        lRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallTrianglePath);
        /**
         *onScrollUp()——RecyclerView向上滑动的监听事件；
         onScrollDown()——RecyclerView向下滑动的监听事件；
         onScrolled()——RecyclerView正在滚动的监听事件；
         onScrollStateChanged(int state)——RecyclerView正在滚动的监听事件；
         */

        lRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });
        //下拉刷新
     lRecyclerView .setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this,"刷新",Toast.LENGTH_SHORT).show();

                   commonAdapter.clear();
                lRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
              //  isRefresh = true;
                requestData();
            }
        });
        lRecyclerView.setLoadMoreEnabled(true);
        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    Toast.makeText(MainActivity.this,"加载"+mCurrentCounter,Toast.LENGTH_SHORT).show();
                    requestData();
                } else {
                    //the end
                    Toast.makeText(MainActivity.this,"到底了",Toast.LENGTH_SHORT).show();
                    lRecyclerView.   setNoMore(true);
                }
            }
        });
      // lRecyclerView.refresh();

    }

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
              mHandler.sendEmptyMessage(-1);
            //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(MainActivity.this)) {

                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
    private  Handler mHandler =new  Handler (){



        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:
                    int currentSize = commonAdapter.getItemCount();
                    //模拟组装10个数据
                    ArrayList<String> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }
                      newList.add("1");
                        Info info=new Info();
                        info.ImageUrl=imageUrl[currentSize+ i];
                        info.title="ITEM   "+(currentSize+ i);
                     list.add(info);
                    }
                    mCurrentCounter =list.size();
                    lRecyclerView.refreshComplete(REQUEST_COUNT);
                    notifyDataSetChanged();
                    break;
                case -2:
                   notifyDataSetChanged();
                    break;
                case -3:
                  lRecyclerView.refreshComplete(REQUEST_COUNT);
                   notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private void notifyDataSetChanged() {

        lRecyclerViewAdapter.notifyDataSetChanged();
    }


}
