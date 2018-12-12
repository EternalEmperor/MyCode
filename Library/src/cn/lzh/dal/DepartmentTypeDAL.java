package cn.lzh.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.lzh.vo.AbstractModel;
import cn.lzh.vo.DepartmentType;
import cn.lzh.db.SQLHelper;


public class DepartmentTypeDAL extends AbstractDAL {

	@Override
	public AbstractModel[] getAllObjects() throws Exception {
		// TODO 自动生成的方法存根
				ArrayList<DepartmentType> objects = new ArrayList<DepartmentType>();
				ResultSet rs  = SQLHelper.getResultSet("select * from tb_departmenttype");
				if(rs !=null){
					while(rs.next()){
						DepartmentType td = initDepartmentType(rs);
						objects.add(td);
					}
					rs.close();
				}
				DepartmentType[] types  =new DepartmentType[objects.size()];
				objects.toArray(types);
				
				return types;
	}

	@Override
	public int add(AbstractModel object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(AbstractModel object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(AbstractModel object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public AbstractModel getObjectByID(int rdDept) throws SQLException {
		return null;
	}
	
	private DepartmentType initDepartmentType(ResultSet rs) throws SQLException{
		DepartmentType td = new DepartmentType();
		td.setRdDept(rs.getString("rdDept"));
		return td;
	}

	@Override
	public String[] getPrettyColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMethodNames() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DepartmentType getObjectByID(String rdDept) throws SQLException {
		DepartmentType rdDeptType = null;
		String sql = "select * from tb_departmenttype where rdDept = ?";
		Object[] params = new Object[]{rdDept};
		ResultSet rs = SQLHelper.getResultSet(sql,params);
		if(rs!=null){
			if(rs.next()){
				rdDeptType = initDepartmentType(rs);
			}
			rs.close();
		}
		return rdDeptType;
	}

	
	

}
