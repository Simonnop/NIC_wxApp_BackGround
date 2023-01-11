package group.dao;

import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;

public interface LessonDao {

    void addLessonInfo(String userid, String lessonStr);

    <T> ArrayList<Document> showLessonsByInput(String field, T value);

    FindIterable<Document> showAll();
}
