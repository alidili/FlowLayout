package com.yang.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yang.flowlayoutlibrary.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowLayout flKeyword = findViewById(R.id.fl_keyword);

        // 关键字集合
        List<String> list = new ArrayList<>();
        list.add("关键词一");
        list.add("关键词二");
        list.add("关键词三");
        list.add("关键词四");
        list.add("关键词五");

        // 设置TextView水平margin
        flKeyword.setHorizontalSpacing(15);
        // 设置TextView垂直margin
        flKeyword.setVerticalSpacing(15);
        // 设置TextView水平padding
        flKeyword.setTextPaddingH(15);
        // 设置TextView垂直padding
        flKeyword.setTextPaddingH(8);

        // 设置UI与点击事件监听
        flKeyword.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
