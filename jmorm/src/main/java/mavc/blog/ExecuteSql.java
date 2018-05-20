package mavc.blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class ExecuteSql {
	
	public static final int RETURN_KEY = PreparedStatement.RETURN_GENERATED_KEYS;
    public static final int READ_ONLY = ResultSet.CONCUR_READ_ONLY;
    public static final int SENSITIVE = ResultSet.TYPE_SCROLL_SENSITIVE;   
	
	private MappingTableSql tableSql;
	private LoadBeanToSql beanToSql;
	private LoadSqlToBean sqlToBean;
	
	public ExecuteSql() {
		tableSql = new MappingTableSql();
		beanToSql = new LoadBeanToSql();
		sqlToBean = new LoadSqlToBean();
	}

	public <T> Object insert(T bean, Connection connection, ArrayList<PreparedStatement> statements) throws Exception {
		String sql = tableSql.sqlInsert(bean.getClass());
		Morm.debugSql(sql);		
		PreparedStatement pstm = getPreparedStatementInsert(connection, sql);		
		beanToSql.loadBeanToSqlInsert(bean, pstm);
		pstm.executeUpdate();
		statements.add(pstm);
		//ResultSet resIds = pstm.getGeneratedKeys();
		//return core.getKeysResultSet(resIds);
		return null;
	}
	
	public <T> void update(T bean,Connection connection, ArrayList<PreparedStatement> statements) throws Exception {                
		String sql = tableSql.sqlUpdate(bean.getClass());
		Morm.debugSql(sql);        
        PreparedStatement pstm = getPreparedStatement(connection,sql);
        beanToSql.loadBeanToSqlUpdate(bean, pstm);
        pstm.executeUpdate();
        statements.add(pstm);        
    }
	
	public <T> void delete(T bean, Connection connection, ArrayList<PreparedStatement> statements) throws Exception{
        String sql = tableSql.sqlDelete(bean.getClass());
        Morm.debugSql(sql);        
        PreparedStatement pstm = getPreparedStatement(connection,sql);
        beanToSql.loadBeanToSqlDelete(bean, pstm); 
        pstm.execute();
        statements.add(pstm);        
    }
    
    public <T>  void delete(Class<T> type, Connection connection, ArrayList<PreparedStatement> statements, Object... param) throws Exception{        
        Object bean = type.newInstance();
        String sql = tableSql.sqlDelete(bean.getClass());
        Morm.debugSql(sql);        
        PreparedStatement pstm = getPreparedStatement(connection,sql);        
        beanToSql.loadBeanToSqlDelete(bean, pstm, param);
        pstm.execute();
        statements.add(pstm);        
    }
        
    public <T> String getSqlFind(Class<T> type, Object... param) throws Exception{
    	return tableSql.sqlFind(type, param);    	
    }
    
    public <T> String getSqlAll(Class<T> type) throws Exception{
    	return tableSql.sqlALl(type);
    }
    
    public <T> T getBean( CachedRowSet crs, Class<T> type ) throws Exception{
    	return sqlToBean.sqlToBean(crs, type);
    }
    
    public <T> List<T> getListBean( CachedRowSet crs, Class<T> type ) throws Exception{
    	return sqlToBean.sqlToList(type, crs);
    }
    
    public CachedRowSet executeQuery(Connection connSql,String sql, Object... params) throws Exception {            	
    	PreparedStatement pstm = connSql.prepareStatement(sql,SENSITIVE,READ_ONLY);                       
        loadParamsToPstm(pstm,params);                    
        ResultSet resultSet = pstm.executeQuery();
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(resultSet);
        resultSet.close();
        pstm.close();
        return crs;
    }        
      
    public void loadParamsToPstm(PreparedStatement pstm, Object[] params) throws  Exception{
        if (params != null) {                        
            for (int i = 0; i < params.length; i++)                
                pstm.setObject(i + 1, params[i]);                       
        }
    }
    
    public PreparedStatement getPreparedStatement( Connection connection, String sql ) throws SQLException{
    	return connection.prepareStatement(sql);
    }
    
    public PreparedStatement getPreparedStatementInsert( Connection connection, String sql ) throws SQLException{
    	return connection.prepareStatement(sql, RETURN_KEY);
    }
       
}
