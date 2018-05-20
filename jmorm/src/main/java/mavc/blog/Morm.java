package mavc.blog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Morm {

	public static boolean debug = true;
	public static String formatDebug = "Morm: %s";
	
	private final String NOT_CONNECTION = "Connection not established";
	private final String NOT_CORE = "Uninitialized core";

	private final String OPEN_CONNECTION = "Open connection";
	private final String CLOSE_CONNECTION = "Closed connection";

	private final String OPEN_TRANSACTION = "Initialized Transaction";
	private final String CLOSE_TRANSACTION = "Transaction completed";
	private final String NULL_OBJECT = "Null object";
	private final String NULL_SQL = "Null sql";
	
	private boolean validation = true;
	
	private String formatConnection = "jdbc:%s://%s:%s/%s";
	private String formatMessageValidated = "%s in %s";

	private DataConnection dataConnection;
	private Connection connectionMain;
	private Validator validator;
	private ArrayList<PreparedStatement> statements;
	
	private ExecuteSql executeSql;

	public Morm() {
	}
	
	public Morm(DataConnection dataConnection) {
		this.setDataConnection(dataConnection);		
	}

	public void setDataConnection(DataConnection dataConnection) {
		this.executeSql = new ExecuteSql();
		this.dataConnection = dataConnection;                        
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();     
	}

	public Connection open() {
		try {
			String stringConnection = String.format(formatConnection, dataConnection.getRDBMS(),
					dataConnection.getHost(), dataConnection.getPort(), dataConnection.getDataBase());
			
			debugSql(stringConnection);			
			Class.forName(dataConnection.getDrive());
			Connection connection = DriverManager.getConnection(stringConnection, dataConnection.getUser(),
					dataConnection.getPassword());

			debugSql(OPEN_CONNECTION);			
			return connection;
		} catch (ClassNotFoundException | SQLException er) {
			debugSql(er.getMessage());
		}
		return null;
	}

	public void init() {
		try {
			connectionMain = open();
			if (connectionMain == null || connectionMain.isClosed())
				throw new Exception(NOT_CONNECTION);

			debugSql(OPEN_TRANSACTION);
			connectionMain.setAutoCommit(false);
			statements = new ArrayList<>();
		} catch (Exception er) {
			debugSql(er.getMessage());
		}
	}

	public void finish() {
		try {
			if (connectionMain == null || connectionMain.isClosed())
				throw new Exception(CLOSE_TRANSACTION);

			connectionMain.commit();
			for (PreparedStatement ps : statements)
				ps.close();

			debugSql(CLOSE_TRANSACTION);
			closeConnection(connectionMain);
		} catch (Exception er) {
			close();
			debugSql(er.getMessage());
		}
	}

	public void close() {
		try {
			if (connectionMain == null  || connectionMain.isClosed())
				return;

			connectionMain.rollback();
			closeConnection(connectionMain);
		} catch (SQLException er) {
			debugSql(er.getMessage());
		}
	}

	private void closeConnection(Connection conn) {
		try {
			while (!conn.isClosed())
				conn.close();

			if (debug)
				System.out.println(String.format(formatDebug, CLOSE_CONNECTION));
		} catch (SQLException er) {
			debugSql(er.getMessage());
		}
	}
	
	public <T> Object save(T bean) {        
        try{        
        	if(connectionMain == null  || connectionMain.isClosed())
        		throw new Exception(CLOSE_TRANSACTION);
        	
        	validateOperation(bean);            
        	return executeSql.insert(bean, selectConnection(),statements);
        }catch(Exception er){
            close();
            debugSql(er.getMessage());
        }   
        return null;
    }
        
    public <T> void edit(T bean) {
        try{   
        	if(connectionMain == null  || connectionMain.isClosed())
        		throw new Exception(CLOSE_TRANSACTION);
        	
        	validateOperation(bean);
        	executeSql.update(bean, selectConnection(), statements);        	
        }catch(Exception er){
            close();
            debugSql(er.getMessage());            
        }           
    }
    
    public <T> void editOrSave(T bean) {                        
        try{
        	if(connectionMain == null  || connectionMain.isClosed())
        		throw new Exception(CLOSE_TRANSACTION);
        	
        	validateOperation(bean);        
        	//executeInsertOrUpdate(bean);
        }catch(Exception er){     
        	close();
        	debugSql(er.getMessage());            
        }
    }     
            
    public <T> void delete(T bean)  throws Exception{        
        try{
            validateOperation(bean);
            executeSql.delete(bean, selectConnection(), statements);
        }catch(Exception er){
            close();
            debugSql(er.getMessage());            
        }        
    }

    public <T> void delete(Class<T> type, Object... param) throws Exception{        
        try{
        	executeSql.delete(type, selectConnection(), statements, param);
        }catch(Exception er){
            close();
            debugSql(er.getMessage());            
        }
    }
        
    public <T> T get(Class<T> type, Object... param)  {       
        try{
        	String sql = executeSql.getSqlFind(type, param);
        	debugSql(sql);
        	CachedRowSet crs = query(sql, param);
        	if(crs.next())
        		return executeSql.getBean(crs, type);
        }catch(Exception er){
        	debugSql(er.getMessage()); 
        }
        return null;
    }

    public <T> List<T> getAll(Class<T> type) {
        try{
        	String sql = executeSql.getSqlAll(type);
        	debugSql(sql);   
        	CachedRowSet crs = query(sql);
        	return executeSql.getListBean(crs, type);
        }catch(Exception er){
        	debugSql(er.getMessage());
        }
        return null;
    }

    public String json(Object bean) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(bean);
    }

    public <T> T bean(String json, Class<T> className) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, className);
    }
    
    public CachedRowSet query(String sql, Object... params) {
    	try{
	    	if( sql == null )
	    		throw new Exception(NULL_SQL);
	    		
	        Connection connSql = selectConnection();             
	        debugSql(sql);        
	        CachedRowSet crs = executeSql.executeQuery(connSql, sql, params);
	        if (connectionMain != connSql)
	            closeConnection(connSql);
	        
	        return crs;
    	}catch (Exception er) {
    		debugSql(er.getMessage());
		}
    	return null;
    }
                
    public <T> void validationBean(T bean) throws Exception {
        for (ConstraintViolation<T> constraintViolation : validator.validate(bean)) {
            String message = String.format(formatMessageValidated,constraintViolation.getMessage(),
            		constraintViolation.getPropertyPath());
            throw new Exception(message);
        }
    }
    
    private <T> void validateOperation(T bean) throws Exception{
    	if(bean == null)
    		throw new Exception(NULL_OBJECT);
    	
        if (executeSql == null) 
            throw new Exception(NOT_CORE);

        if (connectionMain == null || connectionMain.isClosed()) 
            throw new Exception(NOT_CONNECTION);
             
        if (validation) 
            validationBean(bean);
    }
    
    private Connection selectConnection() throws Exception {        
        if (connectionMain == null || connectionMain.isClosed())
            return open();
        else
            return connectionMain;        
    }
            
    public static void debugSql(String msg ){
    	if(debug)
        	System.err.println(String.format(formatDebug, msg));
    }
}
