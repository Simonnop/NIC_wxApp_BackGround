package group.service.manager;

import com.mongodb.client.FindIterable;
import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MissionManager {
    private static final MissionManager missionManager = new MissionManager();
    private MissionManager() {
    }
    public static MissionManager getMissionManager() {
        return missionManager;
    }

    final UserDao userDao = UserDaoImpl.getUserDao();
    final MissionDao missionDao = MissionDaoImpl.getMissionDao();

    public void updateMissionStatus(String missionID) {

        Document document = missionDao.searchMissionByInput("missionID", missionID).first();
        for (String kind : ((Document) calculateLack(document).get("reporterNeeds")).keySet()
        ) {
            if (!((Document) document.get("reporterLack"))
                    .get(kind)
                    .equals(0)) {
                return;
            }
        }

        missionDao.updateInMission(
                "missionID", missionID,
                "status.接稿", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("updated");
    }

    public ArrayList<Document> changeFormAndCalculate(FindIterable<Document> documents) {
        // 改成可以 addAll 的格式并计算 缺少人数
        ArrayList<Document> missionArray = new ArrayList<>();

        for (Document document : documents) {
            // 计算还缺少的人数
            missionArray.add(calculateLack(document));
        }

        return missionArray;
    }

    public Document calculateLack(Document document) {

        document.remove("_id");
        // 计算还缺少的人数
        Document reporterNeeds = (Document) document.get("reporterNeeds");
        Document reporters = (Document) document.get("reporters");
        Document reporterLack = new Document();

        for (String str : reporterNeeds.keySet()
        ) {
            reporterLack.put(str, (Integer) reporterNeeds.get(str)
                    - reporters.getList(str, String.class).size());
        }
        document.put("reporterLack", reporterLack);

        return document;
    }
}
