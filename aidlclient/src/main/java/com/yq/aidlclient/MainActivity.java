package com.yq.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yq.aidltest.Book;
import com.yq.aidltest.BookController;
import com.yq.aidltest.IMyAidlInterface;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNum1;
    private EditText etNum2;
    private Button btnCalc;
    private TextView tvResult;
    private Button btnGetBook;
    private Button btnAddBook;
    private Button btn_addBookOutType;

    private IMyAidlInterface myAidlInterface;
    private BookController bookController;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        bindService();
        bindBookService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("action.add");
        intent.setPackage("com.yq.aidltest");
//        intent.setComponent(new ComponentName("com.yq.aidltest", "com.yq.aidltest.IRemoteService"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void bindBookService() {
        Intent intent = new Intent();
        intent.setAction("action.book");
        intent.setPackage("com.yq.aidltest");
//        intent.setComponent(new ComponentName("com.yq.aidltest", "com.yq.aidltest.IRemoteService"));
        bindService(intent, bookConn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aidl", name.getPackageName());
            Log.i("aidl", name.getClassName());
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAidlInterface = null;
        }
    };
    private ServiceConnection bookConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aidl", name.getPackageName());
            Log.i("aidl", name.getClassName());
            bookController = BookController.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookController = null;
        }
    };

    private void assignViews() {
        etNum1 = (EditText) findViewById(R.id.et_num1);
        etNum2 = (EditText) findViewById(R.id.et_num2);
        btnCalc = (Button) findViewById(R.id.btn_calc);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btnGetBook = (Button) findViewById(R.id.btn_getBook);
        btnAddBook = (Button) findViewById(R.id.btn_addBook);
        btn_addBookOutType = (Button) findViewById(R.id.btn_addBookOutType);

        btnCalc.setOnClickListener(this);
        btnGetBook.setOnClickListener(this);
        btnAddBook.setOnClickListener(this);
        btn_addBookOutType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calc:
                try {
                    int num1 = Integer.parseInt(etNum1.getText().toString());
                    int num2 = Integer.parseInt(etNum2.getText().toString());
                    tvResult.setText(String.valueOf(myAidlInterface.add(num1, num2)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_getBook:
                try {
                    List<Book> books = bookController.getBookList();
                    for (int i = 0; i < books.size(); i++) {
                        Log.i("aidl", books.get(i).toString());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_addBook:
                try {
                    Random random = new Random();
                    Book book = new Book("book added " + random.nextInt());
                    bookController.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_addBookOutType:
                try {
                    book = new Book("hello world");
                    bookController.addBookOut(book);
                    Log.i("aidl", book.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unbindService(bookConn);
    }
}
