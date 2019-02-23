## 序

以前一直使用使用天船老师的 [model-view-builder](https://github.com/PhantomThief/model-view-builder)来解决model到view树状渲染
也学到了很多

当我把这么好用的东西想推广给其他同事使用时候，大家反馈上手太难太慢，不愿意使用，故一直想通过注解直接映射对应关系来解决，想在本项目解决


本项目面是第一版本
规划目标：

- [x] 解决树状渲染痛点 
- [x] 减少上手门槛
- [x] 提高开发效率
- [x] 支持view自定义组合
- [ ] 高效渲染


例子参见`test`代码


核心代码如下

`View`逻辑如下
```java

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

```

渲染
```java

    @Autowired
    private ViewBuilder viewBuilder;

    @GetMapping("/test")
    @ViewResult
    public Object test() {
        //model生成逻辑
        viewBuilder.<Long, ImageDo>addModelData(id -> {
            ImageDo imageDo = new ImageDo();
            imageDo.setFormat("jpg");
            imageDo.setId(id);
            imageDo.setUrl("http://baidu.com");
            return imageDo;
        }, ImageDo.class);
        
        //渲染
        return viewBuilder.build(userDos, UserVo.class);
        
    }

```
### 最新使用方法参见
`com.hai046.builder.TestController`


