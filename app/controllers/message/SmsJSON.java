package controllers.message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.common.util.StringUtils;
import models.mer.Merchant;
import play.Logger;
import play.mvc.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 */
public class SmsJSON extends Controller {
    public  static void sendSms(String code , String phone , String message){


        Map<String , Object> resultMap = new HashMap<>();

        /**
         * 判断 code 是否为空
         */
        if(StringUtils.isBlank(code)) {
            resultMap.put("success", "false");
            resultMap.put("msg" , "code  is null");
            renderJSON(resultMap);
        }

        /**
         * 判断手机号是否为空
         */
        if(StringUtils.isBlank(phone)){
            resultMap.put("success","false");
            resultMap.put("msg","phone is null");
            renderJSON(resultMap);
        }

        /**
         * 判断发送的信息是否为空
         */
        if(StringUtils.isBlank(message)){
            resultMap.put("success","false");
            resultMap.put("msg","message is null");
            renderJSON(resultMap);
        }


        /**
         * 判断商户是否存在
         */
        Merchant merchant = Merchant.findByCode(code);
        if(merchant == null) {
            resultMap.put("success" , "false");
            resultMap.put("msg", "code not exist or code is delete");
         //   String resultBack = "callback("+resultMap+")";

            renderJSON(resultMap);
        }

        String type="";
       // String resp = "";
        String content = "你的验证码是:"+message+" 【悠悠小镇】";
        Logger.info("11");
        try {
            StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/send/?");//访问地址
            sb.append("apikey=").append("fac4f722c98730807fbcb7f4fd6bc586");//秘钥
            sb.append("&username=").append("haigao");//登录名
            sb.append("&password=").append("327958036");//密码
            sb.append("&mobile=").append(phone);//手机号
            sb.append("&content=" + URLEncoder.encode(content));
            Logger.info("22");
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            type = in.readLine();
            String[] resps=type.split(":");
            Logger.info("33");
            if(resps[0].equals("success")){
                Logger.info("44");

                resultMap.put("success", "true");
                resultMap.put("msg" , "success");
                renderJSON(resultMap);
            }else{
                Logger.info("55");
                resultMap.put("success", "false");
                resultMap.put("msg" , "not know error");
                renderJSON(resultMap);
                Logger.info("发送短信失败");
            }
        } catch (Exception exp) {
            Logger.info("66");
            resultMap.put("success", "false");
            resultMap.put("msg" , "not know error");
            renderJSON(resultMap);
            Logger.info("发送短信失败22");

        }
    }
}
