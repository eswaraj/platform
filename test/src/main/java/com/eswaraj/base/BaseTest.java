package com.eswaraj.base;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

public class BaseTest {

	private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA_NUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMERIC_CHARS = "0123456789";
	private static final String HEX_CHARS = "ABCDEF0123456789";
	private static final String SINGLE_LINE_STRING_CHARS = ALPHA_NUMERIC_CHARS + " `~!@#$%^&*()_+-=[]{}\\|;:'\",./<>?";
	private static final String MULTI_LINE_STRING_CHARS = SINGLE_LINE_STRING_CHARS + "\n";
	private static final Random RANDOM = new Random();
	private static final String[] TLDS = new String[] { "com", "org", "net", "info", "biz", "co.uk" };
	private static final String[] URL_SCHEMES = new String[] { "http", "https" };
	protected static final Map<Class, Map> UNIQUENESS_GROUPS_BY_CLASS = new HashMap<Class, Map>();

	private static <T> Set<T> getUniquenessGroup(Class<T> clazz, String groupName) {
		Map groupsByName = UNIQUENESS_GROUPS_BY_CLASS.get(clazz);
		if (groupsByName == null) {
			groupsByName = new HashMap();
			UNIQUENESS_GROUPS_BY_CLASS.put(clazz, groupsByName);
		}
		Set<T> group = (Set<T>) groupsByName.get(groupName);
		if (group == null) {
			group = new HashSet<T>();
			groupsByName.put(groupName, group);
		}
		return group;
	}

	/**
	 * Generate a random string using a specific set of characters
	 */
	public static String randomString(String chars, int len) {
	    StringBuilder sb = new StringBuilder(len);
	    for (int i = 0; i < len; i++) {
	        sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
	    }
	    return sb.toString();
	}

	/**
	 * Inject a dependency (i.e. autowired) into an object
	 * @param targetObject the object whose dependency is being injected
	 * @param propertyName the name of the object's property
	 * @param propertyObjectValue the value being injected
	 */
	protected void inject(Object targetObject, String propertyName, Object proprtyObjectValue) {
		ReflectionTestUtils.setField(targetObject, propertyName, proprtyObjectValue);
		/*
	    try {
	        Class cls = targetObject.getClass();
	        System.out.println("***baseTest****");
	        
	        Field field = null;
	        while (cls != Object.class) {
	        	System.out.println("cls="+cls);
	            try {
	                field = cls.getDeclaredField(propertyName);
	                System.out.println("field="+field);
	                break;
	            } catch (NoSuchFieldException e) {
	            	System.out.println("NoSuchFieldException="+propertyName);
	                cls = cls.getSuperclass();
	            }
	        }
	        if (field != null) {
	            field.setAccessible(true);
	            try {
	                field.set(targetObject, proprtyObjectValue);
	            } catch (IllegalAccessException e) {
	                //If field is static then try it once again with null
	                field.set(null, proprtyObjectValue);
	            }
	        } else {
	            throw new NoSuchFieldException("property "+ propertyName +" not found");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail("Failed to inject dependency for " + propertyName + " on " + targetObject.getClass().getName());
	    }
	    */
	}

	/**
	 * Inject a dependency (i.e. autowired) into an object, quietly ignoring any
	 * exceptions that may be thrown, i.e. if the object doesn't even have the
	 * given property
	 * @param targetObject the object whose dependency is being injected
	 * @param propertyName the name of the object's property
	 * @param propertyObjectValue the value being injected
	 */
	protected void injectQuietly(Object targetObject, String propertyName, Object proprtyObjectValue) {
	    try {
	        Class cls = targetObject.getClass();
	        Field field = null;
	        while (cls != Object.class) {
	            try {
	                field = cls.getDeclaredField(propertyName);
	                break;
	            } catch (NoSuchFieldException e) {
	                cls = cls.getSuperclass();
	            }
	        }
	        if (field != null) {
	            field.setAccessible(true);
	            try {
	                field.set(targetObject, proprtyObjectValue);
	            } catch (IllegalAccessException e) {
	                //If field is static then try it once again with null
	                field.set(null, proprtyObjectValue);
	            }
	        }
	    } catch (Exception ignored) {
	    }
	}

	/**
	 * Generate a random single-line string using any printable characters
	 */
	public static String randomSingleLineString(int len) {
	    return randomString(SINGLE_LINE_STRING_CHARS, len);
	}

	/**
	 * Generate a random multi-line string using any printable characters
	 */
	public static String randomMultiLineString(int len) {
	    return randomString(MULTI_LINE_STRING_CHARS, len);
	}

	/**
	 * Generate a random string consisting of only letters
	 */
	public static String randomAlphaString(int len) {
	    return randomString(ALPHA_CHARS, len);
	}

	/**
	 * Generate a random string consisting of letters and/or digits
	 */
	public static String randomAlphaNumericString(int len) {
	    return randomString(ALPHA_NUMERIC_CHARS, len);
	}
	
	/**
	 * Generate a random string consisting of digits
	 */
	public static String randomNumericString(int len) {
	    return randomString(NUMERIC_CHARS, len);
	}
	

	/**
	 * Generate a random string consisting of letters and/or digits
	 * that is unique among a named group of values
	 */
	public static String uniqueAlphaNumericString(int len, String groupName) {
	    Set<String> group = getUniquenessGroup(String.class, groupName);
	    String value;
	    do {
	        value = randomAlphaNumericString(len);
	    } while (group.contains(value));
	    group.add(value); // track that we've used this value
	    return value;
	}

