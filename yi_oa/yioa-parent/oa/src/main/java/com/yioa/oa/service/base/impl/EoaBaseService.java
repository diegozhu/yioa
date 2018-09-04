package com.yioa.oa.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
	
public class EoaBaseService <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T>{

    @Autowired
	protected JdbcTemplate jdbcTemplate;
    
    protected Gson gson = new GsonBuilder()
		    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		    .create();
    
    protected Gson gson1 = new GsonBuilder().create();
    
//    public List<T> find(){
//    	return null;
//    }
//    
//    public List<T> find(){
//    	return null;
//    }
//    
    
//    public Page<T> executeSQL(String sql, int pageNumber, int pageSize, HashMap<String, Object> params) {
//		Page<T> res = new Page<T>(0, 0);
//		String sqlCount = "select count(DISTINCT customer_id) from biz_customer_bizinfo cb where cb.del_flag != -1"; //params.get("page")
//		int rowCount = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
//		if(rowCount == 0) {
//			res.setCurrent(pageNumber);
//			res.setTotal(rowCount);
//			res.setSize(pageSize);
//			res.setRecords(new ArrayList<T>());
//			return res;
//		}
//		
//		
//		
//	    MapSqlParameterSource paramSource = new MapSqlParameterSource();
//	    for (String key : params.keySet()) {
//	    	paramSource.addValue(key, params.get(key));
//		}
//	    
//		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql, paramSource);
//		List<T> resss = new ArrayList<>();
//		
//			    
//	    Type collectionType = new TypeToken<T>() {}.getType();
//
//		for (Map<String, Object> map : result) {
//			String json = gson1.toJson(map);
//			T obj = gson.fromJson(json, collectionType);
//			resss.add(obj);
//		}
//		
//		res.setCurrent(pageNumber + 1);
//		res.setTotal(rowCount);
//		res.setSize(pageSize);
//		res.setRecords(resss);
//		return res;
//    }
}
