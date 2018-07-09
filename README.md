# FlowLayout
Android 流式布局FlowLayout 实现关键字标签

[![](https://img.shields.io/badge/JitPack.io-v1.4-green.svg)](https://jitpack.io/#alidili/FlowLayout)

## 效果图

![FlowLayout](https://upload-images.jianshu.io/upload_images/3270074-83aafecef0611ca4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/308)

## 使用方法

- **在项目根目录的build.gradle文件中加入如下代码**

```
maven { url "https://jitpack.io" }
```

![jitpack](https://upload-images.jianshu.io/upload_images/3270074-b462d52412d723a8?imageMogr2/auto-orient/strip%7CimageView2/2/w/425)

- **在app根目录的buil.gradle文件中加入依赖**

```
compile 'com.github.alidili:FlowLayout:v1.4'
```

![加入依赖](https://upload-images.jianshu.io/upload_images/3270074-5e69850e095a952f?imageMogr2/auto-orient/strip%7CimageView2/2/w/459)

- **在Activity中使用，设置点击事件**

```
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

        // 设置文字大小
        flKeyword.setTextSize(15);
        // 设置文字颜色
        flKeyword.setTextColor(Color.BLACK);
        // 设置文字背景
        flKeyword.setBackgroundResource(R.drawable.bg_frame);

        // 设置文字水平margin
        flKeyword.setHorizontalSpacing(15);
        // 设置文字垂直margin
        flKeyword.setVerticalSpacing(15);

        // 设置文字水平padding
        flKeyword.setTextPaddingH(15);
        // 设置文字垂直padding
        flKeyword.setTextPaddingH(8);

        // 设置UI与点击事件监听
	// 最后调用setFlowLayout方法
        flKeyword.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

- **布局文件**

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.yang.flowlayoutlibrary.FlowLayout
        android:id="@+id/fl_keyword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp"
        android:layout_marginTop="10dp"
        app:backgroundResource="@drawable/bg_frame"
        app:horizontalSpacing="15dp"
        app:itemColor="@color/colorAccent"
        app:itemSize="15sp"
        app:textPaddingH="15dp"
        app:textPaddingV="8dp"
        app:verticalSpacing="15dp" />

</RelativeLayout>
```

## License

```
Copyright (C) 2018 YangLe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
