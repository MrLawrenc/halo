package run.halo.app.listener.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import run.halo.app.cache.mars.CacheUtil;
import run.halo.app.event.cache.CacheEvent;
import run.halo.app.utils.LogUtil;

/**
 * @author : MrLawrenc
 * @date : 2020/5/2 18:22
 * @description : 缓存监听
 */
@Component
public class CacheListener {

    @Autowired
    private CacheUtil cacheUtil;

    @EventListener
    @Async
    public void blogIndexCache(CacheEvent cacheEvent) {
        LogUtil.infoLog(cacheEvent.getEventDesc());
        cacheUtil.cleanBlogIndexCache();
    }
}