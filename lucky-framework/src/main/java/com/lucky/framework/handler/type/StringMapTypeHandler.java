package com.lucky.framework.handler.type;

import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Map<String, Object> 的类型转换器实现类，对应数据库的 varchar 类型
 *
 * @author lucky
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Map.class)
public class StringMapTypeHandler implements TypeHandler<Map<String, Object>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setString(i, null);
        } else {
            String json = JSON.toJSONString(parameter);
            ps.setString(i, json);
        }
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return parseJson(value);
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return parseJson(value);
    }

    @Override
    public Map<String, Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return parseJson(value);
    }

    private Map<String, Object> parseJson(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(value);
        } catch (Exception e) {
            return null;
        }
    }

}