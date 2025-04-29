package toolkit.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import toolkit.annotation.Column;
import toolkit.annotation.Table;
import toolkit.exception.SQLBuilderException;
import toolkit.reflect.Reflect;

public class SQLBuilderHelper {
    public static String getTableName(Object obj) throws SQLBuilderException{
        String tableName="";
        Table annotationtable= obj.getClass().getAnnotation(Table.class);
        if(annotationtable==null){
            throw new SQLBuilderException("METTEZ L' ANNOTAION @Table SUR L' OBJET MODEL");
        }else{
            tableName= annotationtable.name();
        }
        return tableName;
    }
    public static String getPrimaryKeyName(Object obj) throws SQLBuilderException{
        Field[] fields=Reflect.getAnnotatedFields(obj, Column.class);
        for (Field field : fields) {
            if(field.getAnnotation(Column.class).primaryKey()){
                return field.getName();
            }
        }
        throw new SQLBuilderException("LE PRIMARY KEY DE LA TABLE DOIT ETRE PRECISES DANS @Column");
        
    }
    public static Field getFieldPrimaryKey(Object obj) throws SQLBuilderException{
        Field[] fields=Reflect.getAnnotatedFields(obj, Column.class);
        for (Field field : fields) {
            if(field.getAnnotation(Column.class).primaryKey()){
               return field;
            }
        }
        throw new SQLBuilderException("LE FIELD PRIMARY KEY DE LA TABLE DOIT ETRE PRECISES DANS @Column ");
        
    }
    public static String[] getColumnsName(Object obj) throws SQLBuilderException{
        List<String> columns = new ArrayList<>();
        Field[] fields=Reflect.getAnnotatedFields(obj, Column.class);
        if(fields.length<=0 || fields==null){
            throw new SQLBuilderException("METTEZ L' ANNOTATION @Column SUR LES ATTRIBUTS");
        }
        else{
            for (Field field : fields) {
                columns.add(field.getAnnotation(Column.class).name() );
            }
        }
        return columns.toArray(new String[0]);
    }
}
