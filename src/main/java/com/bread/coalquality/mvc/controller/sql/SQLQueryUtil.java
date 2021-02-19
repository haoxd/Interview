package com.bread.coalquality.mvc.controller.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class SQLQueryUtil  {

	private static final long serialVersionUID = -4008847394285279230L;


	/**
	 * main()
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SQLQueryUtil util = new SQLQueryUtil();
		util.addTable("tf_f_ord");
		util.addColumn("user_name");
		util.addColumn("user_age");
		util.addAndCondition("id", RelationalOperators.EQ, "xxxx", ValueType.STRING);
		util.addAndInCondition("user_age", "'x','x','x','x','x'");
		util.addOrLikeCondition("xxx", "yyyy");
		util.addGroupBy("user_name");
		util.addAscOrderBy("create_time");
		util.addDescOrderBy("id");
		try {
			System.out.println(util.toSQL());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 日期格式化字符串 如:2013-06-20
	 */
	public static final String FMT_DATE = "yyyy-MM-dd";

	/**
	 * 时间格式化字符串 如:13:01:01
	 */
	public static final String FMT_TIME = "HH:mm:ss";

	/**
	 * 日期时间格式化字符串 如:2013-06-20 13:01:01
	 */
	public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 逻辑运算
	 * 
	 * @author Vity
	 * 
	 */
	public enum LogicalOperators {
		AND("AND"), OR("OR");
		private String value;

		private LogicalOperators(String value) {
			this.value = value;
		}

		public String toString() {
			return new String(this.value);
		}
	}

	/**
	 * 关系运算
	 * 
	 * <p>
	 * EQ("="), NE("<>"), GT(">"), GE(">="), LT("<"), LE("<=")
	 * </p>
	 * 
	 * @author Vity
	 * 
	 */
	public enum RelationalOperators {
		EQ("="), NE("<>"), GT(">"), GE(">="), LT("<"), LE("<=");
		private String value;

		private RelationalOperators(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}

	/**
	 * 排序方式
	 * 
	 * @author Vity
	 * 
	 */
	public enum SortMethod {
		ASC("ASC"), DESC("DESC");
		private String value;

		private SortMethod(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}

	/**
	 * 值类型
	 * 
	 * @author Vity
	 * 
	 */
	public enum ValueType {
		NUMBER, DATE, STRING, TIME, DATETIME
	}

	/**
	 * 内置表别名前缀
	 */
	private final String TABLE_ALIAS_NAME = "TEMP_TABLE_ALIAS_";

	/**
	 * 内置表别名索引
	 */
	private int TABLE_ALIAS_INDEX = 0;

	/**
	 * 是否调试模式 非调试模式则不打印SQL语句
	 */
	private boolean isDebug = true;

	/**
	 * 是否去重
	 */
	private boolean isDistinct = false;

	/**
	 * 表名及别名集合 key : table & view name | value : alias name
	 * <p>
	 * <b>注意:</b>目前不支持重复表查询
	 * </p>
	 */
	private Map<String, String> tableMap = new HashMap<String, String>();

	/**
	 * 待查列名集合 key : column name value : | alias name
	 */
	private Map<String, String> columnMap = new HashMap<String, String>();

	/**
	 * 查询条件集合 key : and & or | value 条件值
	 */
	private Map<String, String> conditionMap = new IdentityHashMap<String, String>();

	/**
	 * 排序条件集合
	 */
	private Map<String, String> orderMap = new HashMap<String, String>();

	/**
	 * 分组集合
	 */
	private List<String> groupList = new ArrayList<String>();

	/**
	 * sql生成结果
	 */
	private StringBuilder sqlResult = new StringBuilder();

	/**
	 * 追加字符串,在后面添加一个空格
	 * 
	 * @param string
	 * @return
	 */
	private SQLQueryUtil append(String string) {
		sqlResult.append(string).append(" ");
		return this;
	}

	/**
	 * 删除最后两位字符串
	 * 
	 * @return
	 */
	private SQLQueryUtil clearEndChar() {
		sqlResult.delete(sqlResult.length() - 2, sqlResult.length());
		return this;
	}

	/**
	 * 生成SQL语句
	 * 
	 * @return
	 */
	public String toSQL() throws Exception {
		if (tableMap.isEmpty()) {
			throw new Exception( "未发现表或视图");
		}

		this.append("SELECT");

		if (isDistinct) {
			this.append("DISTINCT");
		}

		if (columnMap.isEmpty()) {
			this.append("*");
		} else {
			for (String key : columnMap.keySet()) {
				this.append(key);
				if (!StringUtils.isBlank(columnMap.get(key))) {
					this.append("AS").append(columnMap.get(key));
				}
				this.append(",");
			}
			this.clearEndChar();
		}

		this.append("FROM");

		for (String key : tableMap.keySet()) {
			this.append(key);
			if (!StringUtils.isBlank(tableMap.get(key))) {
				this.append("AS").append(tableMap.get(key));
			}
			this.append(",");
		}
		this.clearEndChar().append(" ");

		if (!groupList.isEmpty()) {
			this.append("GROUP BY");
			for (String field : groupList) {
				this.append(field).append(",");
			}
			this.clearEndChar();
		}

		if (!conditionMap.isEmpty()) {
			if (!groupList.isEmpty()) {
				this.append("HAVING");
			} else {
				this.append("WHERE");
			}
			this.append("1 = 1");
			for (Entry<String, String> entry : conditionMap.entrySet()) {
				this.append(entry.getKey()).append(entry.getValue());
			}
		}

		if (!orderMap.isEmpty()) {
			this.append("ORDER BY");
			for (String key : orderMap.keySet()) {
				this.append(key).append(orderMap.get(key)).append(",");
			}
			this.clearEndChar();
		}

		if (isDebug) {
			log.trace(sqlResult.toString().replace(" ,", ",").replace("  ", " ").replace(" 1 = 1 AND", "").replace(" 1 = 1 OR", ""));
		}
		return sqlResult.toString().replace(" ,", ",").replace("  ", " ").replace(" 1 = 1 AND", "").replace(" 1 = 1 OR", "");
	}

	/**
	 * 设置 是否Distinct查询
	 * 
	 * @param isDistinct
	 */
	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	/**
	 * 添加待查询的表或视图名称
	 * 
	 * @param tableName
	 * @return
	 */
	public SQLQueryUtil addTable(String tableName) {
		tableMap.put(tableName, TABLE_ALIAS_NAME + TABLE_ALIAS_INDEX++);
		return this;
	}

	/**
	 * 添加待查询的表或视图名称,并指定别名
	 * 
	 * @param tableName
	 * @param aliasName
	 * @return
	 */
	public SQLQueryUtil addTable(String tableName, String aliasName) throws Exception {
		if (aliasName.contains(TABLE_ALIAS_NAME)) {
			throw new Exception( "不能使用这个别名前缀:" + TABLE_ALIAS_NAME);
		}
		tableMap.put(tableName, aliasName);
		return this;
	}

	/**
	 * 添加待查列
	 * 
	 * @param columnName
	 * @return
	 */
	public SQLQueryUtil addColumn(String columnName) {
		return this.addColumn(columnName, "");
	}

	/**
	 * 添加待查列,并指定别名
	 * 
	 * @param columnName
	 * @param aliasName
	 * @return
	 */
	public SQLQueryUtil addColumn(String columnName, String aliasName) {
		columnMap.put(columnName, aliasName);
		return this;
	}

	/**
	 * 添加查询条件
	 * 
	 * <p>
	 * <span>针对日期类型查询的说明</span> <div>
	 * 日期查询必须将查询值按照本类提供的格式化字符串进行格式化,并指定相应的ValueType </div>
	 * </p>
	 * 
	 * @param logicalOperators
	 * @param cKey
	 * @param relationalOperators
	 * @param cValue
	 * @param valueType
	 * @return
	 */
	private SQLQueryUtil addCondition(LogicalOperators logicalOperators, String cKey, RelationalOperators relationalOperators, String cValue,
			ValueType valueType) throws Exception {
		switch (valueType) {
		case STRING:
			conditionMap.put(logicalOperators.toString(),
					cKey.concat(" ").concat(relationalOperators.toString()).concat(" '").concat(cValue).concat("'"));
			break;
		case NUMBER:
			conditionMap.put(logicalOperators.toString(), cKey.concat(" ").concat(relationalOperators.toString()).concat(" ").concat(cValue));
			break;
		default:
			conditionMap.put(logicalOperators.toString(), this.getDateTimeQuery(cKey, relationalOperators, cValue, valueType));
			break;

		}
		return this;
	}

	/**
	 * 目前仅支持 Oracle, MySQL ; 日期查询生成 其他数据库方式请根据需要自行扩展
	 */
	private String getDateTimeQuery(String cKey, RelationalOperators ro, String cValue, ValueType vt) throws Exception {

			switch (vt) {
			case DATE:
				return cKey.concat(ro.toString()).concat("UNIX_TIMESTAMP('").concat(cValue).concat(" 00:00:00')");
			case TIME:
				throw new Exception("暂不支持MySQL某段时间内查询,请确认日期后使用 ValueType.DATETIME 查询");
			case DATETIME:
				return cKey.concat(ro.toString()).concat("UNIX_TIMESTAMP('").concat(cValue).concat("')");
			default:
				break;

		}
		return cKey.concat(" ").concat(ro.toString()).concat(" \"").concat(cValue).concat("\"");
	}

	private SQLQueryUtil addLikeCondition(LogicalOperators logicalOperators, String cKey, String cValue) {
		if (cValue.contains("%")) {
			conditionMap.put(logicalOperators.toString(), cKey.concat(" LIKE \"").concat(cValue).concat("\""));
		} else {
			conditionMap.put(logicalOperators.toString(), cKey.concat(" LIKE \"%").concat(cValue).concat("%\""));
		}
		return this;
	}

	private SQLQueryUtil addInCondition(LogicalOperators logicalOperators, String cKey, String cValue) {
		conditionMap.put(logicalOperators.toString(), cKey.concat(" IN ( ").concat(cValue).concat(" )"));
		return this;
	}

	private SQLQueryUtil addNotInCondition(LogicalOperators logicalOperators, String cKey, String cValue) {
		conditionMap.put(logicalOperators.toString(), cKey.concat(" NOT IN ( ").concat(cValue).concat(" )"));
		return this;
	}

	private SQLQueryUtil addOrderBy(String orderField, SortMethod sortMethod) {
		orderMap.put(orderField, sortMethod.toString());
		return this;
	}

	/**
	 * 添加分组字段
	 * 
	 * @param groupField
	 * @return
	 */
	public SQLQueryUtil addGroupBy(String groupField) {
		groupList.add(groupField);
		return this;
	}

	/**
	 * 添加 AND查询条件
	 * 
	 * @param cKey
	 * @param relationalOperators
	 * @param cValue
	 * @param valueType
	 * @return
	 */
	public SQLQueryUtil addAndCondition(String cKey, RelationalOperators relationalOperators, String cValue, ValueType valueType) throws Exception {
		return this.addCondition(LogicalOperators.AND, cKey, relationalOperators, cValue, valueType);
	}

	/**
	 * 添加 OR查询条件
	 * 
	 * @param cKey
	 * @param relationalOperators
	 * @param cValue
	 * @param valueType
	 * @return
	 */
	public SQLQueryUtil addOrCondition(String cKey, RelationalOperators relationalOperators, String cValue, ValueType valueType) throws Exception {
		return this.addCondition(LogicalOperators.OR, cKey, relationalOperators, cValue, valueType);
	}

	/**
	 * 添加 AND LIKE 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addAndLikeCondition(String cKey, String cValue) {
		return this.addLikeCondition(LogicalOperators.AND, cKey, cValue);
	}

	/**
	 * 添加 OR LIKE 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addOrLikeCondition(String cKey, String cValue) {
		return this.addLikeCondition(LogicalOperators.OR, cKey, cValue);
	}

	/**
	 * 添加 AND IN 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addAndInCondition(String cKey, String cValue) {
		return this.addInCondition(LogicalOperators.AND, cKey, cValue);
	}

	/**
	 * 添加 OR IN 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addOrInCondition(String cKey, String cValue) {
		return this.addInCondition(LogicalOperators.OR, cKey, cValue);
	}

	/**
	 * 添加 AND NOT IN 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addAndNotInCondition(String cKey, String cValue) {
		return this.addNotInCondition(LogicalOperators.AND, cKey, cValue);
	}

	/**
	 * 添加 OR NOT IN 查询条件
	 * 
	 * @param cKey
	 * @param cValue
	 * @return
	 */
	public SQLQueryUtil addOrNotInCondition(String cKey, String cValue) {
		return this.addNotInCondition(LogicalOperators.OR, cKey, cValue);
	}

	/**
	 * 添加正向排序字段
	 * 
	 * @param orderField
	 * @return
	 */
	public SQLQueryUtil addAscOrderBy(String orderField) {
		return this.addOrderBy(orderField, SortMethod.ASC);
	}

	/*
	 * 添加逆向排序字段
	 */
	public SQLQueryUtil addDescOrderBy(String orderField) {
		return this.addOrderBy(orderField, SortMethod.DESC);
	}

	/**
	 * 清除待查表和视图
	 * 
	 * @return
	 */
	public SQLQueryUtil clearTable() {
		tableMap.clear();
		return this;
	}

	/**
	 * 清除待查列
	 * 
	 * @return
	 */
	public SQLQueryUtil clearColumn() {
		columnMap.clear();
		return this;
	}

	/**
	 * 清除查询条件
	 * 
	 * @return
	 */
	public SQLQueryUtil clearCondition() {
		conditionMap.clear();
		return this;
	}

	/**
	 * 清除排序条件
	 * 
	 * @return
	 */
	public SQLQueryUtil clearOrder() {
		orderMap.clear();
		return this;
	}

	/**
	 * 清除分组列
	 * 
	 * @return
	 */
	public SQLQueryUtil clearGroup() {
		groupList.clear();
		return this;
	}
}