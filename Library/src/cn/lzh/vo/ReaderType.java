package cn.lzh.vo;

public class ReaderType extends AbstractModel{
	private int rdType; //读者类别
	private String rdTypeName;//读者类别名称
	private int canLendQty; //可借书数量
	private int canLendDay; //可借书数量
	private int canContinueTimes; //可续借的次数
	private float punishRate; //罚款率（元/天）
	private int dateValid; //证书有效期（年） 0表示永久有效
	
	public ReaderType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRdType() {
		return rdType;
	}

	public void setRdType(int rdType) {
		this.rdType = rdType;
	}

	public String getRdTypeName() {
		return rdTypeName;
	}

	public void setRdTypeName(String rdTypeName) {
		this.rdTypeName = rdTypeName;
	}

	public int getCanLendQty() {
		return canLendQty;
	}

	public void setCanLendQty(int canLendQty) {
		this.canLendQty = canLendQty;
	}

	public int getCanLendDay() {
		return canLendDay;
	}

	public void setCanLendDay(int canLendDay) {
		this.canLendDay = canLendDay;
	}

	public int getCanContinueTimes() {
		return canContinueTimes;
	}

	public void setCanContinueTimes(int canContinueTimes) {
		this.canContinueTimes = canContinueTimes;
	}

	public float getPunishRate() {
		return punishRate;
	}

	public void setPunishRate(float punishRate) {
		this.punishRate = punishRate;
	}

	public int getDateValid() {
		return dateValid;
	}

	public void setDateValid(int dateValid) {
		this.dateValid = dateValid;
	}

	@Override
	public String toString() {
		return this.rdTypeName;
	}

	
	
	
	
	
}
