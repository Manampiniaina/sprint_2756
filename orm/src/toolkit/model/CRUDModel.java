package toolkit.model;

import toolkit.annotation.Table;
import toolkit.sql.SQLBuilder;
import toolkit.sql.SQLResult;

import java.sql.*;
import java.util.*;

public class CRUDModel<T> {
    public String nextId(Connection c) {
        try {
            HashMap<String , Object > classSetColumns=SQLBuilder.buildNewId(this);
            PreparedStatement st = SQLResult.getStatement(c, this, classSetColumns);
            String id="";
            String val= SQLResult.getSpecString(st, "nextval");
            Table t= this.getClass().getAnnotation(Table.class);
            id=t.desc();
            int lengthless = t.length()-id.length();
            for(int i = 0 ; i<lengthless ;i++){
                id+="0";
            }
            id+=val;
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(Connection c ) {
        try {
            HashMap<String, Object> s=SQLBuilder.buildCreate(this);
            PreparedStatement st = SQLResult.getStatement(c,  this, s);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(Connection c ) {
        try {
            HashMap<String, Object> s=SQLBuilder.buildUpdate(this);
            PreparedStatement st = SQLResult.getStatement(c,  this, s);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<T> readAll(Connection c) {
        try {
            List<T> objs=null;
            HashMap<String , Object> classSet = SQLBuilder.buildReadAll(this);
            PreparedStatement st = SQLResult.getStatement(c, this, classSet);
            objs = SQLResult.<T>getResult(st, this);
            return objs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public T readByPk(Connection c) {
        try {
            T obj=null;
            HashMap<String , Object> classSet = SQLBuilder.buildReadByPk(this);
            PreparedStatement st = SQLResult.getStatement(c, this, classSet);
            obj = SQLResult.<T>getObject(st, this );
            return obj; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void deleteByPk(Connection c) {
        try {
            Object obj = this;
            HashMap<String, Object> s=SQLBuilder.buildDelete(obj);
            PreparedStatement st = SQLResult.getStatement(c,obj, s);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
