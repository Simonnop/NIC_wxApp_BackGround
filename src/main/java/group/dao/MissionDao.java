package group.dao;

import group.pojo.Mission;
import org.bson.Document;

import java.util.ArrayList;

public interface MissionDao {

    // 添加任务
    void add(Mission mission);

    // 显示所有任务
    ArrayList<Document> showAll();
    // 显示缺人的任务
    ArrayList<Document> showNeed();

    Document showById(String missionID);

    // 接任务
    void get(String username, String missionID, String kind);

    void updateStatus(String missionID);

    void updateFilePath(String filePath, String missionID);
}
