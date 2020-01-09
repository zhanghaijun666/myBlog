package com.blog.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author haijun
 * 简单sql builder。注意：这个不保证sql正确，sql正确是开发的责任。这个只是方便把一些sql拼接简单化
 * Example:
 * SqlBuilder sql = new SqlBuilder()
 * .withSeperatorCrlf() //用换行连接，可选
 * .withSeperatorSpace() //用空格连接（默认）
 * .append("select t1.xxxx, t1.yyyy+? from table1 t1", yyyyOffset)
 * .append("join table2 t2 on t1.xxxx=t2.xxxx")
 * .append("where t1.xxxx > ?", miniValue)
 * .appendIf(!yyyyList.isEmpty(),new SqlBuilder().in("and t1.yyyy",yyyyList));  //and t1.yyyy in (?,?,?)
 * .append("and").in("t1.yyyy",yyyyList);  //"and t1.yyyy in (?,?,?)"  or "and 1=1" if yyyyList is empty
 * Table1.findBySQL(sql.sql(),sql.parameters());
 */
public class SqlBuilder {
    private String seperator = " ";
    private StringBuilder sql = new StringBuilder();
    private List<Object> paras = new ArrayList<>();

    public SqlBuilder(String sql, Object... paras) {
        append(sql, paras);
    }

    public SqlBuilder() {
    }

    public String sql() {
        return getSql().toString();
    }

    public Object[] parameters() {
        return paras.toArray();
    }

    public SqlBuilder withSeperatorCrlf() {
        seperator = "\n";
        return this;
    }

    public SqlBuilder withSeperatorSpace() {
        seperator = "  ";
        return this;
    }

    public SqlBuilder append(String sql, Object... paras) {
        if (paras != null && paras.length == 1 && paras[0] instanceof Collection) {
            return append(sql, (Collection) paras[0]);
        }
        return append(sql, Arrays.asList(paras));
    }

    public SqlBuilder append(String sql, Collection paras) {
        if (this.sql.length() > 0) {
            this.sql.append(seperator);
        }
        this.sql.append(sql);
        if (paras != null) {
            this.paras.addAll(paras);
        }
        return this;
    }

    public SqlBuilder append(SqlBuilder sql) {
        if (sql != null) {
            return append(sql.sql(), sql.paras);
        }
        return this;
    }

    public SqlBuilder appendIf(boolean contidition, String sql, Object... paras) {
        if (paras != null && paras.length == 1 && paras[0] instanceof Collection) {
            return appendIf(contidition, sql, (Collection) paras[0]);
        }
        if (contidition) {
            return append(sql, Arrays.asList(paras));
        }
        return this;
    }

    public SqlBuilder appendIf(boolean contidition, SqlBuilder truesql) {
        if (contidition) {
            return append(truesql);
        }
        return this;
    }

    public SqlBuilder appendIf(boolean contidition, SqlBuilder truesql, SqlBuilder falsesql) {
        if (contidition) {
            return append(truesql);
        }
        return append(falsesql);
    }


    public SqlBuilder in(String sql, Object... paras) {
        if (paras != null && paras.length == 1 && paras[0] instanceof Collection) {
            return in(sql, (Collection) paras[0]);
        }
        return in(sql, Arrays.asList(paras));
    }

    public SqlBuilder in(String sql, Collection paras) {
        if (paras.isEmpty()) {
            return append("1=0");
        }
        StringBuilder inSql = getIn(paras);

        return append(sql + " in (" + inSql.toString() + ")", paras);
    }

    protected StringBuilder getIn(Collection paras1) {
        StringBuilder inSql = new StringBuilder("");
        for (Object para : paras1) {
            if (inSql.length() > 0) {
                inSql.append(",");
            }
            inSql.append("?");
        }
        return inSql;
    }

    public SqlBuilder notIn(String sql, Object... paras) {
        if (paras != null && paras.length == 1 && paras[0] instanceof Collection) {
            return notIn(sql, (Collection) paras[0]);
        }
        return notIn(sql, Arrays.asList(paras));
    }

    public SqlBuilder notIn(String sql, Collection paras) {
        if (paras.isEmpty()) {
            return append("1=1");
        }
        StringBuilder inSql = getIn(paras);

        return append(sql + " not in (" + inSql.toString() + ")", paras);
    }

    //简单的连接符
    public SqlBuilder where() {
        return append(" where ");
    }

    public SqlBuilder from() {
        return append(" from ");
    }

    public SqlBuilder and() {
        return append(" and ");
    }

    public SqlBuilder or() {
        return append(" or ");
    }

    public SqlBuilder orderBy() {
        return append(" order by ");
    }

    public SqlBuilder groupBy() {
        return append(" group by ");
    }

    public StringBuilder getSql() {
        return sql;
    }

    public List<Object> getParas() {
        return paras;
    }
}
