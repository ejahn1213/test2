package Tacktile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import mirshod.braille_pad.R;

/**
 * Created by Mirshod on 12/9/2016.
 */

public class CustomView_Large extends View {

    int check = 0;
    String data = "";

    Paint paint;
    Paint p ;
    Paint pBackground ;
    Paint p_white ;

    int width=0;
    int height=0;
    int wx=0;
    int wy=0;

    public CustomView_Large(Context context) {
        super(context);
    }

    public CustomView_Large(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getStyleableAttributes(context, attrs);
    }

    public void setDatatoCustomView(int m_check , String m_byte) {
        this.check= m_check;
        this.data = m_byte;
        invalidate();
        requestLayout();
    }

    protected void onDraw(Canvas canvas) {
        //TODO Auto-generated method stub
        super.onDraw(canvas);


        int xw = getWidth();
        int xy = getHeight();

        paint.setColor(Color.parseColor("#dedede"));
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#000000"));
        p.setColor(Color.parseColor("#808080"));
        p_white.setColor(Color.parseColor("#FFFFFF"));

        int x = wx/53 ;
        int y = wx/53 ;
        int radius = wx/53;
        int paddingW = wx/26;
        int paddingH = wx/26;

        Log.d("CustomViewWidth" , String.valueOf(wx));
        Log.d("CustomViewHeight" , String.valueOf(wy));

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                canvas.drawCircle(x, y, radius, p_white);
                canvas.drawCircle(x + paddingW, y, radius, p_white);
                canvas.drawCircle(x, y + paddingH, radius, p_white);
                canvas.drawCircle(x + paddingW, y + paddingH, radius, p_white);
                canvas.drawCircle(x, y + 2 * paddingH, radius, p_white);
                canvas.drawCircle(x + paddingW, y + 2 * paddingH, radius, p_white);
                y += 160;
            }
            x += 160;
            y = 20;
        }
    }

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
        wx = width;
        wy = height;


    }

    private void getStyleableAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
    }

    public void init()  {
        paint = new Paint();
        pBackground = new Paint();
        p = new Paint();
        p_white = new Paint();

    }

    public boolean [] bytesToBooleans(byte [] bytes){
        boolean [] bools = new boolean[bytes.length * 8];

        for(int i = 0; i < bytes.length; i++){
            int j = i * 8;
            bools[j] = (bytes[i] & 0x80) != 0;
            bools[j + 1] = (bytes[i] & 0x40) != 0;
            bools[j + 2] = (bytes[i] & 0x20) != 0;
            bools[j + 3] = (bytes[i] & 0x10) != 0;
            bools[j + 4] = (bytes[i] & 0x8) != 0;
            bools[j + 5] = (bytes[i] & 0x4) != 0;
            bools[j + 6] = (bytes[i] & 0x2) != 0;
            bools[j + 7] = (bytes[i] & 0x1) != 0;

        }
        return bools;
    }

}
