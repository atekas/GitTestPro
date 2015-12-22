package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwentao on 2015/12/22.
 */
public class LocationService {

    private LocationManager mLocationManager;
    private Context mContext;

    private OnLocationListener mOnLocationListener;
    private OnLocationFinish mOnLocationFinish;

    public LocationService(Context context, OnLocationFinish onLocationFinish) {
        mContext = context;
        mOnLocationFinish = onLocationFinish;

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//无海拔要求
        criteria.setBearingRequired(false);//无方位要求
        criteria.setCostAllowed(true);//允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        String provider = mLocationManager.getBestProvider(criteria, true);

        mOnLocationListener = new OnLocationListener();
        if (provider != null) {
            mLocationManager.requestLocationUpdates(provider, 0, 0, mOnLocationListener);
        } else {
            PromptUtils.showToast("无法定位");
        }
    }

    private class OnLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(mContext);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        String name = address.getAdminArea();
                        List<String> list = new ArrayList<String>();
                        list.add(name);
                        list.add(String.valueOf(latitude));
                        list.add(String.valueOf(longitude));
                        mOnLocationFinish.getLocationInfo(list);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            mLocationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    public interface OnLocationFinish {
        public void getLocationInfo(List<String> stringList);
    }
}
