package mirshod.braille_pad;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mirshod on 12/5/2016.
 */

public class circleButton extends View implements View.OnTouchListener{

    private circleInterface i;

    public circleButton(Context context) {
        super(context);
        bm1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbutton);
    }

    private Bitmap bm1;
    Context c;

    public void setI(circleInterface i) {
        this.i = i;
    }

    public circleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;

    }

    public circleButton(Context context, AttributeSet attrs, int defStyleAttr, circleInterface i) {
        super(context, attrs, defStyleAttr);
        this.c = context;
    }

    public circleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.c = context;
    }

    private int downx;
    private int downy;

    int wx = 0;
    int wy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int wx = width;
        int wy = height;

        if (wx > wy) {
            wx = wy;
        } else {
            wy = wx;
        }
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            downx = (int) event.getX();
            downy = (int) event.getY();
            //left side
            if (downx < wx / 3 && downy > wy / 3 && downy < wy - wy / 3) {
                i.on_click_left();
            //                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.right_painted);
//                bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
//                invalidate();

//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                };

                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.left_painted);
                bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
                invalidate();

            } else
                //top side
                if (downx > wx / 3 && downx < wx - wx / 3 && downy < wy / 3) {
                    i.on_click_top();
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.top_painted);
                    bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
                    invalidate();
                } else
                    //right side
                    if (downx > wx - wx / 3 && downy > wy / 3 && downy < wy - wy / 3) {
                        i.on_click_right();
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.right_painted);
                        bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
                        invalidate();
                    } else
                        //bottom side
                        if (downx > wx / 3 && downx < wx - wx / 3 && downy > wy - wy / 3) {
                            i.on_click_bottom();
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_painted);
                            bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
                            invalidate();
                        }
            //invalidate();
        }
        if(event.getAction()== MotionEvent.ACTION_UP){
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.mainbutton);
            bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
            invalidate();
        }

        return true;
    }

    private int width;
    private int height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY)
        {
            width = Integer.MAX_VALUE;
        }

        if (heightMode != MeasureSpec.EXACTLY)
        {
            height = Integer.MAX_VALUE;
        }

        this.setMeasuredDimension(width, height);
        int wx = width;
        int wy = height;
        if (wx > wy) {
            wx = wy;
        } else {
            wy = wx;
        }

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.mainbutton);
        bm1 = Bitmap.createScaledBitmap(b, wx, wy, false);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }

    Paint paint;

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bm1, 0, 0, null);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

}

