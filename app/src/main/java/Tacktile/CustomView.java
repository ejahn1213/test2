package Tacktile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CustomView extends View {

    boolean customViewClick = false;

    boolean check = false;
    String data = "";

    Paint paint;
    Paint p ;
    Paint pBackground;
    Paint p_white ;
    Paint p_red;
    Paint p_black;

     int width=0;
     int height=0;
     int wx=0;
     int wy=0;


    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initizlize();
        getStyleableAttributes(context, attrs);
    }

    public void setDataToCustomView(boolean m_check , String m_byte) {
        this.check= m_check;
        this.data = m_byte;
        invalidate();
        requestLayout();
    }

    public boolean checkToClick()
    {
        this.customViewClick =true;
        return true;
    }

    protected void onDraw(Canvas canvas) {
        //TODO Auto-generated method stub
        super.onDraw(canvas);

        // when receive data
//        if(check != 0){
//            //여기다 Boolean array 갱신
//            //   byteArray = dataString.getBytes();
//            //  booleanArray = bytesToBooleans(byteArray);
//            char[] x_test = data.toCharArray();
//            int x = 60;
//            int y = 20;
//            int radius = 20;
//
//            paint.setColor(Color.parseColor("#dedede"));
//            canvas.drawPaint(paint);
//            paint.setColor(Color.parseColor("#000000"));
//            p.setColor(Color.parseColor("#808080"));
//            p_white.setColor(Color.parseColor("#FFFFFF"));
//
//            int z = 0;
//            for (int i = 1; i < 25; i++) {
//                for (int j = 0; j < 16; j++) {
//                    if(x_test[z] == '1') {
//                        p.setColor(Color.parseColor("#000000"));
//                    }else {
//                        p.setColor(Color.parseColor("#FFFFFF"));
//                    }
//
//                    if(j%2 == 0){
//                        canvas.drawCircle(x, y, radius, p);
//                        x += 50;
//                    }else {
//                        canvas.drawCircle(x, y, radius, p);
//                        x += 100;
//                    }
//                    z++;
//
//                }
//                if(i%3 == 0){
//                    x = 60;
//                    y+=70;
//                }else {
//                    x = 60;
//                    y+=45;
//                }
//
//            }
//
//        }
//        // default view
//        else
//        {
//            int x = 60;
//            int y = 20;
//            int radius = 20;
//            int paddingW = 40;
//            int paddingH = 40;
//
//            paint.setColor(Color.parseColor("#dedede"));
//            canvas.drawPaint(paint);
//            paint.setColor(Color.parseColor("#000000"));
//            p.setColor(Color.parseColor("#808080"));
//            p_white.setColor(Color.parseColor("#FFFFFF"));
//
//            for (int i = 1; i < 25; i++) {
//                for (int j = 0; j < 16; j++) {
//                    if(j%2 == 0){
//                        canvas.drawCircle(x, y, radius, p_white);
//                        x += 50;
//                    }else {
//                        canvas.drawCircle(x, y, radius, p_white);
//                        x += 100;
//                    }
//                }
//                if(i%3 == 0){
//                    x = 60;
//                    y+=70;
//                }else {
//                    x = 60;
//                    y+=45;
//                }
//
//            }
////        }

//        if(check == 0)
//        {

        customViewClick = true;

        int x = wx/106;
        int y = wx/106;
        int radius = wx/106;
        int paddingW = wx/53;
        int paddingH = wx/53;

        paint.setColor(Color.parseColor("#dedede"));
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#000000"));
        p.setColor(Color.parseColor("#0000ff"));
        p.setStrokeWidth(5);
        p_white.setColor(Color.parseColor("#FFFFFF"));
        p_red.setColor(Color.parseColor("#FF0000"));
        p_black.setColor(Color.parseColor("#000000"));


//
//        if(check) {
//            for (int j = 0; j < 12; j++) {
//                for (int i = 0; i < 6; i++) {
//                    if(j==0) {
//                        canvas.drawCircle(x, y, radius, p_white);
//                        canvas.drawCircle(x + paddingW, y, radius, p_red);
//                        canvas.drawCircle(x, y + paddingH, radius, p_white);
//                        canvas.drawCircle(x + paddingW, y + paddingH, radius, p_white);
//                        canvas.drawCircle(x, y + 2 * paddingH, radius, p_red);
//                        canvas.drawCircle(x + paddingW, y + 2 * paddingH, radius, p_white);
//                    }
//                    else
//                    {
//                        canvas.drawCircle(x, y, radius, p_white);
//                        canvas.drawCircle(x + paddingW, y, radius, p_white);
//                        canvas.drawCircle(x, y + paddingH, radius, p_black);
//                        canvas.drawCircle(x + paddingW, y + paddingH, radius, p_white);
//                        canvas.drawCircle(x, y + 2 * paddingH, radius, p_white);
//                        canvas.drawCircle(x + paddingW, y + 2 * paddingH, radius, p_black);
//                    }
//                        y += wy / 6;
//                }
//                x += wx / 11.5;
//                y = wx / 106;
//            }
//        }
//            else
//        {
//            for (int j = 0; j < 12; j++) {
//                 for (int i = 0; i < 6; i++) {
//                    canvas.drawCircle(x, y, radius, p_white);
//                    canvas.drawCircle(x + paddingW, y, radius, p_white);
//                    canvas.drawCircle(x, y + paddingH, radius, p_white);
//                    canvas.drawCircle(x + paddingW, y + paddingH, radius, p_white);
//                    canvas.drawCircle(x, y + 2 * paddingH, radius, p_white);
//                    canvas.drawCircle(x + paddingW, y + 2 * paddingH, radius, p_white);
//                    y += wy / 6;
//                }
//                x += wx / 11.5;
//                y = wx / 106;
//            }
//        }
            Paint p_horizontal = new Paint();
            p_horizontal.setColor(Color.parseColor("#0000ff"));
            p_horizontal.setStrokeWidth(3);


            double down_line = (double)(int)(wy/ (double)1.21);
            Log.d("down_line" , String.valueOf(down_line));
            canvas.drawLine(wx/16,2,wx/16,wy,p);
            canvas.drawLine(wx/16,Float.parseFloat(String.valueOf(down_line)),wx,Float.parseFloat(String.valueOf(down_line)),p_horizontal);
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

    public void initizlize()  {
        checkToClick();
        paint = new Paint();
        pBackground = new Paint();
        p = new Paint();
        p_white = new Paint();
        p_red = new Paint();
        p_black = new Paint();
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
