package com.violet.api.aspect;

import com.violet.api.entity.SysLog;
import com.violet.api.mapper.SysLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect    // 告诉 Spring：我是一个“监控摄像头”（切面）
@Component // 告诉 Spring：请把我放进 IOC 容器里管理
public class LogAspect{
    @Autowired
    private SysLogMapper sysLogMapper;

    // 1. 定义监控范围 (Pointcut)：监控 com.violet.api 包下所有以 Controller 结尾的类里面的所有方法
    @Pointcut("execution(* com.violet.api..*Controller.*(..))")
    public void logPointCut() {
        // 这个方法不需要写代码，它只是一个“标记”
    }

    // 2. 环绕通知 (Around)：在目标方法执行的【前面】和【后面】都做点事情（最适合用来计算耗时）
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis(); // 记录开始时间

        // 拿到当前的 HTTP 请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //放行目标方法
        Object result = point.proceed();
        long timeCost = System.currentTimeMillis() - beginTime;
        try {
            // 🌟 核心新增逻辑：把刚才在控制台打印的数据，装进对象里存到数据库
            SysLog sysLog = new SysLog();
            sysLog.setUrl(request.getRequestURI().toString());
            sysLog.setMethod(request.getMethod());
            sysLog.setClassMethod(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
            sysLog.setTimeCost(timeCost);
            sysLog.setCreateTime(new Date());
            //存入数据库
            sysLogMapper.insert(sysLog);
        } catch (Exception e) {
            // 日志保存失败不能影响正常的业务运行，所以这里用 try-catch 包起来
            System.err.println("日志保存数据库失败:"+e.getMessage());
        }


        // 打印请求的基本信息
        System.out.println("========== API 调用监控开始 ==========");
        System.out.println("请求地址: " + request.getRequestURL().toString());
        System.out.println("请求方式: " + request.getMethod());
        System.out.println("调用方法: " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());

        // 🌟 最关键的一步：让目标方法（Controller）真正去执行！
//        Object result = point.proceed();

        // 目标方法执行完后，记录结束时间并计算耗时
        long time = System.currentTimeMillis() - beginTime;
        System.out.println("执行耗时: " + time + " 毫秒");
        System.out.println("========== API 调用监控结束 ==========\n");

        // 把 Controller 返回的结果原封不动地交还给前端
        return result;
    }
}