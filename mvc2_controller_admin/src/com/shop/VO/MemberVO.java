package com.shop.VO;

public class MemberVO {
	private int num;
	private int memberseq;
	
	private String id;
	private String pass;
	private String name;
	private String phone;
	private String address;
	private String role;
	private String enabled;
	private String paycard;
	private String account;
	
	
	
	
	

	@Override
	public String toString() {
		return "MemberVO [num=" + num + ", memberseq=" + memberseq + ", id="
				+ id + ", pass=" + pass + ", name=" + name + ", phone=" + phone
				+ ", address=" + address + ", role=" + role + ", enabled="
				+ enabled + ", paycard=" + paycard + ", account=" + account
				+ "]";
	}
	
	public int getMemberseq() {
		return memberseq;
	}
	public String getId() {
		return id;
	}
	public String getPass() {
		return pass;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddress() {
		return address;
	}
	public String getRole() {
		return role;
	}
	public String getEnabled() {
		return enabled;
	}

	public String getPaycard() {
		return paycard;
	}

	public String getAccount() {
		return account;
	}

	public void setPaycard(String paycard) {
		this.paycard = paycard;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	public void setMemberseq(int memberseq) {
		this.memberseq = memberseq;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}