package cn.lzh.bll;

import java.sql.SQLException;

import cn.lzh.dal.BookDAL;
import cn.lzh.vo.Book;

public class BookAdmin extends LibraryBLL {
	
	public BookAdmin(){
		dal = new BookDAL();
	}
	
	public int addBook(Book book){
		try {
			return dal.add(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateBook(Book book){
		try {
			return dal.update(book);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public Book[] getBooksBy(String value,String content){
		try {
			return ((BookDAL)dal).getObjectBy(value, content);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Book[] getBookBy(String bkName,String bkAuthor,String bkPress){
		try {
			return ((BookDAL)dal).getObjectBy(bkName, bkAuthor,bkPress);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Book[] getBookByIdAndName(String bkID,String bkName){
		try {
			return ((BookDAL)dal).getObjectByIdAndName(bkID, bkName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int deleteBook(Book book){
		try {
			return dal.delete(book);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public Book[] getBooks(){
		try {
			return (Book[])dal.getAllObjects();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
