package util.android.ys.com.androidutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


//    @OnClick({R.id.Btn1, R.id.Btn2, R.id.Btn3})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.Btn1:
//                break;
//            case R.id.Btn2:
//                break;
//            case R.id.Btn3:
//                break;
//        }
//    }

}
