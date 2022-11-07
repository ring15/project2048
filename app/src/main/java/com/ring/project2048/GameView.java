package com.ring.project2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {

    private static final String TAG = "gameView";

    private int row = 4;//行
    private int column = 4;//列
    private int spacing = 20;//行间距，列间距

    private Context mContext;
    private Paint blockPaint;
    private Paint numPaint;
    private int scope = 0;//分数
    private List<NumberBlock> blocks = new ArrayList<>();
    private boolean isGame = false;//是否在游戏中
    private GameListener gameListener;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        blockPaint = new Paint();
        numPaint = new Paint();
        blockPaint.setStyle(Paint.Style.FILL);
        numPaint.setStyle(Paint.Style.FILL);
        numPaint.setTextAlign(Paint.Align.CENTER);
        numPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先生成m*n个0方块
        int blockWidth = (getWidth() - spacing * (row + 1)) / row;
        int blockHeight = (getHeight() - spacing * (column + 1)) / column;
        for (int j = 0; j < column; j++) {
            for (int i = 0; i < row; i++) {
                int startX = (i + 1) * spacing + i * blockWidth;
                int startY = (j + 1) * spacing + j * blockHeight;
                RectF rectF = new RectF(startX, startY, startX + blockWidth, startY + blockHeight);
                if (blocks.size() > i + j * row) {
                    if (blocks.get(i + j * row).getNumber() == 0) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number0));
                    } else if (blocks.get(i + j * row).getNumber() == 2) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number2));
                    } else if (blocks.get(i + j * row).getNumber() == 4) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number4));
                    } else if (blocks.get(i + j * row).getNumber() == 8) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number8));
                    } else if (blocks.get(i + j * row).getNumber() == 16) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number16));
                    } else if (blocks.get(i + j * row).getNumber() == 32) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number32));
                    } else if (blocks.get(i + j * row).getNumber() == 64) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number64));
                    } else if (blocks.get(i + j * row).getNumber() == 128) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number128));
                    } else if (blocks.get(i + j * row).getNumber() == 256) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number256));
                    } else if (blocks.get(i + j * row).getNumber() == 512) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number512));
                    } else if (blocks.get(i + j * row).getNumber() == 1024) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number1024));
                    } else if (blocks.get(i + j * row).getNumber() == 2048) {
                        blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number2048));
                    }

                } else {
                    blockPaint.setColor(ContextCompat.getColor(mContext, R.color.number0));
                }
                //绘制方块
                canvas.drawRoundRect(rectF, 16, 16, blockPaint);

                //绘制数字
                if (blocks.size() > i + j * row && blocks.get(i + j * row).getNumber() > 0) {
                    if (blocks.get(i + j * row).getNumber() > 4) {
                        numPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
                    } else {
                        numPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
                    }
                    Paint.FontMetrics fontMetrics = numPaint.getFontMetrics();
                    float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
                    canvas.drawText(String.valueOf(blocks.get(i + j * row).getNumber()), rectF.centerX(), baseLineY, numPaint);
                }
            }
        }
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public void startGame() {
        isGame = true;
        createEmptyBlock();
        randomCreateBlock(2);
        invalidate();
    }

    //清空矩阵
    private void createEmptyBlock() {
        scope = 0;
        blocks.clear();
        for (int i = 0; i < row * column; i++) {
            NumberBlock numberBlock = new NumberBlock(i, 0);
            blocks.add(numberBlock);
        }
        if (gameListener != null) gameListener.refreshScope(scope);
    }

    //随机生成size个方块，2(70%)/4(20%)/8(10%)
    public void randomCreateBlock(int size) {
        ArrayList<NumberBlock> numberBlocks = new ArrayList<>();
        for (NumberBlock numberBlock : blocks) {
            if (numberBlock.getNumber() == 0) {
                numberBlocks.add(numberBlock);
            }
        }
        if (numberBlocks.size() <= 0) {
            isGame = false;
            if (gameListener != null) gameListener.gameOver();
            return;
        }
        for (int i = 0; i < size; i++) {
            int random = new Random().nextInt(numberBlocks.size());
            NumberBlock block = numberBlocks.get(random);
            int numRandom = new Random().nextInt(10);
            if (numRandom < 7) {
                block.setNumber(2);
            } else if (numRandom < 9) {
                block.setNumber(4);
            } else {
                block.setNumber(8);
            }
            for (NumberBlock numberBlock : blocks) {
                if (numberBlock.getId() == block.getId()) {
                    numberBlock = block;
                    break;
                }
            }
            numberBlocks.remove(block);
        }
    }

    private float downX;//点击时左标
    private float downY;//点击时左标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                float currentY = event.getY();
                if (Math.sqrt(Math.pow((currentX - downX), 2.0) + Math.pow((currentY - downY), 2.0)) > 20) {//滑动超过一定距离
                    if (Math.abs(currentX - downX) > Math.abs(currentY - downY)) {
                        //左右位移大于上下位移
                        if (currentX - downX > 0) {
                            Log.e(TAG, "right");
                            right();//现在位置在起始位置右边
                        } else {
                            Log.e(TAG, "left");
                            left();//现在位置在起始位置左边
                        }
                    } else {
                        //上下位移大于左右位移
                        if (currentY - downY > 0) {
                            Log.e(TAG, "down");
                            down();//现在位置在起始位置下边
                        } else {
                            Log.e(TAG, "top");
                            top();//现在位置在起始位置上边
                        }
                    }
                }
                break;
        }
        return true;
    }

    //向上滑动
    private void top() {
        if (!isGame) {
            Toast.makeText(mContext, "请开始新游戏！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isMove = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (blocks.size() <= i + j * row) break;//数组越界，退出，正常不会出现
                if (blocks.get(i + j * row).getNumber() <= 0) continue;//当前数字为0，跳到下一个
                for (int m = j + 1; m < column; m++) {
                    if (blocks.get(i + j * row).getNumber() == blocks.get(i + m * row).getNumber()) {
                        blocks.get(i + j * row).setNumber(2 * blocks.get(i + j * row).getNumber());
                        blocks.get(i + m * row).setNumber(0);
                        isMove = true;
                        scope += 1;
                        if (gameListener != null) gameListener.refreshScope(scope);
                        break;
                    } else if (blocks.get(i + m * row).getNumber() > 0) {
                        break;
                    }
                }
            }
            for (int j = 0; j < column; j++) {
                if (blocks.size() <= i + j * row) break;//数组越界，退出，正常不会出现
                if (blocks.get(i + j * row).getNumber() > 0) continue;//当前数字不为0，跳到下一个

                for (int m = j + 1; m < column; m++) {
                    if (blocks.get(i + m * row).getNumber() > 0) {
                        blocks.get(i + j * row).setNumber(blocks.get(i + m * row).getNumber());
                        blocks.get(i + m * row).setNumber(0);
                        isMove = true;
                        break;
                    }
                }
            }
        }

        if (isMove) {
            randomCreateBlock(1);
            invalidate();
            checkGameOver();
        }
    }

    //向下滑动
    private void down() {
        if (!isGame) {
            Toast.makeText(mContext, "请开始新游戏！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isMove = false;
        for (int i = 0; i < row; i++) {
            for (int j = column - 1; j >= 1; j--) {
                if (blocks.size() <= i * column + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i + j * row).getNumber() <= 0) continue;//当前数字为0，跳到下一个
                for (int m = j - 1; m >= 0; m--) {
                    if (blocks.get(i + j * row).getNumber() == blocks.get(i + m * row).getNumber()) {
                        blocks.get(i + j * row).setNumber(2 * blocks.get(i + j * row).getNumber());
                        blocks.get(i + m * row).setNumber(0);
                        isMove = true;
                        scope += 1;
                        if (gameListener != null) gameListener.refreshScope(scope);
                        break;
                    } else if (blocks.get(i + m * row).getNumber() > 0) {
                        break;
                    }
                }
            }
            for (int j = column - 1; j >= 1; j--) {
                if (blocks.size() <= i + j * row) break;//数组越界，退出，正常不会出现
                if (blocks.get(i + j * row).getNumber() > 0) continue;//当前数字不为0，跳到下一个

                for (int m = j - 1; m >= 0; m--) {
                    if (blocks.get(i + m * row).getNumber() > 0) {
                        blocks.get(i + j * row).setNumber(blocks.get(i + m * row).getNumber());
                        blocks.get(i + m * row).setNumber(0);
                        isMove = true;
                        break;
                    }
                }
            }
        }

        if (isMove) {
            randomCreateBlock(1);
            invalidate();
            checkGameOver();
        }
    }

    //向左滑动
    private void left() {
        if (!isGame) {
            Toast.makeText(mContext, "请开始新游戏！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isMove = false;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                if (blocks.size() <= i * row + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i * row + j).getNumber() <= 0) continue;//当前数字为0，跳到下一个
                for (int m = j + 1; m < row; m++) {
                    if (blocks.get(i * row + j).getNumber() == blocks.get(i * row + m).getNumber()) {
                        blocks.get(i * row + j).setNumber(2 * blocks.get(i * row + j).getNumber());
                        blocks.get(i * row + m).setNumber(0);
                        isMove = true;
                        scope += 1;
                        if (gameListener != null) gameListener.refreshScope(scope);
                        break;
                    } else if (blocks.get(i * row + m).getNumber() > 0) {
                        break;
                    }
                }
            }
            for (int j = 0; j < row; j++) {
                if (blocks.size() <= i * row + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i * row + j).getNumber() > 0) continue;//当前数字不为0，跳到下一个

                for (int m = j + 1; m < row; m++) {
                    if (blocks.get(i * row + m).getNumber() > 0) {
                        blocks.get(i * row + j).setNumber(blocks.get(i * row + m).getNumber());
                        blocks.get(i * row + m).setNumber(0);
                        isMove = true;
                        break;
                    }
                }
            }
        }

        if (isMove) {
            randomCreateBlock(1);
            invalidate();
            checkGameOver();
        }
    }

    //向右滑动
    private void right() {
        if (!isGame) {
            Toast.makeText(mContext, "请开始新游戏！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isMove = false;
        for (int i = 0; i < column; i++) {
            for (int j = row - 1; j >= 1; j--) {
                if (blocks.size() <= i * row + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i * row + j).getNumber() <= 0) continue;//当前数字为0，跳到下一个
                for (int m = j - 1; m >= 0; m--) {
                    if (blocks.get(i * row + j).getNumber() == blocks.get(i * row + m).getNumber()) {
                        blocks.get(i * row + j).setNumber(2 * blocks.get(i * row + j).getNumber());
                        blocks.get(i * row + m).setNumber(0);
                        isMove = true;
                        scope += 1;
                        if (gameListener != null) gameListener.refreshScope(scope);
                        break;
                    } else if (blocks.get(i * row + m).getNumber() > 0) {
                        break;
                    }
                }
            }
            for (int j = row - 1; j >= 1; j--) {
                if (blocks.size() <= i * row + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i * row + j).getNumber() > 0) continue;//当前数字不为0，跳到下一个

                for (int m = j - 1; m >= 0; m--) {
                    if (blocks.get(i * row + m).getNumber() > 0) {
                        blocks.get(i * row + j).setNumber(blocks.get(i * row + m).getNumber());
                        blocks.get(i * row + m).setNumber(0);
                        isMove = true;
                        break;
                    }
                }
            }
        }

        if (isMove) {
            randomCreateBlock(1);
            invalidate();
            checkGameOver();
        }
    }

    private void checkGameOver() {
        boolean hasSameNum = false;
        for (int i = 0; i < column; i++ ) {
            for (int j = 0; j < row; j++ ) {
                if (blocks.size() <= i * row + j) break;//数组越界，退出，正常不会出现
                if (blocks.get(i * row + j).getNumber() <= 0) {
                    hasSameNum = true;
                    break;
                }
                if (j + 1 < column) {
                    if (blocks.get(i * row + j).getNumber() == blocks.get(i * row + j + 1).getNumber()) {
                        hasSameNum = true;
                        break;
                    }
                }
                if (i + 1 < row) {
                    if (blocks.get(i * row + j).getNumber() == blocks.get((i + 1) * row + j).getNumber()) {
                        hasSameNum = true;
                        break;
                    }
                }
            }
        }
        if (!hasSameNum) {
            isGame = false;
            if (gameListener != null) gameListener.gameOver();
        }
    }

    public interface GameListener {
        void refreshScope(int scope);

        void gameOver();
    }
}
