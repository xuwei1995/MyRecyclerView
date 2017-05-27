package xuwei.com.myrecyclerview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import xuwei.com.myrecyclerview.impl.MultiItemTypeSupport;
import xuwei.com.myrecyclerview.view.ViewHolder;

/**
 * Created by Xu Wei on 2017/5/27.
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>
{
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;
   private List<T> mDatas;
    private Context mContext;
    public MultiItemCommonAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport)
    {
        super(context,-1, datas);
        mMultiItemTypeSupport = multiItemTypeSupport;
        this.mDatas=datas;
        this.mContext=context;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        ViewHolder holder = ViewHolder.get(mContext, parent, layoutId);
        return holder;
    }

}