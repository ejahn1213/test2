package mirshod.braille_pad;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.util.Log;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RunnableFuture;
import java.util.BitSet;    //바이트배열을 넘기기위해 BitSet 사용

import Tacktile.CustomView;
import Tacktile.CustomView_Large;
import Tacktile.CustomView_xLarge;
import bluetooth.Bluetooth;
import global.Application;

public class MainActivity extends Activity implements View.OnClickListener , circleInterface , DialogInterface.OnKeyListener {
    Intent serviceIntent;
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter;
    private Bluetooth bluetooth;

    private MyReceiver myReceiver;
    private IntentFilter intentFilter;
    private static final String KEY_INDEX = "index";
    private static boolean [] key_Msg = new boolean[32];  // 5 : Flag / 3 : 전원, TTS / 4 :  방향키 / 6 : 점자키 / 4 : Page, Voluem / 5 : Function key

    Button Fun_home,Fun_tts,Fun_power,Fun_ctrl,Fun_shift,Fun_space,
            Braille_one,Braille_two,Braille_three,Braille_four,Braille_five,
            Braille_six,Braille_seven,Braille_eight,Fun_PageLeft,Fun_PageRight,Fun_VolumeUp,Fun_VolumeDown , Joystick,
            Fun_up, Fun_down, Fun_left, Fun_right;

    private CustomView_xLarge customViewXLarge;
    private boolean powerFlag = false;
    private boolean ttsFlag = false;
    private boolean ttsLongFlag = false;
    private boolean ttsUpFlag = false;
    private boolean ttsDownFlag = false;

    circleButton circleButtonView;

    boolean temp = false;
    boolean bluetoothConnectChk = false;
    boolean resume = false;
    boolean [] multouch = new boolean[7];
    boolean editMode = false;

    Intent intent = new Intent(MainActivity.this,Bluetooth.class);
    String sendMessage;
    String temp_Str;
    String key_Str = new String();
    SharedPreferences back;
    SharedPreferences getBack;
    SharedPreferences.Editor editor;
    Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_xlarge);

        Fun_power = (Button) findViewById(R.id.button1);
        Fun_tts = (Button) findViewById(R.id.button2);
        Braille_one = (Button) findViewById(R.id.button3);
        Braille_two = (Button) findViewById(R.id.button4);
        Braille_three = (Button) findViewById(R.id.button5);
        Braille_four = (Button) findViewById(R.id.button6);
        Braille_five = (Button) findViewById(R.id.button7);
        Braille_six = (Button) findViewById(R.id.button8);
        Fun_PageLeft = (Button) findViewById(R.id.button9);
        Fun_PageRight = (Button) findViewById(R.id.button10);
        Fun_VolumeUp = (Button) findViewById(R.id.button11);
        Fun_VolumeDown = (Button) findViewById(R.id.button12);
        Braille_seven = (Button) findViewById(R.id.button13);
        Braille_eight = (Button) findViewById(R.id.button14);
        Fun_space = (Button) findViewById(R.id.button15);
        Fun_ctrl = (Button) findViewById(R.id.button16);
        Fun_shift = (Button) findViewById(R.id.button17);


        Fun_power.setOnLongClickListener(mLongVickListener);
        Fun_tts.setOnLongClickListener(mLongVickListener);
        Fun_VolumeUp.setOnLongClickListener(mLongVickListener);
        Fun_VolumeDown.setOnLongClickListener(mLongVickListener);
