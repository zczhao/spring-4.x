package zzc.spring.aop;

import org.junit.Test;

public class ArithmeticCalculatorTests {

	@Test
	public void test01() {
		ArithmeticCalculator arithmeticCalculator = new ArithmeticCalculatorLoggingImpl();
		int result = arithmeticCalculator.add(1, 2);
		System.out.println("-->" + result);

		result = arithmeticCalculator.div(4, 2);
		System.out.println("-->" + result);
	}


	@Test
	public void testProxy() {
		ArithmeticCalculator target = new ArithmeticCalculatorImpl();
		ArithmeticCalculator proxy = new ArithmeticCalculatorLoggingProxy(target).getLoggingProxy();
		System.out.println(proxy.getClass().getName());
		int result = proxy.add(1, 2);
		System.out.println("-->" + result);

		result = proxy.div(4, 2);
		System.out.println("-->" + result);
	}

}
