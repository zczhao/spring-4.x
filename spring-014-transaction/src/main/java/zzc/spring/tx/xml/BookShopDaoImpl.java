package zzc.spring.tx.xml;

import org.springframework.jdbc.core.JdbcTemplate;

public class BookShopDaoImpl implements BookShopDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int findBookPriceByIsbn(String isbn) {
		String sql = "SELECT price FROM book WHERE isbn = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class,isbn);
	}

	public void updateBookStock(String isbn) {
		//检查书的库存是否足够
		String sql2 = "SELECT stock FROM book_stock where isbn = ?";
		int stock = jdbcTemplate.queryForObject(sql2, Integer.class,isbn);
		if(stock==0){
			throw new BookStockExcetion("库存不足!");
		}
		String sql = "UPDATE book_stock SET stock = stock - 1 WHERE isbn = ?";
		jdbcTemplate.update(sql,isbn);
	}

	public void updateUserAccount(String username, int price) {
		String sql2 = "SELECT balance FROM account where username = ?";
		int balance = jdbcTemplate.queryForObject(sql2, Integer.class,username);
		if(balance < price){
			throw new UserAccountException("余额不足!");
		}
		String sql = "UPDATE account SET balance = balance - ? WHERE username = ?";
		jdbcTemplate.update(sql,price,username);
	}

}
