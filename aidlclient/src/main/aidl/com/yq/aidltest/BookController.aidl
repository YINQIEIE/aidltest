// BookController.aidl
package com.yq.aidltest;
import com.yq.aidltest.Book;

interface BookController {

        List<Book> getBookList();
        void addBook(inout Book book);
        void addBookOut(out Book book);

}
