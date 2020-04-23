package run.halo.app.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : LiuMingyao
 * @date : 2019/8/27 10:21
 * @description : 切面
 */
@Aspect
@Component
public class LogAspect {

    private static ConcurrentHashMap<Long, StopWatch> stopWatchMap = new ConcurrentHashMap<>();

    @Pointcut("execution(public * run.halo.app.controller..*Controller.*(..))")
    public void writeLog() {
    }

    /**
     * 日志切面
     */
    @Before("writeLog()")
    public void before(JoinPoint joinPoint) {
        StopWatch stopWatch = new StopWatch();
        stopWatchMap.put(Thread.currentThread().getId(), stopWatch);
        stopWatch.start("切面耗时");

        StringBuilder sb = new StringBuilder("进入");
        Class<?> clz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        appendStr(sb, "[",clz.getSimpleName(), "#", methodName, "方法]\t");
        String[] paramNames = getParamNames(clz, methodName);
        Object[] paramValues = joinPoint.getArgs();
        if (paramValues == null || paramValues.length == 0) {
            sb.append("--该方法无参数");
        } else {
            for (int i = 0; i < paramNames.length; i++) {
                Object obj = paramValues[i];
                if ( obj instanceof ServletRequest || obj instanceof ServletResponse || obj instanceof HttpSession) {
                    continue;
                }
                String str = JSON.toJSONString(paramValues[i]);
                appendStr(sb, "方法参数", paramNames[i], "的值是:", str, "\t");
            }
        }
        LogUtil.infoLog(sb.toString());
    }


    @AfterReturning(returning = "ret", pointcut = "writeLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        StopWatch stopWatch = stopWatchMap.get(Thread.currentThread().getId());
        if (stopWatch.isRunning()) {
            stopWatch.stop();
            LogUtil.infoLog("耗时:{}s", stopWatch.getTotalTimeSeconds());
//            LogUtil.debugLog(stopWatch.prettyPrint());
        }
    }

    /**
     * 获取clz的methodName方法的所有参数名
     */
    String[] getParamNames(Class<?> clz, String methodName) {
        Parameter[] parameters;
        String[] paramNames = new String[10];
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                parameters = method.getParameters();
                //注意无参数情况下长度为0
                if (parameters.length == 0) {
                    return null;
                }
                //j8新增获取方法参数名称的方法
                LocalVariableTableParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
                paramNames = pnd.getParameterNames(method);
            }
        }
        return paramNames;
    }


    /**
     * 在sb之后追加拼接objects里面所有的字符串
     */
    public static void appendStr(StringBuilder sb, String... strs) {
        Arrays.asList(strs).forEach(sb::append);
    }

    /**
     * 判断传入的字符串是否为empty
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isBlank(str);
    }
}