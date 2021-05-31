package udienza;

import java.util.Calendar;
import java.util.Comparator;

import entities.Udienza;

public class UdienzaDateComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		Udienza u1 = (Udienza) o1;
		Udienza u2 = (Udienza) o2;
		Calendar c1 = u1.getDate();
		Calendar c2 = u2.getDate();
		
		if(c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)) {
			return 1;
		}
		if(c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
			return -1;
		}
		
		if(c1.get(Calendar.MONTH) > c2.get(Calendar.MONTH)) {
			return 1;
		}
		if(c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH)) {
			return -1;
		}
		
		if(c1.get(Calendar.DAY_OF_MONTH) > c2.get(Calendar.DAY_OF_MONTH)) {
			return 1;
		}
		if(c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH)) {
			return -1;
		}		
		
		return 0;
	}

}
