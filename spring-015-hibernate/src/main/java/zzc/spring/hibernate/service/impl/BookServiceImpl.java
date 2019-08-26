package zzc.spring.hibernate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzc.spring.hibernate.dao.BookDao;
import zzc.spring.hibernate.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDao;

	/**
	 * Spring Hibernate事务流程
	 *  1、在方法开始之前
	 *      1、获取Session
	 *      2、把Session和当前线程绑定，这样就可以在DAO中使用SessionFactory的getCurrentSession()方法来获取Session了
	 *      3、开启事务
	 *
	 *  2、若方法正常结束，即没有出现异常，则
	 *      1、提交事务
	 *      2、使和当前线程绑定的session解除绑定
	 *      3、关闭Session
	 *
	 *  3、若方法出现异常，则：
	 *      1、回滚事务
	 *      2、使和当前线程绑定的session解除绑定
	 *      3、关闭Session
	 */
	@Override
	public void purchase(String username, String isbn) {
		int price = bookDao.findBookPriceByIsbn(isbn);
		bookDao.updateBookStock(isbn);
		bookDao.updateUserAccount(username, price);
	}

}
