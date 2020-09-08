package com.example.kevin.myapplication;

/*
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
*/

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {
    static BufferedReader mBufferedReaderClient = null;
    static PrintWriter mPrintWriterClient = null;
    private ImageButton GoAhead;
    private ImageButton Stop;

    private View.OnClickListener GoAheadListenClient = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting && MainActivity.this.mSocketClient != null) {
                if ("1".length() <= 0) {
                    Toast.makeText(MainActivity.this.mContext, "发送内容不能为空！", 0).show();
                    return;
                }
                try {
                    MainActivity.mPrintWriterClient.print("0");
                    MainActivity.mPrintWriterClient.flush();
                    return;
                } catch (Exception exception) {
                    Toast.makeText(MainActivity.this.mContext, "发送异常"+ exception.getMessage(), 0).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this.mContext, "没有连接", 0).show();
        }

    };

    private View.OnClickListener StopListenClient = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting && MainActivity.this.mSocketClient != null) {
                if ("1".length() <= 0) {
                    Toast.makeText(MainActivity.this.mContext, "发送内容不能为空！", 0).show();
                    return;
                }
                try {
                    MainActivity.mPrintWriterClient.print("1");
                    MainActivity.mPrintWriterClient.flush();
                    return;
                } catch (Exception exception) {
                    Toast.makeText(MainActivity.this.mContext, "发送异常"+ exception.getMessage(), 0).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this.mContext, "没有连接", 0).show();
        }

    };

    private View.OnClickListener LeftClickListenerClient = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting && MainActivity.this.mSocketClient != null) {
                if ("1".length() <= 0) {
                    Toast.makeText(MainActivity.this.mContext, "发送内容不能为空！", 0).show();
                    return;
                }
                try {
                    MainActivity.mPrintWriterClient.print("2");
                    MainActivity.mPrintWriterClient.flush();
                    return;
                } catch (Exception exception) {
                    Toast.makeText(MainActivity.this.mContext, "发送异常"+ exception.getMessage(), 0).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this.mContext, "没有连接", 0).show();
        }
    };

    private View.OnClickListener RightClickListenerClient = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting && MainActivity.this.mSocketClient != null) {
                if ("1".length() <= 0) {
                    Toast.makeText(MainActivity.this.mContext, "发送内容不能为空！", 0).show();
                    return;
                }
                try {
                    MainActivity.mPrintWriterClient.print("3");
                    MainActivity.mPrintWriterClient.flush();
                    return;
                } catch (Exception exception) {
                    Toast.makeText(MainActivity.this.mContext, "发送异常"+ exception.getMessage(), 0).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this.mContext, "没有连接", 0).show();
        }
    };

    private View.OnClickListener BackClickListenerClient = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting && MainActivity.this.mSocketClient != null) {
                if ("1".length() <= 0) {
                    Toast.makeText(MainActivity.this.mContext, "发送内容不能为空！", 0).show();
                    return;
                }
                try {
                    MainActivity.mPrintWriterClient.print("4");
                    MainActivity.mPrintWriterClient.flush();
                    return;
                } catch (Exception exception) {
                    Toast.makeText(MainActivity.this.mContext, "发送异常"+ exception.getMessage(), 0).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this.mContext, "没有连接", 0).show();
        }
    };

    private View.OnClickListener StartClickListener = new View.OnClickListener() {
        public void onClick(View param1View) {
            if (MainActivity.this.isConnecting) {
                MainActivity.this.isConnecting = false;
                try {
                    if (MainActivity.this.mSocketClient != null) {
                        MainActivity.this.mSocketClient.close();
                        MainActivity.this.mSocketClient = null;
                        MainActivity.mPrintWriterClient.close();
                        MainActivity.mPrintWriterClient = null;
                    }
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                MainActivity.this.mThreadClient.interrupt();
                MainActivity.this.startButton.setText("开始连接");
                MainActivity.this.recvText.setText("信息：\n");
                return;
            }
            MainActivity.this.isConnecting = true;
            MainActivity.this.startButton.setText("停止连接");
            MainActivity.this.mThreadClient = new Thread(MainActivity.this.mRunnable);
            MainActivity.this.mThreadClient.start();
        }
    };

    private EditText editMsgTextClient;

    private boolean isConnecting = false;

    private Context mContext;

    Handler mHandler = new Handler() {
        public void handleMessage(Message param1Message) {
            super.handleMessage(param1Message);
            if (param1Message.what == 1)
                MainActivity.this.recvText.append("Client:" + MainActivity.this.recvMessageClient);
        }
    };

    private Runnable mRunnable = new Runnable() {
        public void run() {
            Message message;
/*
            String str = MainActivity.this.IPText.getText().toString();
            if (str.length() <= 0) {
                MainActivity.this.recvMessageClient = "IP不能为空！\n";
                message = new Message();
                message.what = 1;
                MainActivity.this.mHandler.sendMessage(message);
                return;
            }

            int i = message.indexOf(":");
            if (i == -1 || i + 1 >= message.length()) {
                MainActivity.this.recvMessageClient = "IP地址不合法\n";
                message = new Message();
                message.what = 1;
                MainActivity.this.mHandler.sendMessage(message);
                return;
            }
*/
            int i = Integer.parseInt("5000");
            Log.d("gjz", "IP" + "192.168.4.1" + ":" + i);
            try {
                MainActivity.this.mSocketClient = new Socket("192.168.4.1", i);
                MainActivity.mBufferedReaderClient = new BufferedReader(new InputStreamReader(MainActivity.this.mSocketClient.getInputStream()));
                MainActivity.mPrintWriterClient = new PrintWriter(MainActivity.this.mSocketClient.getOutputStream(), true);
                MainActivity.this.recvMessageClient = "已经连接server! \n";
                message = new Message();
                message.what = 1;
                MainActivity.this.mHandler.sendMessage(message);
                char[] arrayOfChar = new char[256];
                while (true) {
                    if (MainActivity.this.isConnecting) {
                        try {
                            i = MainActivity.mBufferedReaderClient.read(arrayOfChar);
                            if (i > 0) {
                                MainActivity.this.recvMessageClient = String.valueOf(MainActivity.this.getInfoBuff(arrayOfChar, i)) + "\n";
                                Message message1 = new Message();
                                message1.what = 1;
                                MainActivity.this.mHandler.sendMessage(message1);
                            }
                        } catch (Exception exception) {
                            MainActivity.this.recvMessageClient = "接收异常："+ exception.getMessage() + "\n";
                            Message message1 = new Message();
                            message1.what = 1;
                            MainActivity.this.mHandler.sendMessage(message1);
                        }
                        continue;
                    }
                    return;
                }
            } catch (Exception exception) {
                MainActivity.this.recvMessageClient = "连接IP异常："+ exception.toString() + exception.getMessage() + "\n";
                Message message1 = new Message();
                message1.what = 1;
                MainActivity.this.mHandler.sendMessage(message1);
                return;
            }
        }
    };

    private Socket mSocketClient = null;

    private Thread mThreadClient = null;

    private String recvMessageClient = "";

    private TextView recvText;

    private ImageButton LeftButtonClient;

    private ImageButton RightButtonClient;

    private Button BackButtonClient;

    private Button startButton;

    private String getInfoBuff(char[] paramArrayOfchar, int paramInt) {
        char[] arrayOfChar = new char[paramInt];
        for (int i = 0;; i++) {
            if (i >= paramInt)
                return new String(arrayOfChar);
            arrayOfChar[i] = paramArrayOfchar[i];
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        this.mContext = (Context)this;
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder()).detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy((new StrictMode.VmPolicy.Builder()).detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
        this.startButton = (Button)findViewById(R.id.connect);
        this.startButton.setOnClickListener(this.StartClickListener);
        this.GoAhead = (ImageButton)findViewById(R.id.Button_up);
        this.GoAhead.setOnClickListener(this.GoAheadListenClient);
        this.Stop = (ImageButton)findViewById(R.id.Button_stop);
        this.Stop.setOnClickListener(this.StopListenClient);
        this.LeftButtonClient = (ImageButton)findViewById(R.id.Button_left);
        this.LeftButtonClient.setOnClickListener(this.LeftClickListenerClient);
        this.RightButtonClient = (ImageButton)findViewById(R.id.Button_right);
        this.RightButtonClient.setOnClickListener(this.RightClickListenerClient);
        this.BackButtonClient = (Button)findViewById(R.id.Button_back);
        this.BackButtonClient.setOnClickListener(this.BackClickListenerClient);
        this.recvText = (TextView)findViewById(R.id.textView2);
        this.recvText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}