package global;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

import bluetooth.Bluetooth;
import mirshod.braille_pad.MainActivity;

/**
 * Created by user on 2016-12-07.
 */

public class Application extends android.app.Application {

    private static Application instance;
    protected Context context = this;
    protected ContextThemeWrapper dialogContext;
    protected Toast toast;
    protected SharedPreferences back;
    SharedPreferences getBack;
    SharedPreferences.Editor editor;

    public static Application getInstance(){
        if(instance == null) instance = new Application();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize(){
        instance = this;
        //Toast.makeText(this,"Application",Toast.LENGTH_LONG).show();
        back = getSharedPreferences("Back", MODE_WORLD_WRITEABLE );
        editor = back.edit();
        editor.clear();
        editor.putBoolean("state", false);
        editor.commit();
    }

    public void finish(){
        finish();
    }
}