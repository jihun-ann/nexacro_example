package com.shop.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.shop.VO.MemberVO;
import com.shop.VO.OrderProductVO;
import com.shop.VO.OrderVO;
import com.shop.VO.ProdOptionVO;
import com.shop.VO.ProductVO;

@Component
public class AdminDaoImpl extends SqlSessionDaoSupport implements AdminDao {

	public int insertProd(HashMap<String, Object> map) {
		return getSqlSession().insert("insertProd",map);
	}

	public int prodCurrVal() {
		return getSqlSession().selectOne("prodCurrVal");
	}

	public int insertProdOption(HashMap<String, Object> map) {
		return getSqlSession().insert("insertProdOption",map);
	}
	
	public int insertProdImage(HashMap<String, Object> map) {
		return getSqlSession().insert("insertProdImage",map);
	}

	public int countProdList(HashMap<String, Object> map) {
		return getSqlSession().selectOne("countProdList",map);
	}
	public List<ProductVO> selectProdList(HashMap<String, Object> map) {
		return getSqlSession().selectList("selectProdList",map);
	}

	public HashMap<String, Object> selectProdImage(String id) {
		return getSqlSession().selectOne("selectProdImage",id);
	}

	public List<ProdOptionVO> selectProdOption(String id) {
		return getSqlSession().selectList("selectProdOption",id);
	}

	public int updateProdAdmin(HashMap<String, Object> map) {
		return getSqlSession().update("updateProdAdmin",map);
	}

	public int deleteProdOption(HashMap<String, Object> map) {
		return getSqlSession().delete("deleteProdOption",map);
	}

	public int deleteProdImage(HashMap<String, Object> map) {
		return getSqlSession().delete("deleteProdImage",map);
	}

	public int prodDelete(HashMap<String, Object> map) {
		return getSqlSession().delete("prodDelete",map);
	}

	public List<MemberVO> memberList(HashMap<String, Object> map) {
		return getSqlSession().selectList("memberList",map);
	}

	public int memberListCount(HashMap<String, Object> map) {
		return getSqlSession().selectOne("memberListCount",map);
	}

	public int memberUpdateCompulsion(MemberVO vo) {
		return getSqlSession().update("memberUpdateCompulsion",vo);
	}

	public int prodOpCountAdmin(String id) {
		return getSqlSession().selectOne("prodOpCountAdmin",id);
	}
	
	public int selectOrderCount(HashMap<String, Object> map) {
		return getSqlSession().selectOne("selectOrderCount",map);
	}

	public List<OrderVO> selectOrderList(HashMap<String, Object> map) {
		return getSqlSession().selectList("selectOrderList",map);
	}

	public OrderVO selectOrderMember(String orderseq) {
		return getSqlSession().selectOne("selectOrderMember",orderseq);
	}

	public List<OrderProductVO> selectOrderProd(String orderseq) {
		return getSqlSession().selectList("selectOrderProd",orderseq);
	}

	public int updateOrderStatusAdmin(HashMap<String, Object> map) {
		return getSqlSession().update("updateOrderStatusAdmin",map);
	}

	public int updateProdCountAdmin(HashMap<String, Object> map) {
		return getSqlSession().update("updateProdCountAdmin",map);
	}

	public int deleteProdOptionBasket(HashMap<String, Object> map) {
		return getSqlSession().delete("deleteProdOptionBasket",map);
	}

	public int deleteMemberseq(HashMap<String, Object> map) {
		return getSqlSession().delete("deleteMemberseq",map);
	}

	public String selectmemberid(String memseq) {
		return getSqlSession().selectOne("selectmemberid",memseq);
	}

}
