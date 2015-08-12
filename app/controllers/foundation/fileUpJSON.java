package controllers.foundation;

import cache.CacheHelper;
import helper.imageupload.ImageUploadResult;
import helper.imageupload.ImageUploader;
import me.chanjar.weixin.common.util.StringUtils;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantImage;
import play.Logger;
import play.cache.Cache;
import play.mvc.Controller;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/7.
 */
public class fileUpJSON extends Controller {
    /**
     * 图片上传接口
     */
    public static void excute(File image,String code, Long folderId){
        Map<String ,Object> map=new HashMap<>();

          if(image==null){
              map.put("success",false);
              map.put("msg","传入的文件不能为空");
              renderJSON(map);
          }

        //判断文件类型
        if(isImage(image)){
          //  Logger.info("是图片");
        }else{
            map.put("success",false);
            map.put("msg","传入的文件不是图片文件");
            renderJSON(map);
        }
        if(StringUtils.isBlank(code)){
            map.put("success",false);
            map.put("msg","未获得code信息");
            renderJSON(map);
        }
        if(folderId==null||folderId==0l){
            map.put("success",false);
            map.put("msg","找不到指定的文件夹");
            renderJSON(map);
        }

        Merchant merchant=Merchant.findByCode(code);
        if(merchant==null){
            map.put("success",false);
            map.put("msg","该code信息不存在");
            renderJSON(map);
        }


        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(image));
        } catch (IOException e) {
            Logger.info("获取图片宽度 长度 大小失败");
            map.put("success",false);
            map.put("msg", "获取图片宽度 长度 大小失败");
            renderJSON(map);
            e.printStackTrace();
        }

        Long size=image.length();//文件的大小
        Long maxSize=1024*1024*5l;
        if(size>maxSize){
            map.put("success",false);
            map.put("msg", "上传的图片不能超过5M,请重新上传");
            renderJSON(map);
        }

        Integer width=sourceImg.getWidth();
        Integer height= sourceImg.getHeight();

        System.out.println("width=="+width);
        System.out.println("height==="+height);

        FolderPropertie folderPropertie = FolderPropertie.findByUN_DeletedAndId(folderId);
        MerchantImage merchantImage = new MerchantImage(merchant , folderPropertie , width , height);
        /* 保存图片代码 */
        if (image != null && image.getName() != null) {
            ImageUploadResult imageUploadResult = null;
            try {
                imageUploadResult = ImageUploader.upload(image);
            } catch (Exception e) {
                map.put("success",false);
                map.put("msg", "未知错误上传图片失败");
                renderJSON(map);
            }
            merchantImage.uFId =  imageUploadResult.ufId;
            merchantImage.url = ImageUploader.getImageUrl(imageUploadResult.ufId, width+"x"+height);
            merchantImage.save();
            // CACHE_IMAGE_WEIXIAO_300x100
          //  Cache.add("CACHE_IMAGE_"+merchantImage.uFId+"_"+width+"x"+height , merchantImage.url , "24h");

        }
        map.put("success",true);
        map.put("uFid", merchantImage.uFId );
        map.put("url", merchantImage.url);
        renderJSON(map);

    }




    /**
     * 判断文件是否是图片文件
     * @param file
     * @return
     */
    public static final boolean isImage(File file){
        boolean flag = false;
        try
        {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if(width==0 || height==0){
                flag = false;
            }else {
                flag = true;
            }
        }
        catch (IOException e)
        {
            flag = false;
        }catch (Exception e) {
            flag = false;
        }
        return flag;
    }


}
