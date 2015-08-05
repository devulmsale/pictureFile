package controllers.wap;

import controllers.wap.auth.MerchantSecure;
import controllers.wap.auth.SkipLoginCheck;
import helper.imageupload.ImageUploadResult;
import helper.imageupload.ImageUploader;
import models.common.FolderAttr;
import models.common.FolderJSON;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantImage;
import models.mer.MerchantUser;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by upshan on 15/8/4.
 */
@With(MerchantSecure.class)
public class FileUploadController extends Controller {

    public static void upload() {
        render();
    }

    public static void folderJSON() {
        List<FolderPropertie> topFolderList = FolderPropertie.findByTopFolder();
        List<FolderJSON> topFolderJsonList = new ArrayList<>();
        FolderJSON topFolderJSON = null;
        FolderAttr topAttr = null;
        for(FolderPropertie topFolder : topFolderList) {
            topFolderJSON = new FolderJSON();
            Long count = FolderPropertie.countByParentFolder(topFolder.id);
            topAttr = new FolderAttr();
            topAttr.id = topFolder.id.toString();
            topFolderJSON.attr = topAttr;
            topFolderJSON.title = topFolder.name;
            topFolderJSON.type = count > 0 ? "folder" : "item";
            if(count > 0) {
                List<FolderPropertie> childFolderList = FolderPropertie.findByParentFolder(topFolder.id);
                List<FolderJSON> folderJSONList = new ArrayList<>();
                FolderJSON folderJSON = null;
                FolderAttr childAttr = null;
                for(FolderPropertie childFolder : childFolderList) {
                    folderJSON = new FolderJSON();
                    childAttr = new FolderAttr();
                    childAttr.id = childFolder.id.toString();
                    folderJSON.attr = childAttr;
                    folderJSON.title = childFolder.name;
                    folderJSON.type = "item";
                    folderJSONList.add(folderJSON);
                }
                topFolderJSON.products = folderJSONList;
            }
            topFolderJsonList.add(topFolderJSON);

        }
        Logger.info("topFolderJsonList :" + topFolderJsonList);
        renderJSON(topFolderJsonList);
    }


    public static void saveFile(File images , Long folderId , Integer width , Integer height) throws Exception {
        Merchant merchant = MerchantSecure.getMerchant();
        // 拿到文件夹
        FolderPropertie folderPropertie = FolderPropertie.findByUN_DeletedAndId(folderId);
        MerchantImage merchantImage = new MerchantImage(merchant , folderPropertie , width , height);
        /* 保存图片代码 */
        if (images != null && images.getName() != null) {
            ImageUploadResult imageUploadResult = ImageUploader.upload(images);
            merchantImage.uFId =  imageUploadResult.ufId;
            merchantImage.url = ImageUploader.getImageUrl(imageUploadResult.ufId, width+"x"+height);
            merchantImage.save();
        }
        Logger.info("file : %s ----------" , images);
    }
}
