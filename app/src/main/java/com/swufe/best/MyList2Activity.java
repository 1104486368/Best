package com.swufe.best;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyList2Activity extends ListActivity {
    Handler handler;
    // 存放文字、图片信息
    private ArrayList<HashMap<String, String>> listItems;
    // 适配器
    private SimpleAdapter listItemAdapter;
    private int msgWhat = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
