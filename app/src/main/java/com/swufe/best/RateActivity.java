package com.swufe.best;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends AppCompatActivity implements Runnable {   //Runnable一个接口，指定一个Run方法

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;

    EditText rmb;
    TextView show;
    Handler handler;   //用于获取真实网络汇率数据，不允许时间开销过多的响应，因此多线程，不影响主线程

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);


        //获取SP里保存的数据
        //私有访问，文件名“myrate”
        //打开的时候从文件读取汇率
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE); //MODE_PRIVATE访问权限
        //新版高版本的sdk可以用SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);  //第二个参数用默认值
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);

        Log.i(TAG, "onCreate:sp dollarRate=" + dollarRate);
        Log.i(TAG, "onCreate:sp euroRate=" + euroRate);
        Log.i(TAG, "onCreate:sp wonRate=" + wonRate);


        //开启子线程，用于获取真实网络汇率数据，不允许时间开销过多的响应，因此多线程，不影响主线程
        Thread t = new Thread(this);  //一定要加上当前对象，去寻找Run方法，t就代表当前线程
        t.start();

        //两个线程之间的数据交换可以用handler方法，handler从msg消息队列去取
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG,"handleMessage:getMessage msg = "+ str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };

    }



    public void Onclick(View btn) {
        Log.i(TAG,"onClick:");

        //获取当前用户输入内容
        String str = rmb.getText().toString();
        Log.i(TAG,"onClick:get str=" + str);
        //给 r定义一个初值，使r不被定义在方法内
        float r = 0;
        //考虑用户输入为空的情况
        if (str.length() > 0) {
            //文本内容转成浮点型
            r = Float.parseFloat(str);
        } else {
            //提示用户输入内容
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        Log.i(TAG,"onClick:r="+r);
        //计算
        if (btn.getId() == R.id.btn_dollar) {
            //强制转换成浮点型后面加f
            show.setText(String.format("%.2f",r*dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            show.setText(String.format("%.2f",r*euroRate));
        } else {
            show.setText(String.format("%.2f",r*wonRate));
        }
    }
        //接收按钮事件，调用openConfig
        public void openOne(View btn){
            //打开一个页面Activity
            openConfig();

        }

    private void openConfig() {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne:dollarRate=" + dollarRate);
        Log.i(TAG,"openOne:euroRate=" + euroRate);
        Log.i(TAG,"openOne:wonRate=" + wonRate);

        //startActivity(config);
        startActivityForResult(config,1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            Intent config = new Intent(this,ConfigActivity.class);
            config.putExtra("dollar_rate_key",dollarRate);
            config.putExtra("euro_rate_key",euroRate);
            config.putExtra("won_rate_key",wonRate);

            Log.i(TAG,"openOne:dollarRate=" + dollarRate);
            Log.i(TAG,"openOne:euroRate=" + euroRate);
            Log.i(TAG,"openOne:wonRate=" + wonRate);

            //startActivity(config);
            startActivityForResult(config,1);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    //通过requestCode区分是谁产生的数据
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){

            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:dollarRate=" + dollarRate);
            Log.i(TAG,"onActivityResult:euroRate=" + euroRate);
            Log.i(TAG,"onActivityResult:wonRate=" + wonRate);


            //将新设置的汇率写到SP里，下一次启动的时候已经保存到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();  //改变数据需要一个editor对象
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已保存到sharedPreferences");

        }


        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        //for循环给他做延时处理
        for(int i = 1;i<6;i++){
            Log.i(TAG,"run:i = "+ i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //获取msg对象，用于返回主线程
        //Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "Hello from run()";
        //handler.sendMessage(msg);





        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run:html="+html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }





    }



    private  String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[]buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"UTF-8");
        for(;;){
            int rsz = in.read(buffer,0,buffer.length);
            if (rsz < 0 )
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();


    }
}







