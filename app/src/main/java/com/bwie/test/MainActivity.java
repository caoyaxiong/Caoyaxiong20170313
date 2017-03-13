package com.bwie.test;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    protected static final int WHAT_INCREASE = 1;

    /**
     * 圆形带数字的进度条
     */
    private CircleNumberProgress cnp_citcleNumberProgress;

    /**
     * 指定给进度条的进程
     */
    private int progress;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress++;
            cnp_citcleNumberProgress.setProgress(progress);
            handler.sendEmptyMessageDelayed(WHAT_INCREASE, getRadomNumber(50, 300));
            if (progress >= 100) {
                handler.removeMessages(WHAT_INCREASE);
            }
        }

    };
    private Button btn_numberProgressBar;
    private Button replys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cnp_citcleNumberProgress = (CircleNumberProgress) findViewById(R.id.cnp_citcleNumberProgress);
        btn_numberProgressBar = (Button) findViewById(R.id.btn_numberProgressBar);
        replys = (Button) findViewById(R.id.replys);
        
        
       
        btn_numberProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = 0;
                handler.sendEmptyMessage(WHAT_INCREASE);
            }
        });
        replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = 0;
                cnp_citcleNumberProgress.setProgress(progress);
                handler.removeMessages(WHAT_INCREASE);
            }
        });
    }

    

    /**
     * 得到两个整数之间的一个随机数
     *
     * @param start 较小的数
     * @param end   较大的数
     * @return 随机整数
     */
    public int getRadomNumber(int start, int end) {
        return (int) (start + Math.random() * (end - start));
    }
}

