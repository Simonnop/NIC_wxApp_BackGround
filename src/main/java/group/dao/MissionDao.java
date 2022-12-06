package group.dao;

import group.pojo.Mission;
import group.pojo.util.MyTime;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Map;

public interface MissionDao {
    void add(Mission mission);

    ArrayList<Document> showAll();

    void showNeed();

}
