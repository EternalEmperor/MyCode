package cn.lzh.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cn.lzh.db.*;

import cn.lzh.vo.AbstractModel;
import cn.lzh.vo.ReaderType;

public class ReaderTypeDAL extends AbstractDAL {

	private String[] dispColNames = new String[]{
			"ID","读者类型","可借数量","可借天数","可续借次数","罚金率","证件有效期"
	};
	private String[] methodNames = new String[]{
			"getRdType","getRdTypeName","getCanLendQty","getCanLendDay","getCanContinueTimes"
			,"getPunishRate","getDateValid"
	};
	
	@Override
	public AbstractModel[] getAllObjects() throws Exception {
		ArrayList<ReaderType> objects = new ArrayList<ReaderType>();
		ResultSet rs = SQLHelper.getResultSet("select * from tb_readertype");
		if(rs != null){
			while(rs.next()){
				ReaderType rt = initReaderType(rs);
				objects.add(rt);
			}
			rs.close();
		}
		ReaderType[] types = new ReaderType[objects.size()];
		objects.toArray(types);
		return types;
	}
	

	@Override
	public int add(AbstractModel object) throws Exception {
		if(object instanceof ReaderType == false){
			throw new Exception("Can only handle ReaderType");
		}
		ReaderType rt = (ReaderType)object;
		ReaderType hvrt = (ReaderType)getObjectByID(rt.getRdType());
		if(hvrt != null){
			JOptionPane.showMessageDialog(null, "已存在该类型！");
			return 0;
		}
		
		String sql = "insert into tb_readertype ("
				+ "rdType,rdTypeName,CanLendQty,CanLendDay,"
				+ "CanContinueTimes,PunishRate,DateValid) "
				+ "values(?,?,?,?,?,?,?)";
		Object[] params = new Object[7];
		params[0] = rt.getRdType();
		params[1] = rt.getRdTypeName();
		params[2] = rt.getCanLendQty();
		params[3] = rt.getCanLendDay();
		params[4] = rt.getCanContinueTimes();
		params[5] = rt.getPunishRate();
		params[6] = rt.getDateValid();
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int delete(AbstractModel object) throws Exception {
		if(object instanceof ReaderType == false){
			throw new Exception("Can only handle ReaderType");
		}
		ReaderType rt = (ReaderType)object;
		String sql = "delete from tb_readertype where rdType = ?";
		Object[] params = new Object[]{rt.getRdType()};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int update(AbstractModel object) throws Exception {
		if(object instanceof ReaderType == false){
			throw new Exception("Can only handle ReaderType");
		}
		ReaderType rt = (ReaderType)object;
		String sql = "update tb_readertype set rdTypeName = ?, "
				+ "CanLendQty = ?, CanLendDay = ?, "
				+ "CanContinueTimes = ?, PunishRate = ?, "
				+ "DateValid = ? where rdType = ?";
		Object[] params = new Object[]{
			rt.getRdTypeName(),
			rt.getCanLendQty(),
			rt.getCanLendDay(),
			rt.getCanContinueTimes(),
			rt.getPunishRate(),
			rt.getDateValid(),
			rt.getRdType()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public AbstractModel getObjectByID(int rdType) throws SQLException {
		ReaderType rt = null;
		ResultSet rs = SQLHelper.getResultSet(""
				+ "select rdType,rdTypeName,CanLendQty,"
				+ "CanLendDay,CanContinueTimes,PunishRate,"
				+ "DateValid from tb_readertype where rdType ="
				+ rdType);
		if(rs != null){
			if(rs.next()){
				rt = initReaderType(rs);
			}
			rs.close();
		}
		return rt;
	}
	
	public ReaderType getObjectByName(String rdTypeName) throws Exception {
		// TODO 自动生成的方法存根
		ReaderType rt = null;
		ResultSet rs = SQLHelper.getResultSet("select * from tb_readertype where rdTypeName = "+rdTypeName);
		if(rs!=null){
			if(rs.next()){
				rt = initReaderType(rs);
			}
			rs.close();
		}
		return rt;
	}
	
	private ReaderType initReaderType(ResultSet rs) throws SQLException{
		ReaderType rt = new ReaderType();
		rt.setRdType(rs.getInt("rdType"));
		rt.setRdTypeName(rs.getString("rdTypeName"));
		rt.setCanLendQty(rs.getInt("CanLendQty"));
		rt.setCanLendDay(rs.getInt("CanLendDay"));
		rt.setCanContinueTimes(rs.getInt("CanContinueTimes"));
		rt.setPunishRate(rs.getFloat("PunishRate"));
		rt.setDateValid(rs.getInt("DateValid"));
		return rt;
	}
	
	@Override
	public String[] getPrettyColumnNames() {
		return dispColNames;
	}

	@Override
	public String[] getMethodNames() {
		return methodNames;
	}

}
