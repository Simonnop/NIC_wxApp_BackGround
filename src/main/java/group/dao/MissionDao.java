package group.dao;

import com.mongodb.client.FindIterable;
import group.pojo.Mission;
import org.bson.Document;

import java.util.ArrayList;

public interface MissionDao {

    // 添加任务
    void add(Mission mission);

    // 显示所有任务
    FindIterable<Document> showAll();

    FindIterable<Document> showById(String missionID);

    // 接任务
    void tryTakeByUser(String username, String missionID, String kind);

    void updateStatus(String missionID);

    void updateFilePath(String filePath, String missionID);
}
