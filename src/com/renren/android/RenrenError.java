/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renren.android;

/**
 * ��װ���������صĴ�����
 * 
 * @author yong.li@opi-corp.com
 */
public class RenrenError extends RuntimeException {

	private static final long serialVersionUID = 1L;
    
    /** �����룺����Ϊ�� */
    public static final int ERROR_CODE_NULL_PARAMETER = -1;
    
    /** �����룺����������� <br>
     *  ���磬�����״̬���ȳ�����140���ַ�. */
    public static final int ERROR_CODE_PARAMETER_EXTENDS_LIMIT = -2;
    
    /** �����룺�Ƿ����� */
    public static final int ERROR_CODE_ILLEGAL_PARAMETER = -3;
    
    /** �����룺Access TokenΪ�ջ�Session KeyΪ�� */
    public static final int ERROR_CODE_TOKEN_ERROR = -4;
    
    /** �����룺�޷�������������Ӧ�ַ� */
    public static final int ERROR_CODE_UNABLE_PARSE_RESPONSE = -5;
    
    /** �����룺������ȡ��*/
    public static final int ERROR_CODE_OPERATION_CANCELLED = -6;
    
    /** �����룺���/Ȩ����֤ʧ�� */
    public static final int ERROR_CODE_AUTH_FAILED = -7;
    
    /** �����룺��֤��̱�ȡ�� */
    public static final int ERROR_CODE_AUTH_CANCELLED = -8;
    
    /** �����룺δ֪���� */
    public static final int ERROR_CODE_UNKNOWN_ERROR = -9;
    
    /** �����룺��ʼ��ʧ�� */
    public static final int ERROR_RENREN_INIT_ERROR = -10;

	/**
	 * ���������صĴ�����룬��ϸ��Ϣ��
	 * http://wiki.dev.renren.com/wiki/API%E9%94%99%E8%AF%AF%E4
	 * %BB%A3%E7%A0%81%E6%9F%A5%E8%AF%A2
	 */
	private int errorCode;

	/** ԭʼ��ӦURL */
	private String orgResponse;

	public RenrenError(String errorMessage) {
		super(errorMessage);
	}

	public RenrenError(int errorCode, String errorMessage, String orgResponse) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.orgResponse = orgResponse;
	}

	public String getOrgResponse() {
		return orgResponse;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "errorCode:" + this.errorCode + "\nerrorMessage:"
				+ this.getMessage() + "\norgResponse:" + this.orgResponse;
	}
	
	/**
	 * �����������ص�errorMessageת����SDK��������������ַ�
	 * 
	 * @param errorCode
	 * 			���������صĴ������
	 * @param errorMessage
	 * 			���������صĴ����ַ��ʹ������һһ��Ӧ
	 * @return
	 */
	public static String interpretErrorMessage(int errorCode, String errorMessage) {
		switch (errorCode) {
		// ͼƬ�ߴ�̫С����ʱ���������ֱ�ӷ����ϴ�ʧ�ܣ��պ���ܻ���������
		 case 300:
			 errorMessage = "";
			 break;
		// �ϴ���Ƭʧ��
		case 20101:
			// ������İ������ʵ��޸ģ�ʹ��ʾ��Ϣ��Ӽ���׶�
			// errorMessage = "�ϴ���Ƭʧ�ܣ����Ժ�����";
			errorMessage = "请稍后重试";
			break;
		// �ϴ���Ƭ���ʹ����δ֪
		case 20102:
		case 20103:
			errorMessage = "暂不支持此格式照片，请重新选择";
			break;
		default:
			break;
		}
		
		return errorMessage;
	}
}