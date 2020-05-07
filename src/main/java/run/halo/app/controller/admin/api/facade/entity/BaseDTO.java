package run.halo.app.controller.admin.api.facade.entity;

import lombok.Getter;
import lombok.Setter;
import run.halo.app.controller.admin.api.facade.util.RSAUtil;

import javax.validation.constraints.NotNull;

/**
 * @author : hz20035009-逍遥
 * @date : 2020/5/6 17:42
 * @description : 传输对象基类
 */
@Getter@Setter
public abstract class BaseDTO<T> {
    /**
     * 分隔符，user#pwd#data ，data为加密之后的数据
     */
    protected static final String SPLIT = "#";
    /**
     * 加密之后的字符串
     */
    protected String data;


    /**
     * 在解密之后需要执行的业务逻辑
     *
     * @param user 用户名
     * @param pwd  密码
     * @return result
     * @throws Exception e
     */
    public abstract T service(@NotNull String user, @NotNull String pwd) throws Exception;

    /**
     * 通用的解密方法
     *
     * @param user user
     * @param pwd  password
     * @return 解密之后的数据字符串
     * @throws Exception 解析失败
     */
    public final String validate(@NotNull String user, @NotNull String pwd) throws Exception {
        try {
            String sourceStr = RSAUtil.decode(data);
            String[] result = sourceStr.split(SPLIT);
            if (user.equals(result[0]) && pwd.equals(result[1])) {
                return result[2];
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}