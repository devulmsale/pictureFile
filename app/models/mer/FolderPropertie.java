package models.mer;

import models.constants.DeletedStatus;
import org.h2.command.dml.Delete;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

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
     * 文件夹名称
     */
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

}
