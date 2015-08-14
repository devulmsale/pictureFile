package controllers.foundation;

import com.google.gson.Gson;
import me.chanjar.weixin.common.util.StringUtils;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantUser;
import models.movie_api.vo.FolderJSON;
import play.Logger;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
public class folderJSON extends Controller {


    /**
     *
     * @param code  创建用户时自动分配的 code
     * @param password  用户自定义密码
     * @return  JSON {success : "false"/true , msg : '错误信息/[正确信息]' , tree}
     *
     */
    public static void excute(String code , String loginName , String password,String callback) {

        Logger.info("执行 folderJSON.excute");


        Logger.info(" code : %s  | loginName : %s  | password : %s ---" , code , loginName , password);

        Map<String , Object> resultMap = new HashMap<>();

        /**
         * 判断 code 是否为空
         */
        if(StringUtils.isBlank(code)) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "未获取到code信息");
            renderJSON(resultMap);
        }

        Logger.info("1");

        /**
         * 判断 loginName 是否为空
         */
        if(StringUtils.isBlank(loginName)) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "未获取到loginName信息");
            renderJSON(resultMap);
        }

        Logger.info("2");

        /**
         * 判断 password 是否为空
         */
        if(StringUtils.isBlank(password)) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "未获取到password信息");
            renderJSON(resultMap);
        }
        Logger.info("3");

        /**
         * 判断商户是否存在
         */
        Merchant merchant = Merchant.findByCode(code);
        if(merchant == null) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "code 不存在或已删除");
            String resultBack = "callback("+resultMap+")";
            renderJSON(resultBack);
        }
        Logger.info("4");

        /**
         * 判断账号密码是否匹配
         */
        MerchantUser merchantUser = MerchantUser.findByLoginNameAndPassword(loginName, password);
        if(merchantUser == null) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "用户名或密码错误");
            renderJSON(resultMap);
        }
        Logger.info("5");
        /**
         * 判断登录人和商户是否一致
         */
        if(merchant != merchantUser.merchant) {
            resultMap.put("success" , "false");
            resultMap.put("msg" , "登录账号所属商户跟code商户不一致");
            renderJSON(resultMap);
        }

        List<FolderJSON> topFolderJsonList = FolderPropertie.findAllFolderJSON();
        Gson gson = new Gson();
        gson.toJson(topFolderJsonList);
        renderJSON(callback + "(" + gson.toJson(topFolderJsonList) + ")");
    }
}
