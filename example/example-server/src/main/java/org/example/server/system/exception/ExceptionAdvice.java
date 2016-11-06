package org.example.server.system.exception;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.server.system.base.BaseAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAdvice extends BaseAdvice{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Pointcut("@annotation(org.example.server.system.exception.ExceptionCable)")
	public void exceptionPointcut() {}
	
	@AfterThrowing(value = "exceptionPointcut()", throwing = "e")
    public void afterThrowing(final JoinPoint joinPoint, Throwable e)  throws Throwable{
		Method method = getMethod(joinPoint);
		ExceptionCable exceptionCache = method.getAnnotation(ExceptionCable.class);
		if(exceptionCache == null){
			return;
		}
        if (e instanceof Exception) {
            logger.error("程序发现Exception异常！", e);
        } else {
            logger.error("程序发现未知异常", e);
        }
    } 
	@Around(value="exceptionPointcut()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint){
		Object result=null;
		try {
			//前置通知
			logger.debug("前置通知开始执行......】");
			//执行目标方法
			result = proceedingJoinPoint.proceed(); 
			//返回通知
			logger.debug("返回通知开始执行......】");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//异常通知
			logger.debug("异常通知开始执行......】");
		}
		//后置通知
		logger.debug("后置通知开始执行......】");
		return result;
	}
	
	
	
}
