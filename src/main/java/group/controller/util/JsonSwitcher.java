package group.controller.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

public class JsonSwitcher {
    public static Map<String, Integer> readStringIntegerJson(JSONObject dataJson, String key) {

        return JSONObject.parseObject(
                dataJson.getJSONObject("reporterNeeds").toJSONString(),
                new TypeReference<Map<String, Integer>>() {});

    }

    /*
    * TODO 补齐常用的 json 工具
    * */
}
