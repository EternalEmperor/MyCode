package cn.lzh.bll;

import cn.lzh.dal.DepartmentTypeDAL;
import cn.lzh.vo.DepartmentType;

public class DepartmentTypeAdmin extends LibraryBLL {
	
	public DepartmentTypeAdmin(){
		dal = new DepartmentTypeDAL();
	}
	
	public DepartmentType[] getDepartmentTypes(){
		try{
			return (DepartmentType[]) dal.getAllObjects();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DepartmentType getObjectById(String rdDpet) {
		try{
			return ((DepartmentTypeDAL)dal).getObjectByID(rdDpet);
		}catch (Exception e) {
			// TODO: handle exceptionW
			e.printStackTrace();
		}
		return null;
	}
}
