package com.week1.jvm.task2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * 功能描述: 自定义类加载器
 * @Author: wei qiang
 * @Date: 2021/11/7 21:07
 * @description
 * 第一周作业:
 * 2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 */
public class TestClassloader extends ClassLoader{


	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String resourcePath = name.replace(".", "/");

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcePath + ".xlass");

		try {
			int length  =is.available();
			byte[] bytes = new byte[length];
			is.read(bytes);
			byte[] newBytes = TestClassloader.decode(bytes);
			// 通知底层定义这个类
			return defineClass(name, newBytes, 0, newBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return super.findClass(name);
	}

	/*
	 * 功能描述:
	 * @param bytes
	 * @return: byte[]
	 * @Author: wei qiang
	 * @Date: 2021/11/7 21:12
	 * @description  处理文件字节
	 */
	public static byte[] decode(byte[] bytes){
		byte[] returnBytes = new byte[bytes.length];
		for(int i=0,size = bytes.length; i < size; i++){
			returnBytes[i] =  (byte)(255 - bytes[i]);
		}
		return returnBytes;
	}



	public static void main(String[] args) {

		String className = "Hello";
		String methodName = "hello";
		ClassLoader classLoader = new TestClassloader();

		try {
			Class<?> clazz = classLoader.loadClass(className);

			for(Method method : clazz.getMethods()){
				System.out.println(clazz.getSimpleName() + "." + method.getName());
			}

			Object hello =  clazz.getDeclaredConstructor().newInstance();

			Method method = clazz.getMethod(methodName);
			method.invoke(hello);
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}



}
