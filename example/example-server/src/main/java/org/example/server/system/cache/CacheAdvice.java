package org.example.server.system.cache;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.server.system.base.BaseAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class CacheAdvice extends BaseAdvice{
	
	protected final Logger logger = LoggerFactory.getLogger(CacheAdvice.class);
	@Autowired
	private CacheProperties cacheProperties;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Pointcut("@annotation(org.example.server.system.cache.CacheCable)")
	public void redisCachePointcut() {}

	@Around(value="redisCachePointcut()") 
	public Object get(final ProceedingJoinPoint pJointPoint) throws Throwable {
		// 读取配置项，是否开启缓存
    	int isCache = cacheProperties.getIsCache();
    	if(isCache==0){
    		return pJointPoint.proceed(); 
    	}
		
    	Method method = getMethod(pJointPoint);
		CacheCable redisCache = method.getAnnotation(CacheCable.class);
		if(redisCache == null){
			return pJointPoint.proceed();
		}
		//校验参数
		boolean check = checkParams(redisCache);
		if(!check){
			return pJointPoint.proceed();
		}
		
		String spelValue = null;
		try {
			spelValue = parseSpel(redisCache.key(), method,
					pJointPoint.getArgs()); //获取组合key
		} catch (Exception e) {
			logger.error("获取缓存的key对应参数值异常:key="+redisCache.key(), e);
			return pJointPoint.proceed();
		}
		
		logger.debug("获取缓存的key对应参数Spel值:spelValue="+spelValue +" ,expiration="+redisCache.expiration());
		
		if(StringUtils.isBlank(spelValue)){
			logger.error("获取缓存的key对应参数值[:key="+redisCache.key()+"]为空");
			return pJointPoint.proceed();
		}
    	
		long expiration = redisCache.expiration(); //redis缓存过期时间
		/***********先redis缓存，再方法取************/
		Object value = null;
		//从redis缓存获取
		try {
			value = redisTemplate.opsForValue().get(spelValue);
		} catch (Exception e) {
			logger.error("redis缓存读取数据时异常 key="+spelValue, e);
			return pJointPoint.proceed();
		}
		
		//如果redis中有缓存，则把redis中的缓存根据需要（是否需要本地缓存）保存至本地缓存
		if(null!=value){ 
			logger.debug("命中redis缓存 key="+spelValue);
			return value;
		}else{
			//如果redis中没有缓存，则从方法中取出值保存至redis缓存和本地缓存（根据需要是否保存本地缓存）
			logger.debug("缓存注解CacheCable查询数据为空，准备执行目标方法，"+method.getName()+"获取缓存:spelValue="+spelValue +" ,expiration="+redisCache.expiration());
			value = pJointPoint.proceed();
			if(value != null){
				try{
					redisTemplate.opsForValue().set(spelValue, value, expiration, TimeUnit.SECONDS);
				}catch(Exception e){
					logger.error("缓存注解CacheCable处理[结果存入缓存]异常， key="+spelValue, e);
				}
			}
		}

		return value;
	}
	
	/**
	 * 检验注解参数
	 * @param redisCache
	 * @return  true:参数合法；false:参数不合法
	 */
	private boolean checkParams(CacheCable redisCache) {
		String key = redisCache.key();
		if(StringUtils.isBlank(key)){
			return false;
		}
		return true;
	}

	/**
	 * 获取缓存的key对应参数值 (SPEL表达式) 
	 * @param key
	 * @param method
	 * @param args
	 * @return
	 */
	private String parseSpel(String key, Method method, Object[] args) throws Exception{
		ExpressionParser parser = new SpelExpressionParser();
		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = parameterNameDiscoverer.getParameterNames(method);
		// 无入参情况直接返回原始key
		if(paraNameArr == null || paraNameArr.length == 0){
			return key;
		}
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < paraNameArr.length; i++) {
			// 判断目标方法参数
			if(args[i] == null){
				throw new Exception("缓存目标方法对应参数值:"+paraNameArr[i]+"=null");
			}
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}

}