//        Fun_tts.setOnClickListener(this);
//        Braille_one.setOnClickListener(this);
//        Braille_two.setOnClickListener(this);
//        Braille_three.setOnClickListener(this);
//        Braille_four.setOnClickListener(this);
//        Braille_five.setOnClickListener(this);
//        Braille_six.setOnClickListener(this);
//        Fun_PageLeft.setOnClickListener(this);
//        Fun_PageRight.setOnClickListener(this);
//        Fun_VolumeUp.setOnClickListener(this);
//        Fun_VolumeDown.setOnClickListener(this);
//        Braille_seven.setOnClickListener(this);
//        Braille_eight.setOnClickListener(this);
//        Fun_space.setOnClickListener(this);
//        Fun_ctrl.setOnClickListener(this);
//        Fun_shift.setOnClickListener(this);


        Fun_power.setOnTouchListener(mTouchEvent);
        Fun_tts.setOnTouchListener(mTouchEvent);
        Braille_one.setOnTouchListener(mTouchEvent);
        Braille_two.setOnTouchListener(mTouchEvent);
        Braille_three.setOnTouchListener(mTouchEvent);
        Braille_four.setOnTouchListener(mTouchEvent);
        Braille_five.setOnTouchListener(mTouchEvent);
        Braille_six.setOnTouchListener(mTouchEvent);
        Fun_PageLeft.setOnTouchListener(mTouchEvent);
        Fun_PageRight.setOnTouchListener(mTouchEvent);
        Fun_VolumeUp.setOnTouchListener(mTouchEvent);
        Fun_VolumeDown.setOnTouchListener(mTouchEvent);
        Braille_seven.setOnTouchListener(mTouchEvent);
        Braille_eight.setOnTouchListener(mTouchEvent);
        Fun_space.setOnTouchListener(mTouchEvent);
        Fun_ctrl.setOnTouchListener(mTouchEvent);
        Fun_shift.setOnTouchListener(mTouchEvent);

        customViewXLarge = (CustomView_xLarge) findViewById(R.id.customView_xLarge);
        circleButtonView = (circleButton) findViewById(R.id.aylbtn);
        circleButtonView.setI(this);

