package run.halo.app.controller.admin.api.facade.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : MrLawrenc
 * @date : 2020/5/5 17:09
 * @description : 远程调用响应实体类
 */
@Data
@Accessors(chain = true)
public class Resp {
    private int code;
    private boolean success;

    private String desc;


    public static Resp success() {
        return new Resp().setSuccess(true).setCode(200).setDesc("成功");
    }
    public static Resp fail(String msg) {
        return new Resp().setSuccess(false).setCode(500).setDesc(msg);
    }
}