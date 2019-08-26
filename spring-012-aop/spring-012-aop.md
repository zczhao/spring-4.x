# AOP前奏

```java
package zzc.spring.aop;

public interface ArithmeticCalculator {

	int add(int i,int j);
	int sub(int i,int j);

	int mul(int i,int j);
	int div(int i,int j);
}
```

```java
package zzc.spring.aop;

/**
 * 需求1-日志：在程序执行期间追踪正在发生的活动
 * 需求2-验证：希望计算器只能处理正数的运算
 *
 *
 * 问题：
 * 代码混乱：越来越多的非业务需求(日志和验证等),加入后，原有的业务方法急剧膨胀，每个方法在处理核心逻辑的同时还必须兼顾其他多个关注点
 * 代码分散：以日志需求为例，只是为了满足这个单一需求，就不得不在多个模块（方法）里多次重复相同的日志代码，如果日志需求发生变化，必须修改所有模块
 *
 */
public class ArithmeticCalculatorLoggingImpl implements ArithmeticCalculator {

	@Override
	public int add(int i, int j) {
		System.out.println("The method add begins with [" + i + "," + j + "]");
		int result = i + j;
		System.out.println("The method add ends with " + result);
		return result;

	}

	@Override
	public int sub(int i, int j) {
		System.out.println("The method sub begins with [" + i + "," + j + "]");
		int result = i - j;
		System.out.println("The method sub ends with " + result);
		return result;
	}

	@Override
	public int mul(int i, int j) {
		System.out.println("The method mul begins with [" + i + "," + j + "]");
		int result = i * j;
		System.out.println("The method mul ends with " + result);
		return result;
	}

	@Override
	public int div(int i, int j) {
		System.out.println("The method div begins with [" + i + "," + j + "]");
		int result = i / j;
		System.out.println("The method div ends with " + result);
		return result;
	}
}
```

```java
@Test
public void test01() {
    ArithmeticCalculator arithmeticCalculator = new ArithmeticCalculatorLoggingImpl();
    int result = arithmeticCalculator.add(1, 2);
    System.out.println("-->" + result);

    result = arithmeticCalculator.div(4, 2);
    System.out.println("-->" + result);
}
```

使用动态代理解决上述问题

​	代理设置模式的原理：**使用一个代理将对象包装起来**，然后用该代理对象取代原始对象，任何对原始对象的调用都要通过代理，代理对象决定是否以及何时将方法调用到原始对象上

```java
package zzc.spring.aop;


public class ArithmeticCalculatorImpl implements ArithmeticCalculator {

	@Override
	public int add(int i, int j) {
		int result = i + j;
		return result;

	}

	@Override
	public int sub(int i, int j) {
		int result = i - j;
		return result;
	}

	@Override
	public int mul(int i, int j) {
		int result = i * j;
		return result;
	}

	@Override
	public int div(int i, int j) {
		int result = i / j;
		return result;
	}
}
```

```java
package zzc.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ArithmeticCalculatorLoggingProxy {

	// 要代理的对象
	private ArithmeticCalculator target;

	public ArithmeticCalculatorLoggingProxy(ArithmeticCalculator target) {
		this.target = target;
	}

	public ArithmeticCalculator getLoggingProxy() {
		ArithmeticCalculator proxy = null;
		// 代理对象由哪一个类加载器负责加载
		ClassLoader loader = target.getClass().getClassLoader();
		// 代理对象的类型，即其中有哪些方法
		Class[] interfaces = new Class[]{ArithmeticCalculator.class};
		// 当调用代理对象其中的方法时，该执行的代码
		InvocationHandler handler = new InvocationHandler() {
			/**
			 *
			 * @param proxy
			 *      正在返回的那个代理对象，一般情况下，在invoke方法中都不使用该对象
			 * @param method
			 *      正在被调用的方法
			 * @param args
			 *      调用方法时，传入的参数
			 * @return
			 * @throws Throwable
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				String methodName = method.getName();
				System.out.println("The method " + methodName + " begins with " + Arrays.asList(args));
				// 执行方法
				Object result = null;

				try {
					// 前置通知
					result = method.invoke(target, args);
					// 返回通知，可以访问到方法的返回值
				} catch (Exception e) {
					e.printStackTrace();
					// 异常通知，可以访问到方法出现的异常
				}
				// 后置通知，因为方法可能会出异常，所以访问不到方法的返回值
				System.out.println("The method " + methodName + " ends with " + result);
				return result;
			}
		};
		proxy = (ArithmeticCalculator) Proxy.newProxyInstance(loader, interfaces, handler);
		return proxy;
	}
}

```

```java
@Test
public void testProxy() {
    ArithmeticCalculator target = new ArithmeticCalculatorImpl();
    ArithmeticCalculator proxy = new ArithmeticCalculatorLoggingProxy(target).getLoggingProxy();
    int result = proxy.add(1, 2);
    System.out.println("-->" + result);

    result = proxy.div(4, 2);
    System.out.println("-->" + result);
}
```

# AOP简介

AOP(Aspect-Oriented Programming，**面向切面编程**)，是一种新的方法论，是对传统OOP(Object-Oriented Programming，面向对象编程)的补充

