package toolkit.sql;

import java.lang.reflect.InvocationTargetException;

import toolkit.exception.SQLBuilderException;
import toolkit.reflect.Reflect;
import toolkit.util.StringUtil;
import toolkit.annotation.Column;
import toolkit.annotation.Table;

import java.util.List;


import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * SQLBuilder
 */
public class SQLBuilder {

    public static HashMap<String , Object> buildNewId(Object obj) throws SQLBuilderException{
        String seq = obj.getClass().getAnnotation(Table.class).seq();
        if(seq==null || seq=="" || seq.equals("") ){
            throw new SQLBuilderException("THERE ARE NO SEQ FOR THIS TABLE ");
        }
        String sql="SELECT "+seq+".NEXTVAL AS nextval FROM DUAL";
        System.out.println("sql nextid:"+sql);
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        classSetColumns.put("query", sql);
        return classSetColumns;
    }

    public static HashMap<String , Object> buildCreate(Object obj) throws SQLBuilderException{
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        String tableName = SQLBuilderHelper.getTableName(obj);
        Field[] columns=Reflect.getAnnotatedFields(obj, Column.class);
        List<Class<?>> classSet = new ArrayList<>();
        List<String> getters = new ArrayList<>();
        String sql ="INSERT INTO "+tableName;
        String sqlColumns="(";
        String sqlVariable="(";
        for (int i = 0; i < columns.length; i++) {
            Column annotation= columns[i].getAnnotation(Column.class);
            Class<?> clazz = columns[i].getType();
            String attributeName=columns[i].getName();
            String getter="get"+StringUtil.firstToUpperCase(attributeName);
            if(i== columns.length-1){
                sqlColumns+=annotation.name();
                sqlVariable+="?";
            }
            else{
                sqlColumns+=annotation.name()+",";
                sqlVariable+="?,";
            }
            classSet.add(clazz);
            getters.add(getter);
        }
        sqlColumns+=")";
        sqlVariable+=")";
        sql+=sqlColumns + " VALUES " +sqlVariable;
        classSetColumns.put("query", sql);
        classSetColumns.put("classSet" , classSet.toArray(new Class<?>[0]));
        classSetColumns.put("getter",getters.toArray(new String[0]));
        return classSetColumns;
    }
    public static HashMap<String , Object> buildReadAll(Object obj) throws SQLBuilderException{
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        String tableName = SQLBuilderHelper.getTableName(obj);
        String sql ="SELECT * FROM "+tableName;
        classSetColumns.put("query", sql);  
        return classSetColumns;
    }
    public static HashMap<String , Object> buildReadByPk(Object obj) throws SQLBuilderException{
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        List<Class<?>> classSet = new ArrayList<>();
        List<String> getters = new ArrayList<>();
        String tableName = SQLBuilderHelper.getTableName(obj);
        String sql ="SELECT * FROM "+tableName +" WHERE ";
        String pk=SQLBuilderHelper.getPrimaryKeyName(obj);

        sql+=pk+ " = ? ";

        Field field=SQLBuilderHelper.getFieldPrimaryKey(obj);
        Class<?> clazz = field.getType();
        String getter = "get"+StringUtil.firstToUpperCase( field.getName());
        classSet.add(clazz);
        getters.add(getter);
        classSetColumns.put("query", sql);
        classSetColumns.put("classSet",classSet.toArray(new Class<?>[0]));
        classSetColumns.put("getter",getters.toArray(new String[0]) );

        return classSetColumns;
    }
    public static HashMap<String , Object> buildUpdate(Object obj)throws SQLBuilderException,NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        String tableName = SQLBuilderHelper.getTableName(obj);
        Field[] columns=Reflect.getAnnotatedFields(obj, Column.class);
        List<Class<?>> classSet = new ArrayList<>();
        List<String> getters = new ArrayList<>();
        String sql ="UPDATE "+tableName + " SET ";
        String pk = SQLBuilderHelper.getPrimaryKeyName(obj);
        Class<?> clazzPk=null;
        String attributeNamePk=null;
        String getterPk=null;
        for (int i=0 ; i<columns.length ;i++) {
            Column annotation = columns[i].getAnnotation(Column.class);
            String columnName = annotation.name();
            if(!annotation.primaryKey()){
                Class<?> clazz=columns[i].getType();
                String attributeName=columns[i].getName();
                String getter="get"+StringUtil.firstToUpperCase(attributeName);
                if(i== columns.length-1){
                    sql+=columnName+"="+"?";   
                }
                else{
                    sql+=columnName+"="+"?"+",";
                }        
                classSet.add(clazz);
                getters.add(getter);
            }
            else{
                clazzPk=columns[i].getType();
                attributeNamePk=columns[i].getName();
                getterPk="get"+StringUtil.firstToUpperCase(attributeNamePk);
            }
        }
        classSet.add(clazzPk);
        getters.add(getterPk);
        sql+=" WHERE "+pk+ " = ?";
        classSetColumns.put("query", sql);
        classSetColumns.put("classSet" , classSet.toArray(new Class<?>[0]));
        classSetColumns.put("getter",getters.toArray(new String[0]));
        return classSetColumns;
    }
    public static HashMap<String , Object>  buildDelete(Object obj)throws SQLBuilderException,NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        HashMap<String , Object> classSetColumns=new HashMap<String , Object>(); 
        List<Class<?>> classSet = new ArrayList<>();
        List<String> getters = new ArrayList<>();
        String tableName = SQLBuilderHelper.getTableName(obj);
        String sql ="DELETE FROM "+tableName +" WHERE ";
        String pk=SQLBuilderHelper.getPrimaryKeyName(obj);
        sql+=pk+ " = ? ";

        Field field=SQLBuilderHelper.getFieldPrimaryKey(obj);
        Class<?> clazz = field.getType();
        String getter = "get"+StringUtil.firstToUpperCase( field.getName());
        classSet.add(clazz);
        getters.add(getter);
        classSetColumns.put("query", sql);
        classSetColumns.put("classSet",classSet.toArray(new Class<?>[0]));
        classSetColumns.put("getter",getters.toArray(new String[0]) );

        return classSetColumns;
    }

}