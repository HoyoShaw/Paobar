package com.hoyo.paobar.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoyo.paobar.R;

/**
 * 顶部区域
 * @author hoyo
 *
 */
public class CommonTopBar extends RelativeLayout{
	
	private Button mLeftButton;
	private TextView mCenterView;
	private TextView mCenterViewRight;
	private Button mRightButton;
	private Context mContext;

	public CommonTopBar(Context context) {
		this(context,null);
	}
	
	public CommonTopBar(Context context, AttributeSet attrs) {
		this(context, attrs,-1);
	}
	
	public CommonTopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	private void initView(){
		View root = LayoutInflater.from(mContext).inflate(R.layout.view_titlebar, this,true);
		mLeftButton = (Button) root.findViewById(R.id.title_bar_left);
		mCenterView = (TextView) root.findViewById(R.id.title_bar_center);
		mCenterViewRight = (TextView) root.findViewById(R.id.title_bar_center_right);
		mRightButton = (Button) root.findViewById(R.id.title_bar_right);
	}
	

	public void setOnLeftBtnClickListener(OnClickListener listener){
		if (listener != null) {
			mLeftButton.setOnClickListener(listener);
		}
	}
	
	public void setOnRightBtnClickListener(OnClickListener listener){
		if (listener != null) {
			mRightButton.setOnClickListener(listener);
		}
	}
	
	public void setOnCenterViewClickListener(OnClickListener l){
		if (l != null) {
			mCenterView.setOnClickListener(l);
		}
	}

	public void setTitle(int resId){
		mCenterView.setText(mContext.getString(resId));
	}
	
	public void setTitle(String title){
		if (!TextUtils.isEmpty(title)) {
			mCenterView.setText(title);
		}
	}
	
	public void setTitleRight(String titleRight){
		if (!TextUtils.isEmpty(titleRight)) {
			mCenterViewRight.setText(titleRight);
		}
	}
}
