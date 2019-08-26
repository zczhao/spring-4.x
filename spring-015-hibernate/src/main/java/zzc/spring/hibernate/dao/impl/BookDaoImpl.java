package zzc.spring.hibernate.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import zzc.spring.hibernate.dao.BookDao;
import zzc.spring.hibernate.exception.AccountException;
import zzc.spring.hibernate.exception.StockExcetion;

/**
 * 不推荐使用HibernateTemplate和HibernateDaoSupport，因为这样会导致Dao和Spring的API进行耦合，可移植性变差
 */
@Repository
public class BookDaoImpl implements BookDao {

	@Autowired
	private SessionFactory sessionFactory;

	// 获取和当前线程绑定的session
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public int findBookPriceByIsbn(String isbn) {
		// 关于在Hibernate5.3.x中HQL语句使用"?"参数占位符运行报错的问题
		// 1.将HQL语句中的"?"改为JPA-style
		// String hql = "SELECT b.price FROM Book b WHERE b.isBn = ?0";
		// Query query = getSession().createQuery(hql).setParameter(0, isbn);
		// 2.别名：要求必须以冒号开头
		String hql = "SELECT b.price FROM Book b WHERE b.isBn = :isBn";
		Query query = getSession().createQuery(hql).setParameter("isBn", isbn);
		return (int) query.uniqueResult();
	}

	@Override
	public void updateBookStock(String isbn) {
		// 验证书的库存是否充足
		String hql2 = "SELECT b.stock FROM Book b WHERE b.isBn = :isBn";
		int result = (int) getSession().createQuery(hql2).setParameter("isBn", isbn).uniqueResult();
		if (result == 0) {
			throw new StockExcetion("库存不足!");
		}
		String hql = "UPDATE Book b SET b.stock = b.stock-1 WHERE b.isBn = :isBn";
		getSession().createQuery(hql).setParameter("isBn", isbn).executeUpdate();
	}

	@Override
	public void updateUserAccount(String username, int price) {
		// 验证余额是否充足
		String hql2 = "SELECT a.balance FROM Account a WHERE a.username = :username";
		int balance = (int) getSession().createQuery(hql2).setParameter("username", username).uniqueResult();
		if (balance < price) {
			throw new AccountException("余额不足");
		}
		String hql = "UPDATE Account a SET a.balance= a.balance - :price WHERE a.username= :username";
		getSession().createQuery(hql).setParameter("price", price).setParameter("username", username).executeUpdate();
	}
}
