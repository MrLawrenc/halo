package run.halo.app.controller.admin.api.facade;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run.halo.app.controller.admin.api.facade.util.RSAUtil;

/**
 * @author : hz20035009-逍遥
 * @date : 2020/5/6 15:05
 * @description : 获取公钥
 */
@RestController
public class RsaPublicKeyController {

    /**
     * @return 返回公钥
     * <p>
     * 连接方使用公钥加密方式如下(客户端只能加密):
     * <code>
     * RSA newRsa = new RSA(null, 公钥);
     * String encode2 = Base64.getEncoder().encodeToString(newRsa.encrypt("我是ss", KeyType.PublicKey));
     * </code>
     */
    @GetMapping("/facade/publicKey")
    @ApiOperation("获取公钥")
    public byte[] publicKey() {
        return RSAUtil.getPublicKey().getEncoded();
    }
}