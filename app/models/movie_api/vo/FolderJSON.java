package models.movie_api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by upshan on 15/8/4.
 */
public class FolderJSON implements Serializable {

    public String title ;

    public String type;

    public FolderAttr attr;

    public List<FolderJSON> products;
}
