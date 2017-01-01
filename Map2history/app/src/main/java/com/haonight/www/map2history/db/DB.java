package com.haonight.www.map2history.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.haonight.www.map2history.mo.MoChaoDaiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHANGLIKUN on 16/12/30.
 * 高度封装的数据库操作API
 */

public class DB {

    static MyDataBaseHelper helper = null;

    private static MyDataBaseHelper _instance(Context context){
        if(helper == null){
            helper = new MyDataBaseHelper(context);
        }
        return helper;
    }

    public static List<MoChaoDaiInfo> getChaoDaiList(Context context){
        Cursor cursor = _instance(context).getReadableDatabase().rawQuery(
                "select * from DbHistoryCountryList",null
        );

        if(cursor==null){
            return null;
        }

        List<MoChaoDaiInfo>  list= new ArrayList<>();

        while (cursor.moveToNext()){
            Log.v("cursor",""+cursor.getString(1));
            MoChaoDaiInfo info = new MoChaoDaiInfo();
            info.setChaoDaiA(cs(cursor,"ChaoDaiA"));
            info.setChaoDaiB(cs(cursor,"ChaoDaiB"));
            info.setChaoDaiC(cs(cursor,"ChaoDaiC"));
            info.setQiZhiNianDai(cs(cursor,"QiZhiNianDai"));
            info.setStartYear(ci(cursor,"StartYear"));
            info.setEndYear(ci(cursor,"EndYear"));
            info.setDuCheng(cs(cursor,"DuCheng"));
            info.setAddressNow(cs(cursor,"AddressNow"));
            info.setTongZhiMinZu(cs(cursor,"TongZhiMinZu"));
            info.setKaiGuoYuanLao(cs(cursor,"KaiGuoYuanLao"));

            list.add(info);
        }
        return list;
    }


    private static String cs(Cursor c, String field){
        return c.getString(c.getColumnIndex(field));
    }

    private static int ci(Cursor c, String field){
        return c.getInt(c.getColumnIndex(field));
    }

}
