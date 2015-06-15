package com.hoyo.paobar.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hoyo.paobar.R;
import com.hoyo.paobar.service.po.BarInfo;
import com.hoyo.paobar.ui.adapter.ArrayAdapter;
import com.hoyo.paobar.volley.LruImageCache;
import com.hoyo.paobar.volley.VolleyManager;

class GoogleCardsAdapter extends ArrayAdapter<BarInfo> {

		private Context mContext;
		private ImageLoader mImageLoader;
		
		public GoogleCardsAdapter(Context context) {
			mContext = context;
			mImageLoader = VolleyManager.getImageLoader();
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.view_card_listview_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
				view.setTag(viewHolder);

				viewHolder.imageView = (NetworkImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.textView.setText("This is card " + (position + 1));
			viewHolder.imageView.setErrorImageResId(R.drawable.img_nature2);
			viewHolder.imageView.setImageUrl(getItem(position).getPicurl(), mImageLoader);

			return view;
		}

		private static class ViewHolder {
			TextView textView;
			NetworkImageView imageView;
		}
	}