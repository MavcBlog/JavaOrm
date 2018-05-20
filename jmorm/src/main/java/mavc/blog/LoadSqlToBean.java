package mavc.blog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang3.ArrayUtils;

public class LoadSqlToBean {

	public <T> List<T> sqlToList(Class<T> type, CachedRowSet crs) throws Exception {
		List<T> list = new ArrayList<>();
		while (crs.next()) {
			T bean = type.newInstance();
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					String nameColumn = column.name();
					String nameFiled = field.getName();
					String nameMethod = getNameMethod(nameFiled);
					Method method = bean.getClass().getMethod(nameMethod, field.getType());
					evaluateType(bean, method, field, crs, nameColumn);
				}
			}
			list.add(bean);
		}
		return list;
	}

	public <T> T sqlToBean(CachedRowSet crs, Class<T> type) throws Exception {
		T bean = type.newInstance();
		Field[] fields = type.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				String nameColumn = column.name();
				String nameFiled = field.getName();
				String nameMethod = getNameMethod(nameFiled);
				Method method = type.getMethod(nameMethod, field.getType());
				evaluateType(bean, method, field, crs, nameColumn);
			}
		}
		return bean;
	}

	public String getNameMethod(String nameFiled) {
		String nameFormat = "set%s";
		String name = String.valueOf(nameFiled.charAt(0)).toUpperCase().concat(nameFiled.substring(1));
		return String.format(nameFormat, name);
	}

	public void evaluateType(Object bean, Method method, Field field, CachedRowSet crs, String nameColumn)
			throws Exception {
		if (field.getType() == LocalDate.class) {
			LocalDate localDate = Instant.ofEpochMilli(crs.getDate(nameColumn).getTime()).atZone(ZoneId.systemDefault())
					.toLocalDate();
			method.invoke(bean, localDate);
		} else if (field.getType() == LocalDateTime.class) {
			Date date = new Date(crs.getDate(nameColumn).getTime());
			LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			method.invoke(bean, localDateTime);
		} else if (field.getType() == Byte[].class) {
			Byte[] b = ArrayUtils.toObject(crs.getBytes(nameColumn));
			method.invoke(bean, b);
		} else
			method.invoke(bean, field.getType().cast(crs.getObject(nameColumn)));
	}
}
