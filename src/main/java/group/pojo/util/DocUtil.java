package group.pojo.util;

import org.bson.Document;

import java.lang.reflect.Field;

public class DocUtil {
    // 定义泛型方法：
    // 1. 在返回值前，声明泛型参数 <参数名>；
    // 2. 传入参数必须是 Class，但这个 Class 是泛型参数，不限制类型
    public static <T> T doc2Obj(Document doc, Class<T> clazz) {
        // 实例化泛型对象
        try {
            T obj = clazz.newInstance();
            doc.remove("_id");
            for (String key : doc.keySet()) {
                // 获取字段
                Field field = clazz.getDeclaredField(key);
                // 开放字段操作权限
                field.setAccessible(true);
                // 设置值
                field.set(obj, doc.get(key));

            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 定义泛型方法：
    // 1. 在返回值前，声明泛型参数 <参数名>；
    // 2. 传入参数时，指定一个泛型参数
    public static <T> Document obj2Doc(T obj) {

        Document doc = new Document();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 开放字段操作权限
                field.setAccessible(true);
                // 设置值
                doc.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取所有字段：通过 getClass() 方法获取 Class 对象，然后获取这个类所有字段

        return doc;
    }
}
