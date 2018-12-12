package cn.lzh.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.mysql.cj.protocol.a.result.ResultsetRowsStatic;

import cn.lzh.vo.Reader;
import cn.lzh.vo.ReaderType;
import cn.lzh.db.SQLHelper;
import cn.lzh.vo.AbstractModel;
import cn.lzh.vo.DepartmentType;

public class ReaderDAL extends AbstractDAL {

	private String[] dispColNames = new String[]{
		"ID","姓名","性别","类型","单位","电话","状态","已借书数量","注册日期"
	};
	private String[] methodNames = new String[]{
		"getRdID","getRdName","getRdSex","getRdType","getRdDept",
		"getRdPhone","getRdStatus","getRdBorrowQty","getRdDateReg"
	};
	@Override
	public AbstractModel[] getAllObjects() throws Exception {
		ArrayList<Reader> objects = new ArrayList<Reader>();
		ResultSet rs = SQLHelper.getResultSet("select * from tb_reader");
		if(rs != null){
			while(rs.next()){
				Reader rd = initReader(rs);
				objects.add(rd);
			}
			rs.close();
		}
		Reader[] readers = new Reader[objects.size()];
		objects.toArray(readers);
		return readers;
	}

	@Override
	public int add(AbstractModel object) throws Exception {
		if(object instanceof Reader == false){
			throw new Exception("Can only handle Reader");
		}
		Reader rd = (Reader)object;
		Reader hvrd = (Reader)getObjectByID(rd.getRdID());
		if(hvrd != null){
			JOptionPane.showMessageDialog(null, "已存在该用户！");
			return 0;
		}
		String sql = "insert into tb_reader ("
				+ "rdID,rdName,rdSex,rdType,rdDept,"
				+ "rdPhone,rdDateReg,"
				+ "rdStatus,rdBorrowQty,rdPwd,rdAdminRoles) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[]{
			rd.getRdID(),
			rd.getRdName(),
			rd.getRdSex(),
			rd.getRdType(),
			rd.getRdDept(),
			rd.getRdPhone(),
			rd.getRdDateReg(),
			rd.getRdStatus(),
			rd.getRdBorrowQty(),
			rd.getRdPwd(),
			rd.getRdAdminRoles()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int delete(AbstractModel object) throws Exception {
		if(object instanceof Reader == false){
			throw new Exception("Can only handle Reader");
		}
		Reader rd = (Reader)object;
		String sql = "delete from tb_reader where rdID = ?";
		Object[] params = new Object[]{rd.getRdID()};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int update(AbstractModel object) throws Exception {
		if(object instanceof Reader == false){
			throw new Exception("Can only handle Reader");
		}
		Reader rd = (Reader)object;
		String sql = "update tb_reader set rdName = ?,"
				+ "rdSex = ?, rdType = ?, rdDept = ?,rdPhone = ?, rdDateReg = ?,"
				+ "rdStatus = ?, rdBorrowQty = ?, rdPwd = ?, rdAdminRoles = ? "
				+ "where rdID = ?";
		Object[] params = new Object[]{
			rd.getRdName(),
			rd.getRdSex(),
			rd.getRdType(),
			rd.getRdDept(),
			rd.getRdPhone(),
			rd.getRdDateReg(),
			rd.getRdStatus(),
			rd.getRdBorrowQty(),
			rd.getRdPwd(),
			rd.getRdAdminRoles(),
			rd.getRdID()
		};
		return SQLHelper.ExecSql(sql,params);
	}
	
	public int updatePwd(AbstractModel object,String password) throws Exception{
		if(object instanceof Reader == false){
			throw new Exception("Can only handle Reader");
		}
		Reader rd = (Reader)object;
		String sql = "update tb_reader set rdPwd = ? where rdID = ?";
		Object[] params = new Object[]{
				password,
				rd.getRdID()
		};
		return SQLHelper.ExecSql(sql,params);
	}
	
	public int updateAdmin(AbstractModel object,int Admin) throws Exception{
		if(object instanceof Reader == false){
			throw new Exception("Can only handle Reader");
		}
		Reader rd = (Reader)object;
		String sql = "update tb_reader set rdAdminRoles = ? where rdID = ?";
		Object[] params = new Object[]{
				Admin,
				rd.getRdID()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public AbstractModel getObjectByID(int rdID) throws SQLException {
		Reader rd = null;
		//select rdID,rdName,rdSex,rdType,rdDateReg,rdStatus,rdBorrowQty,rdPwd from tb_reader where rdID = 1
		ResultSet rs = SQLHelper.getResultSet("select rdID,rdName,rdSex,rdType,rdDept,rdPhone,rdDateReg,rdStatus,"
				+ "								rdBorrowQty,rdPwd,rdAdminRoles from tb_reader where rdID =" + rdID);
		if(rs != null){
			if(rs.next()){
				rd = initReader(rs);
			}
			rs.close();
		}
		return rd;
	}
	
	private Reader initReader(ResultSet rs) throws SQLException{
		Reader rd = new Reader();
		rd.setRdID(rs.getInt("rdID"));
		rd.setRdName(rs.getString("rdName"));
		rd.setRdSex(rs.getString("rdSex"));
		rd.setRdType(rs.getInt("rdType"));
		rd.setRdDept(rs.getString("rdDept"));
		rd.setRdPhone(rs.getString("rdPhone"));
		rd.setRdDateReg(rs.getDate("rdDateReg"));
		rd.setRdStatus(rs.getString("rdStatus"));
		rd.setRdBorrowQty(rs.getInt("rdBorrowQty"));
		rd.setRdPwd(rs.getString("rdPwd"));
		rd.setRdAdminRoles(rs.getInt("rdAdminRoles"));
		return rd;
	}

	@Override
	public String[] getPrettyColumnNames() {
		// TODO Auto-generated method stub
		return dispColNames;
	}

	@Override
	public String[] getMethodNames() {
		// TODO Auto-generated method stub
		return methodNames;
	}
	

	
	public Reader[] getReadersBy(ReaderType rdType, DepartmentType deptType,
			String userName) throws SQLException{
		ArrayList<Reader> readers = new ArrayList<Reader>();
		String sql = "Select * from tb_reader where rdType = ? and "
				+ "rdDept = ? and rdName like ?";
		Object[] params = new Object[]{rdType.getRdType(),
				deptType.getRdDept(),"%" + userName + "%"};
		ResultSet rs = SQLHelper.getResultSet(sql,params);
		if(rs != null){
			while(rs.next()){
				Reader reader = initReader(rs);
				readers.add(reader);
			}
			rs.close();
		}
		if(readers.size() > 0){
			Reader[] array = new Reader[readers.size()];
			readers.toArray(array);
			return array;
		}
		return null;
	
	}

}
