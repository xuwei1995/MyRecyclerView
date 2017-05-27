# MyRecyclerView
### 利用hongyang 的RecyclerView万能适配器 和LRecyclerView 写的一个小小DEMO
##  先来示例图gif有点失帧

![img](https://github.com/xuwei1995/MyRecyclerView/blob/master/img/2.gif)
![img](https://github.com/xuwei1995/MyRecyclerView/blob/master/img/3.gif)
![img](https://github.com/xuwei1995/MyRecyclerView/blob/master/img/4.gif)



在project  gradle 

allprojects {
   
   repositories {
       
       jcenter()
      
      maven { url "https://jitpack.io" }
  
  }
}

在 Module gradle
compile 'com.github.jdsjlzx:LRecyclerView:1.4.3'


 监听LRecyclerView

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
        //上拉加载
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
      
      //适配器
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
                 });
              
                 }
                 
                 
                 
                 
                      
package xuwei.com.myrecyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import xuwei.com.myrecyclerview.view.ViewHolder;

/**
 * Created by Xu Wei on 2017/5/26.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }
    public CommonAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        convert(holder, mDatas.get(position) ,position);
    }

    public abstract void convert(ViewHolder holder, T t,int position);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
}
                 
                 
