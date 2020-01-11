package com.lzacking.wanandroid.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lzacking.wanandroid.R;
import com.lzacking.wanandroid.util.UIUtils;

import androidx.annotation.Nullable;

public class RoundProgress extends View {
    // 使用自定义属性来初始化如下的变量
    private int roundColor; // 圆环的颜色
    private int roundProgressColor; // 圆弧的颜色
    private int textColor; // 文本的颜色
    private float roundWidth; // 圆环的宽度
    private float textSize; // 文本的字体大小
    private int max; // 圆环的最大值
    private int progress; // 圆环的进度
    private int width; // 当前视图的宽度（=高度）
    private Paint paint; // 画笔

    private RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true); // 去除毛边

        // 获取自定义的属性
        // 1.获取TyperArray的对象（调用两个参数的方法）
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
        // 2.取出所有的自定义属性（attrs里面都是自定义的属性，下面代码的目的是如果没有取出attrs里面的属性，就使用下面的属性）
        roundColor = typedArray.getColor(R.styleable.RoundProgress_roundColor, Color.GRAY);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.RED);
        textColor = typedArray.getColor(R.styleable.RoundProgress_textColor, Color.GREEN);
        roundWidth = typedArray.getDimension(R.styleable.RoundProgress_roundWith, UIUtils.dp2px(10));
        textSize = typedArray.getDimension(R.styleable.RoundProgress_textSize, UIUtils.dp2px(20));
        max = typedArray.getInteger(R.styleable.RoundProgress_max, 100);
        progress = typedArray.getInteger(R.styleable.RoundProgress_progress, 30);

        // 3.回收处理
        typedArray.recycle();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    // 测量：获取当前视图宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 1.绘制圆环
        // 获取圆心坐标
        int cx = width / 2;
        int cy = width / 2;
        float radius = width / 2 - roundWidth / 2;
        paint.setColor(roundColor); // 设置画笔颜色
        paint.setStyle(Paint.Style.STROKE); // 设置圆环的样式
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        canvas.drawCircle(cx, cy, radius, paint);

        // 2.绘制圆弧
        RectF rectF = new RectF(roundWidth / 2, roundWidth / 2, width - roundWidth / 2, width - roundWidth / 2);
        paint.setColor(roundProgressColor); // 设置画笔颜色
        canvas.drawArc(rectF, 0, progress * 360 / max ,false, paint);

        // 3.绘制文本
        String text = progress * 100 / max + "%";
        // 设置paint
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);
        Rect rect = new Rect(); // 创建了一个矩形，此时矩形没有具体的宽度和高度
        paint.getTextBounds(text, 0, text.length(), rect);
        // 获取左下顶点的坐标
        int x = width / 2 - rect.width() / 2;
        int y = width / 2 + rect.height() / 2;
        canvas.drawText(text, x, y, paint);
    }

}