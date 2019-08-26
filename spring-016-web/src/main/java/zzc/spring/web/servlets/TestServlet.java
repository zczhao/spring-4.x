package zzc.spring.web.servlets;

import org.springframework.context.ApplicationContext;
import zzc.spring.web.beans.Person;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1、从application域对象中得到IOC容器的引用
		ServletContext servletContext = getServletContext();
		ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute("ApplicationContext");

		// 2、从IOC容器中得到需要的bean
		Person person = ctx.getBean(Person.class);
		person.hello();
	}
}
