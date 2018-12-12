package cn.lzh.bll;

import cn.lzh.dal.AbstractDAL;

public abstract class LibraryBLL {
	protected AbstractDAL dal;
	
	public String[] getDisplayColumnNames() {
		return dal.getPrettyColumnNames();
	}
	public String[] getMethodNames() {
		return dal.getMethodNames();
	}
	
	
}
