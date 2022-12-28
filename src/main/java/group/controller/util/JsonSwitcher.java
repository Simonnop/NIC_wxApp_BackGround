package group.controller.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

public class JsonSwitcher {
    public static <T> Map<String, T> readStringIntegerJson(JSONObject dataJson, String key) {

        return JSONObject.parseObject(
                dataJson.getJSONObject(key)
                        .toJSONString(),
                new TypeReference<Map<String, T>>() {});

    }

    /*
    * TODO 补齐常用的 json 工具
    * */

}
