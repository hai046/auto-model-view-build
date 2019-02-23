package com.xlhy.saas.cloud.builder.view;

import com.xlhy.saas.cloud.builder.annotation.AutoModel;
import com.xlhy.saas.cloud.builder.annotation.View;
import com.xlhy.saas.cloud.builder.annotation.ViewType;
import com.xlhy.saas.cloud.builder.model.ImageDo;
import com.xlhy.saas.cloud.builder.model.SchoolDo;
import com.xlhy.saas.cloud.builder.model.UserDo;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@View(model = UserDo.class,
        fieldMapper = {@ViewType(id = "schoolId", referenceType = SchoolDo.class),
                @ViewType(id = "imageId", referenceType = ImageDo.class)})
public class UserVo {

    @AutoModel
    private SchoolDo schoolDo;

    @AutoModel
    private ImageDo imageDo;

    @AutoModel
    private UserDo userDo;

    public String getName() {
        return userDo.getName();
    }

    public String getId() {
        return "uuid=" + userDo.getId();
    }

    public SchoolDo getSchoolDo() {
        return schoolDo;
    }

    public ImageDo getImageDo() {
        return imageDo;
    }

}
