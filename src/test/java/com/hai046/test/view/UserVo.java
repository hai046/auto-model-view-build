package com.hai046.test.view;

import com.hai046.builder.annotation.AutoModel;
import com.hai046.builder.annotation.View;
import com.hai046.builder.annotation.ViewType;
import com.hai046.test.model.ImageDo;
import com.hai046.test.model.SchoolDo;
import com.hai046.test.model.UserDo;

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