AOP的主要编程对象是**切面**(aspect)、而**切面模块化横切关注点**

在应用AOP编程时，仍然需要**定义公共功能**，但可以明确的定义这个功能在哪里，以什么方式应用，并且**不必修改受影响的类**，这样一来**横切关注点y就被模块化到特殊的对象(切面)**里

AOP的好处：

​	每个事物逻辑位于一个位置，代码不分散，便于维护和升级

​	业务模块更简洁，只包含核心业务代码

# AOP术语

切面(Aspect)：**横切关注点(跨越应用程序多个模块的功能)被模块化的特殊对象**

通知(Advice)：**切面必须要完成的工作**

目标(Target)：**被通知的对象**

代理(Proxy)：**向目标对象应用通知之后创建的对象**

连接点(Joinpoint)：**程序执行的某个特定位置**：如类某个方法调用前、调用后、方法抛出异常后等。**连接点由两个信息确定：方法表示的程序执行点、相对点表示的方法**。例如ArithmeticCalculator#add()方法执行前的连接点，执行点为ArithmeticCalculator#add()，方位为该方法执行前的位置

切点(pointcut)：每个类都拥有多个连接点。命名ArithmeticCalculator的所有方法实际上都是连接点，即连接点是程序类中客观存在的事务，**AOP通过切点定位到特定的连接点。类比：连接点相当于数据库中的记录，切点相当于查询条件**。切点和连接点不是一对一的关系，一个切点匹配多个连接点，切点通过org.springframework.aop.Pointcut接口进行描述，它使用类和方法作为连接点的查询条件

# Spring AOP

AspectJ：Java社区里最完整最流行的AOP框架

在Spring2.0以上版本中，可以使用基于AspectJ注解或基于XML配置的AOP

## 在Spring中启用AspectJ注解支持

​	1、要在Spring应用使用AspectJ注解，必须在classpath下包含AspectJ类库

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
</dependency>
```

​	2、**将aop Schema添加到<beans>根元素中**

​	3、要在Spring IOC容器中启用AspectJ注解支持，只要**在Bean配置文件中定义一个空的XML元素<aop:aspectj-autoproxy>**

​	4、当Spring IOC容器侦测到Bean配置文件中的<aop:aspectj-autoproxy>元素时，会自动为与AspectJ切面匹配的Bean创建代理

### 用AspectJ注解声明切面

​	要在Spring中声明AspectJ切面，只需要在IOC容器中将切面声明为Bean实例，当在Spring IOC容器中初始化AspectJ切面之后，Spring IOC容器就会为那些与AspectJ切面相匹配的Bean创建代理

​	在AspectJ注解中，切面只是一个带有@Aspect注解的Java类

​	通知是标注有某种注解的简单的Java方法

​	AspectJ支持5种类型的通知注解：

​		@Before：前置通知，在方法执行之前执行

​		@After：后置通知，在方法执行之后执行

​		@AfterRunning：返回通知，在方法返回结果之后执行

​		@AfterThrowing：异常通知，在方法抛出异常之后

​		@Around：环绕通知，围绕着方法执行

**利用方法签名编写AspectJ切入点表达式**：

​	最典型的切入点表达式是根据方法的签名来匹配各种方法：

​		execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(..))：匹配ArithmeticCalculator中声明的呢的方法。**第一个*代表任意修饰符及任意返回值，第二个*代表任意方法，..匹配任意数量的参数**。若目标类与接口与该切面在同一个包中，可以省略包名

​		execution(==public== * zzc.spring.aop.impl.ArithmeticCalculator.*(..))：匹配ArithmeticCalculator接口的==所有公有方法==

​		execution(public ==double== zzc.spring.aop.impl.ArithmeticCalculator.*(..))：匹配ArithmeticCalculator中==返回double类型数值的方法==

​		execution(public double zzc.spring.aop.impl.ArithmeticCalculator.*(==double==,..))：匹配第一个参数为double类型的方法，..匹配任意数量任意类型的参数

​		execution(==public== double zzc.spring.aop.impl.ArithmeticCalculator.*(==double==,==double==))：匹配参数类型为double,double类型的方法

#### 前置通知&后置通知&返回通知&异常通知

```java
package zzc.spring.aop.impl;

public interface ArithmeticCalculator {

	int add(int i, int j);
	int sub(int i, int j);

	int mul(int i, int j);
	int div(int i, int j);
}
```

```java
package zzc.spring.aop.impl;

import org.springframework.stereotype.Component;

@Component
public class ArithmeticCalculatorImpl implements ArithmeticCalculator {

	@Override
	public int add(int i, int j) {
		int result = i + j;
		return result;

	}

	@Override
	public int sub(int i, int j) {
		int result = i - j;
		return result;
	}

	@Override
	public int mul(int i, int j) {
		int result = i * j;
		return result;
	}

	@Override
	public int div(int i, int j) {
		int result = i / j;
		return result;
	}
}
```

```java
package zzc.spring.aop.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 把这个类声明为一个切面：
 * 1、需要把该类放入到IOC容器中
 * 2、声明为一个切面
 */
