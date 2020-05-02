package run.halo.app.event.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author : MrLawrenc
 * @date : 2020/5/2 19:41
 * @description : 缓存相关事件
 */
@Setter
@Getter
@Accessors(chain = true)
public class CacheEvent {

    /**
     * 事件描述
     */
    private String eventDesc;

    public CacheEvent(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}