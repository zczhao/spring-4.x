package zzc.spring.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("bookShopService")
public class BookShopServiceImpl implements BookShopService {

	@Autowired
	private BookShopDao bookShopDao;

	/**
	 * 添加事务注解
	 *
	 * 1.使用propagation 指定事务的传播行为,即当前的事务方法被另外一个事务方法调用时
	 * 	如何使用事务,默认取值为REQUIRED,即使用调用方法的事务
	 * 	REQUIRES_NEW:使用自己的事务,调用的事务方法的事务被挂起
	 *
	 * 2.使用isolation 指定事务的隔离级别,最常用的取值为READ_COMMITTED
	 *
	 * 3.默认情况下spring的声明式事务对所有的运行时异常进行回滚,可以通过对应的属性进行设置
	 *  noRollbackFor={UserAccountException.class} 对哪些异常不捕获
	 *
	 * 4.使用readOnly 指定事务是否为只读,若真的是一个只读取数据库值的方法,应设置readOnly=true
	 *
	 * 5.使用timeout 指定强制回滚事务之前可以占用的时间(单位:秒)
	 *
	 * 超时和只读属性：
	 * 由于事务可以在行和表上获得锁，因此长事务会占用资源，并对整体性能产生影响
	 * 如果一个事务只读取数据但不做修改，数据库引擎可以对这个事务进行优化
	 * 超时事务属性：事务在强制回滚之前可以保持多久，这样可以防止长期运行的事务占用资源
	 * 只读事务属性：表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false, timeout = 1)
	public void purchase(String username, String isbn) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//获取书单价
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		//更新书的库存
		bookShopDao.updateBookStock(isbn);
		//更新用户余额
		bookShopDao.updateUserAccount(username, price);
	}

}
