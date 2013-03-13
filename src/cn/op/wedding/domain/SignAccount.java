package cn.op.wedding.domain;

/**
 * 签到人
 * 
 * @author Frank
 * 
 */
public class SignAccount {

	public String name;
	public String relation;
	public String msg;
	public String date;

	public SignAccount(String name, String relation, String msg, String date) {
		super();
		this.name = name;
		this.relation = relation;
		this.msg = msg;
		this.date = date;
	}

}
