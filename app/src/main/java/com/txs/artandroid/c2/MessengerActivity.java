package com.txs.artandroid.c2;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.txs.artandroid.R;

import java.util.List;

public class MessengerActivity extends Activity {
    private static final String TAG = "MessengerActivity";
    private Messenger mService;
    private TextView mTvSend;
    private String[] str = new String[]{"_id","name"};

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            sendMsg("hello this is client");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private  static class MessengerHandler extends Handler{
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 222:
                Log.d(TAG,"client receive:"+msg.getData().getString("reply"));
                break;
                default:
                    super.handleMessage(msg);
                    break;


        }

    }
}
private Binder mBinder = new IBookManager.Stub() {
    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

    }

    @Override
    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);

        mTvSend = findViewById(R.id.tv_send);
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg("hello this is client again");
            }
        });

    }

    private void sendMsg(String s) {
        Message msg = Message.obtain(null, 111);
        Bundle data = new Bundle();
        data.putString("msg", s);
        msg.setData(data);
        msg.replyTo = mGetReplyMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
