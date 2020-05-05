package run.halo.app.controller.admin.api.facade.entity;

import lombok.Data;
import run.halo.app.model.entity.Attachment;

/**
 * @author : MrLawrenc
 * @date : 2020/5/5 15:43
 * @description : 文件传输对象
 */
@Data
public class FileDTO extends Attachment {
    private byte[] data;
}