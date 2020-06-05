package com.swufe.best;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements AdapterView.OnItemClickListener {
    Handler handler;
    // 存放文字、图片信息
    private ArrayList<HashMap<String, String>> listItems;
    // 适配器
    private SimpleAdapter listItemAdapter;
    private int msgWhat = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);


        Thread t = new Thread((Runnable) this); // 创建新线程
        t.start(); // 开启线程

        }





    public void run() {
        Log.i("thread","run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();

        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 6; i < tds.size(); i+=6) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);
                String tdStr = td.text();
                String pStr = td2.text();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", tdStr);
                map.put("ItemDetail", pStr);

                rateList.add(map);
                Log.i("td",tdStr + "=>" + pStr);
            }
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage();
        msg.what = msgWhat;
        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");



    }






        private void initListView() {
            listItems = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < 10; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                // 标题文字
                map.put("ItemTitle", "Rate： " + i);
                // 详情描述
                map.put("ItemDetail", "detail" + i);
                listItems.add(map);
            }
            // 生成适配器的Item和动态数组对应的元素
                       // listItems数据源
            listItemAdapter = new SimpleAdapter(this, listItems,
                    // ListItem的XML布局实现
                    R.layout.list_item,
                    new String[] { "ItemTitle", "ItemDetail" },
                    new int[] { R.id.itemTitle, R.id.itemDetail }
            );








    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
