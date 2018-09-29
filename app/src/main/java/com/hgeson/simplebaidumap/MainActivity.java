package com.hgeson.simplebaidumap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hgeson.simplebaidumap.utils.AddressBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private RecyclerView recyclerView;
    private TextView tv;
    private EditText searchAddressEt;
    private GeoCoder geoCoder;

    private boolean isFirstLoc = true;

    private List<PoiInfo> poiInfos = new ArrayList<>();
    private List<PoiInfo> lists = new ArrayList<>();
    private AddressBean bean;

    public MyLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;

    private BaseQuickAdapter<PoiInfo,BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null){
            getActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        findView();
        init();
        setListener();
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.address_recycler);
        tv = (TextView) findViewById(R.id.tv);
        searchAddressEt = (EditText) findViewById(R.id.edit_search);
    }

    private void init() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start(); // 调用此方法开始定位

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<PoiInfo, BaseViewHolder>(R.layout.item_new_info_address,null) {
            @Override
            protected void convert(BaseViewHolder helper, final PoiInfo item) {
                helper.setText(R.id.choose_address_name,item.name)
                        .setText(R.id.choose_address,item.address)
                        .getView(R.id.select_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bean = new AddressBean(item.city,item.name,item.address,item.phoneNum,item.uid,item.postCode,
                                String.valueOf(item.location.longitude),String.valueOf(item.location.latitude));
//                        String info = JSON.toJSONString(bean);
                        String info = "城市：" + bean.getCity() + "\n"
                                + "名称：" + bean.getName() + "\n"
                                + "地址：" + bean.getAddress() + "\n"
                                + "经度：" + bean.getLongitude() + "\n"
                                + "纬度：" + bean.getLatitude();
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(info);
                    }
                });
            }
        });
    }

    private void setListener() {
        searchAddressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void search() {
        String etName = searchAddressEt.getText().toString().trim();
        poiInfos.clear();

        for (int i = 0; i < lists.size(); i++) {
            PoiInfo info = lists.get(i);

            String strName = info.name + "";
            //原理很简单,只要检索的字符,被数据库包含,即可展示出来
            if (info.name.contains(etName) || strName.contains(etName)) {
                poiInfos.add(info);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ptCenter = new LatLng(latitude, longitude);
                Log.e("TAG", "latitude = " + latitude + ",longitude = " + longitude);
                getdata(ptCenter);
            }
        }
    }

    private void getdata(LatLng ptCenter) {
        geoCoder = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseResult) {
                if (reverseResult == null || reverseResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                    Log.e("TAG", "error = " + reverseResult.error.toString());
                    Toast.makeText(MainActivity.this, reverseResult.error.toString(), Toast.LENGTH_SHORT).show();

                }
                List<PoiInfo> list = new ArrayList<>();
                list.clear();
                list = reverseResult.getPoiList();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        poiInfos.add(list.get(i));
                        lists.add(list.get(i));
                        Log.e("TAG", "name = " + list.get(i).name + "address" + list.get(i).address);
                    }
                    adapter.setNewData(poiInfos);
//                    adapter.setItem(poiInfos);
                }
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter).newVersion(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
    }
}
