package mavc.blog;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

public class MappingTableField {

	private String formatNameTable = "%s.%s";
	
	public <T> String nameTable(Class<T> type){        
        Table tableAno = type.getAnnotation(Table.class);
        String nameTable = tableAno.name();
        String nameSchema = tableAno.schema();
        String nameCatalog = tableAno.catalog();
                    
        if(!nameCatalog.isEmpty())
            return String.format(formatNameTable, nameCatalog, nameTable);
        
        if(!nameSchema.isEmpty())
            return String.format(formatNameTable, nameSchema, nameTable);
        
        return nameTable;
    }
    
    public <T> String[] columnsInsert(Class<T> type){        
        String columns = "";
        String columnsValues = "";
        Field[] fields = type.getDeclaredFields();
        for(Field field : fields){        
            Column column = field.getAnnotation(Column.class);
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);            
            if(generatedValue == null && column != null){
                String name = column.name();
                columns =  columns.concat(",").concat(name);
                columnsValues = columnsValues.concat(",?");
            }            
        }        
        return new String[]{ columns.substring(1),columnsValues.substring(1) };
    }
    
    public <T> String[] columnsUpdate(Class<T> type){  
        String columns = "";
        String where = "";
        Field[] fields = type.getDeclaredFields();
        for(Field field : fields){        
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if(column != null){
            		String name = column.name();
                if(id == null)                
                    columns = columns.concat(",").concat(name).concat("=?");
                else
                    where = where.concat(" AND ").concat(name).concat("=?");                        
            }
        } 
        return new String[]{ columns.substring(1),where.substring(4) };
    }
    
    public <T> String columnsFind(Class<T>  type){        
        String columns = "";        
        Field[] fields = type.getDeclaredFields();
        for(Field field : fields){        
            Column column = field.getAnnotation(Column.class); 
            if(column != null){
                String name = column.name();
                columns =  columns.concat(",").concat(name);            
            }
        }        
        return columns.substring(1);
    }
    
    public  <T>  String whereFind(Class<T> type){        
        String where = "";
        Field[] fields = type.getDeclaredFields();        
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if(column != null){
                String name = column.name();
                if(id != null)
                    where = where.concat(" AND ").concat(name).concat("=").concat("?");                        
            }
        }
        return where.substring(4);
    }
    
    public <T> String whereDelete(Class<T> type){        
        String where = "";
        Field[] fields = type.getDeclaredFields();
        for(Field field : fields){        
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);            
            if(column != null){
                String name = column.name();
                if(id != null)                                
                    where = where.concat(" AND ").concat(name).concat("=?");                        
            }
        }
        return where.substring(4);
    }
}
