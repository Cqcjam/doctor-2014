package com.github.jdk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 日志，自定义抛出异常都必须带者从异常出现代码中的 e slf4j1.6版本之前。
 * 
 *         log规范 log info/warn/error
 * 
 *         log参数只能两种：log.error(msg)，log.error(msg, e)，
 *         没有exception时才能用前者，
 *         有exception的一定要在源处log出堆栈。
 * 
 *         msg内容格式为json格式，例子如下：
 * 
 *         catch (IOException e) {
 *         StringBuilder sb = new StringBuilder(80);
 *         for(String addr: addrList){
 *         sb.append(addr).append(',');
 *         }
 *         sb.deleteCharAt(sb.length() - 1);
 *         String msg = String.format("{addrList:'%1$s'}", sb.toString());
 *         log.error(msg, e);//在右边界处log出这段代码的重要输入参数，因为已有Throwable， 所以不需要自己再在msg中提供哪类哪方法哪行
 *         throw e;
 *         }
 * 
 *         String.format()对于很多人来说还算生疏，不要用错了
 * 
 *         由于 slf4j 支持 {} 作占位符 参数化msg的，但只有 error(String, Object, Object)这种一String两Object的，
 *         如果最后一个是Exception类型的，才能将堆栈打印出来，而且只支持一个{}，这个还是比较容易用错的。
 *         error(String, Object[])，将Exception类型放入Object[]里，会丢失异常堆栈。
 *         见slf4j的FAQ
 * 
 *         所以希望用参数化，并带异常的，最好还是使用String.format()方式，或字符串拼接方式。
 * 
 *         //slf4j版本1.60之后,这种限制变宽松了，但是异常必须是最后一个参数。
 *         In the presence of an exception/throwable, is it possible to parameterize a logging statement?
 * 
 *         Yes, as of SLF4J 1.6.0, but not in previous versions. The SLF4J API supports parametrization in the presence of an exception, assuming the exception is the last parameter. Thus,
 * 
 *         String s = "Hello world";
 *         try {
 *         Integer i = Integer.valueOf(s);
 *         } catch (NumberFormatException e) {
 *         logger.error("Failed to format {}", s, e);
 *         }
 * 
 *         will print the NumberFormatException with its stack trace as expected. The java compiler will invoke the error method taking a String and two Object arguments. SLF4J, in accordance with the programmer's most probable intention, will interpret NumberFormatException instance as a throwable
 *         instead of an unused Object parameter. In SLF4J versions prior to 1.6.0, the NumberFormatException instance was simply ignored.
 * 
 *         If the exception is not the last argument, it will be treated as a plain object and its stack trace will NOT be printed. However, such situations should not occur in practice.
 * 
 * 
 * 
 *
 */
public class ExceptionRule {
	private static final Logger log = LoggerFactory.getLogger(ExceptionRule.class);

	@Test
	public void test1() {
		boolean b = exception1();
		System.out.println(b);
	}

	@Test
	public void test2() {
		boolean b = exception2();
		System.out.println(b);
	}

	private boolean exception1() {
		try {
			int a = 1 / 0;
			return true;
		} catch (Exception e) {
			String error = "error";
			log.error("{error:{}}", error);
			return false;
		}
	}

	private boolean exception2() {
		try {
			int a = 1 / 0;
			return true;
		} catch (Exception e) {
			String error = "error";
			log.info("right --------");
			log.error("{error:{}}", error, e);
			log.info("right --------");

			log.error(String.format("error:%s", error), e);
			
			log.info("wrong --------");
			log.error("{error:{}}", e, error);

			return false;
		}
	}
}
