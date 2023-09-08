package shop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import shop.VO.BasketVO;
import shop.VO.MemberVO;
import shop.VO.OrderProductVO;
import shop.VO.OrderVO;
import shop.VO.ProductVO;
import shop.VO.ProdOptionVO;

public class ShopDAO {
	private static ShopDAO instance = null;
	private ShopDAO(){}
	public static ShopDAO getInstance(){
		if(instance == null){
			synchronized (ShopDAO.class) {
				instance = new ShopDAO();
				
			}
		}
		return instance;
	}
	
	//상품 갯수 찾기
	public int selectProdListCount(HashMap<String, Object> map){
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT count(*) FROM "
				+ " (SELECT rownum AS num, pd.*,im.filename, op.productprice  FROM PRODUCTTABLE pd	"
					+ " LEFT JOIN (SELECT PRODUCTSEQ, min(PRODUCTPRICE) AS PRODUCTPRICE  FROM PRODUCT_OPTION GROUP BY PRODUCTSEQ ) op ON op.PRODUCTSEQ=pd.PRODUCTSEQ	"
					+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'simple')im ON im.PRODUCTSEQ=pd.PRODUCTSEQ	"
					+ " WHERE pd.name LIKE ?"
					+ " ORDER BY pd.productseq ASC)";
		
