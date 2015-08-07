package controllers.pic_api.impl;

import com.google.gson.Gson;
import controllers.pic_api.framework.ApiBase2Action;
import controllers.pic_api.framework.ApiContext;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantUser;
import models.movie_api.ApiResultCode;
import models.movie_api.request.FolderRequest;
import models.movie_api.vo.FolderJSON;
import play.Logger;

import java.util.List;

/**
 * 登录处理
 * <p/>
 * 参数名 	类型 	是否必填 	说明
 * userName 	String 	是 	用户名
 * password 	String 	是 	密码
 */
public class GetFolderAction extends ApiBase2Action<FolderRequest, List<FolderJSON>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getParamNames() {
        return new String[]{
                "userName", "password"
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getRequiredParamNames() {
        return new String[]{
                "code", "loginName" , "password"
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMethod() {
        return "folderJSON";
    }

    @Override
    public List<FolderJSON> doExecute(ApiContext context, FolderRequest requestObject) {
        Logger.info("     requestObject on action=" + new Gson().toJson(requestObject));
        String code = requestObject.code;
        String loginName = requestObject.loginName;
        String password = requestObject.password;

        /**
         * 判断商户是否存在
         */
        Merchant merchant = Merchant.findByCode(code);
        if(merchant == null) {
            context.result = ApiResultCode.USER_PASSWORD_ERROR;
            return null;
        }

        /**
         * 判断账号密码是否匹配
         */
        MerchantUser merchantUser = MerchantUser.findByLoginNameAndPassword(loginName, password);
        if(merchantUser == null) {
            context.result = ApiResultCode.MERCHANT_USER_OR_PASSWORD_ERROR;
            return null;
        }

        /**
         * 判断登录人和商户是否一致
         */
        if(merchant != merchantUser.merchant) {
            context.result = ApiResultCode.MERCHANTUSER_MERCHANT_DIFF;
            return null;
        }

        List<FolderJSON> topFolderJsonList = FolderPropertie.findAllFolderJSON();

        return topFolderJsonList;
    }

    @Override
    public FolderRequest createEmptyRequestObject() {
        return new FolderRequest();
    }
}
