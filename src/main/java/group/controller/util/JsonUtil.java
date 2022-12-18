package group.controller.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

public class JsonUtil {
    public static Map<String, Integer> readStringIntegerJson(JSONObject dataJson, String key) {

        return JSONObject.parseObject(
                dataJson.getJSONObject("reporterNeeds").toJSONString(),
                new TypeReference<Map<String, Integer>>() {});
    }
}
