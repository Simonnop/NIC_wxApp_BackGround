import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import group.service.helper.SocketHelper;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestAny {
    @Test
    public void testGetLesson(){
        System.out.println("go!");

        String userLesson = SocketHelper.getSocketHelper().getUserLesson("U202111390", "Glgl1234567");

        System.out.println(userLesson);

        JSONArray lessonJson = JSON.parseArray(userLesson);

        System.out.println(lessonJson);

        MongoCollection<Document> lessonDocument = DataBaseUtil.getMongoDB().getCollection("Lesson");

        Document insertDoc = new Document();
        insertDoc.put("userid", "test");
        insertDoc.put("lessons", lessonJson);

        lessonDocument.insertOne(insertDoc);

    }

    @Test
    public void testGetLessonInDB(){

        MongoCollection<Document> lessonDocument = DataBaseUtil.getMongoDB().getCollection("Lesson");

        Document document = lessonDocument.find(Filters.eq("userid", "U202111390")).first();

        ArrayList<Document> lessons = (ArrayList<Document>) document.getList("lessons", Document.class);

        for (Document lesson : lessons) {
            System.out.println(lesson.toJson());
        }

        for (int i = 2; i < 4; i++) {
            System.out.println(lessons.get(i - 1));
        }


    }
}
