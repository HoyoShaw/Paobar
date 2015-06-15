package com.hoyo.paobar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.hoyo.paobar.R;
import com.hoyo.paobar.service.po.BarInfo;
import com.hoyo.paobar.ui.view.ParallaxScollListView;
import com.hoyo.paobar.volley.VolleyManager;

public class BarActivity extends Activity {

	private ParallaxScollListView mListView;
	private NetworkImageView mImageView;
	private BarInfo mBarInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parallax);

		Intent intent = getIntent();

		if (intent == null) {
			return;
		}

		mBarInfo = (BarInfo) intent.getSerializableExtra("bar");

		mListView = (ParallaxScollListView) findViewById(R.id.layout_listview);
		View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
		mImageView = (NetworkImageView) header.findViewById(R.id.layout_header_image);
		mImageView.setDefaultImageResId(R.drawable.img_nature5);
		mImageView.setImageUrl(mBarInfo.getPicurl(),VolleyManager.getImageLoader());
		mListView.setParallaxImageView(mImageView);
		mListView.addHeaderView(header);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, new String[] {
						"First Item", "Second Item", "Third Item",
						"Fifth Item", "Sixth Item", "Seventh Item",
						"Eighth Item", "Ninth Item", "Tenth Item", "....."

				});
		mListView.setAdapter(adapter);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mListView.setViewsBounds(ParallaxScollListView.ZOOM_X2);
		}
	}

}
