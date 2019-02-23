package com.xlhy.saas.cloud.builder;

import com.xlhy.saas.cloud.builder.model.ImageDo;
import com.xlhy.saas.cloud.builder.model.UserDo;
import com.xlhy.saas.cloud.builder.view.UserTwoVo;
import com.xlhy.saas.cloud.builder.view.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ViewBuilder viewBuilder;

    @GetMapping("/t1")
    public Object test(@RequestParam(value = "count", defaultValue = "10") int count) {
        List<UserDo> userDos = getUserDos(count);
        long s = System.currentTimeMillis();
        final List<UserVo> build = viewBuilder.build(userDos, UserVo.class);
        return Collections.singletonMap((System.currentTimeMillis() - s), build);
    }

    @GetMapping("/t2")
    public Object t2(@RequestParam(value = "count", defaultValue = "10") int count) {
        List<UserDo> userDos = getUserDos(count);
        long s = System.currentTimeMillis();
        final Object build = viewBuilder.build(userDos, UserTwoVo.class);
        return Collections.singletonMap((System.currentTimeMillis() - s), build);
    }

    private List<UserDo> getUserDos(int count) {
        viewBuilder.<Long, ImageDo>addId2ModelMapper(ids -> ids.stream()
                .collect(Collectors.toMap(k -> k, v -> {
                    ImageDo imageDo = new ImageDo();
                    imageDo.setFormat("jpg");
                    imageDo.setId(v);
                    imageDo.setUrl("http://baidu.com");
                    return imageDo;
                })), ImageDo.class);

        final List<UserDo> userDos = IntStream.rangeClosed(1, count).boxed().map(id -> {
            final UserDo userDo = new UserDo();
            userDo.setId(id);
            userDo.setName("userName:hai046");
            userDo.setImageId(111L);
            return userDo;
        }).collect(Collectors.toList());

        return userDos;
    }


}
