package run.halo.app.controller.admin.api.facade;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import run.halo.app.controller.admin.api.facade.entity.ImgFileDTO;
import run.halo.app.controller.admin.api.facade.entity.MockMultipartFile;
import run.halo.app.controller.admin.api.facade.entity.Resp;
import run.halo.app.service.AttachmentService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : MrLawrenc
 * @date : 2020/5/5 15:38
 * @description : 暴露给外部调用的上传图片接口
 * 不走api接口，越过鉴权过滤器
 */
@RestController
@RefreshScope
public class AttachmentController4Service {
    private static HttpClient client = HttpClient.newBuilder().build();

    private final AttachmentService attachmentService;
    @Value("${halo.upload.user:lmy}")
    private String user;
    @Value("${halo.upload.pwd:123lmy}")
    private String pwd;


    public AttachmentController4Service(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/facade/uploadImg")
    @ApiOperation("对外开放的文件上传接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "url", value = "需要上传的图片文件地址")})
    public String uploadAttachment(@RequestBody String data) throws Exception {
        ImgFileDTO fileDTO = new ImgFileDTO();
        fileDTO.setData(data);
        String imgUrl;
        try {
            imgUrl = fileDTO.service(user, pwd);
        } catch (Exception e) {
            return JSON.toJSONString(Resp.fail(e.getMessage()));
        }

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(imgUrl)).build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        //上传图片
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", format + ".jpg", "image/jpeg", response.body());
        attachmentService.upload(multipartFile);
        return JSON.toJSONString(Resp.success());
    }
}