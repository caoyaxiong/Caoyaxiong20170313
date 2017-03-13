package com.bwie.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 1. 类的用途
 * 2. @author dell
 * 3. @date 2017/3/13 09:13
 */

public class CircleNumberProgress extends View {
    /**
     * Context上下文环境
     */
    private Context context;

    /**
     * 调用者设置的进程 1 - 100
     */
    private int progress;

    /**
     * 画未完成进度圆弧的画笔
     */
    private Paint paintA;

    /**
     * 画已经完成进度条的画笔
     */
    private Paint paintB;

    /**
     * 画文字的画笔
     */
    private Paint paintText;

    /**
     * 画笔的宽度和画笔文字的大小(px)
     */
    private int paintWidth;

    /**
     * 包围进度条圆弧的矩形
     */
    private RectF rectF = new RectF();

    /**
     * 包围文字所在路径圆弧的矩形，比上一个矩形略小
     */
    private RectF rectF2 = new RectF();

    /**
     * 进度文字所在的路径
     */
    private Path path = new Path();

    /**
     * 文字所在路径圆弧的半径
     */
    private int radiusText;

    /**
     * 文字距离圆弧路径的高度（为了使文字在圆环中恰好竖直居中）
     */
    private int textMarginHeight;

    public CircleNumberProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 构造器中初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        paintWidth = Utils.dip2px(context, 20);

        // 未完成进度圆环的画笔的属性
        paintA = new Paint();
        paintA.setColor(0xffaaaaaa);
        paintA.setStrokeWidth(paintWidth);
        paintA.setStyle(Paint.Style.STROKE);

        // 已经完成进度条的画笔的属性
        paintB = new Paint();
        paintB.setColor(0xff67aae4);
        paintB.setStrokeWidth(paintWidth);
        paintB.setStyle(Paint.Style.STROKE);

        // 文字的画笔的属性
        paintText = new Paint();
        paintText.setColor(0xffff0077);
        paintText.setTextSize(paintWidth);
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getWidthAndHeight();
    }

    /**
     * 得到视图等的高度宽度尺寸数据
     */
    private void getWidthAndHeight() {

        //得到自定义视图的高度
        int viewHeight;

        //得到自定义视图的宽度
        int viewWidth;

        //得到自定义视图的X轴中心点
        int viewCenterX;

        //得到自定义视图的Y轴中心点
        int viewCenterY;

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        viewCenterX = viewWidth / 2;
        viewCenterY = viewHeight / 2;

        //取本View长宽较小者的一半为圆环的半径
        int radius = viewHeight > viewWidth ? viewWidth / 2 : viewHeight / 2;

        //进度条圆弧的半径
        int radiusArc = radius - paintWidth / 2;

        rectF.left = viewCenterX - radiusArc;
        rectF.top = viewCenterY - radiusArc;
        rectF.right = viewCenterX + radiusArc;
        rectF.bottom = viewCenterY + radiusArc;

        //文字所在路径圆弧的半径
        radiusText = radius - paintWidth;

        rectF2.left = viewCenterX - radiusText;
        rectF2.top = viewCenterY - radiusText;
        rectF2.right = viewCenterX + radiusText;
        rectF2.bottom = viewCenterY + radiusText;

        //文字距离圆弧路径的高度（为了使文字在圆环中恰好竖直居中）
        Rect rect = new Rect();
        paintText.getTextBounds("%", 0, 1, rect);
        int textHeight = rect.height();
        textMarginHeight = (paintWidth - textHeight) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画未完成进度的圆环
        canvas.drawArc(rectF, 0, 360, false, paintA);

        //画已经完成进度的圆弧 从-90度开始，即从圆环顶部开始
        canvas.drawArc(rectF, -90, progress / 100.0f * 360, false, paintB);

        //为文字所在路径添加一段圆弧轨迹，进度为0%-9%时应该最短，进度为10%-99%时应该边长，进度为100%时应该最长
        //这样才能保证文字和圆弧的进度一致，不会出现超前或者滞后的情况

        //要画的文字
        String text = progress + "%";

        //存储字符所有字符所占宽度的数组
        float[] widths = new float[text.length()];

        //得到所有字符所占的宽度
        paintText.getTextWidths(text, 0, text.length(), widths);

        //所有字符所占宽度之和
        float textWidth = 0;
        for (float f : widths) {
            textWidth += f;
        }

        //根据长度得到路径对应的扫过的角度
        //width = sweepAngle * 2 * π * R / 360 ; sweepAngle = width * 360 / 2 / π / R
        float sweepAngle = (float) (textWidth * 360 / 2 / Math.PI / radiusText);

        //添加路径
        path.addArc(rectF2, progress * 3.6f - 90.0f - sweepAngle / 2.0f, sweepAngle);

        //绘制进度的文字
        canvas.drawTextOnPath(text, path, 0, -textMarginHeight, paintText);

        //重置路径
        path.reset();
    }

    /**
     * @param progress 外部传进来的当前进度,强制重绘
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}