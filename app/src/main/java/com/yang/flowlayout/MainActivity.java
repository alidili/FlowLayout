package com.yang.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flKeyword = (FlowLayout) findViewById(R.id.fl_keyword);

        List<String> list = new ArrayList<>();
        list.add("关键词一");
        list.add("关键词二");
        list.add("关键词三");
        list.add("关键词四");
        list.add("关键词五");
        flKeyword.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
