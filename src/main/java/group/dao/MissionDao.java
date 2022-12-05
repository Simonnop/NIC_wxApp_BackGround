package group.dao;

import group.pojo.util.MyTime;

import java.util.Map;

public interface MissionDao {
    void add(MyTime time, String place, String title,
                    String description, Map<String, Integer> reporterNeeds);
}