//        for(int i=0; i < 32; i++){
//            toBitToBool[i]=false;
//        }

        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Bluetooth.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        bluetooth = new Bluetooth();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {     //기기에 블루투스 모듈이 없을 때 앱 종료.
            // Toast.makeText(this,"Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        getBack = getSharedPreferences("Back", MODE_WORLD_WRITEABLE );
        //Toast.makeText(this, String.valueOf(getBack.getBoolean("state",false)), Toast.LENGTH_LONG).show();
        if (getBack.getBoolean("state",false)){
            //Fun_power.performLongClick();
            back = getSharedPreferences("Back", MODE_WORLD_WRITEABLE );
            editor = back.edit();
            //editor.clear();       //Sharedpreferences에서 저장한 모든 데이터 삭제.
            editor.putBoolean("state", false);
            editor.commit();

           // Toast.makeText(this,String.valueOf(getBack.getBoolean("state",false)),Toast.LENGTH_LONG).show();
        }
//// UI 테스트를 위해 블루투스 통신 없이도 빈 점자가 나타나도록하는 소스
//        String strReceived =  "bytes received:" ;
//
//        Intent intent = new Intent();
//        intent.setAction(bluetooth.MY_ACTION);
//        intent.putExtra("DATA_BACK", strReceived);
//        sendBroadcast(intent);
//
//        customViewXLarge.setDataToCustomView(true, strReceived);
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        try{
            bluetooth.onDestroy();
            this.unregisterReceiver(myReceiver);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
        super.onStop();
        Log.d("Stop ",  "!!!");
        //Log.d("Stop Adpater : ", Integer.toString(bluetoothAdapter.getState()) );
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
        super.onPause();
//        back = getSharedPreferences("Back", MODE_WORLD_WRITEABLE );
//        editor = back.edit();
//        //editor.clear();
//        editor.putBoolean("state", true);
//        editor.commit();
        Log.d("Pause Adpater : ", Integer.toString(bluetoothAdapter.getState()) );
    }

    @Override
    protected void onStart() {
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
        super.onResume();
        Log.d("Resume Adpater : ", Integer.toString(bluetoothAdapter.getState()) );
//        if (bluetooth.bluetoothPair == true) {
//            //sendData("10000011000000000000000000000000");
//
//            customViewXLarge.setDataToCustomView(true, temp_Str);
//        }
    }

    @Override
    protected void onRestart() {
        Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
        super.onRestart();
        Log.d("Restart Adpater : ", Integer.toString(bluetoothAdapter.getState()) );
        //sendData("10000011000000000000000000000000");
        //Fun_power.performLongClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT)
        {
            if(resultCode == Activity.RESULT_OK){
                startService(intent);
            }else {
                finish();
            }
        }
    }

    public boolean keyBoolCheck(boolean[] key){
        boolean chk =false;
        for(int i=0; i < key.length; i++){
            if(key[i] == true){
                chk = true;
                return  chk;
            }
        }
        return chk;
    }

    @Override
    public void on_click_top() {
        if(key_Msg == null || keyBoolCheck(key_Msg) == false){
            key_Msg[1] = true;
            key_Msg[9] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[9] = false;
            boolToString(key_Msg);
        }else if(keyBoolCheck(key_Msg)){
            multouch[6] = true;
            key_Msg[1] = true;
            key_Msg[9] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[9] = false;
            boolToString(key_Msg);
        }
    }
    @Override
    public void on_click_bottom() {
        if(key_Msg == null || keyBoolCheck(key_Msg) == false){
            key_Msg[1] = true;
            key_Msg[10] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[10] = false;
            boolToString(key_Msg);
        }else if(keyBoolCheck(key_Msg)){
            multouch[6] = true;
            key_Msg[1] = true;
            key_Msg[10] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[10] = false;
            boolToString(key_Msg);
        }
    }
    @Override
    public void on_click_left() {
        if(key_Msg == null || keyBoolCheck(key_Msg) == false){
            key_Msg[1] = true;
            key_Msg[11] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[11] = false;
            boolToString(key_Msg);
        }else if(keyBoolCheck(key_Msg)){
            multouch[6] = true;
            key_Msg[1] = true;
            key_Msg[11] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[11] = false;
            boolToString(key_Msg);
        }
    }
    @Override
    public void on_click_right() {
        if(key_Msg == null || keyBoolCheck(key_Msg) == false){
            key_Msg[1] = true;
            key_Msg[12] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[12] = false;
            boolToString(key_Msg);
        }else if(keyBoolCheck(key_Msg)){
            multouch[6] = true;
            key_Msg[1] = true;
            key_Msg[12] = true;
            sendData(boolToString(key_Msg));
            key_Msg[1] = false;
            key_Msg[12] = false;
            boolToString(key_Msg);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            handleTouch(v, event);
            return false;
        }
    };

    void handleTouch(View v, MotionEvent event){
        if(temp == false) {  //멀티 버튼 누를때 메시지 날리는 곳.
            switch (v.getId()) {
                default:
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (multouch[0] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[0] = true;
                        } else if (multouch[1] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[1] = true;
                            multouch[6] = true;
                        } else if (multouch[2] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[2] = true;
                        } else if (multouch[3] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[3] = true;
                        } else if (multouch[4] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[4] = true;
                        } else if (multouch[5] == false) {
                            chkBtnID_bit(v, key_Msg);
                            multouch[5] = true;
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (multouch[5] == true) {
                            multouch[5] = false;
                            sendMessage = chkBtnID_bit(v, key_Msg);
                            sendData(sendMessage);
                            delBtnID_bit(v, key_Msg);
//                            Log.d("6 해제: ", key_Str);
                            temp = true;
                            multouch[4] = false;
                            multouch[3] = false;
                            multouch[2] = false;
                            multouch[1] = false;
                            multouch[0] = false;
                            break;
                        } else if (multouch[4] == true) {
                            multouch[4] = false;
                            sendMessage = chkBtnID_bit(v, key_Msg);
                            sendData(sendMessage);
                            delBtnID_bit(v, key_Msg);
//                            Log.d("5 해제: ", key_Str);
                            temp = true;
                            multouch[3] = false;
                            multouch[2] = false;
                            multouch[1] = false;
                            multouch[0] = false;
                            break;
                        } else if (multouch[3] == true) {
                            multouch[3] = false;
                            sendMessage = chkBtnID_bit(v, key_Msg);
                            sendData(boolToString(key_Msg));
                            delBtnID_bit(v, key_Msg);
//                            Log.d("4 해제: ", key_Str);
                            temp = true;
                            multouch[2] = false;
                            multouch[1] = false;
                            multouch[0] = false;
                            break;
                        } else if (multouch[2] == true) {
                            multouch[2] = false;
                            multouch[1] = false;
                            sendMessage = chkBtnID_bit(v, key_Msg);
                            sendData(sendMessage);
                            delBtnID_bit(v, key_Msg);
                            temp = true;
                            break;
                        } else if (multouch[1] == true) {
                            multouch[1] = false;
                            sendMessage = chkBtnID_bit(v, key_Msg);
                            if(key_Msg[27] == true && key_Msg[28] == true){
                                Log.d("Edit Mode Key Input", String.valueOf(editMode));
                                if(editMode == false){
                                    editMode = true;
                                    sendMessage = chkBtnID_bit(v, key_Msg);
                                }
                                else if(editMode == true){
                                    editMode = false;
                                    sendMessage = chkBtnID_bit(v, key_Msg);
                                }
                            }
                            sendData(sendMessage);

                            delBtnID_bit(v, key_Msg);
//                            Log.d("2 해제: ", key_Str);
                            temp = true;
                            break;
                        } else if (multouch[0] == true) {
                            //여기서는 메세지 보내면 안됨.
                            if (multouch[6] || key_Msg[6]==true || key_Msg[19]==true) {
                                multouch[0] = false;
                                delBtnID_bit(v, key_Msg);
                                //Log.d("1-다중 해제: ", key_Str);
                                nullBtnID_bit(key_Msg);
                                temp = true;
                                multouch[6] = false;
                            } else if (!multouch[6]) {
                                multouch[0] = false;
                                sendMessage = chkBtnID_bit(v, key_Msg);
                                sendData(sendMessage);
                                if(!ttsFlag)
                                    ttsFlag = true;
                                else
                                    ttsFlag = false;
                                delBtnID_bit(v, key_Msg);
                                nullBtnID_bit(key_Msg);
                                temp = true;
                            }
                        }
                    }
            }
        }
        temp = false;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String orgData = arg1.getStringExtra("DATA_BACK");
            temp_Str = orgData;
            customViewXLarge.setDataToCustomView(true, orgData);

            bluetoothConnectChk = true;
        }
    }

    public void sendBroadcast(String sendMessage) {
        Intent intent = new Intent(MainActivity.this,Bluetooth.class);
        intent.putExtra("INIT_DATA", sendMessage);

        startService(intent);
    }

    public void sendData(String sendMessage) {
        Intent intent = new Intent(MainActivity.this,Bluetooth.class);
        intent.putExtra("INIT_DATA", sendMessage);

        startService(intent);
    }

    public String chkBtnID_bit(View v, boolean[] key){
        if(editMode == false){
            if (v.getId() == R.id.button1 || v.getId() == R.id.button2) {
                key_Msg[0] = true;
                switch (v.getId()) {
                    case R.id.button1:
                        if (powerFlag) {
                            if (!ttsFlag ) {
                                Log.d("진입 : ttsFlag - False ::", String.valueOf(ttsFlag));
                                key_Msg[7] = true;
                                key_Msg[8] = true;
                                break;
                            } else {
                                Log.d("진입 : ttsFlag - True ::", String.valueOf(ttsFlag));
                                key_Msg[7] = true;
                                key_Msg[8] = false;

                                break;
                            }
                        } else if(!powerFlag)
                                key_Msg[7] = false;

                    case R.id.button2:
                        key_Msg[8] = true;
                        break;
                }

            } else if (v.getId() == R.id.button3 || v.getId() == R.id.button4 || v.getId() == R.id.button5 || v.getId() == R.id.button6 || v.getId() == R.id.button7 || v.getId() == R.id.button8) {
                key_Msg[2] = true;
                switch (v.getId()) {
                    case R.id.button3:
                        key_Msg[13] = true;
                        break;
                    case R.id.button4:
                        key_Msg[14] = true;
                        break;
                    case R.id.button5:
                        key_Msg[15] = true;
                        break;
                    case R.id.button6:
                        key_Msg[16] = true;
                        break;
                    case R.id.button7:
                        key_Msg[17] = true;
                        break;
                    case R.id.button8:
                        key_Msg[18] = true;
                        break;
                }

            } else if (v.getId() == R.id.button9 || v.getId() == R.id.button10 || v.getId() == R.id.button11 || v.getId() == R.id.button12) {
                key_Msg[3] = true;
                switch (v.getId()) {
                    case R.id.button9:
                        key_Msg[20] = true;
                        break;
                    case R.id.button10:
                        key_Msg[21] = true;
                        break;
                    case R.id.button11:
                        key_Msg[22] = true;
                        break;
                    case R.id.button12:
                        key_Msg[23] = true;
                        break;
                }

            } else if (v.getId() == R.id.button13 || v.getId() == R.id.button14 || v.getId() == R.id.button15 || v.getId() == R.id.button16 || v.getId() == R.id.button17) {
                key_Msg[4] = true;
                switch (v.getId()) {
                    case R.id.button13:
                        key_Msg[24] = true;
                        break;
                    case R.id.button14:
                        key_Msg[25] = true;
                        break;
                    case R.id.button15:
                        key_Msg[26] = true;
                        break;
                    case R.id.button16:
                        key_Msg[27] = true;
                        break;
                    case R.id.button17:
                        key_Msg[28] = true;
                        break;
                }
            }
            key_Msg[5] = false;

        } else if(editMode == true){
            if (v.getId() == R.id.button1 || v.getId() == R.id.button2) {
                key_Msg[0] = true;
                switch (v.getId()) {
                    case R.id.button1:
                        key_Msg[7] = true;
                        break;
                    case R.id.button2:
                        key_Msg[8] = true;
                }

            } else if (v.getId() == R.id.button3 || v.getId() == R.id.button4 || v.getId() == R.id.button5 || v.getId() == R.id.button6 || v.getId() == R.id.button7 || v.getId() == R.id.button8) {
                key_Msg[2] = true;
                switch (v.getId()) {
                    case R.id.button3:
                        key_Msg[13] = true;
                        break;
                    case R.id.button4:
                        key_Msg[14] = true;
                        break;
                    case R.id.button5:
                        key_Msg[15] = true;
                        break;
                    case R.id.button6:
                        key_Msg[16] = true;
                        break;
                    case R.id.button7:
                        key_Msg[17] = true;
                        break;
                    case R.id.button8:
                        key_Msg[18] = true;
                        break;
                }

            } else if (v.getId() == R.id.button9 || v.getId() == R.id.button10 || v.getId() == R.id.button11 || v.getId() == R.id.button12) {
                key_Msg[3] = true;
                switch (v.getId()) {
                    case R.id.button9:
                        key_Msg[20] = true;
                        break;
                    case R.id.button10:
                        key_Msg[21] = true;
                        break;
                    case R.id.button11:
                        key_Msg[22] = true;
                        break;
                    case R.id.button12:
                        key_Msg[23] = true;
                        break;
                }

            } else if (v.getId() == R.id.button13 || v.getId() == R.id.button14 || v.getId() == R.id.button15 || v.getId() == R.id.button16 || v.getId() == R.id.button17) {
                key_Msg[4] = true;
                switch (v.getId()) {
                    case R.id.button13:
                        key_Msg[24] = true;
                        break;
                    case R.id.button14:
                        key_Msg[25] = true;
                        break;
                    case R.id.button15:
                        key_Msg[26] = true;
                        break;
                    case R.id.button16:
                        key_Msg[27] = true;
                        break;
                    case R.id.button17:
                        key_Msg[28] = true;
                        break;
                }
            }
                key_Msg[5] = true;
        }

        key_Str = boolToString(key);

        return key_Str;
    }

    public String delBtnID_bit(View v, boolean[] key){
        if (v.getId() == R.id.button1 || v.getId() == R.id.button2) {
            key_Msg[0] = false;
            switch (v.getId()) {
                case R.id.button1:
                    key_Msg[7] = false;
                    break;
                case R.id.button2:
                    key_Msg[8] = false;
                    break;
            }

        } else if (v.getId() == R.id.button3 || v.getId() == R.id.button4 || v.getId() == R.id.button5 || v.getId() == R.id.button6 || v.getId() == R.id.button7 || v.getId() == R.id.button8) {
            key_Msg[2] = false;
            switch (v.getId()) {
                case R.id.button3:
                    key_Msg[13] = false;
                    break;
                case R.id.button4:
                    key_Msg[14] = false;
                    break;
                case R.id.button5:
                    key_Msg[15] = false;
                    break;
                case R.id.button6:
                    key_Msg[16] = false;
                    break;
                case R.id.button7:
                    key_Msg[17] = false;
                    break;
                case R.id.button8:
                    key_Msg[18] = false;
                    break;
            }

        } else if (v.getId() == R.id.button9 || v.getId() == R.id.button10 || v.getId() == R.id.button11 || v.getId() == R.id.button12) {
            key_Msg[3] = false;
            switch (v.getId()) {
                case R.id.button9:
                    key_Msg[20] = false;
                    break;
                case R.id.button10:
                    key_Msg[21] = false;
                    break;
                case R.id.button11:
                    key_Msg[22] = false;
                    break;
                case R.id.button12:
                    key_Msg[23] = false;
                    break;
            }

        } else if (v.getId() == R.id.button13 || v.getId() == R.id.button14 || v.getId() == R.id.button15 || v.getId() == R.id.button16 || v.getId() == R.id.button17) {
            key_Msg[4] = false;
            switch (v.getId()) {
                case R.id.button13:
                    key_Msg[24] = false;
                    break;
                case R.id.button14:
                    key_Msg[25] = false;
                    break;
                case R.id.button15:
                    key_Msg[26] = false;
                    break;
                case R.id.button16:
                    key_Msg[27] = false;
                    break;
                case R.id.button17:
                    key_Msg[28] = false;
                    break;
            }
        }

        key_Str = boolToString(key);

        return key_Str;
    }

    public void nullBtnID_bit(boolean[] key){
        for(int i=0; i < key.length; i++)
            key[i] = false;
    }

    public String boolToString(boolean [] bool){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<bool.length; i++){
            if(bool[i] == true){
                sb = sb.append("1");
            }else {
                sb = sb.append("0");
            }
        }
        return  sb.toString();
    }

    public String byteArrayToBinaryString(byte[] b){
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<b.length; ++i){
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    public void onClick(View v) {
        long mLastClickTime = 0;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                 Toast.makeText(this, "Back키를 누르셨군요", Toast.LENGTH_SHORT).show();
                back = getSharedPreferences("Back", MODE_WORLD_WRITEABLE );
                editor = back.edit();
                //editor.clear();
                editor.putBoolean("state", true);
                editor.commit();
                Fun_power.performClick();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    View.OnLongClickListener mLongVickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    //Toast.makeText(getBaseContext(),String.valueOf(powerFlag),Toast.LENGTH_LONG).show();
                    if (powerFlag){
                        key_Msg[6] = true;
                        chkBtnID_bit(v, key_Msg);
                        key_Msg[7] = false;
                        key_Msg[8] = false;
                        Log.d("전원 끔", boolToString(key_Msg));
                        sendMessage = boolToString(key_Msg);
                        sendBroadcast(sendMessage);
                        finish();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //여기에 딜레이 후 시작할 작업들을 입력
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }, 500);
                        break;
                    }else if(!powerFlag){
                        key_Msg[6] = true;
                        chkBtnID_bit(v, key_Msg);
                        key_Msg[7] = true;
                        key_Msg[8] = false;
                        Log.d("전원 켬", boolToString(key_Msg));
                        sendMessage = boolToString(key_Msg);
                        sendBroadcast(sendMessage);
                        powerFlag = true;
                        break;
                    }
                case R.id.button2:
                    if (ttsLongFlag){
                        sendMessage = chkBtnID_bit(v,key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }else{
                        key_Msg[6] = true;
                        sendMessage = chkBtnID_bit(v, key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }
                case R.id.button11:
                    if (ttsUpFlag){
                        sendMessage = chkBtnID_bit(v,key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }else{
                        key_Msg[19] = true;
                        sendMessage = chkBtnID_bit(v, key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }
                case R.id.button12:
                    if (ttsDownFlag){
                        sendMessage = chkBtnID_bit(v,key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }else{
                        key_Msg[19] = true;
                        sendMessage = chkBtnID_bit(v, key_Msg);
                        sendBroadcast(sendMessage);
                        temp = true;
                        break;
                    }
                default:
                    break;
            }
            return false;
        }
    };
}