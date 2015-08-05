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
        Map<String , Object> resultMap = new HashMap<>();
        List<FolderPropertie> topFolderList = FolderPropertie.findByTopFolder();
        System.out.println("topFolderlis==="+topFolderList.size());
        for(FolderPropertie topFolder : topFolderList) {
            Long count = FolderPropertie.countByParentFolder(topFolder.id);
            resultMap.put("title" , topFolder.name);
            resultMap.put("type" , count > 0 ? "folder" : "item");
            if(count > 0) {
                List<FolderPropertie> childFolderList = FolderPropertie.findByParentFolder(topFolder.id);
                System.out.println("childFolderList==="+childFolderList.size());
              //  Map<String , Object> childMap = new HashMap<>();
                List<FolderJSON> folderJSONList = new ArrayList<>();
                FolderJSON folderJSON = null;
                for(FolderPropertie childFolder : childFolderList) {
                    folderJSON = new FolderJSON();
                    folderJSON.title = childFolder.name;
                    folderJSON.type = "item";
                    folderJSONList.add(folderJSON);
                }
                resultMap.put("products" , folderJSONList);
            }

        }
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
       // Logger.info("ddd=="+resultMap.size());
       renderJSON(resultMap);
    }
}
