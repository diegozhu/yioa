package com.yioa.oa.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
	
public class EoaBaseService <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T>{

    protected static final Logger logger = LoggerFactory.getLogger(EoaBaseService.class);
    
    @Autowired
	protected JdbcTemplate jdbcTemplate;
    
    @Autowired 
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    protected Gson gson = new GsonBuilder()
		    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		    .create();
    
    protected Gson gson1 = new GsonBuilder().create();
    
    
    public <E> List<E> execSql(String sql, Class<E> clazz) {
    	return execSql(sql, clazz);
    }
    
    public <E> List<E> execSql(String sql, Map<String, ?> params, Class<E> clazz) {
		
	    MapSqlParameterSource paramSource = new MapSqlParameterSource();
	    for (String key : params.keySet()) {
	    	paramSource.addValue(key, params.get(key));
		}

		List<Map<String, Object>> result = this.namedParameterJdbcTemplate.queryForList(sql, paramSource);
		List<E> res = new ArrayList<>();

		for (Map<String, Object> map : result) {
			String json = gson1.toJson(map);
			E obj = gson.fromJson(json, clazz);
			res.add(obj);
		}
		
		return res;
    }
    
    public <E> Page<E> execSqlPage(String sql, int pageNumber, int pageSize, Class<E> clazz) {
    	return execSqlPage(sql, pageNumber, pageSize, clazz);
    }

    public <E> Page<E> execSqlPage(String sql, int pageNumber, int pageSize, HashMap<String, Object> params, Class<E> clazz) {
    	logger.error("[execSqlPage] " + sql);
    	Page<E> res = new Page<E>(0, 0);
		String sqlCount = "select count(*) from (" + sql + ") as __xxxxtablealias "; //params.get("page")
		int rowCount = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		if(rowCount == 0) {
			res.setCurrent(pageNumber);
			res.setTotal(rowCount);
			res.setSize(pageSize);
			res.setRecords(new ArrayList<E>());
			return res;
		}
		
		List<Map<String, Object>> result;

		if(params != null) {
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
		    for (String key : params.keySet()) {
		    	paramSource.addValue(key, params.get(key));
			}
		    result = this.jdbcTemplate.queryForList(sql, paramSource);
		} else {
			result = this.jdbcTemplate.queryForList(sql);			
		}
	    
		List<E> resss = new ArrayList<>();
		
		for (Map<String, Object> map : result) {
			String json = gson1.toJson(map);
			E obj = gson.fromJson(json, clazz);
			resss.add(obj);
		}
		
		res.setCurrent(pageNumber + 1);
		res.setTotal(rowCount);
		res.setSize(pageSize);
		res.setRecords(resss);
		return res;
    }
}
