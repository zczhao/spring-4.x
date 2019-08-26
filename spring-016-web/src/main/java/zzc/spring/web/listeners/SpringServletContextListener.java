package zzc.spring.web.listeners;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 1、获取Spring配置文件的名称
		ServletContext servletContext = sce.getServletContext();
		String configLocation = servletContext.getInitParameter("configLocation");

		// 1、创建IOC容器
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);

		// 2、把IOC容器放在ServletContext的一个属性中
		servletContext.setAttribute("ApplicationContext", ctx);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
