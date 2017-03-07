package Tacktile;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import mirshod.braille_pad.R;

/**
 * Created by Mirshod on 12/9/2016.
 */

public class CustomView_xLarge extends View {

    boolean check = false;
    String data = "";

    Paint paint;
    Paint p ;
    Paint pBackground ;
    Paint p_white ;
    Paint p_red;
    Paint p_black;

    int width=0;
    int height=0;
    int wx=0;
    int wy=0;

    public CustomView_xLarge(Context context) {
        super(context);
    }

    public CustomView_xLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getStyleableAttributes(context, attrs);

    }

    public void setDataToCustomView(boolean m_check , String m_byte) {
        this.check= m_check;
        this.data = m_byte;
        invalidate();
        requestLayout();
    }

    protected void onDraw(Canvas canvas) {
        //TODO Auto-generated method stub
        super.onDraw(canvas);

        Log.d("CustomViewWidth" , String.valueOf(wx));
        Log.d("CustomViewHeight" , String.valueOf(wy));
        char[] x_test = data.toCharArray();

        int x = wx/88;
        int y = wx/88;
        int radius = wx/88;
        int paddingW = wx/46;
        int paddingH = wx/46;

        paint.setColor(Color.parseColor("#dedede"));
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#000000"));
        p.setColor(Color.parseColor("#0000ff"));
        p.setStrokeWidth(10);
        p_white.setColor(Color.parseColor("#FFFFFF"));
        p_red.setColor(Color.parseColor("#FF0000"));
        p_black.setColor(Color.parseColor("#000000"));
        x = 44;
        int z = 0;

        if(check) {
            for (int i = 1; i < 37; i++) { //3x12 = 36
                for (int j = 0; j < 24; j++) { //y = x * 12 = 24
                    if(j<2){
                        //파란선 외부
                        if (x_test[z] == '1') {
                            p.setColor((Color.RED));
                        } else {
                            p.setColor(Color.parseColor("#FFFFFF"));
                        }

                        if (j % 2 == 0) {
                            canvas.drawCircle(x, y, radius, p);
                            x += 44;
                        } else {
                            canvas.drawCircle(x, y, radius, p);
                            x += 70;
                        }
                        z++;
                        //파란선 내부
                    }else {
                        if (x_test[z] == '1') {
                            p.setColor(Color.parseColor("#000000"));
                        } else {
                            p.setColor(Color.parseColor("#FFFFFF"));
                        }

                        if (j % 2 == 0) {
                            canvas.drawCircle(x, y, radius, p);
                            x += 44;
                        } else {
                            canvas.drawCircle(x, y, radius, p);
                            x += 70;
                        }
                        z++;
                    }
                }
                if (i % 3 == 0) {
                    x = 44;
                    y += 58;
                } else {
                    x = 44;
                    y += 35;
                }
            }
        }

        Paint p_horizontal = new Paint();
        p_horizontal.setColor(Color.parseColor("#0000ff"));
        p_horizontal.setStrokeWidth(7);
        p.setColor(Color.parseColor("#0000ff"));
        double horizontal_line = (double)(int)(wy/ (double)1.1);
        double vertical_line_wy = (double)(int)(wy/ (double)0.98);
        Log.d("down_line" , String.valueOf(horizontal_line));

        canvas.drawLine(128,2,128, Float.parseFloat(String.valueOf(vertical_line_wy)) ,p);
        canvas.drawLine(128,Float.parseFloat(String.valueOf(horizontal_line)),wx,Float.parseFloat(String.valueOf(horizontal_line)),p_horizontal);

//        //점자 셀 그리는 부분
//        if(x_test.length<833 && x_test.length > 433){   //12x12로 데이터 들어올때 그려주는 부분
//
//        }
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
        //   checkToClick();
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
