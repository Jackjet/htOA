package test;

import java.util.Date;

import com.kwchina.core.util.DateHelper;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date today = new Date();
		Date d1 = DateHelper.getDate("2015-12-30");
		boolean a = today.after(d1) && today.getDay() != d1.getDay();
		System.out.println(today.getDate());
		System.out.println(a);
	}
}
