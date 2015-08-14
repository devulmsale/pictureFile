package models.mer;

import models.constants.DeletedStatus;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by upshan on 15/8/3.
 */
@Entity
@Table(name = "merchant_images")
public class MerchantImage extends Model{

    /**
     * 所属商户
     */
    @JoinColumn(name = "merchant_id")
    @ManyToOne
    public Merchant merchant;

    /**
     * 属于哪个文件夹
     */
    @JoinColumn(name = "folder_propertie_id")
    @ManyToOne
    public FolderPropertie folderPropertie;

    /**
     * 图片宽度
     */
    public Integer width;

    /**
     * 图片高度
     */
   public Integer height;

    /**
     * 图片路径
     */
    public String url;

    /**
     * 图片上传订单号
     */
    public String uFId;


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


    public MerchantImage() {
        super();
    }

    public MerchantImage(Merchant merchant , FolderPropertie folderPropertie , Integer width , Integer height , String url , String ufid) {
        this.merchant = merchant;
        this.folderPropertie = folderPropertie;
        this.width = width;
        this.height = height;
        this.uFId = ufid;
        this.url = url;
        this.createdAt = new Date();
        this.deleted = DeletedStatus.UN_DELETED;
        this.save();
    }

    public MerchantImage(Merchant merchant , FolderPropertie folderPropertie , Integer width , Integer height) {
        this.merchant = merchant;
        this.folderPropertie = folderPropertie;
        this.width = width;
        this.height = height;
        this.createdAt = new Date();
        this.deleted = DeletedStatus.UN_DELETED;
        this.save();
    }

    public static List<MerchantImage> findByMerChant(Long mid){
        return MerchantImage.find("merchant.id = ?" , mid ).fetch();
    }
    public static List<MerchantImage> findByFolderMerChant(Long mid,Long fid){
        return MerchantImage.find("merchant.id = ? and (folderPropertie.id = ? or folderPropertie.parentFoulder.id = ? )" , mid,fid ,fid).fetch();
    }



    public static List<MerchantImage> findByFolderPropertie(Long fid) {
        return MerchantImage.find("folderPropertie.id = ? or folderPropertie.parentFoulder.id = ?" , fid , fid).fetch();
    }

    public static List<MerchantImage> findByFolderPropertieAndMerchant(Long merchantId ,  Long fid) {
        return MerchantImage.find("merchant.id = ? and ( folderPropertie.id = ? or folderPropertie.parentFoulder.id = ? )" , merchantId , fid , fid).fetch();
    }
}
