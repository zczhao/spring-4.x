package zzc.spring.beans.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态工厂方法：直接调用某一个类的静态方法，就可以返回bean的实例
 */
public class StaticCarFactory {

	private static Map<String, Car> cars = new HashMap<String, Car>();

	static {
		cars.put("Audi", new Car("Audi", 300000));
		cars.put("Ford", new Car("Ford", 200000));
		cars.put("BaoMa", new Car("BaoMa", 500000));
	}

	// 静态工厂方法
	public static Car getCar(String name) {
		return cars.get(name);
	}

}
