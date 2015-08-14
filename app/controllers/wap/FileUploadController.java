package controllers.wap;

import controllers.wap.auth.MerchantSecure;
import helper.imageupload.ImageUploadResult;
import helper.imageupload.ImageUploader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import me.chanjar.weixin.common.util.StringUtils;
import models.mer.FolderPropertie;
import models.mer.Merchant;
import models.mer.MerchantImage;
import models.movie_api.vo.FolderJSON;
import models.movie_api.vo.ImageVO;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import play.Logger;
import play.mvc.Controller;
import play.mvc.With;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Logger.info("floderList %s",folderList.size());
        //获取文件下的图片
        List<MerchantImage>  imageList=  MerchantImage.findByMerChant(merchant.getId());
        List<ImageVO> imageVOList=ImageVO.changeMerchantImageToImageOV(imageList);
       // System.out.println("size===="+imageVOList.size());
      //  System.out.println(imageList.get(0).url);
        render(imageVOList, folderList);
    }

    /**
     * ajax取到某个文件夹下的图片
     * @param id
     */
    public static void getImageByFolderAjax(Long id){
        //获取指定文件下的图片
        Merchant merchant = MerchantSecure.getMerchant();
        List<MerchantImage>  imageList=  MerchantImage.findByFolderMerChant(merchant.getId(), id);
        List<ImageVO> imageVOList=ImageVO.changeMerchantImageToImageOV(imageList);
        renderJSON(imageVOList);
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
     * @throws Exception
     */
    public static void saveFile(File[] images , Long folderId ) throws Exception {
        Map<String ,Object> map=new HashMap<>();

        Logger.info("执行 saveFile :" + images + "---------------");
        if(images == null){
         //   map.put("success",false);
            Logger.info("uuuuuuu");
            flash.put("msg" , "传入的文件不能为空");
            //map.put("msg","传入的文件不能为空");
            upload();
        }
        if(images.length <= 0){
            flash.put("msg","传入的文件不是图片文件");
            Logger.info("传入的文件不是图片文件");
            upload();
        }
        Logger.info("tttttttt");
        for(int i=0;i<images.length;i++){
            Logger.info("pppppppp");
            File image=images[i];
            Logger.info("image=="+image);
            //判断文件类型
            if(isImage(image)){
                  Logger.info("是图片1111111111111111111111");
            }else{
                flash.put("msg","传入的文件不是图片文件");
                Logger.info("传入的文件不是图片文件");
                //    map.put("msg","传入的文件不是图片文件");
                upload();
            }

            if(folderId==null||folderId==0l){
                flash.put("msg","找不到指定的文件夹");
                //map.put("msg","找不到指定的文件夹");
                upload();
            }

            Merchant merchant=MerchantSecure.getMerchant();



            BufferedImage sourceImg = null;
            try {
                sourceImg = ImageIO.read(new FileInputStream(image));
            } catch (IOException e) {
                Logger.info("获取图片宽度 长度 大小失败");
                flash.put("msg","获取图片宽度 长度 大小失败");
                //  map.put("msg", "获取图片宽度 长度 大小失败");
                upload();
                e.printStackTrace();
            }

            Long size=image.length();//文件的大小
            Long maxSize=1024*1024*5l;
            if(size>maxSize){
                flash.put("msg","上传的图片不能超过5M,请重新上传");
                // map.put("msg", "上传的图片不能超过5M,请重新上传");
                upload();
            }

            Integer width=sourceImg.getWidth();
            Integer height= sourceImg.getHeight();
            FolderPropertie folderPropertie = FolderPropertie.findByUN_DeletedAndId(folderId);
            MerchantImage merchantImage = new MerchantImage(merchant , folderPropertie , width , height);
        /* 保存图片代码 */
            if (image != null && image.getName() != null) {
                ImageUploadResult imageUploadResult = null;
                try {
                    imageUploadResult = ImageUploader.upload(image);
                } catch (Exception e) {
                    flash.put("msg","未知错误上传图片失败");
                    // map.put("msg", "未知错误上传图片失败");
                    upload();
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
            // renderJSON(map);
            flash.put("msg","上传成功");
        }


        upload();

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

    /**
     * 判断文件是否是图片文件
     * @param file
     * @return
     */
    public static final boolean isImage(File file){
        boolean flag = false;
        try{
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if(width==0 || height==0){
                Logger.info("11111111111111111");
                flag = false;
            }else {
                Logger.info("2222222222222222222");

                flag = true;
            }
        }catch (IOException e){
            Logger.info("333333333333333333");
            flag = false;
        }catch (Exception e) {
            Logger.info("444444444444444444444444444444");
            flag = false;
        }
        return flag;
    }

}
