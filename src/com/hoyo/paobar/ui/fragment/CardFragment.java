package com.hoyo.paobar.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.hoyo.paobar.R;
import com.hoyo.paobar.ui.view.CardListviewWrapper;
import com.hoyo.paobar.ui.view.CommonTopBar;

public class CardFragment extends Fragment implements AMapLocationListener {

    private CardListviewWrapper mCardWrapper;
    private CommonTopBar mTitleBar;
    private LocationManagerProxy mLocationManagerProxy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cardlist, null);
        mCardWrapper = (CardListviewWrapper) root.findViewById(R.id.card);
        mTitleBar = (CommonTopBar) root.findViewById(R.id.main_title_bar);
        return root;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLocation();
    }

    /**
     * 初始化定位服务
     */
    private void initLocation() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
        //      mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();

        //移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), amapLocation.getLatitude() + ",\n" + amapLocation.getLongitude(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
