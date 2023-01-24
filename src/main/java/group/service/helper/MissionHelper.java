package group.service.helper;

import com.mongodb.client.FindIterable;
import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.WorkDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.dao.impl.WorkDaoImpl;
import group.pojo.WorkFlow;
import group.pojo.util.DocUtil;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MissionHelper {
    private static final MissionHelper missionHelper = new MissionHelper();
    private MissionHelper() {
    }
    public static MissionHelper getMissionHelper() {
        return missionHelper;
    }

    final UserDao userDao = UserDaoImpl.getUserDao();
    final MissionDao missionDao = MissionDaoImpl.getMissionDao();
    final WorkDaoImpl workDao = WorkDaoImpl.getWorkDaoImpl();

    public void updateMissionStatus(String missionID) {

        Document document = workDao.searchMissionByInput("missionID", missionID).first();
        WorkFlow workFlow = DocUtil.doc2Obj(document, WorkFlow.class);
        workFlow.checkProgress();

        workDao.replaceMission(
                "missionID", missionID,
                DocUtil.obj2Doc(workFlow));
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
        Document reporterNeeds = (Document) document.get("peopleNeeds");
        Document reporters = (Document) document.get("peopleGet");
        Document reporterLack = new Document();

        for (String str : reporterNeeds.keySet()
        ) {
            reporterLack.put(str, (Integer) reporterNeeds.get(str)
                    - reporters.getList(str, String.class).size());
        }
        document.put("peopleLack", reporterLack);

        return document;
    }
}
