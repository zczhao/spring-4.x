package zzc.spring.hibernate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzc.spring.hibernate.service.BookService;
import zzc.spring.hibernate.service.CashierService;

import java.util.List;

@Service
public class CashierServiceImpl implements CashierService {

	@Autowired
	private BookService bookService;

	@Override
	public void checkout(String username, List<String> isbns) {
		for (String isbn : isbns) {
			bookService.purchase(username, isbn);
		}

	}
}
