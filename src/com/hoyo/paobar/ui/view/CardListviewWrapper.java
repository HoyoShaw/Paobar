package com.hoyo.paobar.ui.view;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hoyo.paobar.R;
import com.hoyo.paobar.service.po.BarInfo;
import com.hoyo.paobar.service.protocol.PaobarProtocol;
import com.hoyo.paobar.service.task.IServiceCallback;
import com.hoyo.paobar.service.task.IVolleyServiceCallback;
import com.hoyo.paobar.service.task.MessageHandler;
import com.hoyo.paobar.service.task.PaobarResponseHandler;
import com.hoyo.paobar.service.task.PaobarService;
import com.hoyo.paobar.ui.BarActivity;
import com.hoyo.paobar.ui.adapter.OnDismissCallback;
import com.hoyo.paobar.ui.adapter.SwingBottomInAnimationAdapter;
import com.hoyo.paobar.util.SettingTools;

/**
 * 卡片listview封装
 *
 * @author Hoyo
 *
 */
public class CardListviewWrapper extends RelativeLayout implements OnDismissCallback,IVolleyServiceCallback,OnItemClickListener{

	private static final int DEFAULT_PAGE_SIZE = 20;
	private RefreshListView mCardListView;
	private GoogleCardsAdapter mGoogleCardsAdapter;
	private SwingBottomInAnimationAdapter mSwingBottomInAnimationAdapter;
	private Context mContext;
	private PaobarService mService;
	private int mPageIndex = 0;
	public static final int LISTVIEWID = 1;
	private int mRequestPageSize = DEFAULT_PAGE_SIZE;
    private boolean mIsRefresh = false;

	public CardListviewWrapper(Context context) {
		this(context, null);
	}

	public CardListviewWrapper(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public CardListviewWrapper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		initView();
		bindData();

	}

	private void initView(){
		mCardListView = (RefreshListView)findViewById(R.id.id_view_card_list);
	}

	private void bindData(){
		mService = PaobarService.getInstance(mContext);
		mGoogleCardsAdapter = new GoogleCardsAdapter(mContext);
		mSwingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
		mSwingBottomInAnimationAdapter.setAbsListView(mCardListView);
		mCardListView.setAdapter(mSwingBottomInAnimationAdapter);
        mCardListView.setOnRefreshListener(mRefreshListener, LISTVIEWID);
        mCardListView.setOnItemClickListener(this);
		mCardListView.initRefresh();
	}

    private JSONObject buildJsonRequest(int pSize, int pIndex, int dis) {
		JSONObject json = new JSONObject();
		try {
			json.put("udid", "adsfadgdsgsdgwqrwe");
			json.put("key", "");
			json.put("city", "");
            json.put("gps", "116.45957833933,39.940153651506");
			json.put("dis",  ( dis != 0 ? dis : 0 ) + "");
            json.put("psize", pSize + "");
			json.put("pindex", pIndex + "");
			json.put("typ", "getbarList");
			json.put("sortType", "2,0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	private void doAction(JSONObject request){
		mService.doAction(new IServiceCallback(new MessageHandler(mContext.getMainLooper())) {

			@Override
			public void onFinish(JSONObject response) {

				handleFinish(response);
				//刷新
				mCardListView.onRefreshFinish();
				//保存本次网络刷新成功时间戳
				SettingTools.saveLong(SettingTools.UPDATED_AT + LISTVIEWID, System.currentTimeMillis());

			}

			@Override
			public void onError(VolleyError error) {
				handleError(error);
                mCardListView.onRefreshFinish();
			}
		}, request);
	}


	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			mGoogleCardsAdapter.remove(position);
		}
	}

	@Override
	public void handleFinish(JSONObject response) {
		PaobarResponseHandler<BarInfo> responseHandler = new PaobarResponseHandler<BarInfo>(PaobarProtocol.SERVICE_TYPE_GETBARLIST);
		List<BarInfo> oneRequestData = responseHandler.handleResponse(response);
		Log.d("krui", "oneRequestData = " + oneRequestData.toString());
		if (oneRequestData.size() > 0) {
            if (mIsRefresh) {
                mGoogleCardsAdapter.clearDataOnly();
            }
			mGoogleCardsAdapter.addAll(oneRequestData);//添加到数据源中
		}
	}


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

	private OnRefreshListener mRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			Log.d("krui", "mPageIndex = " + mPageIndex);
            //下拉刷新，请求整个数据

			mPageIndex = 0;

            mIsRefresh = true;

            doAction(buildJsonRequest(mRequestPageSize, mPageIndex++, 0));
		}

		@Override
		public void onLoadMoring() {
			Log.d("krui", "mPageIndex = " + mPageIndex);
            mIsRefresh = false;
            doAction(buildJsonRequest(mRequestPageSize, mPageIndex++, 0));
		}
	};

	@Override
	public void handleError(VolleyError error) {
        if (error.errorCode == VolleyError.ERROR_NETWORK) {
            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_LONG).show();
        }
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BarInfo barInfo = mGoogleCardsAdapter.getItem(position - 1);
		Intent intent = new Intent(mContext, BarActivity.class);
		intent.putExtra("bar", barInfo);
		mContext.startActivity(intent);
	}

}
