package sk.emaa.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IDgenerator {

	private static AtomicLong idCounter = new AtomicLong();

	public static String createID() {
	    return String.valueOf(idCounter.getAndIncrement());
	}
	
}
