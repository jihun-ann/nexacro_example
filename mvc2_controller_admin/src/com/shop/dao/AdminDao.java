package com.shop.dao;

import java.util.HashMap;
import java.util.List;

import com.shop.VO.MemberVO;
import com.shop.VO.OrderProductVO;
import com.shop.VO.OrderVO;
import com.shop.VO.ProdOptionVO;
import com.shop.VO.ProductVO;

public abstract interface AdminDao {
	public abstract int insertProd(HashMap<String, Object> map);
	
	public abstract int insertProdOption(HashMap<String, Object> map);
	
	public abstract int insertProdImage(HashMap<String, Object> map);
	
	public abstract int countProdList(HashMap<String, Object> map);
	
	public abstract List<ProductVO> selectProdList(HashMap<String, Object> map);
	
	public abstract int prodCurrVal();
	
	public abstract HashMap<String, Object> selectProdImage(String id);
	
	public abstract List<ProdOptionVO> selectProdOption(String id);
	
	public abstract int updateProdAdmin(HashMap<String, Object> map);
	
	public abstract int deleteProdOption(HashMap<String, Object> map);
	
	public abstract int deleteProdImage(HashMap<String, Object> map);
	
	public abstract int prodDelete(HashMap<String, Object> map);
	
	public abstract List<MemberVO> memberList(HashMap<String, Object> map);
	
	public abstract int memberListCount(HashMap<String, Object> map);

	public abstract int memberUpdateCompulsion(MemberVO vo);
	
	public abstract int prodOpCountAdmin(String id);
	
	public abstract int selectOrderCount(HashMap<String, Object> map);
	
	public abstract List<OrderVO> selectOrderList(HashMap<String, Object> map);
	
	public abstract OrderVO selectOrderMember(String orderseq);
	public abstract List<OrderProductVO> selectOrderProd(String orderseq);
	
	public abstract int updateOrderStatusAdmin(HashMap<String, Object> map);
	public abstract int updateProdCountAdmin(HashMap<String, Object> map);
	public abstract int deleteProdOptionBasket(HashMap<String, Object> map);
	public abstract int deleteMemberseq(HashMap<String, Object> map);
	public abstract String selectmemberid(String memseq);
}
