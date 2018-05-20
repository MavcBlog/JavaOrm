package mavc.blog;

public class MappingTableSql {
	private final String FORMAT_INSERT = "INSERT INTO %s (%s) VALUES (%s);";
    private final String FORMAT_UPDATE = "UPDATE %s SET %s WHERE %s;";
    private final String FORMAT_DELETE = "DELETE FROM %s WHERE %s;";
    private final String FORMAT_FIND = "SELECT %s FROM %s WHERE %s LIMIT 1;";
    private final String FORMAT_SELECT_ALL = "SELECT %s FROM %s;";
    private final MappingTableField tableField;

    public MappingTableSql() {
    	tableField = new MappingTableField();
    }    
    
    public <T> String sqlInsert(Class<T> type) throws Exception {
        String columns[] = tableField.columnsInsert(type);
        return String.format(FORMAT_INSERT,tableField.nameTable(type), columns[0], columns[1]);        
    }

    public <T> String sqlUpdate(Class<T> type) throws Exception {
        String columnsWhere[] = tableField.columnsUpdate(type);
        return String.format(FORMAT_UPDATE, tableField.nameTable(type), columnsWhere[0], columnsWhere[1]);        
    }

    public <T> String sqlDelete(Class<T> type) throws Exception {
        return String.format(FORMAT_DELETE, tableField.nameTable(type), tableField.whereDelete(type));        
    }    

    public <T> String sqlFind(Class<T> type, Object... param) throws Exception {
        String where = tableField.whereFind(type);
        return String.format(FORMAT_FIND, tableField.columnsFind(type),tableField.nameTable(type), where);        
    }
    
    public <T> String sqlALl(Class<T> type) throws Exception {        
        return String.format(FORMAT_SELECT_ALL, tableField.columnsFind(type),tableField.nameTable(type));        
    }
}
