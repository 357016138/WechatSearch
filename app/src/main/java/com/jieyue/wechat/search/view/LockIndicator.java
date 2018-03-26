package com.jieyue.wechat.search.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.utils.UtilTools;

import java.util.List;


/**
 * 手势密码图案提示
 */
public class LockIndicator extends View {
    private int numRow = 3;    // 行
    private int numColum = 3; // 列
    private int patternWidth;
    private int patternHeight;
    private int f = 5;
    private int g = 5;
    private int widthSpacing = 0;
    private int heightSpacing = 0;
    private int strokeWidth = 3;
    private Paint paint = null;
    private Drawable patternNoraml = null;
    private Drawable patternPressed = null;
    private String lockPassStr; // 手势密码

    public LockIndicator(Context paramContext) {
        super(paramContext);
    }

    public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet, 0);
        widthSpacing = UtilTools.dp2px(paramContext, 5f);
        heightSpacing = UtilTools.dp2px(paramContext, 5f);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        patternNoraml = getResources().getDrawable(R.drawable.lock_pattern_node_normal);
        patternPressed = getResources().getDrawable(R.drawable.lock_pattern_node_pressed);
        if (patternPressed != null) {
            patternWidth = patternPressed.getIntrinsicWidth();
            patternHeight = patternPressed.getIntrinsicHeight();
            this.f = (patternWidth / 4);
            this.g = (patternHeight / 4);
            patternPressed.setBounds(0, 0, patternWidth, patternHeight);
            patternNoraml.setBounds(0, 0, patternWidth, patternHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((patternPressed == null) || (patternNoraml == null)) {
            return;
        }
        // 绘制3*3的图标
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numColum; j++) {
                paint.setColor(-16777216);
                int i1 = j * patternHeight + j * this.g + j * widthSpacing;
                int i2 = i * patternWidth + i * this.f + i * heightSpacing;
                canvas.save();
                canvas.translate(i1, i2);
                String curNum = String.valueOf(numColum * i + (j + 1));
                if (!TextUtils.isEmpty(lockPassStr)) {
                    if (lockPassStr.indexOf(curNum) == -1) {
                        // 未选中
                        patternNoraml.draw(canvas);
                    } else {
                        // 被选中
                        patternPressed.draw(canvas);
                    }
                } else {
                    // 重置状态
                    patternNoraml.draw(canvas);
                }
                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        if (patternPressed != null) {
            setMeasuredDimension(numColum * patternHeight + this.g * (-1 + numColum) + (numColum + -1) * widthSpacing,
                    numRow * patternWidth + this.f * (-1 + numRow) + (numRow + -1) * heightSpacing);
        }
    }

    /**
     * 请求重新绘制
     */
    public void setPath(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            lockPassStr = "";
            invalidate();
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (LockPatternView.Cell cell : pattern) {
            String curNum = String.valueOf(3 * cell.getRow() + (cell.getColumn() + 1));
            sb.append(curNum);
        }
        lockPassStr = sb.toString();
        invalidate();
    }
}