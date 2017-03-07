package mirshod.braille_pad;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mirshod on 12/5/2016.
 */

public interface circleInterface {
    public void on_click_left();
    public void on_click_right();
    public void on_click_top();
    public void on_click_bottom();

    boolean onTouch(View v, MotionEvent event);
}
