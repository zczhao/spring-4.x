package zzc.spring.beans.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 自定义FactoryBean需要实现FactoryBean接口
 */
public class CarFactoryBean implements FactoryBean<Car> {
	private String brand;

	public void setBrand(String brand) {
		this.brand = brand;
	}

	// 返回bean对象
	@Override
	public Car getObject() throws Exception {
		return new Car(brand, 300000);
	}

	// 返回bean的类型
	@Override
	public Class<?> getObjectType() {
		return Car.class;
	}


	@Override
	public boolean isSingleton() {
		return true;
	}
}
