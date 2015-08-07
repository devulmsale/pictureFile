package models.movie_api.vo;

import models.mer.MerchantImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 传给调用者指定的图片信息
 * Created by Administrator on 2015/8/7.
 */
public class ImageVO {

    public String url;

    public String uFid;

    public Integer width;

    public Integer height;

    public static List<ImageVO> changeMerchantImageToImageOV(List<MerchantImage> merchantImageList) {
        List<ImageVO> imageVOList = new ArrayList<>();
        ImageVO vo = null;
        for(MerchantImage mi : merchantImageList) {
            vo = new ImageVO();
            vo.uFid = mi.uFId;
            vo.url = mi.url;
            imageVOList.add(vo);
        }
        return imageVOList;
    }
}
