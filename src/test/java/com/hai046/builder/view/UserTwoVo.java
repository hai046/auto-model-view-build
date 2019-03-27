package com.hai046.builder.view;

import com.hai046.builder.annotation.AutoModel;
import com.hai046.builder.annotation.View;
import com.hai046.builder.annotation.ViewType;
import com.hai046.builder.model.ImageDo;
import com.hai046.builder.model.UserDo;

/**
 * @author denghaizhu
 * * date 2019-02-22
 */
@View(model = UserDo.class,
        fieldMapper = {@ViewType(id = "imageId", referenceType = ImageDo.class)})
public class UserTwoVo {

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

    public ImageDo getImageDo() {
        return imageDo;
    }

}
