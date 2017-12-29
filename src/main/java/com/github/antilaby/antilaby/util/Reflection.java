package com.github.antilaby.antilaby.util;

public final class Reflection {
	
	public static <T, E> E getField(T instance, String fieldName) {
		try {
			return cast(instance.getClass().getDeclaredField(fieldName).get(instance));
		} catch(final ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object toCast) {
		return (T) toCast;
	}
	
	public static <T, E> E invokeMethod(T instance, String methodName, Class<?>[] argTypes, Object... args) {
		if(argTypes.length == args.length) try {
			return cast(instance.getClass().getDeclaredMethod(methodName).invoke(instance));
		} catch(final ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T, E> E invokeNoArgsMethod(T instance, String methodName) {
		try {
			return cast(instance.getClass().getDeclaredMethod(methodName).invoke(instance));
		} catch(final ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Private constructor, no need to instantiate this class
	 */
	private Reflection() {}
}
