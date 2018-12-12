package cn.lzh.bll;

import java.sql.SQLException;

import cn.lzh.dal.ReaderTypeDAL;
import cn.lzh.vo.ReaderType;

public class ReaderTypeAdmin extends LibraryBLL {
		
	public ReaderTypeAdmin(){
		dal = new ReaderTypeDAL();
	}
	
	public ReaderType[] getReaderTypes(){
		try{
			return (ReaderType[])dal.getAllObjects();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ReaderType getObjectById(int rdType) {
		try{
			return (ReaderType)dal.getObjectByID(rdType);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public int addReaderType(ReaderType readertype){
		try {
			return dal.add(readertype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateReaderType(ReaderType readertype){
		try {
			return dal.update(readertype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteReaderType(ReaderType readertype){
		try {
			return dal.delete(readertype);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
