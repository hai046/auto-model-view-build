package com.xlhy.sass.cloud.builder.view;

import com.xlhy.sass.cloud.builder.annotation.AutoModel;
import com.xlhy.sass.cloud.builder.annotation.View;
import com.xlhy.sass.cloud.builder.annotation.ViewType;
import com.xlhy.sass.cloud.builder.model.ImageDo;
import com.xlhy.sass.cloud.builder.model.SchoolDo;
import com.xlhy.sass.cloud.builder.model.UserDo;

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

        return "name";
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

    public UserDo getUserDo() {
        return userDo;
    }
}
