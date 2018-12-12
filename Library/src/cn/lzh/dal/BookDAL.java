package cn.lzh.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cn.lzh.db.SQLHelper;
import cn.lzh.vo.AbstractModel;
import cn.lzh.vo.Book;
import cn.lzh.vo.ReaderType;

public class BookDAL extends AbstractDAL {

	private String[] dispColNames = new String[]{
			"序号","书名","作者","出版社","出版时间","价格","入馆时间","状态"
	};
	
	private String[] methodNames = new String[]{
		"getBkID","getBkName","getBkAuthor","getBkPress","getBkDatePress"
		,"getBkPrice","getBkDateIn","getBkStatus"
	};
	
	@Override
	public AbstractModel[] getAllObjects() throws Exception {
		ArrayList<Book> objects = new ArrayList<Book>();
		ResultSet rs = SQLHelper.getResultSet("select * from tb_book");
		if(rs != null){
			while(rs.next()){
				Book bk = initBook(rs);
				objects.add(bk);
			}
			rs.close();
		}
		Book[] books = new Book[objects.size()];
		objects.toArray(books);
		return books;
	}

	@Override
	public int add(AbstractModel object) throws Exception {
		if(object instanceof Book == false){
			throw new Exception("Can only handle Book");
		}
		Book bk = (Book)object;
		Book hvbk = (Book)getObjectByID(bk.getBkID());
		if(hvbk != null){
			JOptionPane.showMessageDialog(null, "已存在该书！");
			return 0;
		}
		String sql = "insert into tb_book ("
				+ "bkID,bkName,bkAuthor,bkPress,bkDatePress,"
				+ "bkPrice,bkDateIn,bkBrief,bkCover,bkStatus) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[]{
			bk.getBkID(),
			bk.getBkName(),
			bk.getBkAuthor(),
			bk.getBkPress(),
			bk.getBkDatePress(),
			bk.getBkPrice(),
			bk.getBkDateIn(),
			bk.getBkBrief(),
			bk.getBkCover(),
			bk.getBkStatus()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int delete(AbstractModel object) throws Exception {
		if(object instanceof Book == false){
			throw new Exception("Can only handle Book");
		}
		Book bk = (Book)object;
		String sql = "delete from tb_book where bkID = ?";
		Object[] params = new Object[]{bk.getBkID()};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int update(AbstractModel object) throws Exception {
		if(object instanceof Book == false){
			throw new Exception("Can only handle Book");
		}
		Book bk = (Book)object;
		String sql = "update tb_book set bkName = ?,bkAuthor = ?,"
				+ "bkPress = ?,bkDatePress = ?,bkPrice = ?,"
				+ "bkDateIn = ?,bkBrief = ?,bkCover = ?,"
				+ "bkStatus = ? where bkID = ?";
		Object[] params = new Object[]{
				bk.getBkName(),
				bk.getBkAuthor(),
				bk.getBkPress(),
				bk.getBkDatePress(),
				bk.getBkPrice(),
				bk.getBkDateIn(),
				bk.getBkBrief(),
				bk.getBkCover(),
				bk.getBkStatus(),
				bk.getBkID()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public AbstractModel getObjectByID(int bkID) throws SQLException {
		Book bk = null;
		ResultSet rs = SQLHelper.getResultSet(""
				+ "select bkID,bkName,bkAuthor,bkPress,"
				+ "bkDatePress,bkPrice,bkDateIn,bkBrief,"
				+ "bkCover,bkStatus from tb_book where bkID ="
				+ bkID);
		if(rs != null){
			if(rs.next()){
				bk = initBook(rs);
			}
			rs.close();
		}
		return bk;
	}
	
	private Book initBook(ResultSet rs) throws SQLException{
		Book bk = new Book();
		bk.setBkID(rs.getInt("bkID"));
		bk.setBkName(rs.getString("bkName"));
		bk.setBkAuthor(rs.getString("bkAuthor"));
		bk.setBkPress(rs.getString("bkPress"));
		bk.setBkDatePress(rs.getDate("bkDatePress"));
		bk.setBkPrice(rs.getFloat("bkPrice"));
		bk.setBkDateIn(rs.getDate("bkDateIn"));
		bk.setBkBrief(rs.getString("bkBrief"));
		bk.setBkCover(rs.getBytes("bkCover"));
		bk.setBkStatus(rs.getString("bkStatus"));
		return bk;
	}
	
	public Book[] getObjectBy(String value,String content) throws SQLException{
		ArrayList<Book> books = new ArrayList<Book>();
		String sql = "Select * from tb_book where "
				+ value +" like ?";
		Object[] params = new Object[]{"%"+ content + "%"};
		ResultSet rs = SQLHelper.getResultSet(sql,params);
		if(rs != null){
			while(rs.next()){
				Book book = initBook(rs);
				books.add(book);
			}
			rs.close();
		}
		if(books.size() > 0){
			Book[] array = new Book[books.size()];
			books.toArray(array);
			return array;
		}
		return null;
	}
	
	public Book[] getObjectByIdAndName(String bkID, String bkName) throws SQLException{
		ArrayList<Book> books = new ArrayList<Book>();
		String sql = "select * from tb_book where bkID like ? "
				+ "and bkName like ? ";
		Object[] params = new Object[]{
			"%" + bkID + "%",
			"%" + bkName + "%"
		};
		ResultSet rs = SQLHelper.getResultSet(sql,params);
		if(rs != null){
			while(rs.next()){
				Book book = initBook(rs);
				books.add(book);
			}
			rs.close();
		}
		if(books.size() > 0){
			Book[] array = new Book[books.size()];
			books.toArray(array);
			return array;
		}
		return null;
	}
	
	public Book[] getObjectBy(String bkName,String bkAuthor,String bkPress) throws SQLException{
		ArrayList<Book> books = new ArrayList<Book>();
		String sql = "select * from tb_book where bkName like ? "
				+ "and bkAuthor like ? and bkPress like ?";
		Object[] params = new Object[]{
			"%" + bkName + "%",
			"%" + bkAuthor + "%",
			"%" + bkPress + "%"
		};
		ResultSet rs = SQLHelper.getResultSet(sql,params);
		if(rs != null){
			while(rs.next()){
				Book book = initBook(rs);
				books.add(book);
			}
			rs.close();
		}
		if(books.size() > 0){
			Book[] array = new Book[books.size()];
			books.toArray(array);
			return array;
		}
		return null;
		
	}


	@Override
	public String[] getMethodNames() {
		return this.methodNames;
	}

	@Override
	public String[] getPrettyColumnNames() {
		return this.dispColNames;
	}

}
