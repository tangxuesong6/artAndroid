package com.txs.artandroid.c2;
import  com.txs.artandroid.c2.Book;
import  com.txs.artandroid.c2.IOnNewBookArrivedListener;

interface IBookManager{
List<Book> getBookList();
void addBook(in Book book);
void registerListener(IOnNewBookArrivedListener listener);
void unregisterListener(IOnNewBookArrivedListener listener);
}
