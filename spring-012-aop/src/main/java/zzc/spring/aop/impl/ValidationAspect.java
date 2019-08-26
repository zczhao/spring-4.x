package zzc.spring.aop.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 可以使用@Order注解指定切面的优先级，值越小优先级越高
 */
@Order(1)
@Aspect
@Component
public class ValidationAspect {

	// @Before("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
	@Before("LoggingAspect.declareJoinPointExpression()")
	public void validateArgs(JoinPoint joinPoint) {
		System.out.println("validateArgs" + Arrays.asList(joinPoint.getArgs()));
	}

}
