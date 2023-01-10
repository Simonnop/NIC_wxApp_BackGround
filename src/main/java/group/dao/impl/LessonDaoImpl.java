package group.dao.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.LessonDao;
import group.dao.util.DataBaseUtil;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import org.bson.Document;

import java.util.ArrayList;

public class LessonDaoImpl implements LessonDao {

    private static final LessonDaoImpl lessonDaoImpl = new LessonDaoImpl();
    private LessonDaoImpl() {
    }
    public static LessonDaoImpl getLessonDao() {
        return lessonDaoImpl;
    }

    MongoCollection<Document> lessonDocument = DataBaseUtil.getMongoDB().getCollection("Lesson");

    @Override
    public void addLessonInfo(String userid, String lessonStr) {

        Document insertDoc = new Document();
        insertDoc.put("userid", userid);
        insertDoc.put("lessons", JSON.parseArray(lessonStr));

        lessonDocument.insertOne(insertDoc);
    }

    @Override
    public <T> ArrayList<Document> showLessonsByInput(String field, T value) {

        Document document = lessonDocument.find(Filters.eq("userid", "U202111390")).first();
        if (document == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }

        return (ArrayList<Document>) document.getList("lessons", Document.class);
    }
}
