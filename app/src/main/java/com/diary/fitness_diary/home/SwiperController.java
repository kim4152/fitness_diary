package com.diary.fitness_diary.home;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

enum ButtonsState{
    GONE,LEFT_VISIBLE,RIGHT_VISIBLE
        }

class SwipeController extends Callback {

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private final float buttonWidth;
    private SwipeControllerActions buttonsActions = null;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder=null;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setTouchListener(final Canvas c,
                                  final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if(buttonShowedState != ButtonsState.GONE){
                if(buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if(buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }else{
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if(buttonShowedState == ButtonsState.GONE){
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;

        drawButtons(c, viewHolder);
    }

    public interface  SwipeControllerActions{
       void onLeftClicked(int pos);
       void onRightClicked(int pos);
       void onReset(int pos);
    }

    Resources rs;
    public SwipeController(SwipeControllerActions buttonsActions, Resources rs){
        this.buttonsActions=buttonsActions;
        this.rs=rs;
        buttonWidth=toPx(80);
    }
    public float toPx(float dp){
        return dp * (rs.getDisplayMetrics().densityDpi/160f);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth -20;
        float corners = toPx(12);

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft()+40, itemView.getTop()+50, itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom()-50);
        p.setColor(rs.getColor(R.color.red)); //편집
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("삭제", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()+50, itemView.getRight()-40, itemView.getBottom()-50);
        p.setColor(rs.getColor(R.color.red));//삭제
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("삭제", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = toPx(16);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    private void setTouchDownListener(final Canvas c,
                                      final RecyclerView recyclerView,
                                      final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY,
                                      final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    //터치 시 동작 추가
                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c,
                                    final RecyclerView recyclerView,
                                    final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY,
                                    final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowedState=ButtonsState.GONE;
                    if(buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(),event.getY())){
                        buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                    }else if(buttonShowedState ==ButtonsState.RIGHT_VISIBLE){
                        buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder=null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView,
                                   boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }




}