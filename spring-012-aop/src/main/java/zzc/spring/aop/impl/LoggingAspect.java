package zzc.spring.aop.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 把这个类声明为一个切面：
 * 1、需要把该类放入到IOC容器中
 * 2、声明为一个切面
 */
@Order(2)
@Aspect
@Component
public class LoggingAspect {

	/**
	 * 定义一个方法，用于声明切入点表达。一般的该方法中不需要添入其他的代码
	 * 使用@Pointcut来声明切入点表达式
	 * 后面的其他通知直接使用方法名来引用当前的切入点表达式
	 */
	@Pointcut("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
	public void declareJoinPointExpression(){

	}

	/**
	 * 声明该方法是一个前置通知：在目标方法开始之前执行
	 */
	//@Before("execution(public int zzc.spring.aop.impl.ArithmeticCalculator.add(int,int))")
	@Before("declareJoinPointExpression()")
	public void beforeMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		System.out.println("The method " + methodName + " begins with " + args);
	}

	/**
	 * 后置通知：在目标方法执行后(无论是否发生异常)，执行的通知
	 * 后置通知是在连接点完成之后执行的，即连接点返回结果或者抛出异常的时候，下面的后置通知记录了方法的终止
	 * 一个切面可以包括一个或者多个通知
	 * 在后置通知中还不能访问目标方法执行的结果
	 */
	@After("declareJoinPointExpression()")
	public void afterMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends ");
	}

	/**
	 * 返回通知：在方法正常结束后执行的代码
	 * 返回通知是可以访问到方法的返回值
	 */
	@AfterReturning(value = "declareJoinPointExpression()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends with " + result);
	}


	/**
	 * 异常通知：在目标方法出现异常时会执行的代码
	 * 可以访问到异常对象，可以指定在出现特定异常(如：NullPointerException)时再执行通知
	 */
	@AfterThrowing(value = "declareJoinPointExpression()", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, Exception ex) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + "  occurs exception: " + ex);
	}


	/**
	 * 环绕通知需要携带ProceedingJoinPoint类型的参数
	 * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint 类型的参数可以决定是否执行目标方法，且环绕通知必须有返回值，返回值即为目标方法的返回值
	 */
	/*
	@Around("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
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
	}*/

}
