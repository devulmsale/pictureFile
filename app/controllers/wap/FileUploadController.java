package controllers.wap;

import models.common.FolderJSON;
import models.mer.FolderPropertie;
import play.Logger;
import play.Play;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by upshan on 15/8/4.
 */
public class FileUploadController extends Controller {


    public static final String UPLOAD_IMAGE_URL = Play.configuration.getProperty("image-uploader.url",
            "http://localhost:9012/upload/helper");
    public static void upload() {
        render();
    }

    public static void folderJSON() {
        //查询 一级类别
        List<FolderPropertie> topFolderList = FolderPropertie.findByTopFolder();
        // 定义一个顶级 FolderJSON  List
        List<FolderJSON> topFolderJsonList = new ArrayList<>();
        // 定义一个 topFolderJSON 循环使用
        FolderJSON topFolderJSON = null;
        for(FolderPropertie topFolder : topFolderList) {
            topFolderJSON = new FolderJSON();
            Long count = FolderPropertie.countByParentFolder(topFolder.id);
            topFolderJSON.title = topFolder.name;
            topFolderJSON.type = count > 0 ? "folder" : "item";
            // 如果有子菜单
            if(count > 0) {
                // 查询子菜单
                List<FolderPropertie> childFolderList = FolderPropertie.findByParentFolder(topFolder.id);
                // 定义 子菜单的 FolderJSON List
                List<FolderJSON> folderJSONList = new ArrayList<>();
                FolderJSON folderJSON = null;
                // 循环子菜单
                for(FolderPropertie childFolder : childFolderList) {
                    folderJSON = new FolderJSON();
                    folderJSON.title = childFolder.name;
                    folderJSON.type = "item";
                    folderJSONList.add(folderJSON);
                }
                topFolderJSON.products = folderJSONList;
            }
            topFolderJsonList.add(topFolderJSON);

        }
        renderJSON(topFolderJsonList);
    }
}
