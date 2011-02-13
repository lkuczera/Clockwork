package pl.jextreme.util;

import java.lang.reflect.Method;

import android.util.Log;

public class BeanDump {
	public static String dumpFields(Object target) {
		if(target == null) return "null";
		Method[] methods = target.getClass().getDeclaredMethods();
		StringBuilder builder = new StringBuilder();
		for(Method method: methods) {
			if(method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
				try {
					builder.append(method.getName()+": "+method.invoke(target, null).toString()+"\n");
				} catch (Exception e) {
					Log.w("BeanDump", "Ups something went wrong", e);
				}
			}
			
		}
		String ret = builder.toString();
		if(ret.equals("")) {
			return target.getClass().getSimpleName();
		}
		return ret;
	}
}
