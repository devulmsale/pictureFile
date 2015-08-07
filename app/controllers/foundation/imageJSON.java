package controllers.foundation;

import me.chanjar.weixin.common.util.StringUtils;
import models.common.ImageVO;
import models.mer.Merchant;
import models.mer.MerchantImage;
import play.db.jpa.JPA;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/7.
 */
public class imageJSON extends Controller {

    /**
     * 获取图片列表
     * @param code 商户唯一的标识
     * @param folderId 记录的文件夹的id
     */
    public static void excute(String code,Long folderId){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(code)){
            map.put("success",false);
            map.put("msg","未获得code信息");
            renderJSON(map);
        }
        if(folderId==null||folderId==0l){
            map.put("success",false);
            map.put("msg","指定文件不能为空");
            renderJSON(map);
        }
        Merchant merchant=Merchant.findByCode(code);
        if(merchant==null){
            map.put("success",false);
            map.put("msg","该code信息不存在");
            renderJSON(map);
        }

        List<MerchantImage> imageList=  MerchantImage.findByFolderMerChant(merchant.getId(), folderId);

        List<ImageVO> imageVOList = ImageVO.changeMerchantImageToImageOV(imageList);
        renderJSON(imageVOList);

    }
}
