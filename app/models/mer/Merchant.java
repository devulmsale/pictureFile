package models.mer;

import models.constants.DeletedStatus;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.MinLength;
import org.h2.command.dml.Delete;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by upshan on 15/8/3.
 */
@Entity
@Table(name = "merchants")
public class Merchant extends Model{

    /**
     * 商户名称
     */
    @Required(message = "名称不能为空")
    @Unique (message = "该名称已经重复，请重新选择")
    @MinLength(value = 2 ,message = "名称长度不能少于2个字符")
    @MaxLength(value = 15,message = "名称长度不能多于15个字符")
    @Column(name = "name")
    public String name;

    /**
     * 商户编号
     */

    @Column(name = "code")
    public String code;


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


    public static Merchant findByCode(String code) {
        return Merchant.find("code = ? and deleted = ?" , code , DeletedStatus.UN_DELETED).first();
    }

}
