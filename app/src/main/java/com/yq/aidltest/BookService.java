package com.yq.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class BookService extends Service {

    private List<Book> bookList;

    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();
        Log.i("aidl", "service create");
        Book book1 = new Book("活着");
        Book book2 = new Book("或者");
        Book book3 = new Book("叶应是叶");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private BookController.Stub mBinder = new BookController.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (null != book)
                bookList.add(book);
            else
                Log.i("aidl", "the name of book added is not specified!");
        }

        /**
         * 此种方法传递进来的对象即使设置了属性值在这里获取到的值也为null
         * @param book 客户端传递对象
         * @throws RemoteException 异常
         */
        @Override
        public void addBookOut(Book book) throws RemoteException {
            //book.getName() 值为 null
            book.setName("name verified by server" + book.getName());
            bookList.add(book);
        }
    };

}
