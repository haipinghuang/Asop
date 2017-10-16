package com.asop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄海 on 2017/10/12.
 */

public class LockView extends ViewGroup {
    private static final String TAG = "LockView";
    private Paint paint = new Paint(Paint.DITHER_FLAG);
    private Canvas canvas = new Canvas();
    private Bitmap bitmap;
    private String mColorNormal = "#2CC1EC";//黄色
    private String mColorError = "#FB332F";//红色
    private boolean drawEnable = true; // 是否允许绘制
    private StringBuilder pwdSb = new StringBuilder();
    private GesturePoint downPoint;//按下时的点
    private OnGestureCallback callback;
    private boolean verify;//是否校验模式
    private String inputGestureCode;//用户传入的手势密码

    private List<GesturePoint> points = new ArrayList(9);
    private List<Pair<GesturePoint, GesturePoint>> lineList = new ArrayList();// 记录画过的线
    private Map<String, GesturePoint> autoCheckPointMap = new HashMap();// 自动选中的情况点

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);//否则ondraw方法不会调用
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.parseColor(mColorNormal));
        paint.setAntiAlias(true);
        for (int i = 0; i < 9; i++) {

        }
    }

    private void initAutoCheckPointMap() {
        autoCheckPointMap.put("1,3", getGesturePointByNum(2));
        autoCheckPointMap.put("1,7", getGesturePointByNum(4));
        autoCheckPointMap.put("1,9", getGesturePointByNum(5));
        autoCheckPointMap.put("2,8", getGesturePointByNum(5));
        autoCheckPointMap.put("3,7", getGesturePointByNum(5));
        autoCheckPointMap.put("3,9", getGesturePointByNum(6));
        autoCheckPointMap.put("4,6", getGesturePointByNum(5));
        autoCheckPointMap.put("7,9", getGesturePointByNum(8));
    }

    private GesturePoint getGesturePointByNum(int num) {
        for (GesturePoint point : points) {
            if (point.getNum() == num) return point;
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        int width = getMeasuredWidth();

        int blockWidth = width / 6;
        int intervalWidth = (width - blockWidth * 3) / 4;
        int row, col;
        int leftX, rightX, topY, bottomY;
        LayoutParams lp = new LayoutParams(blockWidth, blockWidth);
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.CENTER);
            image.setImageResource(R.drawable.gesture_node_normal);
            this.addView(image, lp);

            row = i / 3;
            col = i % 3;
            leftX = (col + 1) * intervalWidth + blockWidth * col;
            rightX = leftX + blockWidth;
            topY = (row + 1) * intervalWidth + blockWidth * row;
            bottomY = topY + blockWidth;
            GesturePoint point = new GesturePoint(leftX, rightX, topY, bottomY, (ImageView) getChildAt(i), i + 1);
            points.add(point);
        }
        initAutoCheckPointMap();

        bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < 9; i++) {
            View child = getChildAt(i);
            GesturePoint point = points.get(i);
            child.layout(point.getLeftX(), point.getTopY(), point.getRightX(), point.getBottomY());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawEnable) {
            paint.setColor(Color.parseColor(mColorNormal));// 设置默认连线颜色
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downPoint = getPointAt(event.getX(), event.getY());
                    if (downPoint != null) {
                        downPoint.setPointState(PointState.SELECTED);
                        pwdSb.append(downPoint.getNum());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    clearScreenAndDrawList();
                    GesturePoint curPoint = getPointAt(event.getX(), event.getY());
                    if (downPoint == null && curPoint == null) return true;
                    else {
                        if (downPoint == null) {// 先判断当前的point是不是为null
                            downPoint = curPoint;// 如果为空，那么把手指移动到的点赋值给currentPoint
                            downPoint.setPointState(PointState.SELECTED);
                        }
                    }
                    if (curPoint == null || downPoint.equals(curPoint) || PointState.SELECTED == curPoint.getPointState())
                        // 点击移动区域不在圆的区域，或者当前点击的点与当前移动到的点的位置相同，或者当前点击的点处于选中状态
                        // 那么以当前的点中心为起点，以手指移动位置为终点画线
                        canvas.drawLine(downPoint.getCenterX(), downPoint.getCenterY(), event.getX(), event.getY(), paint);
                    else {
                        // 如果当前点击的点与当前移动到的点的位置不同,那么以前前点的中心为起点，以手移动到的点的位置画线
                        canvas.drawLine(downPoint.getCenterX(), downPoint.getCenterY(), curPoint.getCenterX(), curPoint.getCenterY(), paint);
                        curPoint.setPointState(PointState.SELECTED);
                        // 判断是否中间点需要选中
                        GesturePoint betweenPoint = getBetweenCheckPoint(downPoint, curPoint);
                        if (betweenPoint != null && PointState.SELECTED != betweenPoint.getPointState()) {
                            // 存在中间点并且没有被选中
                            Pair<GesturePoint, GesturePoint> pair1 = new Pair(downPoint, betweenPoint);
                            lineList.add(pair1);
                            pwdSb.append(betweenPoint.getNum());
                            Pair<GesturePoint, GesturePoint> pair2 = new Pair(betweenPoint, curPoint);
                            lineList.add(pair2);
                            pwdSb.append(curPoint.getNum());
                            // 设置中间点选中
                            betweenPoint.setPointState(PointState.SELECTED);
                        } else {
                            Pair<GesturePoint, GesturePoint> pair = new Pair(downPoint, curPoint);
                            lineList.add(pair);
                            pwdSb.append(curPoint.getNum());
                        }
                        downPoint = curPoint;// 赋值当前的point;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (callback != null) {
                        if (verify) {
                            callback.onCheckedResult(pwdSb.toString().trim().equals(inputGestureCode));
                        } else {
                            callback.onGestureInput(pwdSb.toString().trim());
                        }
                    } else clearDrawlineStart(1400);
                    break;
            }
        }
        return true;
    }

    private GesturePoint getPointAt(float x, float y) {
        for (GesturePoint point : points) {
            int leftX = point.getLeftX();
            int rightX = point.getRightX();
            if (!(x >= leftX && x <= rightX)) {
                continue;// 如果为假，则跳到下一个对比
            }
            int topY = point.getTopY();
            int bottomY = point.getBottomY();
            if (!(y >= topY && y <= bottomY)) {
                continue;// 如果为假，则跳到下一个对比
            }
            // 如果执行到这，那么说明当前点击的点的位置在遍历到点的位置这个地方
            return point;
        }
        return null;
    }


    private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
        int startNum = pointStart.getNum();
        int endNum = pointEnd.getNum();
        String key;
        if (startNum < endNum) key = startNum + "," + endNum;
        else key = endNum + "," + startNum;
        return autoCheckPointMap.get(key);
    }

    public void clearDrawlineStart(long delayTime) {
        if (delayTime > 0) {
            drawEnable = false;// 绘制红色提示路线
            drawErrorPathTip();
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                pwdSb.setLength(0);// 重置pwdSb
                lineList.clear();
                clearScreenAndDrawList();
                for (GesturePoint point : points) {
                    point.setPointState(PointState.NORMAL);
                }
                drawEnable = true;
                invalidate();
            }
        }, delayTime);

    }

    /**
     * 清掉屏幕上所有的线，然后画出集合里面的线
     */
    private void clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), paint);// 画线
        }
    }

    /**
     * 校验错误/两次绘制不一致提示
     */
    private void drawErrorPathTip() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(Color.parseColor(mColorError));

        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), paint);// 画线
            pair.first.setPointState(PointState.ERROR);
            pair.second.setPointState(PointState.ERROR);
        }
        invalidate();
    }

    public LockView setInputGestureCode(@NonNull String code) {
        inputGestureCode = code;
        verify = true;
        return this;
    }

    public interface OnGestureCallback {
        //手势密码输入完毕
        void onGestureInput(String gestureCode);

        //判断手势密码是否验证成功
        void onCheckedResult(boolean result);
    }

    public OnGestureCallback getCallback() {
        return callback;
    }

    public LockView setCallback(OnGestureCallback callback) {
        this.callback = callback;
        return this;
    }
}
