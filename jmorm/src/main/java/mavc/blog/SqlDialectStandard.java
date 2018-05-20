package mavc.blog;

public class SqlDialectStandard extends SqlDialect {

	private String sql = "";
	
	@Override
	public SqlDialect select() {
		sql = sql.concat(" SELECT ");
        return this;
	}

	@Override
	public SqlDialect select(String columns) {
		sql =  sql.concat(String.format(" SELECT %s ", columns));
        return this;
	}

	@Override
	public SqlDialect column(String... columns) {
		sql =  sql.concat(String.format(" %s ", String.join(",", columns)));
        return this;
	}

	@Override
	public <T> SqlDialect table(Class<T> type) {
		sql =  sql.concat(String.format(" %s ", nameTable(type)));
        return this;
	}

	@Override
	public SqlDialect val(String val) {
		sql =  sql.concat(String.format(" %s ", val)) ;
        return this;
	}

	@Override
	public SqlDialect val(String format, Object val) {
		sql =  sql.concat(String.format(format, val)) ;
        return this;
	}

	@Override
	public SqlDialect val() {
		sql =  sql.concat("? ") ;
        return this;
	}

	@Override
	public SqlDialect from() {
        sql =  sql.concat(" FROM ") ;
        return this;
	}

	@Override
	public SqlDialect from(String name) {
        sql =  sql.concat(String.format(" FROM %s ", name));
        return this;   
	}

	@Override
	public <T> SqlDialect from(Class<T> type) {
		sql =  sql.concat(String.format(" FROM %s ", nameTable(type)));
        return this;   
	}

	@Override
	public SqlDialect where() {
		sql =  sql.concat(" WHERE ") ;
        return this;
	}

	@Override
	public SqlDialect and() {
		sql =  sql.concat(" AND ") ;
        return this;
	}

	@Override
	public SqlDialect or() {
        sql =  sql.concat(" OR ") ;
        return this;
	}

	@Override
	public SqlDialect eq() {
        sql =  sql.concat(" = ") ;
        return this;
	}

	@Override
	public SqlDialect eq(String val) {
		sql =  sql.concat(String.format(" = %s ", val));
        return this;
	}

	@Override
	public SqlDialect eq(String format, Object val) {
		sql =  sql.concat(" = ").concat(String.format(format, val));
        return this;
	}

	@Override
	public SqlDialect dif() {
		sql =  sql.concat(" <> ") ;
        return this;
	}

	@Override
	public SqlDialect dif(String val) {
		sql =  sql.concat(String.format(" <> %s ", val));
        return this;
	}

	@Override
	public SqlDialect dif(String format, Object val) {
		sql =  sql.concat(" <> ").concat(String.format(format, val));
        return this;
	}

	@Override
	public SqlDialect groupBy() {
		sql =  sql.concat(" GROUP BY ") ;
        return this;
	}

	@Override
	public SqlDialect orderBy() {
        sql =  sql.concat(" ORDER BY ") ;
        return this;
	}

	@Override
	public SqlDialect groupBy(String... columns) {
		sql =  sql.concat(" GROUP BY ").concat( String.join(",", columns) ) ;
        return this;
	}

	@Override
	public SqlDialect orderBy(String... columns) {
		sql =  sql.concat(" ORDER BY ").concat(  String.join(",", columns) );
        return this;
	}

	@Override
	public SqlDialect asc() {
		sql =  sql.concat(" ASC ") ;
        return this;
	}

	@Override
	public SqlDialect desc() {
        sql =  sql.concat(" DESC ") ;
        return this;
	}

	@Override
	public SqlDialect in(String param) {
		sql =  sql.concat(String.format(" IN(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect in(String... params) {
		sql =  sql.concat(String.format(" IN(%s) ",  String.join(",", params) )) ;
        return this;
	}

	@Override
	public SqlDialect min(String param) {
		sql =  sql.concat(String.format(" MIN(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect max(String param) {
		sql =  sql.concat(String.format(" MAX(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect count(String param) {
		sql =  sql.concat(String.format(" COUNT(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect avg(String param) {
		sql =  sql.concat(String.format(" AVG(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect sum(String param) {
		sql =  sql.concat(String.format(" SUM(%s) ", param)) ;
        return this;
	}

	@Override
	public SqlDialect like(String param) {
		sql =  sql.concat(String.format(" LIKE %s ", param)) ;
        return this;
	}

	@Override
	public SqlDialect innerJoin() {
        sql =  sql.concat(" INNER JOIN ") ;
        return this;
	}

	@Override
	public SqlDialect leftJoin() {
        sql =  sql.concat(" LEFT JOIN ") ;
        return this;
	}

	@Override
	public SqlDialect rightJoin() {
        sql =  sql.concat(" RIGHT JOIN ") ;
        return this;
	}

	@Override
	public SqlDialect fullJoin() {
        sql =  sql.concat(" FULL JOIN ") ;
        return this;
	}

	@Override
	public SqlDialect innerJoin(String table) {
		sql =  sql.concat(String.format(" INNER JOIN %s ", table));
        return this;
	}

	@Override
	public SqlDialect leftJoin(String table) {
		sql =  sql.concat(String.format(" LEFT JOIN %s ", table));
        return this;
	}

	@Override
	public SqlDialect rightJoin(String table) {
		sql =  sql.concat(String.format(" RIGHT JOIN %s ", table));
        return this;
	}

	@Override
	public SqlDialect fullJoin(String table) {
		sql =  sql.concat(String.format(" FULL JOIN %s ", table));
        return this;
	}

	@Override
	public <T> SqlDialect innerJoin(Class<T> type) {
		sql =  sql.concat(String.format(" INNER JOIN %s ", nameTable(type)));
        return this;
	}

	@Override
	public <T> SqlDialect leftJoin(Class<T> type) {
		sql =  sql.concat(String.format(" LEFT JOIN %s ", nameTable(type)));
        return this;
	}

	@Override
	public <T> SqlDialect rightJoin(Class<T> type) {
		sql =  sql.concat(String.format(" RIGHT JOIN %s ", nameTable(type)));
        return this;
	}

	@Override
	public <T> SqlDialect fullJoin(Class<T> type) {
		sql =  sql.concat(String.format(" FULL JOIN %s ", nameTable(type)));
        return this;
	}

	@Override
	public SqlDialect union() {
		sql =  sql.concat(" UNION ") ;
        return this;
	}

	@Override
	public SqlDialect union(String param) {
		sql =  sql.concat(String.format(" UNION %s ", param));
        return this;
	}

	@Override
	public SqlDialect on() {
		sql =  sql.concat(" ON ") ;
        return this;
	}

	@Override
	public SqlDialect on(String val) {
		sql =  sql.concat(String.format(" ON %s ", val));
        return this;
	}

	@Override
	public SqlDialect between() {
		sql =  sql.concat(" BETWEEN ") ;
        return this;
	}

	@Override
	public SqlDialect limit(int num) {
		sql =  sql.concat(String.format(" LIMIT %s ", num)) ;
        return this;
	}

	@Override
	public SqlDialect func(String name, Object... param) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String done() {
		return sql;
	}

}
