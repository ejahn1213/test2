package bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import global.Application;
import mirshod.braille_pad.MainActivity;

public class Bluetooth extends Service {

    BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> pairedDeviceArrayList;

    private static final int REQUEST_ENABLE_BT = 1;

    private UUID myUUID = UUID.fromString("1f335f20-af00-11e6-9598-0800200c9a66");
    private String myName;

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    public final static String MY_ACTION = "MY_ACTION";
    String initData ="";

    boolean SetupOrUnSetup = false;
    Application application;

    public Bluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Bluetooth onCreate()", Toast.LENGTH_SHORT).show();
        super.onCreate();
        Application application = (Application) getApplicationContext();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Bluetooth onStartCommand()", Toast.LENGTH_SHORT).show();
        // Let it continue running until it is stopped.
        myName = myUUID.toString();

        initData = intent.getStringExtra("INIT_DATA");
        if(myThreadConnected!=null) {
            byte[] bytesToSend = initData.getBytes();
            myThreadConnected.write(bytesToSend);
        }
        Log.d("init" , initData);

        if(initData.equals("10000011000000000000000000000000")) {   //전원 On key 값 들어 왔을 때
            setup();
            // Toast.makeText(Bluetooth.this, "connected", Toast.LENGTH_SHORT).show();
        }
//        else if(initData.equals("10000010000000000000000000000000")){
//            myThreadConnected.cancel();
//            myThreadConnectBTdevice.cancel();
//        }

        return START_REDELIVER_INTENT ;
    }

    boolean b_checkConnect = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setup() {

        SetupOrUnSetup = true;
        int k = 0;
        BluetoothDevice remoteDevice=null;
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        pairedDeviceArrayList = new ArrayList<BluetoothDevice>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                k++;
                pairedDeviceArrayList.add(device);
                if (k == 1) {
                    remoteDevice = device;
                    // Toast.makeText(Bluetooth.this, remoteDevice.getName() + remoteDevice.getAddress(),Toast.LENGTH_SHORT).show();
                }
                break;
            }
            myThreadConnectBTdevice = new ThreadConnectBTdevice(remoteDevice);
            myThreadConnectBTdevice.start();
        }
    }

    private class ThreadConnectBTdevice extends Thread {
        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;

        public ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                b_checkConnect = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                b_checkConnect = false;
                e.printStackTrace();
            }
        }

        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;

            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                // Toast.makeText(Bluetooth.this, msgconnected , Toast.LENGTH_SHORT).show();
                Log.d("connect" , msgconnected);

                startThreadConnected(bluetoothSocket);
            }else{
                //fail
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void startThreadConnected(BluetoothSocket socket){
        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {       //소켓 객체에서 데이터를 읽고 쓰기 위한 스트림 객체 생성
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }
        @Override
        public void run() {     //데이터 읽기
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = String.valueOf(bytes) + "bytes received:\n" + strReceived;

                    Log.d("message" , strReceived);

                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    intent.putExtra("DATA_BACK", strReceived);
                    sendBroadcast(intent);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    cancel();
                    android.os.Process.killProcess(android.os.Process.myPid());

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();
                }
            }
        }

        public void write(byte[] buffer) {      //데이터 쓰기
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Bluetooth onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        stopSelf();
    }
}

