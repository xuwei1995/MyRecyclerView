package xuwei.com.myrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.IRefreshHeader;

import xuwei.com.myrecyclerview.R;

/**
 * Created by Xu Wei on 2017/5/27.
 */

public class MyRefreshView extends RelativeLayout implements IRefreshHeader {
    View view;
    IRefreshHeader mRefreshHeader;
    public MyRefreshView(Context context) {
        super(context);
        init(context);
    }
    public MyRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context) {

      view=  inflate(context, R.layout.my_refresh, this);
    }
    @Override
    public void onReset() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onMove(float offSet, float sumOffSet) {

    }

    @Override
    public boolean onRelease() {
        return false;
    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getHeaderView() {

        return view;
    }

    @Override
    public int getVisibleHeight() {
        return view.getHeight();
    }
    /**
     * 设置自定义的RefreshHeader
     */
    public void setRefreshHeader(IRefreshHeader refreshHeader) {
        this.mRefreshHeader = refreshHeader;
    }
}
