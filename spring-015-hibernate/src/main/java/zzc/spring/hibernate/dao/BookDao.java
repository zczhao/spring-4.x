package zzc.spring.hibernate.dao;

public interface BookDao {

	//根据书号获取书的单价
	int findBookPriceByIsbn(String isbn);

	//更新书的库存,使书号对应的库存-1
	void updateBookStock(String isbn);

	//更新用户的账户余额,使用username 的 balance - price 
	void updateUserAccount(String username, int price);
}
