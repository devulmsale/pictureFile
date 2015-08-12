package models.movie_api.vo;

import cache.CacheCallBack;
import cache.CacheHelper;
import controllers.foundation.imageJSON;
import helper.imageupload.ImageUploader;
import models.mer.MerchantImage;
import play.Logger;
import play.cache.Cache;

import java.util.ArrayList;
import java.util.List;

/**
 * 传给调用者指定的图片信息
 * Created by Administrator on 2015/8/7.
 */
public class ImageVO {

    public static final String CACHE_IMG_URL_KEY = "cache_image_url_key_";

    public String url;

    public String uFid;

    public Integer width;

    public Integer height;

    public Long  id;

    public static List<ImageVO> changeMerchantImageToImageOV(List<MerchantImage> merchantImageList) {
        List<ImageVO> imageVOList = new ArrayList<>();
        ImageVO vo = null;
        for(MerchantImage mi : merchantImageList) {
            vo=new ImageVO();
            if(mi.height>=200){
                vo.url = getImage(mi.uFId , "x200");
            }else{
                vo.url = getImage(mi.uFId , "x100");

            }
          //  vo.url=mi.url;
            vo.width=mi.width;
            vo.height=mi.height;
            vo.uFid = mi.uFId;

            vo.id=mi.id;
            imageVOList.add(vo);
        }
        return imageVOList;
    }

    /**
     * 图片的根据uFide 图片大小获取图片
     * @param uFid
     * @param wh  widthxheight   ->  300x200
     * @return
     */
    private static String getImage(final String uFid ,final String wh) {

        String imageUrl = "";
        Logger.info("Cache key : %S ", CacheHelper.getCacheKey(CACHE_IMG_URL_KEY + uFid + "_" + wh, "GetImage"));

        return CacheHelper.getCache(CacheHelper.getCacheKey(new String[]{CACHE_IMG_URL_KEY + uFid + "_"+wh}, "GetImage" , "24h"), new CacheCallBack<String>() {
            @Override
            public String loadData() {
                Logger.info(CACHE_IMG_URL_KEY + uFid + "_"+wh + "更新数据成功!");
                  //  return ImageUploader.getImageUrl(uFid, "300x");
                return ImageUploader.getImageUrl(uFid, "x200");
                }
        });

    }
}
