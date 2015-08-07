package models.mer;

import models.constants.DeletedStatus;
import models.movie_api.vo.FolderAttr;
import models.movie_api.vo.FolderJSON;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.MinLength;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by upshan on 15/8/4.
 */
@Entity
@Table(name = "folder_properties")
public class FolderPropertie extends Model {

    /**
     * 所属商户
     */
    @JoinColumn(name = "merchant_id")
    @ManyToOne
    public Merchant merchant;

    /**
     * 哪个用户添加的文件夹
     */
    @JoinColumn(name ="merchant_user_id")
    @ManyToOne
    public MerchantUser merchantUser;

    /**
     * 文件夹名称
     */
    @Required(message = "文件夹名称不能为空")
    @MinLength(value = 3,message = "文件夹名称不能长度不能少于3")
    @MaxLength(value = 10,message = "文件夹名称长度不能多于10")
    @Column(name = "name")
    public String name;


    /**
     * 所属文件夹
     */
    @JoinColumn(name = "parent_foulder_id")
    @ManyToOne
    public FolderPropertie parentFoulder;

    /**
     * 文件夹层
     */
    @Column(name = "level")
    public Integer level;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    public Date createdAt;

    /**
     * 逻辑删除,0:未删除，1:已删除
     */
    @Enumerated(EnumType.ORDINAL)
    public DeletedStatus deleted;

    public static FolderPropertie findByUN_DeletedAndId(Long id) {
        return FolderPropertie.find("deleted = ? and id = ?" , DeletedStatus.UN_DELETED , id).first();
    }

    public static List<FolderPropertie> findByTopFolder() {
        return FolderPropertie.find("parentFoulder = null and deleted = ?" , DeletedStatus.UN_DELETED).fetch();
    }

    public static Long countByParentFolder(Long id) {
        return FolderPropertie.count("parentFoulder.id = ? and deleted = ?", id, DeletedStatus.UN_DELETED);
    }

    public static List<FolderPropertie> findByParentFolder(Long id) {
        return FolderPropertie.find("parentFoulder.id = ? and deleted = ?" , id , DeletedStatus.UN_DELETED).fetch();
    }

    public static List<FolderPropertie> findByMerchant(Long id){
        return FolderPropertie.find("merchant.id = ? and deleted = ?" , id , DeletedStatus.UN_DELETED).fetch();
    }

    /**
     * 获取 json true
     * @return
     */
    public static List<FolderJSON> findAllFolderJSON() {
        List<FolderPropertie> topFolderList = FolderPropertie.findByTopFolder();
        List<FolderJSON> topFolderJsonList = new ArrayList<>();
        FolderJSON topFolderJSON = null;
        FolderAttr topAttr = null;
        for(FolderPropertie topFolder : topFolderList) {
            topFolderJSON = new FolderJSON();
            Long count = FolderPropertie.countByParentFolder(topFolder.id);
            topAttr = new FolderAttr();
            topAttr.id = topFolder.id.toString();
            topFolderJSON.attr = topAttr;
            topFolderJSON.title = topFolder.name;
            topFolderJSON.type = count > 0 ? "folder" : "item";
            if(count > 0) {
                List<FolderPropertie> childFolderList = FolderPropertie.findByParentFolder(topFolder.id);
                List<FolderJSON> folderJSONList = new ArrayList<>();
                FolderJSON folderJSON = null;
                FolderAttr childAttr = null;
                for(FolderPropertie childFolder : childFolderList) {
                    folderJSON = new FolderJSON();
                    childAttr = new FolderAttr();
                    childAttr.id = childFolder.id.toString();
                    folderJSON.attr = childAttr;
                    folderJSON.title = childFolder.name;
                    folderJSON.type = "item";
                    folderJSONList.add(folderJSON);
                }
                topFolderJSON.products = folderJSONList;
            }
            topFolderJsonList.add(topFolderJSON);
        }
        Logger.info("topFolderJsonList :" + topFolderJsonList);
        return topFolderJsonList;
    }

}
