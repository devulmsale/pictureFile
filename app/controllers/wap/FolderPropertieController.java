package controllers.wap;

import controllers.wap.auth.MerchantSecure;
import models.constants.DeletedStatus;
import models.mer.FolderPropertie;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

import java.util.Date;


/**
 * Created by Administrator on 2015/8/6.
 */
@With(MerchantSecure.class)
public class FolderPropertieController extends Controller {

    /**
     * 添加文件夹
     *
     */
    public static void createFolder(@Valid FolderPropertie folderPropertie,String idandlev){
        if(validation.hasErrors()){
            params.flash();
            validation.keep();
            FileUploadController.upload();
        }
        folderPropertie.merchant=MerchantSecure.getMerchant();
        folderPropertie.merchantUser=MerchantSecure.getMerchantUser();
        if(idandlev.equals("")){
            //一级文件
            folderPropertie.parentFoulder=null;
            folderPropertie.level=1;
        }else{
            String[] strings=idandlev.split(",");
             FolderPropertie fp=new FolderPropertie();
            fp.id=Long.parseLong(strings[0]);
            folderPropertie.level=Integer.parseInt(strings[1])+1;
            folderPropertie.parentFoulder=fp;
        }
        folderPropertie.deleted= DeletedStatus.UN_DELETED;
        folderPropertie.createdAt=new Date();
        folderPropertie.save();
        redirect("/upload");
    }

}
