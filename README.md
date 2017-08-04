# FlowLayout
Android 流式布局FlowLayout 实现关键字标签

[![](https://img.shields.io/badge/JitPack.io-v1.1-green.svg)](https://jitpack.io/#alidili/FlowLayout)

## 效果图

![FlowLayout](http://img.blog.csdn.net/20161011110254828)

## 使用方法

- **在项目根目录的build.gradle文件中加入如下代码**

```
maven { url "https://jitpack.io" }
```

![jitpack](http://img.blog.csdn.net/20161017154320407)

- **在app根目录的buil.gradle文件中加入依赖**

```
compile 'com.github.alidili:FlowLayout:v1.1'
```

![加入依赖](http://img.blog.csdn.net/20161017154348181)

- **在Activity中使用，设置点击事件**

```
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
        app:itemColor="@color/colorAccent"
        app:itemSize="15sp" />

</RelativeLayout>
```

## License

```
Copyright (C) 2017 YangLe

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