@Aspect
@Component
public class LoggingAspect {

	/**
	 * 声明该方法是一个前置通知：在目标方法开始之前执行
	 */
	//@Before("execution(public int zzc.spring.aop.impl.ArithmeticCalculator.add(int,int))")
	@Before("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
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
	@After("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
	public void afterMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends ");
	}

	/**
	 * 返回通知：在方法正常结束后执行的代码
	 * 返回通知是可以访问到方法的返回值
	 */
	@AfterReturning(value = "execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().getName();
		System.out.println("The method " + methodName + " ends with " + result);
	}
}
```

#### 环绕通知

```java
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
@Aspect
@Component
public class LoggingAspect {
	/**
	 * 环绕通知需要携带ProceedingJoinPoint类型的参数
	 * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint 类型的参数可以决定是否执行目标方法，且环绕通知必须有返回值，返回值即为目标方法的返回值
	 */
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
	}

}
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">

        <!-- 配置自动扫描的包 -->
        <context:component-scan base-package="zzc.spring.aop.impl"/>

        <!-- 使用AspjectJ注解起作用：自动为匹配的类生成代理对象 -->
        <aop:aspectj-autoproxy/>
</beans>
```

```java
@Test
public void test01(){
    // 1、创建Spring的IOC容器
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    // 2、从IOC容器中获取bean的实例
    ArithmeticCalculator arithmeticCalculator = ctx.getBean(ArithmeticCalculator.class);
    // 3、使用bean
    int result = arithmeticCalculator.add(1, 2);
    System.out.println("-->" + result);

    result = arithmeticCalculator.div(4, 2);
    System.out.println("-->" + result);
}
```

### **切面的优先级**

```java
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

	@Before("execution(* zzc.spring.aop.impl.ArithmeticCalculator.*(int,int))")
	public void validateArgs(JoinPoint joinPoint) {
		System.out.println("validateArgs" + Arrays.asList(joinPoint.getArgs()));
	}

}
```

### 重用切点表达式

```java
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
}
```

```java
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
    // 不同包需要加上包名+类名
	@Before("LoggingAspect.declareJoinPointExpression()")
	public void validateArgs(JoinPoint joinPoint) {
		System.out.println("validateArgs" + Arrays.asList(joinPoint.getArgs()));
	}
}
```

## 基于配置文件的方式配置AOP

```java
package zzc.spring.aop.xml;

public interface ArithmeticCalculator {

	int add(int i, int j);
	int sub(int i, int j);

	int mul(int i, int j);
	int div(int i, int j);
}
```

```java
package zzc.spring.aop.xml;

public class ArithmeticCalculatorImpl implements ArithmeticCalculator {

	@Override
	public int add(int i, int j) {
		int result = i + j;
		return result;

	}

	@Override
	public int sub(int i, int j) {
		int result = i - j;
		return result;
	}

	@Override
	public int mul(int i, int j) {
		int result = i * j;
		return result;
	}

	@Override
	public int div(int i, int j) {
		int result = i / j;
		return result;
	}
}
```

```java
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
```

```java
package zzc.spring.aop.xml;

import org.aspectj.lang.JoinPoint;

import java.util.Arrays;


public class ValidationAspect {

	public void validateArgs(JoinPoint joinPoint) {
		System.out.println("validateArgs" + Arrays.asList(joinPoint.getArgs()));
	}

}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">

    <!-- 配置bean -->
    <bean id="arithmeticCalculator" class="zzc.spring.aop.xml.ArithmeticCalculatorImpl"/>

    <!-- 配置切面的bean -->
    <bean id="loggingAspect" class="zzc.spring.aop.xml.LoggingAspect"/>

    <bean id="validationAspect" class="zzc.spring.aop.xml.ValidationAspect"/>

    <!-- 配置AOP -->
   <aop:config>
       <!-- 配置切点表达式 -->
       <aop:pointcut id="pointcut" expression="execution(* zzc.spring.aop.xml.ArithmeticCalculator.*(int,int))"/>
       <!-- 配置切面及通知 -->
       <aop:aspect ref="loggingAspect" order="2">
           <!--
           <aop:before method="beforeMethod" pointcut-ref="pointcut"/>
           <aop:after method="afterMethod" pointcut-ref="pointcut"/>
           <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut" throwing="ex"/>
           <aop:after-returning method="afterReturning" pointcut-ref="pointcut" returning="result"/>
           -->
           <aop:around method="aroundMethod" pointcut-ref="pointcut"/>
       </aop:aspect>
       <aop:aspect ref="validationAspect" order="1">
           <aop:before method="validateArgs" pointcut-ref="pointcut"/>
       </aop:aspect>
   </aop:config>

</beans>
```

```java
@Test
public void test01(){
    // 1、创建Spring的IOC容器
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-xml.xml");
    // 2、从IOC容器中获取bean的实例
    ArithmeticCalculator arithmeticCalculator = ctx.getBean(ArithmeticCalculator.class);
    // 3、使用bean
    int result = arithmeticCalculator.add(1, 2);
    System.out.println("-->" + result);

    result = arithmeticCalculator.div(4, 2);
    System.out.println("-->" + result);
}
```

