package run.halo.app.cache.mars;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.vo.PostListVO;
import run.halo.app.utils.LogUtil;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author : MrLawrenc
 * @date : 2020/4/22 23:07
 * @description :   缓存key相关枚举
 */
@Component
public class CacheUtil {

    private static final Cache<String, Object> BLOG_INDEX_CACHE = CacheBuilder.newBuilder()
            //超过会使用LRU回收策略
            .maximumSize(10)
            //.expireAfterWrite(3, TimeUnit.MINUTES)
            .build();

    private final AbstractStringCacheStore cacheStore;

    public CacheUtil(AbstractStringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }


    /**
     * 博客总页数
     */
    public static final String PAGE_SIZE = "pageSize";


    /**
     * 获取博客总页数
     */
    public int getPageSize(Supplier<Integer> supplier) {
        Object result = BLOG_INDEX_CACHE.getIfPresent(PAGE_SIZE);
        if (Objects.isNull(result)) {
            result = supplier.get();
            BLOG_INDEX_CACHE.put(PAGE_SIZE, result);
        }
        return Integer.parseInt(result.toString());
    }

    public void cleanPageSizeCache() {
        BLOG_INDEX_CACHE.invalidate(PAGE_SIZE);
    }

    /**
     * 博客查询结果
     */
    @SuppressWarnings("all")
    public Page<PostListVO> getPagePostListVO(PostStatus status, int startPage, int pageSize, Supplier<Page<PostListVO>> supplier) {
        String key = status.getValue() + ":" + startPage + ":" + pageSize;
        Page<PostListVO> result = (Page<PostListVO>) BLOG_INDEX_CACHE.getIfPresent(key);
        if (Objects.isNull(result)) {
            LogUtil.debugLog("未命中缓存................");
            result = supplier.get();
            BLOG_INDEX_CACHE.put(key, result);
        } else {
            LogUtil.debugLog("命中缓存.................");
        }
        return result;
    }

}
