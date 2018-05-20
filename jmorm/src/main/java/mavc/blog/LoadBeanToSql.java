package mavc.blog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.ArrayUtils;



public class LoadBeanToSql {

	public void loadBeanToSqlInsert(Object bean, PreparedStatement ps) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();
        int index = 1;         
        for(Field field : fields){      
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
            Column column = field.getAnnotation(Column.class);
            if(generatedValue == null && column != null)
            	evaluateType( index++, ps, getValueField(bean, field) );            
        }
    }
	
	public void loadBeanToSqlUpdate(Object bean, PreparedStatement ps) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();        
        int index = 1;         
        ArrayList<Field> listFields  = new ArrayList<Field>();
        for(Field field : fields){            
            Id id = field.getAnnotation(Id.class);
            Column column = field.getAnnotation(Column.class);
            if(id == null && column != null)
            	evaluateType( index++, ps, getValueField(bean, field) );                
            else if(column != null)
                listFields.add(field);            
        }
        for(Field field : listFields)
        	evaluateType( index++, ps, getValueField(bean, field) );           
    }
	
	public void loadBeanToSqlDelete(Object bean, PreparedStatement ps, Object... param) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();
        int index = 1;         
        for(Field field : fields){  
            Id id = field.getAnnotation(Id.class);
            Column column = field.getAnnotation(Column.class);
            if(id != null && column != null){
                if(param == null || param.length == 0)
                	evaluateType( index++, ps, getValueField(bean, field) );         
                else 
                    ps.setObject(index++, param[index - 1]);            
            }
        }
    }
	
	public void evaluateType( int index, PreparedStatement ps, Object objectData  ) throws SQLException{			
		if( objectData instanceof Byte[] )
			ps.setBytes(index, ArrayUtils.toPrimitive( (Byte[])  objectData) );
		else			
			ps.setObject(index++, objectData);		
	}
	
	public Object getValueField(Object bean, Field field) throws Exception{                
        String prefix = field.getType() ==  boolean.class ? "is" : "get";                    
        String nameFiled = field.getName();
        String nameMethod = "".concat(prefix).concat(String.valueOf(nameFiled.charAt(0)).toUpperCase()).concat(nameFiled.substring(1));        
        Method method = bean.getClass().getMethod(nameMethod);    
        return method.invoke(bean);
    }        
}
