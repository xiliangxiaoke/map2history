package com.haonight.www.map2history;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.haonight.www.map2history.db.DB;
import com.haonight.www.map2history.mo.MoChaoDaiInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    MapView mapView;
    RecyclerView listView;
    List<MoChaoDaiInfo> listData = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        MapView.setMapCustomEnable(true);
        setMapCustomFile(this);
        setContentView(R.layout.activity_main);

        initView();

        initMap();

        initData();
    }

    private void initData() {
        listData = DB.getChaoDaiList(this);
        //init country list
        adapter = new MyRecyclerViewAdapter();
        listView.setAdapter(adapter);
    }

    private void setMapCustomFile(Context context) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/custom_config.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_config.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(moduleName + "/custom_config.txt");
    }

    private void initMap() {
        BaiduMap mBaiduMap = mapView.getMap();

        //普通地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

//        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//
//        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);


        mapView.showZoomControls(false);


    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.bmapView);
        listView = (RecyclerView) findViewById(R.id.country_list);
        listView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listView.setItemAnimator(new DefaultItemAnimator());
    }


    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chaodai, parent, false);

            return new MyViewHolder(view);
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MoChaoDaiInfo info = listData.get(position);
            MyViewHolder mvh = (MyViewHolder) holder;
            if (info.getChaoDaiA().equals(info.getChaoDaiB()) && info.getChaoDaiB().equals(info.getChaoDaiC())) {
                mvh.chaodaia.setVisibility(View.GONE);
                mvh.chaodaib.setVisibility(View.GONE);
                mvh.chaodaic.setVisibility(View.VISIBLE);
                mvh.chaodaic.setBackgroundColor(giveColor(3, true));
                mvh.chaodaic.setText(info.getChaoDaiC());

            } else if (info.getChaoDaiA().equals(info.getChaoDaiB()) || info.getChaoDaiB().equals(info.getChaoDaiC())) {
                mvh.chaodaia.setVisibility(View.VISIBLE);
                mvh.chaodaib.setVisibility(View.GONE);
                mvh.chaodaic.setVisibility(View.VISIBLE);
                mvh.chaodaia.setText(info.getChaoDaiA());
                mvh.chaodaic.setText(info.getChaoDaiC());
                if(position==0){
                    mvh.chaodaia.setBackgroundColor(giveColor(1,false));
                    mvh.chaodaib.setBackgroundColor(giveColor(2,false));
                }else{
                    if(info.getChaoDaiA().equals(listData.get(position-1).getChaoDaiA())){
                        mvh.chaodaia.setBackgroundColor(giveColor(1,false));
                        mvh.chaodaia.setText("");
                    }else{
                        mvh.chaodaia.setBackgroundColor(giveColor(1,true));
                    }

                }
                mvh.chaodaic.setBackgroundColor(giveColor(3,true));


            } else {
                mvh.chaodaia.setVisibility(View.VISIBLE);
                mvh.chaodaib.setVisibility(View.VISIBLE);
                mvh.chaodaic.setVisibility(View.VISIBLE);
                mvh.chaodaia.setText(info.getChaoDaiA());
                mvh.chaodaib.setText(info.getChaoDaiB());
                mvh.chaodaic.setText(info.getChaoDaiC());

                if(position==0){
                    mvh.chaodaia.setBackgroundColor(giveColor(1,false));
                    mvh.chaodaib.setBackgroundColor(giveColor(2,false));
                }else{
                    if(info.getChaoDaiA().equals(listData.get(position-1).getChaoDaiA())){
                        mvh.chaodaia.setBackgroundColor(giveColor(1,false));
                        mvh.chaodaia.setText("");
                    }else{
                        mvh.chaodaia.setBackgroundColor(giveColor(1,true));
                    }

                    if(info.getChaoDaiB().equals(listData.get(position-1).getChaoDaiB())){
                        mvh.chaodaib.setBackgroundColor(giveColor(2,false));
                        mvh.chaodaib.setText("");
                    }else{
                        mvh.chaodaib.setBackgroundColor(giveColor(2,true));
                    }

                }
                    mvh.chaodaic.setBackgroundColor(giveColor(3,true));
            }

        }


        String[] colora = {"#996600", "#669900", "#666600", "#333300", "#cc6600"};
        String[] colorb = {"#993333", "#cc0000", "#ff0066", "#663366", "#9900cc"};
        String[] colorc = {"#009966", "#006699", "#003366", "#0033ff", "#6600cc"};
        int cidxa = 0;
        int cidxb = 0;
        int cidxc = 0;

        //生成一个用于背景的颜色

        /**
         * 拿一个颜色
         *
         * @param type   朝代a,b ,c的背景色
         * @param change 是否需要改变颜色
         * @return
         */
        private int giveColor(int type, boolean change) {
            String colorstr = "#000000";
            if (type == 1) {
                if (change) {
                    cidxa++;
                }
                colorstr = colora[cidxa % 5];
            } else if (type == 2) {
                if (change) {
                    cidxb++;
                }
                colorstr = colorb[cidxb % 5];
            } else if (type == 3) {
                if (change) {
                    cidxc++;
                }
                colorstr = colorc[cidxc % 5];
            }

            return Color.parseColor(colorstr);
        }

        @Override
        public int getItemCount() {
            if (listData == null) {
                return 0;
            }
            return listData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView chaodaia;
            TextView chaodaib;
            TextView chaodaic;

            public MyViewHolder(View itemView) {
                super(itemView);
                chaodaia = (TextView) itemView.findViewById(R.id.chaodaia);
                chaodaib = (TextView) itemView.findViewById(R.id.chaodaib);
                chaodaic = (TextView) itemView.findViewById(R.id.chaodaic);

            }
        }
    }
}
