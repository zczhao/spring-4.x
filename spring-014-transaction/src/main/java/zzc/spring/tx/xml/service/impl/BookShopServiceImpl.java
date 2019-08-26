package zzc.spring.tx.xml.service.impl;


import zzc.spring.tx.xml.BookShopDao;
import zzc.spring.tx.xml.service.BookShopService;

public class BookShopServiceImpl implements BookShopService {

	private BookShopDao bookShopDao;
	
	public void setBookShopDao(BookShopDao bookShopDao) {
		this.bookShopDao = bookShopDao;
	}

	public void purchase(String username, String isbn) {
		//获取书单价
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		//更新书的库存
		bookShopDao.updateBookStock(isbn);
		//更新用户余额
		bookShopDao.updateUserAccount(username, price);
	}

}
