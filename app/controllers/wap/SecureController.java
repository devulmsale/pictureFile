package controllers.wap;

import controllers.wap.auth.MerchantSecure;
import models.constants.DeletedStatus;
import models.mer.Merchant;
import models.mer.MerchantUser;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import util.common.RandomNumberUtil;

import java.util.Date;

/**
 * Created by upshan on 15/8/4.
 */
public class SecureController extends Controller {

    /**
     * 跳转注册
     */
    public static void register() {
        render();
    }


    /**
     * 增加注册信息
     * @param merchantUser
     */
    public static  void creatRegister(MerchantUser merchantUser){


    }

    /**
     * 跳转商户注册
     */
    public static  void  shopRegister(){
        render();
    }

    /**
     * 增加商户
     *
     */
    public  static  void  createShopRegister(@Valid Merchant merchant ,@Valid MerchantUser merchantUser ){

        //生成商户
        if(validation.hasErrors()) {
            Logger.info("商户名称输入的格式不正确");
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            shopRegister();
        }
        Date date=new Date();
        merchant.code=getCode();
        merchant.createdAt=date;
        merchant.deleted= DeletedStatus.UN_DELETED;
        merchant.save();

        //生成系统管理员用户的信息
        if(!merchantUser.password.equals(merchantUser.cofPassword)){
            flash.put("cofPassword" , "输入的两次密码不一致");
            Logger.info("两次密码不一致");
            shopRegister();
        }
        //生成加密盐
        String passwordSalt = RandomNumberUtil.generateRandomChars(6);
        merchantUser.passwordSalt=passwordSalt;
        //Md5加密跟加密盐生成密码
        String encryptedPassword = DigestUtils.md5Hex(merchantUser.cofPassword + passwordSalt);
        merchantUser.encryptedPassword=encryptedPassword;
        merchantUser.createdAt=date;
        merchantUser.deleted=DeletedStatus.UN_DELETED;
        merchantUser.merchant=merchant;
        merchantUser.save();
        try {
            MerchantSecure.logout();
        } catch (Throwable throwable) {
            Logger.info("退出失败");
            throwable.printStackTrace();
        }
    }

    private static String getCode() {
        Integer count = 1;
        String code = "";
        while (count == 1) {
            code =  RandomNumberUtil.generateRandomChars(8); //abcdef
            Long isHave = Merchant.count("code = ?" , code);
            count = isHave > 0 ? 1 : 0;
        }
        return code;
    }





}
