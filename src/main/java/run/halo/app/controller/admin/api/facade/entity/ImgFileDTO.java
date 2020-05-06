package run.halo.app.controller.admin.api.facade.entity;

import javax.validation.constraints.NotNull;

/**
 * @author : MrLawrenc
 * @date : 2020/5/5 15:43
 * @description : 图片传输对象
 */

public class ImgFileDTO extends BaseDTO<String> {

    /**
     * 获取图片url
     */
    @Override
    public String service(@NotNull String user, @NotNull String pwd) throws Exception {
        return validate(user, pwd);
    }
}