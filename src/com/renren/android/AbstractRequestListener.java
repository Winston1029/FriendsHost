/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 对�?�布状�?�?新鲜事�?照片等请求进行�?应的listener的抽象类
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class AbstractRequestListener<T extends ResponseBean> {
	
	/**
	 * 将response请求解�?为针对具体请求的bean
	 * 
	 * @param response 
	 * 			请求完�?�?�的�?应字符串
	 * @return 
	 * 			若解�?�?功，返回解�?�?�的对象，�?�则返回null
	 */
	@SuppressWarnings("unchecked")
    public T parse(String response) {
	    Class<?> c = this.getGenericType();
        try {
            Constructor<T> constructor = (Constructor<T>) c.getDeclaredConstructor(String.class);
            T result = constructor.newInstance(response);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * 获�?�T的类型
	 * @param index
	 * @return
	 */
	private Class<?> getGenericType() {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length < 1) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[0] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) params[0];
    }
	
	 /**
     * 请求完�?�?�以对象形�?返回�?务器的�?应的结果
     * 
     * @param bean 
     * 			�?务器返回的�?应字符串解�?�?�得到的对象
     *        
     */
    public abstract void onComplete(T bean);

    /**
     * �?务器返回了错误结果，已�?正确的链接上了�?务器但有错误如：缺少�?�数�?sessionKey过期等。
     * 
     * @param renrenError
     */
    public abstract void onRenrenError(RenrenError renrenError);

    /**
     * 在请求期间�?�生了严�?问题（如：网络故障�?访问的地�?��?存在等）
     * 
     * @param fault
     */
    public abstract void onFault(Throwable fault);
}
