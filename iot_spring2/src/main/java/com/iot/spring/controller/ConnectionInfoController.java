package com.iot.spring.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.spring.service.ConnectionInfoService;
import com.iot.spring.vo.ColumnVO;
import com.iot.spring.vo.ConnectionInfoVO;
import com.iot.spring.vo.TableVO;
import com.iot.spring.vo.UserInfoVO;

@Controller
@RequestMapping("/connection")
public class ConnectionInfoController {
	private static final Logger log = LoggerFactory.getLogger(UrlController.class);
	@Autowired
	private ConnectionInfoService cis;

	@RequestMapping("/list")
	public @ResponseBody Map<String, Object> getConnectionList(HttpSession hs, Map<String, Object> map) {
		UserInfoVO ui = new UserInfoVO();
		if (hs.getAttribute("user") != null) {
			ui.setUiId(hs.getAttribute("user").toString());
		} else {
			ui.setUiId("red");
		}
		List<ConnectionInfoVO> ciList = cis.getConnectionInfoList(ui.getUiId());
		map.put("list", ciList);
		return map;
	}

	@RequestMapping(value = "/db_list/{ciNo}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDatabaseList(@PathVariable("ciNo") int ciNo, Map<String, Object> map,
			HttpSession hs) {
		List<Map<String, Object>> dbList;
		try {
			dbList = cis.getDatabaseList(hs, ciNo);
			map.put("list", dbList);
			map.put("parentId", ciNo);
		} catch (Exception e) {
			map.put("error", e.getMessage());
			log.error("db connection error =>{}", e);
		}
		return map;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertConnectionInfo(HttpSession hs, ConnectionInfoVO ci, Map<String, Object> map) {
		Map<String, Object> rMap=new HashMap<String, Object>();
		if(hs.getAttribute("user")!=null) {
			ci.setUiId((String) hs.getAttribute("user"));
			cis.insertConnectionInfo(rMap, ci);
			log.info("ci=>{}", ci);
		}else {
			rMap.put("emsg","로그인부터 다시해세요ㅗ^^");
		}
		
		
		return rMap;
	}

	@RequestMapping(value = "/tables/{dbName}/{parentId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTabeList(@PathVariable("dbName") String dbName,
			@PathVariable("parentId") String parentId, HttpSession hs, Map<String, Object> map) {
		List<TableVO> tableList = cis.getTableList(hs, dbName);
		map.put("list", tableList);
		map.put("parentId", parentId);
		return map;
	}

	@RequestMapping(value = "/columns/{dbName}/{tableName}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getColumnList(@PathVariable("dbName") String dbName,
			@PathVariable("tableName") String tableName, HttpSession hs, Map<String, Object> map) {
		Map<String, String> pMap = new HashMap<String, String>();
		pMap.put("dbName", dbName);
		pMap.put("tableName", tableName);
		List<ColumnVO> columnList = cis.getColumnList(hs, pMap);
		map.put("list", columnList);
		return map;
	}

	@RequestMapping(value = "/tabledata/{tableName}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getColumnList(@PathVariable("tableName") String tableName, HttpSession hs,
			Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("tableName", tableName);
		List<Object> tableList = cis.getTableData(hs, pMap);
		map.put("list", tableList);

		return map;
	}

	@RequestMapping(value = "/columns", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getColumnList(Map<String, Object> map) {
		// cis.getColumnList(hs, map)
		return map;
	}

	@RequestMapping(value = "/sql", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSql(HttpSession hs, @RequestParam Map<String, Object> map) {
		long startTime = System.currentTimeMillis();
		String[] sqls = map.get("sqlTa").toString().split(";");
		
		map.put("list",cis.getSql(hs, map, sqls));
		
		long setTime =System.currentTimeMillis()-startTime;
		map.put("time",setTime);
		return map;
	}

}