		String val = "%"+String.valueOf(map.get("val"))+"%";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, val);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	//상품 리스트 찾기
	public List<ProductVO> selectProdListMember(HashMap<String, Object> map){
		List<ProductVO> lst = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "select * from("
				+ " SELECT rownum AS num, a.* FROM ("
				+ " SELECT pd.*,im.filename, op.productprice  FROM PRODUCTTABLE pd"
				+ " LEFT JOIN (SELECT PRODUCTSEQ, min(PRODUCTPRICE) AS PRODUCTPRICE  FROM PRODUCT_OPTION GROUP BY PRODUCTSEQ ) op ON op.PRODUCTSEQ=pd.PRODUCTSEQ"
				+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'simple')im ON im.PRODUCTSEQ=pd.PRODUCTSEQ"
				+ " WHERE pd.name LIKE ?"
				+ " ORDER BY pd.productseq ASC) a"
				+ " ORDER BY num desc)"
				+ " WHERE num BETWEEN ? AND ? ";
		
		String val = "%"+String.valueOf(map.get("val"))+"%";
		String start = String.valueOf(map.get("startnum"));
		String ent = String.valueOf(map.get("endnum"));
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, val);
			ps.setString(2, start);
			ps.setString(3, ent);
			rs = ps.executeQuery();
			if(rs.next()){
				do {
					ProductVO vo = new ProductVO();
					vo.setNum(rs.getInt("num"));
					vo.setName(rs.getString("name"));
					vo.setProductseq(rs.getInt("productseq"));
					vo.setProductprice(rs.getInt("productprice"));
					vo.setFilename(rs.getString("filename"));
					lst.add(vo);
				} while (rs.next());
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return lst;
	}
	
	//아이디 찾기
	public String selectFindid(HashMap<String, Object> map){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "select id from member where name = ? and phone= ?";
		
		String name = String.valueOf(map.get("name"));
		String phone = String.valueOf(map.get("phone"));
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, phone);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//비밀번호 찾기
	public String selectFindPw(HashMap<String, Object> map){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "select pass from member where id = ? and phone= ?";
		
		String id = String.valueOf(map.get("id"));
		String phone = String.valueOf(map.get("phone"));
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, phone);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//로그인시 아이디, 패스워드 체크
	public String duplicateIdPw(HashMap<String, Object> map){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT memberseq FROM MEMBER WHERE id = ? AND pass = ?";
		
		String id = String.valueOf(map.get("id"));
		String pass = String.valueOf(map.get("pass"));
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, pass);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 로그 체크
	public String checkMemberLog(HashMap<String, Object> map){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT logseq FROM memberlog WHERE id = ?";
		
		String id = String.valueOf(map.get("id"));
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	
	//회원 로그인 로그 저장 및 업데이트
	public String insertMemberLog(HashMap<String, Object> map){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String id = String.valueOf(map.get("id"));
		String ip = String.valueOf(map.get("userip"));
		String browser = String.valueOf(map.get("browser"));
		String type = String.valueOf(map.get("type"));
		
		String query = "";
		if(type.equals("insert")){
			query = "insert into memberlog(logseq,userip,browser,id) values(log_seq.nextval, ?, ?, ?)";
		}else if(type.equals("update")){
			query = "update memberlog set userip = ?, browser =? where id = ? ";
		}
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, ip);
			ps.setString(2, browser);
			ps.setString(3, id);
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	
	//로그인 로그 삭제
	public String deleteMemberLog(String id){
		String result = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "delete from memberlog where id = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}

	
	//상품 정보 호출
	public ProductVO selectOneProd(String id){
		ProductVO result = new ProductVO();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "select * from producttable where productseq= ? ";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				result.setProductseq(rs.getInt("PRODUCTSEQ"));
				result.setName(rs.getString("NAME"));
				result.setOpcount(rs.getInt("OPCOUNT"));
				result.setOrdercount(rs.getInt("ORDERCOUNT"));
				result.setDate(rs.getString("DATE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//상품 이미지 정보 호출
	public HashMap<String, Object> selectProdImgAll(String id){
		HashMap<String, Object> map = new HashMap<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "SELECT pd.NAME, im.filename AS simple, im2.filename AS detail FROM PRODUCTTABLE pd"
				+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'simple') im on im.PRODUCTSEQ = pd.PRODUCTSEQ"
				+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'detail') im2 on im2.PRODUCTSEQ = pd.PRODUCTSEQ"
				+ " WHERE PRODUCTSEQ = ? ";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				map.put("NAME", rs.getString("NAME"));
				map.put("SIMPLE", rs.getString("SIMPLE"));
				map.put("DETAIL", rs.getString("DETAIL"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return map;
	}
	
	//상품 옵션 호출
	public List<ProdOptionVO> selectProdOptionAll(String id) {
		List<ProdOptionVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "select * from PRODUCT_OPTION where productseq = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				do{
					ProdOptionVO vo = new ProdOptionVO();
					vo.setOptionseq(rs.getInt("OPTIONSEQ"));
					vo.setProductseq(rs.getInt("PRODUCTSEQ"));
					vo.setName(rs.getString("NAME"));
					vo.setProductcount(rs.getInt("PRODUCTCOUNT"));
					vo.setProductprice(rs.getInt("PRODUCTPRICE"));
					result.add(vo);
				}while(rs.next());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	
	//아이디 중복 체크
	public String duplicateCheck(HashMap<String, Object> map) {
		String result = "0";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query ="";
		if (map.get("key").equals("id")){
			query = "SELECT nvl2(avg(memberseq),1,0) FROM MEMBER WHERE id = ?";
		}else if(map.get("key").equals("phone")){
			query = "SELECT nvl2(avg(memberseq),1,0) FROM MEMBER WHERE phone = ?";
		}
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("val")));
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	//회원가입
	public int signUpMember(MemberVO vo) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query ="insert into member(memberseq, id, pass, name, phone, address, role) values(member_seq.nextval,?,?,?,?,?,?)";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPass());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getPhone());
			ps.setString(5, vo.getAddress());
			ps.setString(6, vo.getRole());
			rs = ps.executeQuery();
			if(rs.next()){
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	
	//회원 번호 seq 찾기
	public int selectMemberSeq(String id) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query ="select memberseq from member where id = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	//회원 장바구니 상품 찾기
	public List<BasketVO> selectBasketMember(int id) {
		List<BasketVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "SELECT ba.BASKETSEQ, ba.PRODUCTSEQ, p.NAME productname, ba.OPTIONSEQ, op.NAME optionname, op.PRODUCTPRICE, ba.ORDERCOUNT, im.filename FROM basket ba"
				+ " LEFT JOIN PRODUCTTABLE p ON p.PRODUCTSEQ = ba.PRODUCTSEQ"
				+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'simple') im on im.PRODUCTSEQ = ba.PRODUCTSEQ"
				+ " LEFT JOIN PRODUCT_OPTION op ON op.OPTIONSEQ = ba.OPTIONSEQ"
				+ " WHERE memberseq = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				do{
					BasketVO vo = new BasketVO();
					vo.setBasketseq(rs.getInt("BASKETSEQ"));
					vo.setProductseq(rs.getInt("PRODUCTSEQ"));
					vo.setProductname(rs.getString("PRODUCTNAME"));
					vo.setOptionseq(rs.getInt("OPTIONSEQ"));
					vo.setOptionname(rs.getString("OPTIONNAME"));
					vo.setProductprice(rs.getInt("PRODUCTPRICE"));
					vo.setOrdercount(rs.getInt("ORDERCOUNT"));
					vo.setFilename(rs.getString("filename"));
					result.add(vo);
				}while(rs.next());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	//회원 정보 찾기
	public MemberVO findMemberId(String id) {
		MemberVO result = new MemberVO();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		String query = "select id, name, phone, address, paycard, account from member where id = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				result.setId(rs.getString("ID"));
				result.setName(rs.getString("NAME"));
				result.setPhone(rs.getString("PHONE"));
				result.setAddress(rs.getString("ADDRESS"));
				result.setPaycard(rs.getString("PAYCARD"));
				result.setAccount(rs.getString("ACCOUNT"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 정보 찾기
	public int memberReplace(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "";
		if(map.get("key").equals("pass")){
			query = "update member set pass = ? where id =?";
		}else if(map.get("key").equals("name")){
			query = "update member set name = ? where id =?";				
		}else if(map.get("key").equals("phone")){
			query = "update member set phone = ? where id =?";
		}else if(map.get("key").equals("address")){
			query = "update member set address = ? where id =?";
		}
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("val")));
			ps.setString(2, String.valueOf(map.get("id")));
			rs = ps.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = 1;
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 장바구니 내역 체크
	public HashMap<String, Object> basketCheck(HashMap<String, Object> map) {
		HashMap<String, Object> result = new HashMap<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT ORDERCOUNT, BASKETSEQ FROM BASKET WHERE PRODUCTSEQ = ? AND OPTIONSEQ = ? AND MEMBERSEQ  = ?";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prodSeq")));
			ps.setString(2, String.valueOf(map.get("opSeq")));
			ps.setString(3, String.valueOf(map.get("memseq")));
			
			rs = ps.executeQuery();
			if(rs.next()){
				result.put("ORDERCOUNT", rs.getString("ORDERCOUNT"));
				result.put("BASKETSEQ", rs.getString("BASKETSEQ"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 장바구니 추가
	public int insertBasket(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "insert into basket(basketseq,productseq,optionseq,ordercount,memberseq) values(basket_seq.nextval,?,?,?,?)";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prodSeq")));
			ps.setString(2, String.valueOf(map.get("opSeq")));
			ps.setString(3, String.valueOf(map.get("count")));
			ps.setString(4, String.valueOf(map.get("memseq")));
			rs = ps.executeQuery();
			if(rs.next()){
				result++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 장바구니 갯수 추가 업데이트
	public int updateBasketCount(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "update basket set ordercount = ? where basketseq = ?";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("count")));
			ps.setString(2, String.valueOf(map.get("check")));
			rs = ps.executeQuery();
			if(rs.next()){
				result++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원의 구매내역 
	public List<BasketVO> selectBasketProdSeq(HashMap<String, Object> map) {
		List<BasketVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT ba.BASKETSEQ, ba.PRODUCTSEQ, p.NAME productname, ba.OPTIONSEQ, op.NAME optionname, op.PRODUCTPRICE, ba.ORDERCOUNT, im.filename FROM basket ba"
						+ " LEFT JOIN PRODUCTTABLE p ON p.PRODUCTSEQ = ba.PRODUCTSEQ"
						+ " LEFT JOIN (SELECT * FROM PRODUCT_IMAGE WHERE SPACE = 'simple') im on im.PRODUCTSEQ = ba.PRODUCTSEQ"
						+ " LEFT JOIN PRODUCT_OPTION op ON op.OPTIONSEQ = ba.OPTIONSEQ"
						+ " WHERE ba.productseq =? and ba.memberseq = ?";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prodseq")));
			ps.setString(2, String.valueOf(map.get("member")));
			rs = ps.executeQuery();
			if(rs.next()){
				do {
					BasketVO vo = new BasketVO();
					vo.setBasketseq(rs.getInt("BASKETSEQ"));
					vo.setProductseq(rs.getInt("PRODUCTSEQ"));
					vo.setProductname(rs.getString("PRODUCTNAME"));
					vo.setOptionseq(rs.getInt("OPTIONSEQ"));
					vo.setOptionname(rs.getString("OPTIONNAME"));
					vo.setProductprice(rs.getInt("PRODUCTPRICE"));
					vo.setOrdercount(rs.getInt("ORDERCOUNT"));
					vo.setFilename(rs.getString("FILENAME"));
					result.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//상품의 잔여갯수 확인
	public int selectProductHasCount(String seq) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "select productcount from product_option where optionseq = ?";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, seq);
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//상품의 잔여갯수 업데이트
	public int updateOptionCount(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "update product_option set productcount = productcount - ? where optionseq = ?";
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(String.valueOf(map.get("order"))));
			ps.setString(2, String.valueOf(map.get("opseq")));
			rs = ps.executeQuery();
			if(rs.next()){
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 개인 결제정보 저장
	public int updatePayment(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "";
		if(map.get("kind").equals("account")){
			query = "update member set account = substr(?,3) where memberseq = ?";
		}else if(map.get("kind").equals("paycard")){
			query = "update member set paycard =substr(?,3) where memberseq = ?";
		}
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("payment")));
			ps.setString(2, String.valueOf(map.get("member")));
			rs = ps.executeQuery();
			if(rs.next()){
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 개인 장바구니 정보 호출
	public List<OrderProductVO> selectBasketPayment(HashMap<String, Object> map) {
		List<OrderProductVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT ba.productseq,pt.name AS productname, ba.optionseq, op.name AS optionname, ba.ordercount, ba.ORDERCOUNT*op.PRODUCTPRICE AS price, im.filename"
						+ " FROM BASKET ba"
						+ " LEFT JOIN PRODUCTTABLE pt ON ba.productseq = PT.PRODUCTSEQ"
						+ " LEFT JOIN PRODUCT_OPTION op ON ba.OPTIONSEQ = op.OPTIONSEQ"
						+ " LEFT JOIN (SELECT filename,PRODUCTSEQ  FROM PRODUCT_IMAGE WHERE SPACE = 'simple') im on im.PRODUCTSEQ = pt.PRODUCTSEQ"
						+ " WHERE ba.PRODUCTSEQ = ? AND ba.MEMBERSEQ = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prod")));
			ps.setString(2, String.valueOf(map.get("member")));
			rs = ps.executeQuery();
			if(rs.next()){
				do {
					OrderProductVO vo = new OrderProductVO();
					vo.setProductseq(rs.getInt("PRODUCTSEQ"));
					vo.setProductname(rs.getString("PRODUCTNAME"));
					vo.setOptionseq(rs.getInt("OPTIONSEQ"));
					vo.setOptionname(rs.getString("OPTIONNAME"));
					vo.setPrice(rs.getInt("PRICE"));
					vo.setFilename(rs.getString("FILENAME"));
					result.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//회원 개인 장바구니 정보 구매내역으로 저장
	public int insertOrder(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "insert into ordertable(orderseq,memberseq,orderproductcount,ordermessage,orderprice,payment,\"date\",deliverystatus,address) "
				+ " values(order_seq.nextval, ?,?,?,?,?,sysdate,0,?)";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("member")));
			ps.setString(2, String.valueOf(map.get("prodCount")));
			ps.setString(3, String.valueOf(map.get("message")));
			ps.setString(4, String.valueOf(map.get("price")));
			ps.setString(5, String.valueOf(map.get("payment")));
			ps.setString(6, String.valueOf(map.get("address")));
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//구매내역의 상세내역 저장
	public int insertOrderProduct(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "insert into order_producttable(opseq,orderseq,productseq,optionseq,ordercount,price,productname,optionname,filename)"
				+ " values(op_seq.nextval,order_seq.currval,?,?,?,?,?,?,?)";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prod")));
			ps.setString(2, String.valueOf(map.get("option")));
			ps.setString(3, String.valueOf(map.get("count")));
			ps.setString(4, String.valueOf(map.get("price")));
			ps.setString(5, String.valueOf(map.get("prodname")));
			ps.setString(6, String.valueOf(map.get("opname")));
			ps.setString(7, String.valueOf(map.get("filename")));
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//주문내역 저장 후 장바구니 삭제
	public int basketDeleteProd(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "delete from basket where productseq =? and memberseq =?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prod")));
			ps.setString(2, String.valueOf(map.get("member")));
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//상품 판매량 업데이트
	public int updateordercountProd(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "update producttable set ordercount =ordercount+1 where productseq=?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("prod")));
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	
	//장바구니 수량 수정 업데이트
	public int updateBasketOne(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "update basket set ordercount = ? where basketseq = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("count")));
			ps.setString(2, String.valueOf(map.get("seq")));
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//장바구니페이지에서 상품 삭제
	public int basketDeleteOne(String seq) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "delete from basket where basketseq =?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, seq);
			rs = ps.executeQuery();
			if(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//구매내역 갯수 호출
	public int selectOrderAllCount(HashMap<String, Object> map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "";
		
		if(!map.get("status").equals("")){
			query = "SELECT count(*)	FROM ordertable ot"
					+ " LEFT JOIN (SELECT max(PRODUCTSEQ) PRODUCTSEQ, orderseq, max(productname) productname, max(filename) filename FROM ORDER_PRODUCTTABLE GROUP BY orderseq) opt ON opt.orderseq = ot.ORDERSEQ"
					+ " where memberseq = ? and opt.productname like ?"
					+ " and deliverystatus = ?";
		}else{
			query = "SELECT count(*)	FROM ordertable ot"
					+ " LEFT JOIN (SELECT max(PRODUCTSEQ) PRODUCTSEQ, orderseq, max(productname) productname, max(filename) filename FROM ORDER_PRODUCTTABLE GROUP BY orderseq) opt ON opt.orderseq = ot.ORDERSEQ"
					+ " where memberseq =? and opt.productname like ?";
		}
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("member")));
			ps.setString(2, '%'+String.valueOf(map.get("val"))+'%');
			if(!map.get("status").equals("")){
				ps.setString(3, String.valueOf(map.get("status")));
			}
			rs = ps.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//구매내역 리스트 호출
	public List<OrderVO> selectOrderListMember(HashMap<String, Object> map) {
		List<OrderVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "";
		
		if(!map.get("status").equals("")){
			query = "SELECT * from("
					+ " SELECT rownum num, a.* FROM ("
					+ " SELECT Ot.orderseq, ot.\"date\",ot.ORDERPRODUCTCOUNT prodcount,ot.ORDERPRICE prodprice, opt.productname prodname, opt.filename, ot.deliverystatus"
					+ " FROM ORDERTABLE ot"
					+ " LEFT JOIN (SELECT max(PRODUCTSEQ) PRODUCTSEQ, orderseq, max(productname) productname, max(filename) filename FROM ORDER_PRODUCTTABLE GROUP BY orderseq) opt ON opt.orderseq = ot.ORDERSEQ"
					+ " WHERE ot.MEMBERSEQ = ? and opt.productname like ? and ot.deliverystatus =?"
				 	+ " ORDER BY ot.orderseq asc)a)"
				 	+ " WHERE num BETWEEN ? AND ?"
				 	+ " ORDER BY num desc";
		}else{
			query = "SELECT * from("
					+ " SELECT rownum num, a.* FROM ("
					+ " SELECT Ot.orderseq, ot.\"date\",ot.ORDERPRODUCTCOUNT prodcount,ot.ORDERPRICE prodprice, opt.productname prodname, opt.filename, ot.deliverystatus"
					+ " FROM ORDERTABLE ot"
					+ " LEFT JOIN (SELECT max(PRODUCTSEQ) PRODUCTSEQ, orderseq, max(productname) productname, max(filename) filename FROM ORDER_PRODUCTTABLE GROUP BY orderseq) opt ON opt.orderseq = ot.ORDERSEQ"
					+ " WHERE ot.MEMBERSEQ = ? and opt.productname like ?"
				 	+ " ORDER BY ot.orderseq asc)a)"
				 	+ " WHERE num BETWEEN ? AND ?"
				 	+ " ORDER BY num desc";
		}
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			if(!map.get("status").equals("")){
				ps.setString(1, String.valueOf(map.get("member")));
				ps.setString(2, '%'+String.valueOf(map.get("val"))+'%');
				ps.setString(3, String.valueOf(map.get("status")));
				ps.setString(4, String.valueOf(map.get("startNum")));
				ps.setString(5, String.valueOf(map.get("endNum")));
			}else{
				ps.setString(1, String.valueOf(map.get("member")));
				ps.setString(2, '%'+String.valueOf(map.get("val"))+'%');
				ps.setString(3, String.valueOf(map.get("startNum")));
				ps.setString(4, String.valueOf(map.get("endNum")));
			}
			rs = ps.executeQuery();
			if(rs.next()){
				do {
					OrderVO vo = new OrderVO();
					vo.setOrderseq(rs.getInt("ORDERSEQ"));
					vo.setDate(rs.getString("DATE"));
					vo.setProdcount(rs.getInt("PRODCOUNT"));
					vo.setProdprice(rs.getInt("PRODPRICE"));
					vo.setProdname(rs.getString("PRODNAME"));
					vo.setFilename(rs.getString("FILENAME"));
					vo.setDeliverystatus(rs.getString("DELIVERYSTATUS"));
					result.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//구매내역 정보 호출 - 상품정보
	public List<OrderProductVO> selectOrderInfoOne(String seq) {
		List<OrderProductVO> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT op.orderseq, op.PRODUCTSEQ, op.productname, op.OPTIONSEQ, op.optionname, op.ORDERCOUNT, op.PRICE, op.FILENAME FROM ORDER_PRODUCTTABLE op"
						+ " WHERE op.ORDERSEQ = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, seq);
			rs = ps.executeQuery();
			if(rs.next()){
				do {
					OrderProductVO vo = new OrderProductVO();
					vo.setOrderseq(rs.getInt("ORDERSEQ"));
					vo.setProductseq(rs.getInt("PRODUCTSEQ"));
					vo.setProductname(rs.getString("PRODUCTNAME"));
					vo.setOptionseq(rs.getInt("OPTIONSEQ"));
					vo.setOptionname(rs.getString("OPTIONNAME"));
					vo.setPrice(rs.getInt("PRICE"));
					vo.setFilename(rs.getString("FILENAME"));
					result.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	//구매내역 정보 호출 - 결제정보
	public OrderVO selectOrderOneOrderseq(String seq) {
		OrderVO result = new OrderVO();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT orderseq, \"date\", ORDERPRODUCTCOUNT  prodcount, orderprice prodprice,ordermessage, payment, deliverystatus, address FROM ORDERTABLE where orderseq =?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, seq);
			rs = ps.executeQuery();
			if(rs.next()){
				result.setOrderseq(rs.getInt("ORDERSEQ"));
				result.setDate(rs.getString("DATE"));
				result.setProdcount(rs.getInt("PRODCOUNT"));
				result.setProdprice(rs.getInt("PRODPRICE"));
				result.setOrdermessage(rs.getString("ORDERMESSAGE"));
				result.setPayment(rs.getString("PAYMENT"));
				result.setDeliverystatus(rs.getString("DELIVERYSTATUS"));
				result.setAddress(rs.getString("ADDRESS"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	//구매내역 배송정보 변경
	public int updateOrderStatus(HashMap<String, Object>map) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "update ordertable set deliverystatus = ? where orderseq = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, String.valueOf(map.get("status")));
			ps.setString(2, String.valueOf(map.get("orderseq")));
			rs = ps.executeQuery();
			if(rs.next()){
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
	
	//구매내역 배송정보 변경
	public HashMap<String, Object> loginLogCheck(String id) {
		HashMap<String, Object> result = new HashMap<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from memberlog where id = ?";
		
		
		try {
			conn= ConnUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				result.put("USERIP", rs.getString("USERIP"));
				result.put("BROWSER", rs.getString("BROWSER"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(ps!=null) try{ ps.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		return result;
	}
}
