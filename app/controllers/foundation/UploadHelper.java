package controllers.foundation;

import models.mer.FolderPropertie;
import models.mer.Merchant;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.libs.Images;
import play.mvc.Controller;
import util.common.RandomNumberUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by upshan on 15/7/21.
 */
public class UploadHelper extends Controller {

    public static final Integer MAX_IMAGE_WIDTH = 1200;
    public static final Integer MAX_IMAGE_HEIGHT = 800;
    public static final Long MAX_IMAGE_SIZE = 1024*1024*5l;

    public static final String BASE_PATH = Play.applicationPath.getAbsolutePath();  //拿到项目目录
    public static final String SPE = "/";
    public static final String BASE_IMAGE_PATH = BASE_PATH + SPE +"public"+SPE + "upload" + SPE;


    public static void execute(String code , File picture, Integer width , Integer height , Long folderId) {

        Map<String , Object> resultMap = new HashMap<>();
         if(StringUtils.isBlank(code)) {
             resultMap.put("success" , false);
             resultMap.put("msg" , "您输入的编号不能为空.");
             renderJSON(resultMap);
         }

        Merchant merchant = Merchant.findByCode(code);
        if(merchant == null ) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "您输入的编号不存在或已删除.");
            renderJSON(resultMap);
        }

        // 到此位置 证明 用户是存在的
        if(folderId == null) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "您传递的文件夹信息为空.不能帮您存储图片");
            renderJSON(resultMap);
        }
        FolderPropertie folderPropertie = FolderPropertie.findByUN_DeletedAndId(folderId);
        if(folderPropertie == null) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "您传递的文件夹信息不存在或已删除.不能帮您存储图片");
            renderJSON(resultMap);
        }

        if(folderPropertie.merchant != merchant) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "您不能使用别人的图片空间.请检查是否操作错误");
            renderJSON(resultMap);
        }

        //判断图片后缀
        String extension = getFileExtension(picture);
        if(!extension.equals(".JPG") || !extension.equals(".jpg") || !extension.equals(".png") || !extension.equals(".PNG")) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "您上传的文件格式不正确.只能上传 .JPG/.PNG文件");
            renderJSON(resultMap);
        }

        if(picture.length() > MAX_IMAGE_SIZE) {
            resultMap.put("success" , false);
            resultMap.put("msg" , "上传图片最大为5M . 请修改图片大小");
            renderJSON(resultMap);
        }

        // 获取图片信息. 知道图片大小 宽度 高度
        try {
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
            Integer imageWidth = sourceImg.getWidth();
            Integer imageHeight = sourceImg.getHeight();
            if(imageWidth > MAX_IMAGE_WIDTH || imageHeight > MAX_IMAGE_HEIGHT) {
                resultMap.put("success" , false);
                resultMap.put("msg" , "图片最大宽度为:" +MAX_IMAGE_WIDTH+ "  最大高度为 :" + MAX_IMAGE_WIDTH);
                renderJSON(resultMap);
            }
            String imagePath = folderPropertie.name;
            while (folderPropertie.parentFoulder != null) {
                imagePath = folderPropertie.name  + SPE + imagePath;
                folderPropertie = folderPropertie.parentFoulder;
            }
            Logger.info("imagePath");
            // application/public/upload/悠悠点/未修改/半修图/
            String folderPath = BASE_IMAGE_PATH + imagePath;
            // 格式 asdfase52l.jpg
            String uFid = RandomNumberUtil.generateRandomChars(10);
            String toFilePath = folderPath + uFid+ extension;
            File toFile = new File(toFilePath);
            if(width > imageWidth || height > imageHeight) {
                Images.resize(picture , toFile , imageWidth, imageHeight , true);
            } else {
                Images.resize(picture , toFile , width , height);
            }
            resultMap.put("success" , true);
            resultMap.put("ufId" , uFid);
            resultMap.put("path" , "http://img.ulmsale.net/"+toFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 获取文件扩展名
     * @param file
     * @return
     */
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

}
