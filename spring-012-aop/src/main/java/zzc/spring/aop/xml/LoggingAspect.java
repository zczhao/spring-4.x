package zzc.spring.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
import java.util.List;


public class LoggingAspect {


	public void beforeMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		System.out.println("The method " + methodName + " begins with " + args);
	}


	public void afterMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends ");
	}


	public void afterReturning(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends with " + result);
	}



	public void afterThrowing(JoinPoint joinPoint, Exception ex) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + "  occurs exception: " + ex);
	}


	public Object aroundMethod(ProceedingJoinPoint joinPoint) {
		System.out.println("aroundMethod");
		Object result = null;
		String methodName = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		try {
			// 前置通知
			System.out.println("The method " + methodName + " begins with " + args);
			// 执行目标方法
			result = joinPoint.proceed();
			// 返回通知
			System.out.println("The method " + methodName + " ends with " + result);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			// 异常通知
			System.out.println("The method " + methodName + "  occurs exception: " + throwable);
		}
		// 后置通知
		System.out.println("The method " + methodName + " ends ");
		return result;
	}

}
