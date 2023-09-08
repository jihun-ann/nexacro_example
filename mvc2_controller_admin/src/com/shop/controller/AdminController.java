package com.shop.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.launcher.resources.launcher;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.shop.VO.MemberVO;
import com.shop.VO.OrderProductVO;
import com.shop.VO.OrderVO;
import com.shop.VO.ProdOptionVO;
import com.shop.VO.ProductVO;
import com.shop.dao.AdminDao;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataSetList;
import com.tobesoft.xplatform.data.DataTypes;
import com.tobesoft.xplatform.data.PlatformData;
import com.tobesoft.xplatform.data.VariableList;
import com.tobesoft.xplatform.tx.HttpPlatformRequest;
import com.tobesoft.xplatform.tx.HttpPlatformResponse;
import com.tobesoft.xplatform.tx.PlatformException;
import com.tobesoft.xplatform.tx.PlatformType;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminDao adminDao;
	@Value("C:/RootSystem/mvc2/WebContent/resources/image")
	private String path;

	@RequestMapping("/test.do")
	public void test(HttpServletRequest req, HttpServletResponse res) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList variableList = xpdata.getVariableList();
		//
		System.out.println(";;;;;;;;");
		
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/insertProd.do")
	@ResponseBody
	public void insertProd(HttpServletRequest req, HttpServletResponse res) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList variableList = xpdata.getVariableList();
		//
		DataSet prodDS = xpdata.getDataSet("prodInfo");
		String name = prodDS.getString(0, "name");
		
		DataSet optionDS = xpdata.getDataSet("prodOption");
		int count = optionDS.getRowCount();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("count", count);
		this.adminDao.insertProd(map);
		
		int prodSeq = this.adminDao.prodCurrVal();
		
		
		for(int i=0; i<count; i++){
			String opname = optionDS.getString(i, "name");
			String prodcount = optionDS.getString(i, "prodcount");
			String prodprice = optionDS.getString(i, "prodprice");
			
			
			map.put("productseq", prodSeq);
			map.put("opname", opname);
			map.put("prodcount", prodcount);
			map.put("prodprice", prodprice);
			
			this.adminDao.insertProdOption(map);
		}
		
		DataSet seq_data = new DataSet("seq_data");
		seq_data.addColumn("text",DataTypes.STRING,256);
		seq_data.newRow();
		seq_data.set(0, "text",prodSeq);
		
		xpdata.addDataSet(seq_data);
		
		String nErrorCode = "0";
		String nErrorMsg = "SUCC";
		
		variableList.add("ErrorCode",nErrorCode);
		variableList.add("ErrorMsg",nErrorMsg);
		
		HttpPlatformResponse pres = new HttpPlatformResponse(res,PlatformType.CONTENT_TYPE_XML,"UTF-8");
		pres.setData(xpdata);
		pres.sendData();
	}
	
	
	@RequestMapping("/fileSave.do")
	@ResponseBody
	public void fileSave(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("id")String id,@RequestParam("type")String type,
			@RequestParam(value="re", defaultValue="")String re
			) throws PlatformException, IOException{
		
		//시작시 필수 사용
		int nErrorCode = 0;
		String nErrorMsg = "START";
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		VariableList resVarList = xpdata.getVariableList();
		//
		int nMaxSize  = 500 * 1024 * 1024;
		
		MultipartRequest multires = new MultipartRequest(req,path,nMaxSize,"UTF-8",new DefaultFileRenamePolicy());
		Enumeration file = multires.getFileNames();

		String space = "";
		if(type.equals("0")){
			space = "simple";
		}else if(type.equals("1")){
			space = "detail";
		}
		
		if(!"".equals(re)){
			File dFile = new File(path+File.separator+re);
			dFile.delete();
			HashMap<String, Object> mm = new HashMap<>();
			mm.put("productseq", id);
			mm.put("space", space);
			this.adminDao.deleteProdImage(mm);
		}
		
		String sysname  = (String)file.nextElement();
		String fname = multires.getFilesystemName(sysname);
		String ext = FilenameUtils.getExtension(fname);
		
		File orgFile = new File(path+File.separator+fname);
		File newFile = new File(path+File.separator+id+"_"+type+"."+ext);
		
		
		orgFile.renameTo(newFile);
		
		HashMap<String , Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", fname);
		map.put("filename", newFile.getName());
		map.put("space", space);
		
		
		System.out.println(newFile.getName());
		int result = this.adminDao.insertProdImage(map);
		System.out.println(result);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/prodList.do")
	@ResponseBody
	public void prodList(HttpServletRequest req, HttpServletResponse res) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		DataSet inds = xpdata.getDataSet("pagingDS");
		String key = inds.getString(0, "key");
		String val = inds.getString(0, "val");
		String pageNum = inds.getString(0, "pageNum");
		
		
		HashMap<String, Object>map = new HashMap<>();
		if(val==null){
			val = "";
		}
		map.put("key", key);
		map.put("val", val);
		
		int count = this.adminDao.countProdList(map);
		
		int endpd = count-((Integer.parseInt(pageNum)-1)*5);
		int startpd = endpd - 4;
			startpd = startpd <=0? 1:startpd;
		
		map.put("startnum",startpd);
		map.put("endnum",endpd);
		List<ProductVO> lst = this.adminDao.selectProdList(map);
		
		DataSet outDS = new DataSet("prodDS");
		outDS.addColumn("num",DataTypes.STRING,256);
		outDS.addColumn("seq",DataTypes.STRING,256);
		outDS.addColumn("name",DataTypes.STRING,256);
		outDS.addColumn("opcount",DataTypes.STRING,256);
		outDS.addColumn("ordercount",DataTypes.STRING,256);
		outDS.addColumn("date",DataTypes.STRING,256);
		outDS.addColumn("filename",DataTypes.STRING,256);
		
		for (int i=0; i<lst.size(); i++) {
			int idx = outDS.newRow();
			outDS.set(idx, "num", lst.get(i).getNum());
			outDS.set(idx, "seq", lst.get(i).getProductseq());
			outDS.set(idx, "name", lst.get(i).getName());
			outDS.set(idx, "opcount", lst.get(i).getOpcount());
			outDS.set(idx, "ordercount", lst.get(i).getOrdercount());
			outDS.set(idx, "date", lst.get(i).getDate());
			outDS.set(idx, "filename", lst.get(i).getFilename());
		}
		
		
		int totalPage = count/5;
		if(count%5 != 0){
			totalPage++;
		}
		int startPage = (Integer.parseInt(pageNum)/10);
		if(Integer.parseInt(pageNum)%10 != 0){
			startPage = (startPage*10)+1;
		}
		int endPage = startPage+9;
			endPage = endPage < totalPage ? endPage : totalPage;
		
		DataSet outDS2 = new DataSet("pagingDS");
		outDS2.addColumn("key",DataTypes.STRING,256);
		outDS2.addColumn("val",DataTypes.STRING,256);
		outDS2.addColumn("pageNum",DataTypes.INT,5);
		outDS2.addColumn("startPage",DataTypes.INT,5);
		outDS2.addColumn("endPage",DataTypes.INT,5);
		outDS2.addColumn("totalPage",DataTypes.INT,5);
		outDS2.addColumn("count",DataTypes.INT,5);
			
		outDS2.newRow();
		outDS2.set(0, "key", key);
		outDS2.set(0, "val", val);
		outDS2.set(0, "pageNum", pageNum);
		outDS2.set(0, "startPage", startPage);
		outDS2.set(0, "endPage", endPage);
		outDS2.set(0, "totalPage", totalPage);
		outDS2.set(0, "count", count);
		
		
		DataSetList dsLst = new DataSetList();
		dsLst.add(outDS);
		dsLst.add(outDS2);
		
		xpdata.setDataSetList(dsLst);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/getProdOption.do")
	@ResponseBody
	public void getProdOption(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("id")String id) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		HashMap<String, Object> reqmap = this.adminDao.selectProdImage(id);
		DataSet outDS = new DataSet("reqProdDS");
		outDS.addColumn("name",DataTypes.STRING,256);
		outDS.addColumn("simple",DataTypes.STRING,256);
		outDS.addColumn("detail",DataTypes.STRING,256);
		outDS.newRow();
		
		outDS.set(0, "name", reqmap.get("NAME"));
		outDS.set(0, "simple", reqmap.get("SIMPLE"));
		outDS.set(0, "detail", reqmap.get("DETAIL"));
		
		
		List<ProdOptionVO> opLst = this.adminDao.selectProdOption(id);
		DataSet outDS2 = new DataSet("reqOpDS");
		outDS2.addColumn("opseq",DataTypes.STRING,256);
		outDS2.addColumn("id",DataTypes.STRING,256);
		outDS2.addColumn("name",DataTypes.STRING,256);
		outDS2.addColumn("prodcount",DataTypes.STRING,256);
		outDS2.addColumn("prodprice",DataTypes.STRING,256);
		
		for (int i=0; i<opLst.size(); i++) {
			int idx = outDS2.newRow();
			outDS2.set(idx, "opseq", opLst.get(i).getOptionseq());
			outDS2.set(idx, "id", opLst.get(i).getProductseq());
			outDS2.set(idx, "name", opLst.get(i).getName());
			outDS2.set(idx, "prodcount", opLst.get(i).getProductcount());
			outDS2.set(idx, "prodprice", opLst.get(i).getProductprice());
		}
		
		DataSetList dsl = new DataSetList();
		dsl.add(outDS);
		dsl.add(outDS2);
		
		xpdata.setDataSetList(dsl);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/prodRetouch.do")
	@ResponseBody
	public void prodRetouch(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("id")String id) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		DataSet inds = xpdata.getDataSet("reqProdDS");
		String prodname = inds.getString(0, "name");
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", prodname);
		map.put("id", id);
		
		DataSet inds3 = xpdata.getDataSet("reqOpDS");
		for(int i=0; i<inds3.getRowCount(); i++){
			String opseq = inds3.getString(i,"opseq");
			String count = inds3.getString(i, "prodcount");
			
			HashMap<String, Object> update = new HashMap<>();
			update.put("opseq", opseq);
			update.put("count", count);
			this.adminDao.updateProdCountAdmin(update);
		}
		
		DataSet inds2 = xpdata.getDataSet("newOpDS");
		for (int i=0; i<inds2.getRowCount(); i++) {
			String stat = inds2.getString(i, "status");
			
			HashMap<String, Object> map2 = new HashMap<>();
			if("new".equals(stat)){
				map2.put("productseq", id);
				map2.put("opname", inds2.getString(i, "name"));
				map2.put("prodcount", inds2.getString(i, "prodcount"));
				map2.put("prodprice", inds2.getString(i, "prodprice"));
				
				this.adminDao.insertProdOption(map2);
				
			}else if("delete".equals(stat)){
				map2.put("optionseq", inds2.getString(i, "opseq"));
				map2.put("productseq", inds2.getString(i, "id"));
				
				this.adminDao.deleteProdOptionBasket(map2);
				this.adminDao.deleteProdOption(map2);
			}
		}
		
		int recount = this.adminDao.prodOpCountAdmin(id);
		map.put("count", recount);
		this.adminDao.updateProdAdmin(map);
		
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/prodDelete.do")
	@ResponseBody
	public void prodDelete(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("id")String id) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		for (int i=0; i<4; i++) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("key", i);
			map.put("id", id);
			this.adminDao.prodDelete(map);
		}
		
		DataSet inds = xpdata.getDataSet("reqProdDS");
		for (int i=1; i<3; i++) {
			String filename = inds.getString(0, i);
			File file = new File(path+File.separator+filename);
			file.delete();
		}
		
		//장바구니, 주문내역 삭제
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	
	
	@RequestMapping("/memberList.do")
	@ResponseBody
	public void memberList(HttpServletRequest req, HttpServletResponse res
			)throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		DataSet inds = xpdata.getDataSet("pagingDS");
		String key = inds.getString(0, "key");
		String val = inds.getString(0, "val");
		String pageNum = inds.getString(0, "pageNum");
		
		HashMap<String, Object>map = new HashMap<>();
		if(val==null){
			val = "";
		}
		
		System.out.println(key);
		map.put("key", key);
		map.put("val", val);
		
		int count = this.adminDao.memberListCount(map);
		
		int endpd = count-((Integer.parseInt(pageNum)-1)*5);
		int startpd = endpd - 4;
			startpd = startpd <=0? 1:startpd;
		
		map.put("startnum",startpd);
		map.put("endnum",endpd);
		
		List<MemberVO> voL = this.adminDao.memberList(map);
		
		DataSet outds = new DataSet("memberDS");
		outds.addColumn("num",DataTypes.STRING,256);
		outds.addColumn("memberseq",DataTypes.STRING,256);
		outds.addColumn("id",DataTypes.STRING,256);
		outds.addColumn("name",DataTypes.STRING,256);
		outds.addColumn("pass",DataTypes.STRING,256);
		outds.addColumn("phone",DataTypes.STRING,256);
		outds.addColumn("address",DataTypes.STRING,256);
		outds.addColumn("enabled",DataTypes.STRING,256);
		
		for (int i=0; i<voL.size(); i++) {
			int idx = outds.newRow();
			outds.set(idx, "num", voL.get(i).getNum());
			outds.set(idx, "memberseq", voL.get(i).getMemberseq());
			outds.set(idx, "id", voL.get(i).getId());
			outds.set(idx, "name", voL.get(i).getName());
			outds.set(idx, "pass", voL.get(i).getPass());
			outds.set(idx, "phone", voL.get(i).getPhone());
			outds.set(idx, "address", voL.get(i).getAddress());
			outds.set(idx, "enabled", voL.get(i).getEnabled());
		}
		

		int totalPage = count/5;
		if(count%5 != 0){
			totalPage++;
		}
		int startPage = (Integer.parseInt(pageNum)/10);
		if(Integer.parseInt(pageNum)%10 != 0){
			startPage = (startPage*10)+1;
		}
		
		int endPage = startPage+9;
			endPage = endPage < totalPage ? endPage : totalPage;
		
		DataSet outDS2 = new DataSet("pagingDS");
		outDS2.addColumn("key",DataTypes.STRING,256);
		outDS2.addColumn("val",DataTypes.STRING,256);
		outDS2.addColumn("pageNum",DataTypes.INT,5);
		outDS2.addColumn("startPage",DataTypes.INT,5);
		outDS2.addColumn("endPage",DataTypes.INT,5);
		outDS2.addColumn("totalPage",DataTypes.INT,5);
		outDS2.addColumn("count",DataTypes.INT,5);
			
		outDS2.newRow();
		outDS2.set(0, "key", key);
		outDS2.set(0, "val", val);
		outDS2.set(0, "pageNum", pageNum);
		outDS2.set(0, "startPage", startPage);
		outDS2.set(0, "endPage", endPage);
		outDS2.set(0, "totalPage", totalPage);
		outDS2.set(0, "count", count);
		
		
		DataSetList dsLst = new DataSetList();
		dsLst.add(outds);
		dsLst.add(outDS2);
		
		xpdata.setDataSetList(dsLst);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/memberRetouch.do")
	@ResponseBody
	public void memberRetouch(HttpServletRequest req, HttpServletResponse res
			) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		DataSet inds = xpdata.getDataSet("reMemberDS");
		String seq = inds.getString(0, "memberseq");
		String name = inds.getString(0, "name");
		String pass = inds.getString(0, "pass");
		String phone = inds.getString(0, "phone");
		String address = inds.getString(0, "address");
		
		MemberVO vo = new MemberVO();
		vo.setMemberseq(Integer.parseInt(seq));
		vo.setName(name);
		vo.setPass(pass);
		vo.setPhone(phone);
		vo.setAddress(address);
		
		this.adminDao.memberUpdateCompulsion(vo);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	

	@RequestMapping("/orderList.do")
	@ResponseBody
	public void orderList(HttpServletRequest req, HttpServletResponse res) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		DataSet inds = xpdata.getDataSet("pagingDS");
		String key = inds.getString(0, "key");
		String val = inds.getString(0, "val");
		String pageNum = inds.getString(0, "pageNum");
		
		
		HashMap<String, Object>map = new HashMap<>();
		if(val==null){
			val = "";
		}
		if(key == null || "".equals(key)){
			key = "all";
		}
		
		map.put("key", key);
		map.put("val", val);
		
		int count = this.adminDao.selectOrderCount(map);
		
		
		int endpd = count-((Integer.parseInt(pageNum)-1)*5);
		int startpd = endpd - 4;
			startpd = startpd <=0? 1:startpd;
		
		map.put("startnum",startpd);
		map.put("endnum",endpd);
		List<OrderVO> lst = this.adminDao.selectOrderList(map);
		
		DataSet outDS = new DataSet("orderDS");
		outDS.addColumn("num",DataTypes.STRING,256);
		outDS.addColumn("orderseq",DataTypes.STRING,256);
		outDS.addColumn("date",DataTypes.STRING,256);
		outDS.addColumn("name",DataTypes.STRING,256);
		outDS.addColumn("status",DataTypes.STRING,256);
		outDS.addColumn("memberid",DataTypes.STRING,256);
		outDS.addColumn("membername",DataTypes.STRING,256);
		outDS.addColumn("filename",DataTypes.STRING,256);
		
		for (int i=0; i<lst.size(); i++) {
			
			if(lst.get(i).getProdcount() > 1){
				int prodcount = lst.get(i).getProdcount() - 1;
				lst.get(i).setProdname(lst.get(i).getProdname() + "외 "+prodcount+"건");
				System.out.println(lst.get(i).getProdname());
			}
			
			int idx = outDS.newRow();
			outDS.set(idx,"num",lst.get(i).getNum());
			outDS.set(idx,"orderseq",lst.get(i).getOrderseq());
			outDS.set(idx,"date",lst.get(i).getDate());
			outDS.set(idx,"name",lst.get(i).getProdname());
			outDS.set(idx,"status",lst.get(i).getDeliverystatus());
			outDS.set(idx,"memberid",lst.get(i).getMemberid());
			outDS.set(idx,"membername",lst.get(i).getMembername());
			outDS.set(idx,"filename",lst.get(i).getFilename());
		}
		
		int totalPage = count/5;
		if(count%5 != 0){
			totalPage++;
		}
		int startPage = (Integer.parseInt(pageNum)/10)*10;
			startPage++;
		int endPage = startPage+9;
			endPage = endPage < totalPage ? endPage : totalPage;
		
		DataSet outDS2 = new DataSet("pagingDS");
		outDS2.addColumn("key",DataTypes.STRING,256);
		outDS2.addColumn("val",DataTypes.STRING,256);
		outDS2.addColumn("pageNum",DataTypes.INT,5);
		outDS2.addColumn("startPage",DataTypes.INT,5);
		outDS2.addColumn("endPage",DataTypes.INT,5);
		outDS2.addColumn("totalPage",DataTypes.INT,5);
		outDS2.addColumn("count",DataTypes.INT,5);
			
		outDS2.newRow();
		outDS2.set(0, "key", key);
		outDS2.set(0, "val", val);
		outDS2.set(0, "pageNum", pageNum);
		outDS2.set(0, "startPage", startPage);
		outDS2.set(0, "endPage", endPage);
		outDS2.set(0, "totalPage", totalPage);
		outDS2.set(0, "count", count);
		
		
		DataSetList dsLst = new DataSetList();
		dsLst.add(outDS);
		dsLst.add(outDS2);
		
		xpdata.setDataSetList(dsLst);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/orderInfo.do")
	@ResponseBody
	public void orderInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("orderseq")String orderseq) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		OrderVO order = this.adminDao.selectOrderMember(orderseq);
		
		DataSet outds = new DataSet("orderDetailDS");
		outds.addColumn("date",DataTypes.STRING,256);
		outds.addColumn("memberid",DataTypes.STRING,256);
		outds.addColumn("membername",DataTypes.STRING,256);
		outds.addColumn("status",DataTypes.STRING,256);
		outds.addColumn("address",DataTypes.STRING,256);
		outds.addColumn("message",DataTypes.STRING,256);
		outds.addColumn("payment",DataTypes.STRING,256);
		
		outds.newRow();
		outds.set(0, "date", order.getDate());
		outds.set(0, "memberid", order.getMemberid());
		outds.set(0, "membername", order.getMembername());
		outds.set(0, "status", order.getDeliverystatus());
		outds.set(0, "address", order.getAddress());
		outds.set(0, "message", order.getOrdermessage());
		outds.set(0, "payment", order.getPayment());
		
		System.out.println(order.getPayment());
		
		
		DataSet outds2 = new DataSet("prodDS");
		outds2.addColumn("prodseq",DataTypes.STRING,256);
		outds2.addColumn("prodname",DataTypes.STRING,256);
		outds2.addColumn("optionseq",DataTypes.STRING,256);
		outds2.addColumn("optionname",DataTypes.STRING,256);
		outds2.addColumn("count",DataTypes.STRING,256);
		outds2.addColumn("price",DataTypes.STRING,256);
		outds2.addColumn("filename",DataTypes.STRING,256);
		
		List<OrderProductVO> prodLst = this.adminDao.selectOrderProd(orderseq);
		for (int i=0; i<prodLst.size(); i++) {
			int idx = outds2.newRow();
			outds2.set(idx, "prodseq", prodLst.get(i).getProductseq());
			outds2.set(idx, "prodname", prodLst.get(i).getProductname());
			outds2.set(idx, "optionseq", prodLst.get(i).getOptionseq());
			outds2.set(idx, "optionname", prodLst.get(i).getOptionname());
			outds2.set(idx, "count", prodLst.get(i).getOrdercount());
			outds2.set(idx, "price", prodLst.get(i).getPrice());
			outds2.set(idx, "filename", prodLst.get(i).getFilename());
		}
		
		DataSetList dsl = new DataSetList();
		dsl.add(outds);
		dsl.add(outds2);
		
		xpdata.setDataSetList(dsl);
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/orderStatus.do")
	@ResponseBody
	public void orderStatus(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("code")String code, @RequestParam("orderseq")String orderseq
			) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		HashMap<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("orderseq", orderseq);
		
		this.adminDao.updateOrderStatusAdmin(map);
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
	
	@RequestMapping("/memberDelete.do")
	@ResponseBody
	public void memberDelete(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("memseq")String memseq
			) throws PlatformException{
		//시작시 필수 사용
		HttpPlatformRequest preq = new HttpPlatformRequest(req);
		PlatformData xpdata = new PlatformData();
		preq.receiveData();
		xpdata = preq.getData();
		VariableList resVarList = xpdata.getVariableList();
		//
		
		String id = this.adminDao.selectmemberid(memseq);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("memseq", memseq);
		map.put("member", id);
		
		for (int i=0; i<3; i++) {
			map.put("key", i);
			this.adminDao.deleteMemberseq(map);
		}
		
		
		resVarList.add("ErrorCode", 200);
		resVarList.add("ErrorMsg", "파일저장에러");
	
		HttpPlatformResponse pres = new HttpPlatformResponse(res);
		pres.setData(xpdata);
		pres.sendData();
	}
}
