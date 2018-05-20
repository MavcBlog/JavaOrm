package mavc.blog;

import javax.persistence.Table;

public abstract class SqlDialect {
	private String formatNameTable = "%s.%s";

	public abstract SqlDialect select();
    public abstract SqlDialect select(String columns);
    public abstract SqlDialect column(String... columns);
    public abstract <T> SqlDialect table(Class<T> type);    
    public abstract SqlDialect val(String val);    
    public abstract SqlDialect val(String format, Object val);    
    public abstract SqlDialect val();
    public abstract SqlDialect from();
    public abstract SqlDialect from(String name);
    public abstract <T> SqlDialect from(Class<T> type);
    public abstract SqlDialect where();
    public abstract SqlDialect and();
    public abstract SqlDialect or();
    public abstract SqlDialect eq();
    public abstract SqlDialect eq(String val);
    public abstract SqlDialect eq(String format, Object val);
    public abstract SqlDialect dif();
    public abstract SqlDialect dif(String val);
    public abstract SqlDialect dif(String format, Object val);
    public abstract SqlDialect groupBy();
    public abstract SqlDialect orderBy();
    public abstract SqlDialect groupBy(String... columns);
    public abstract SqlDialect orderBy(String... columns);
    public abstract SqlDialect asc();
    public abstract SqlDialect desc();
    public abstract SqlDialect in(String param);
    public abstract SqlDialect in(String... params);
    public abstract SqlDialect min(String param);
    public abstract SqlDialect max(String param);
    public abstract SqlDialect count(String param);
    public abstract SqlDialect avg(String param);
    public abstract SqlDialect sum(String param);
    public abstract SqlDialect like(String param);
    public abstract SqlDialect innerJoin();
    public abstract SqlDialect leftJoin();
    public abstract SqlDialect rightJoin();
    public abstract SqlDialect fullJoin();
    public abstract SqlDialect innerJoin(String table);
    public abstract SqlDialect leftJoin(String table);
    public abstract SqlDialect rightJoin(String table);    
    public abstract SqlDialect fullJoin(String table);
    public abstract <T> SqlDialect innerJoin(Class<T> type);    
    public abstract <T> SqlDialect leftJoin(Class<T> type);    
    public abstract <T> SqlDialect rightJoin(Class<T> type);
    public abstract <T> SqlDialect fullJoin(Class<T> type);    
    public abstract SqlDialect union();        
    public abstract SqlDialect union(String param);
    public abstract SqlDialect on();
    public abstract SqlDialect on(String val);
    public abstract SqlDialect between();
    public abstract SqlDialect limit(int num);    
    public abstract SqlDialect func(String name, Object... param);    
    public abstract String done();    
    
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
}
