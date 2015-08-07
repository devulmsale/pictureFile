package controllers.pic_api.v1.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * 接口辅助工具类.
 */
public class ApiUtil {

    /**
     * 生成参数对应的签名.
     * @param params 参数Map
     * @param secretCode 安全码
     * @return 签名.
     */
    public static String sign(Map<String, Object> params, String secretCode) {
        TreeSet<String> sortedKeySet = new TreeSet<>();
        sortedKeySet.addAll(params.keySet());
        List<String> valueList = new ArrayList<>();
        for (String key : sortedKeySet) {
            valueList.add(key + "=" + params.get(key));
        }
        String value = secretCode + StringUtils.join(valueList, "&");
        Logger.info("params=" + value);
        String md5Value1 = DigestUtils.md5Hex(value);
        return DigestUtils.md5Hex(md5Value1 + secretCode);
    }
}
