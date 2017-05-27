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