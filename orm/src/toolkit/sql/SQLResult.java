package toolkit.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import toolkit.annotation.Column;
import toolkit.reflect.Reflect;
import toolkit.util.StringUtil;
import java.util.*;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.ResultSet;
import java.lang.reflect.*;

public class SQLResult{

    public static void getSet(int i, PreparedStatement st ,Object obj , Class<?> clazz ,String getter ) throws SQLException, NumberFormatException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        if(clazz==Double.class || clazz==double.class ){
            st.setDouble(i  , (Double)Reflect.executeWithoutParams(obj, getter)  );
        }
        else if(clazz==Integer.class || clazz==int.class){
            st.setInt(i , (Integer)Reflect.executeWithoutParams(obj, getter) );
        }
        else  if (clazz==Timestamp.class){
            st.setTimestamp(i , (Timestamp)Reflect.executeWithoutParams(obj, getter) );
        }
        else if(clazz==String.class){
            st.setString(i , (String)Reflect.executeWithoutParams(obj, getter) );
        
        }
        else if(clazz==Date.class){
            st.setDate(i , (Date)Reflect.executeWithoutParams(obj, getter) );
        }
        else if(clazz==boolean.class){
            st.setBoolean(i , (boolean)Reflect.executeWithoutParams(obj, getter) );

        }
        else if(clazz==Time.class){
            st.setTime(i , (Time)Reflect.executeWithoutParams(obj, getter) );
        }
    }
    public static PreparedStatement getStatement(Connection c,Object obj, HashMap<String , Object> columnClassSet ) throws SQLException{
        String sql=(String)columnClassSet.get("query");
      
        PreparedStatement st = c.prepareStatement(sql);
        try {
            if( !columnClassSet.containsKey("getter") || !columnClassSet.containsKey("classSet") ){
               
                return st;
            }
            else{
                String[] getters = (String[])columnClassSet.get("getter");
                Class<?>[] classSet = (Class<?>[])columnClassSet.get("classSet");
                for (int i = 0; i < classSet.length; i++) {
                    String getter = getters[i];
                    Class<?> clazz  = classSet[i];
                    getSet(i+1, st, obj, clazz, getter);
                }
                return st;
            }
        } catch (Exception e) {
            if(c!=null){c.close();}
            if(st!=null){st.close();}
            e.printStackTrace();
          
        }
        return st;
    }
    public static void setObject( ResultSet rs,Field field, Object obj) throws SQLException, NumberFormatException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Class<?> clazz =field.getType();
        String method="set"+StringUtil.firstToUpperCase(field.getName()) ;
        String columnName = field.getAnnotation(Column.class).name();
        if(clazz==Double.class || clazz==double.class ){
           Reflect.executeWithParams(obj, method, rs.getDouble(columnName));
        }
        else if(clazz==Integer.class || clazz==int.class){
            Reflect.executeWithParams(obj, method, rs.getInt(columnName));
        }
        else  if (clazz==Timestamp.class){
            Reflect.executeWithParams(obj, method, rs.getTimestamp(columnName));
        }
        else if(clazz==String.class){
            Reflect.executeWithParams(obj, method, rs.getString(columnName));
        }
        else if(clazz==Date.class){
            Reflect.executeWithParams(obj, method, rs.getDate(columnName));
        }
            
        else if(clazz==boolean.class){
            Reflect.executeWithParams(obj, method, rs.getBoolean(columnName));
        }
        else if(clazz==Time.class){
            Reflect.executeWithParams(obj, method, rs.getTime(columnName));
        } else {
            throw new SQLException("NE PEUT PAS CONVERTIR UN TYPE NON TROUVE ");
        }
    }
  public static <T> List<T> getResult(PreparedStatement st,Object obj ) throws  InvocationTargetException,NoSuchMethodException, SQLException, InstantiationException, IllegalAccessException {
    
    @SuppressWarnings("unchecked")    
    Class<T> clazz =(Class<T>)obj.getClass();
        List<T> objs = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            T newObj = clazz.getDeclaredConstructor().newInstance();  
            Field[] fields = clazz.getDeclaredFields();  
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    SQLResult.setObject(rs, field, newObj);  
                }
            }
            objs.add(newObj);
        }
        rs.close();
        return objs;
    }

    public static String getSpecString(PreparedStatement st , String value) throws SQLException{
        ResultSet rs=st.executeQuery();
        while (rs.next()) {
            return rs.getString(value);
        }
        rs.close();
        return null;
    }
   public static <T> T getObject(PreparedStatement st, Object obj) 
        throws SQLException, NumberFormatException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass(); // Cast explicite de Class<?> vers Class<T>
        ResultSet rs = st.executeQuery();
        Field[] fields = Reflect.getAnnotatedFields(obj, Column.class);
        
        while (rs.next()) {
            T newObj = clazz.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                SQLResult.setObject(rs, field, newObj);
            }
            rs.close(); // Assurez-vous de fermer le ResultSet avant de retourner un objet
            return newObj;
        }
        rs.close();
        return null;
    }


   
}
