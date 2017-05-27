package xuwei.com.myrecyclerview.impl;

/**
 * Created by Xu Wei on 2017/5/27.
 */

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