	/**
	 * Generate a random string consisting of only hex characters
	 */
	public static String randomHexString(int len) {
	    return randomString(HEX_CHARS, len);
	}

	/**
	 * Generate a random string consisting of only hex characters
	 * that is unique among a named group of values
	 */
	public static String uniqueHexString(int len, String groupName) {
	    Set<String> group = getUniquenessGroup(String.class, groupName);
	    String value;
	    do {
	        value = randomHexString(len);
	    } while (group.contains(value));
	    group.add(value); // track that we've used this value
	    return value;
	}

	/**
	 * Generate a random integer value
	 */
	public static int randomInteger() {
	    return RANDOM.nextInt();
	}

	/**
	 * Generate a random integer value that is unique in a given "group."
	 * This is useful if you want to assign several random but unique IDs.
	 * For example, let's say you need to generate random IDs for four
	 * different mock objects.  You could call randomInteger() for each of
	 * them, but there's no guarantee that the same value wouldn't be
	 * picked more than once.  Unlikely, but totally possible.  So this
	 * method serves to ensure that the same value won't be picked more
	 * than once.  A group name must be specified, identifying the group
	 * to which the generated value
	 */
	public static int uniqueInteger(String groupName) {
	    Set<Integer> group = getUniquenessGroup(Integer.class, groupName);
	    int value;
	    do {
	        value = randomInteger();
	    } while (group.contains(value));
	    group.add(value); // track that we've used this value
	    return value;
	}

	/**
	 * Generate a random integer value between 0 and max
	 */
	public static int randomInteger(int max) {
	    return RANDOM.nextInt(max);
	}

	/**
	 * Generate a random integer value between 0 and max that is unique in a
	 * given "group."
	 * This is useful if you want to assign several random but unique IDs.
	 * For example, let's say you need to generate random IDs for four
	 * different mock objects.  You could call randomInteger() for each of
	 * them, but there's no guarantee that the same value wouldn't be
	 * picked more than once.  Unlikely, but totally possible.  So this
	 * method serves to ensure that the same value won't be picked more
	 * than once.  A group name must be specified, identifying the group
	 * to which the generated value
	 */
	public static int uniqueInteger(int max, String groupName) {
	    Set<Integer> group = getUniquenessGroup(Integer.class, groupName);
	    int value;
	    do {
	        value = randomInteger(max);
	    } while (group.contains(value));
	    group.add(value); // track that we've used this value
	    return value;
	}

	/**
	 * Generate a random double value between 0.0 and 1.0
	 */
	public static double randomDouble() {
	    return RANDOM.nextDouble();
	}

	/**
	 * Generate a random double value between 0.0 and max
	 * @param max
	 * @return
	 */
	public static double randomDouble(long max) {
	    return RANDOM.nextDouble() * max;
	}

	/**
	 * Generate a random long value
	 */
	public static long randomLong() {
	    return RANDOM.nextLong();
	}

	/**
	 * Generate a random positive long value
	 */
	public static long randomPositiveLong() {
		long randomLong = randomLong();
		if(randomLong < 0){
			randomLong = 0 - randomLong;
		}
	    return randomLong;
	}
	/**
	 * Generate a random long value between 0 and max
	 */
	public static long randomLong(long max) {
	    return Math.abs(RANDOM.nextLong() % max);
	}

	/**
	 * Generate a random long value that is unique in a given "group."
	 * This is useful if you want to assign several random but unique IDs.
	 * For example, let's say you need to generate random IDs for four
	 * different mock objects.  You could call randomLong() for each of
	 * them, but there's no guarantee that the same value wouldn't be
	 * picked more than once.  Unlikely, but totally possible.  So this
	 * method serves to ensure that the same value won't be picked more
	 * than once.  A group name must be specified, identifying the group
	 * to which the generated value
	 */
	public static long uniqueLong(String groupName) {
	    Set<Long> group = getUniquenessGroup(Long.class, groupName);
	    long value;
	    do {
	        value = randomLong();
	    } while (group.contains(value));
	    group.add(value); // track that we've used this value
	    return value;
	}

	/**
	 * Generate a random but structurally valid email address
	 */
	public static String randomEmailAddress() {
	    return randomAlphaNumericString(8) + "@" + randomDomainName();
	}

	/**
	 * Generate a random but value top-level domain (i.e. com)
	 */
	public static String randomTopLevelDomain() {
	    return TLDS[RANDOM.nextInt(TLDS.length)];
	}

	/**
	 * Generate a random but structurally valid domain name (i.e. foobar.com)
	 */
	public static String randomDomainName() {
	    return randomAlphaNumericString(8) + "." + randomTopLevelDomain();
	}

	/**
	 * Generate a random but structurally valid hostname (i.e. foo.bar.com)
	 */
	public static String randomHostName() {
	    return randomAlphaNumericString(10) + "." + randomDomainName();
	}

	/**
	 * Generate a random but structurally valid URL (i.e. http://blah.foobar.com/whatever/something.xyz)
	 */
	public static String randomUrl() {
	    return URL_SCHEMES[RANDOM.nextInt(URL_SCHEMES.length)] + "://" + randomHostName() + "/" + randomAlphaNumericString(10) + "." + randomAlphaString(3);
	}

	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	

}