package cn.lzh.vo;

import java.sql.Date;

public class Book extends AbstractModel{
	private int bkID; //图书序号
	private String bkName; //书名
	private String bkAuthor; //作者
	private String bkPress; //出版社
	private Date bkDatePress; //出版日期
	private float bkPrice; //价格
	private Date bkDateIn; //入馆日期
	private String bkBrief; //内容简介
	private byte[] bkCover; //图书封面照片
	private String bkStatus; //图书状态，在馆，出借，遗失，变卖，销毁
	
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBkID() {
		return bkID;
	}

	public void setBkID(int bkID) {
		this.bkID = bkID;
	}

	public String getBkName() {
		return bkName;
	}

	public void setBkName(String bkName) {
		this.bkName = bkName;
	}

	public String getBkAuthor() {
		return bkAuthor;
	}

	public void setBkAuthor(String bkAuthor) {
		this.bkAuthor = bkAuthor;
	}

	public String getBkPress() {
		return bkPress;
	}

	public void setBkPress(String bkPress) {
		this.bkPress = bkPress;
	}

	public Date getBkDatePress() {
		return bkDatePress;
	}

	public void setBkDatePress(Date bkDatePress) {
		this.bkDatePress = bkDatePress;
	}

	public float getBkPrice() {
		return bkPrice;
	}

	public void setBkPrice(float bkPrice) {
		this.bkPrice = bkPrice;
	}

	public Date getBkDateIn() {
		return bkDateIn;
	}

	public void setBkDateIn(Date bkDateIn) {
		this.bkDateIn = bkDateIn;
	}

	public String getBkBrief() {
		return bkBrief;
	}

	public void setBkBrief(String bkBrief) {
		this.bkBrief = bkBrief;
	}


	public byte[] getBkCover() {
		return bkCover;
	}

	public void setBkCover(byte[] bkCover) {
		this.bkCover = bkCover;
	}

	public String getBkStatus() {
		return bkStatus;
	}

	public void setBkStatus(String bkStatus) {
		this.bkStatus = bkStatus;
	}

	@Override
	public String toString() {
		return "Book [bkID=" + bkID + ", bkName=" + bkName + ", bkAuthor=" + bkAuthor + ", bkPress=" + bkPress
				+ ", bkDatePress=" + bkDatePress + ", bkPrice=" + bkPrice + ", bkDateIn=" + bkDateIn + ", bkBrief="
				+ bkBrief + ", bkCover=" + bkCover + ", bkStatus=" + bkStatus + "]";
	}
	
	
	
	
}
