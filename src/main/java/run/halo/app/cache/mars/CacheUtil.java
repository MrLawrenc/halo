package run.halo.app.cache.mars;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.vo.PostListVO;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author : MrLawrenc
 * @date : 2020/4/22 23:07
 * 缓存key相关枚举
 * <p>
 * 作缓存主要是由于数据库连接的是内网穿透后的内网数据库，比较慢
 */
@Component
@Slf4j
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

    /**
     * 清除blog主页缓存
     */
    public void cleanBlogIndexCache() {
        BLOG_INDEX_CACHE.invalidateAll();
    }

    /**
     * 博客查询结果
     */
    @SuppressWarnings("all")
    public Page<PostListVO> getPagePostListVO(PostStatus status, int startPage, int pageSize, Supplier<Page<PostListVO>> supplier) {
        String key = status.getValue() + ":" + startPage + ":" + pageSize;
        Page<PostListVO> result = (Page<PostListVO>) BLOG_INDEX_CACHE.getIfPresent(key);
        if (Objects.isNull(result)) {
            log.debug("未命中缓存................");
            result = supplier.get();
            BLOG_INDEX_CACHE.put(key, result);
        } else {
            log.debug("命中缓存.................");
        }
        return result;
    }

}
