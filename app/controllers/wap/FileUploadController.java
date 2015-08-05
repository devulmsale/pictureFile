package controllers.wap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import models.common.FolderJSON;
import models.mer.FolderPropertie;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.mvc.Controller;

import java.io.File;
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
        List<FolderPropertie> topFolderList = FolderPropertie.findByTopFolder();
        List<FolderJSON> topFolderJsonList = new ArrayList<>();
        FolderJSON topFolderJSON = null;
        for(FolderPropertie topFolder : topFolderList) {
            topFolderJSON = new FolderJSON();
            Long count = FolderPropertie.countByParentFolder(topFolder.id);
            topFolderJSON.title = topFolder.name;
            topFolderJSON.type = count > 0 ? "folder" : "item";
            if(count > 0) {
                List<FolderPropertie> childFolderList = FolderPropertie.findByParentFolder(topFolder.id);
                List<FolderJSON> folderJSONList = new ArrayList<>();
                FolderJSON folderJSON = null;
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
        Logger.info("topFolderJsonList : %s " , topFolderJsonList);
        renderJSON(topFolderJsonList);
    }
}
