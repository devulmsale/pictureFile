package controllers.wap;

import controllers.wap.auth.MerchantSecure;
import helper.imageupload.ImageUploadResult;
import helper.imageupload.ImageUploader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantImage;
import models.movie_api.vo.FolderJSON;
import play.Logger;
import play.mvc.Controller;
import play.mvc.With;

import java.io.File;
import java.util.List;

/**
 * Created by upshan on 15/8/4.
 */
@With(MerchantSecure.class)
public class FileUploadController extends Controller {

    /**
     * 跳转到该商户下所含文件夹下的所有图片
     *
     */
    public static void upload() {
        Merchant merchant = MerchantSecure.getMerchant();
        //获取该商户下所有的文件夹
       List<FolderPropertie> folderList =FolderPropertie.findByMerchant(merchant.id);
        //获取文件下的图片
        List<MerchantImage>  imageList=  MerchantImage.findByMerChant(merchant.getId());
        render(imageList, folderList);
    }

    /**
     * ajax取到某个文件夹下的图片
     * @param id
     */
    public static void getImageByFolderAjax(Long id){
        //获取指定文件下的图片
        Merchant merchant = MerchantSecure.getMerchant();
        List<MerchantImage>  imageList=  MerchantImage.findByFolderMerChant(merchant.getId(), id);
        renderJSON(imageList);
    }

    /**
     * 取到文件夹子父级关系列表
     */
    public static void folderJSON() {
        List<FolderJSON> topFolderJsonList = FolderPropertie.findAllFolderJSON();
        renderJSON(topFolderJsonList);
    }



    /**
     * 保存添加的图片
     * @param images
     * @param folderId
     * @param width
     * @param height
     * @throws Exception
     */
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




        Logger.info("file : %s ----------", images);
    }
    /**
     * 展示该商户下的所有图片
     */
    public static void showShopPic(){
        Merchant merchant = MerchantSecure.getMerchant();
        //获取该商户下所有的文件夹
        List<FolderPropertie> folderList =FolderPropertie.findByMerchant(merchant.id);
        //获取文件下的图片
        List<MerchantImage>  imageList=  MerchantImage.findByMerChant(merchant.getId());
        render(imageList,folderList);
    }
}
