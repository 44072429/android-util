package util.android.ys.com.androidutil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import util.android.ys.com.util.HttpClientUtil;


public class MainActivity extends AppCompatActivity {

    private String message = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String obj = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this, obj, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, obj, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, obj, Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.Btn1, R.id.Btn2, R.id.Btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Btn1:
                Toast.makeText(MainActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Btn2:

                Toast.makeText(MainActivity.this, "aaaa", Toast.LENGTH_SHORT).show();

                Map test = new ArrayMap<String, String>();
                test.put("topic" , "appservice/checkversion");
                test.put("dtoMessage" , "{     \"devid\": \"d0d8641757b930d5\",\"package\": \"com.abjc.abp.abp_abjc\",     \"versionCode\": 122,     \"versionName\": \"1.0.0.122nx\" }");

                HttpClientUtil.post2("http://192.168.21.126:8080/abp/aa/index.jhtml", test, new HttpClientUtil.HttpClientEventListener() {
                    @Override
                    public void onSuccess(int status_code, byte[] bytes) {
                        Message mes = new Message();
                        mes.what = 1;
                        mes.obj = "成功";
                        handler.sendMessage(mes);
                    }

                    @Override
                    public void onFailure(int status_code, byte[] bytes) {
                        Message mes = new Message();
                        mes.what = 2;
                        mes.obj = "失败";
                        handler.sendMessage(mes);
                    }

                    @Override
                    public void onTimeout() {
                        Message mes = new Message();
                        mes.what = 3;
                        mes.obj = "超时";
                        handler.sendMessage(mes);
                    }
                }, 40000);

                break;
            case R.id.Btn3:
                Toast.makeText(MainActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
