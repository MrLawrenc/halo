package run.halo.app.controller.admin.api.facade;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/facade/admin/attachments")
public class AttachmentController4Service {
    private final AttachmentService attachmentService;
    private static HttpClient client = HttpClient.newBuilder().build();


    public AttachmentController4Service(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("upload")
    @ApiOperation("对外开放的文件上传接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "url", value = "需要上传的图片文件地址")})
    public Resp uploadAttachment(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", format + ".jpg", "image/jpeg", response.body());
        attachmentService.upload(multipartFile);
        return Resp.success();
    }
}