package run.halo.app.cache.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.vo.PostListVO;

import java.util.function.Supplier;

/**
 * @author : MrLawrenc
 * @date : 2020/4/22 23:07
 * @description :   缓存key相关枚举
 */
@Component
public class CacheUtil {

    private final AbstractStringCacheStore cacheStore;

    public CacheUtil(AbstractStringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    /**
     * 博客查询结果
     */
    public static final String PAGE_BLOG = "blogState:startPage:pageSize";
    /**
     * 博客总页数
     */
    public static final String PAGE_SIZE = "pageSize";


    public int getPageSize(Supplier<String> supplier) {
        return Integer.parseInt(cacheStore.get(PAGE_SIZE).orElseGet(() -> {
            System.out.println("未命中缓存.........getPageSize.....");
            String result = supplier.get();
            cacheStore.put(PAGE_SIZE, result);
            return result;
        }));
    }

    public Page<PostListVO> getPagePostListVO(PostStatus status, int startPage, int pageSize, Supplier<Page<PostListVO>> supplier) {
        String key = status.getValue() + ":" + startPage + ":" + pageSize;
        String s = cacheStore.get(key).orElseGet(() -> {
            System.out.println("未命中缓存........getPagePostListVO......");
            String result = JSON.toJSONString(supplier.get());
            cacheStore.put(key, result);
            return result;
        });
        Page<PostListVO> postListVOS = JSON.parseObject(s, new TypeReference<Page<PostListVO>>() {
        });
        return postListVOS;
    }

}
