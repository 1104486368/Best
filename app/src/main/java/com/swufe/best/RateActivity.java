package com.swufe.best;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showOut);

    }
    public void Onclick(View btn) {
        //获取当前用户输入内容
        String str = rmb.getText().toString();
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
        if (btn.getId() == R.id.btn_dollar) {
            //强制转换成浮点型后面加f
            float val = r * (1 / 6.7f);
            show.setText(String.valueOf(val));
        } else if (btn.getId() == R.id.btn_euro) {
            float val = r * (1 / 11f);
            show.setText(val + "");
        } else {
            float val = r * 500;
            show.setText(String.valueOf(val));
        }
    }

        public void openOne(View btn){
            //打开一个页面Activity
            Log.i("open","openOne:");
            Intent hello = new Intent(this,SecondActivity.class);
            Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:13688447430"));
            startActivity(web);

        }

}
