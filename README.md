# FlowLayout
Android 流式布局FlowLayout 实现关键字标签

# **1.介绍** #

流式布局的应用还是很广泛的，比如搜索热词、关键词标签等，GitHub上已经有很多这样的布局了，但是还是想着自己实现一下，最近一直在学自定义控件，也巩固一下所学的知识。
本文实现的效果如下图所示：

![FlowLayout](http://img.blog.csdn.net/20161011110254828)

# **2.思路** #

- 继承自RelativeLayout，可以直接使用RelativeLayout中的相关属性，本文也可以修改为继承ViewGroup，并不会有什么影响。
- 在onMeasure方法中计算出所有childView的宽和高，然后根据childView的宽和高计算出布局自身的宽和高。
- 在onLayout方法中计算出所有childView的位置并进行布局。
- 封装Line对象，管理每行上的View对象

# **3.实现** #

## **初始化一些属性** ##

```
public class FlowLayout extends RelativeLayout {

    // 水平间距，单位为dp
    private int horizontalSpacing = dp2px(10);
    // 竖直间距，单位为dp
    private int verticalSpacing = dp2px(10);
    // 行的集合
    private List<Line> lines = new ArrayList<Line>();
    // 当前的行
    private Line line;
    // 当前行使用的空间
    private int lineSize = 0;
    // 关键字大小，单位为sp
    private int textSize = sp2px(15);
    // 关键字颜色
    private int textColor = Color.BLACK;
    // 关键字背景框
    private int backgroundResource = R.drawable.bg_frame;
    // 关键字水平padding，单位为dp
    private int textPaddingH = dp2px(7);
    // 关键字竖直padding，单位为dp
    private int textPaddingV = dp2px(4);

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FlowLayoutAttrs, defStyleAttr, 0);

        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.FlowLayoutAttrs_horizontalSpacing:
                    horizontalSpacing = typedArray.getDimensionPixelSize(attr, dp2px(10));
                    break;

                case R.styleable.FlowLayoutAttrs_verticalSpacing:
                    verticalSpacing = typedArray.getDimensionPixelSize(attr, dp2px(10));
                    break;

                case R.styleable.FlowLayoutAttrs_textSize:
                    textSize = typedArray.getDimensionPixelSize(attr, sp2px(15));
                    break;

                case R.styleable.FlowLayoutAttrs_textColor:
                    textColor = typedArray.getColor(attr, Color.BLACK);
                    break;

                case R.styleable.FlowLayoutAttrs_backgroundResource:
                    backgroundResource = typedArray.getResourceId(attr, R.drawable.bg_frame);
                    break;

                case R.styleable.FlowLayoutAttrs_textPaddingH:
                    textPaddingV = typedArray.getDimensionPixelSize(attr, dp2px(7));
                    break;

                case R.styleable.FlowLayoutAttrs_textPaddingV:
                    verticalSpacing = typedArray.getDimensionPixelSize(attr, dp2px(4));
                    break;
            }
        }
        typedArray.recycle();
    }
	
	...
}
```

## **onMeasure** ##

首先获取父容器传入的宽高值与测量模式，计算出实际使用的宽和高，遍历所有的childView，对childView进行测量，根据当前行已用的宽度判断是否需要换行，然后累计所有高度，设置布局自身尺寸。

```
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	// 实际可以用的宽和高
	int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
	int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	int heightMode = MeasureSpec.getMode(heightMeasureSpec);

	// Line初始化
	restoreLine();

	for (int i = 0; i < getChildCount(); i++) {
		View child = getChildAt(i);
		// 测量所有的childView
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
				heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

		if (line == null) {
			// 创建新一行
			line = new Line();
		}

		// 计算当前行已使用的宽度
		int measuredWidth = child.getMeasuredWidth();
		lineSize += measuredWidth;

		// 如果使用的宽度小于可用的宽度，这时候childView能够添加到当前的行上
		if (lineSize <= width) {
			line.addChild(child);
			lineSize += horizontalSpacing;
		} else {
			// 换行
			newLine();
			line.addChild(child);
			lineSize += child.getMeasuredWidth();
			lineSize += horizontalSpacing;
		}
	}

	// 把最后一行记录到集合中
	if (line != null && !lines.contains(line)) {
		lines.add(line);
	}

	int totalHeight = 0;
	// 把所有行的高度加上
	for (int i = 0; i < lines.size(); i++) {
		totalHeight += lines.get(i).getHeight();
	}
	// 加上行的竖直间距
	totalHeight += verticalSpacing * (lines.size() - 1);
	// 加上上下padding
	totalHeight += getPaddingBottom();
	totalHeight += getPaddingTop();

	// 设置自身尺寸
	// 设置布局的宽高，宽度直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度
	// 因为该布局的特性就是填满一行后，再换行
	// 高度根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
	setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
			resolveSize(totalHeight, heightMeasureSpec));
}

```

附上restoreLine与newLine方法

```
private void restoreLine() {
	lines.clear();
	line = new Line();
	lineSize = 0;
}

private void newLine() {
	// 把之前的行记录下来
	if (line != null) {
		lines.add(line);
	}
	// 创建新的一行
	line = new Line();
	lineSize = 0;
}

```

## **Line** ##

封装了每行上的View对象，提供添加与绘制childView的方法。

```
/**
 * 管理每行上的View对象
 */
class Line {
	// 子控件集合
	private List<View> children = new ArrayList<View>();
	// 行高
	int height;

	/**
	 * 添加childView
	 *
	 * @param childView 子控件
	 */
	public void addChild(View childView) {
		children.add(childView);

		// 让当前的行高是最高的一个childView的高度
		if (height < childView.getMeasuredHeight()) {
			height = childView.getMeasuredHeight();
		}
	}

	/**
	 * 设置childView的绘制区域
	 *
	 * @param left 左上角x轴坐标
	 * @param top  左上角y轴坐标
	 */
	public void layout(int left, int top) {
		int totalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		// 当前childView的左上角x轴坐标
		int currentLeft = left;

		for (int i = 0; i < children.size(); i++) {
			View view = children.get(i);
			// 设置childView的绘制区域
			view.layout(currentLeft, top, currentLeft + view.getMeasuredWidth(),
					top + view.getMeasuredHeight());
			// 计算下一个childView的位置
			currentLeft = currentLeft + view.getMeasuredWidth() + horizontalSpacing;
		}
	}

	public int getHeight() {
		return height;
	}

	public int getChildCount() {
		return children.size();
	}
}

```

## **onLayout** ##

指定所有childView的位置，调用Line对象中的layout方法。

```
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
	super.onLayout(changed, l, t, r, b);
	int left = getPaddingLeft();
	int top = getPaddingTop();
	for (int i = 0; i < lines.size(); i++) {
		Line line = lines.get(i);
		line.layout(left, top);
		// 计算下一行的起点y轴坐标
		top = top + line.getHeight() + verticalSpacing;
	}
}

```

## **setFlowLayout** ##

用于设置FlowLayout中的内容，并提供点击事件处理。

```
public void setFlowLayout(List<String> list, final OnItemClickListener onItemClickListener) {
	for (int i = 0; i < list.size(); i++) {
		final TextView tv = new TextView(getContext());

		// 设置TextView属性
		tv.setText(list.get(i));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		tv.setTextColor(textColor);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);

		tv.setClickable(true);
		tv.setBackgroundResource(backgroundResource);
		this.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClickListener.onItemClick(tv.getText().toString());
			}
		});
	}
}

public interface OnItemClickListener {
	void onItemClick(String content);
}

```

## **使用方法** ##

相关属性（字体大小、颜色、间距等）可以在布局文件中设置，也可以通过set方法设置。

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

# **写在最后** #

欢迎Fork，觉得还不错就Start一下吧！

博客地址：http://blog.csdn.net/kong_gu_you_lan/article/details/52786219

欢迎同学们吐槽评论，如果你觉得本篇博客对你有用，那么就留个言或者顶一下吧(＾－＾)
