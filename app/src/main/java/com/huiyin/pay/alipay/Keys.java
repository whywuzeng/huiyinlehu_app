/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.huiyin.pay.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088802401450149";
	// 收款支付宝账号
	public static final String DEFAULT_SELLER = " pinyi108@qq.com";
	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMQ4wzkJqJQtxOb4xw+DTqt7nqhkNJlS54ekwUumEqkzzlCT7tHf6pOMvQChsDP/dmphGn2tFPl6fjUKgCX06a1+n4iqpVaBfVmxy6Axg0WbXat7YQ2pSuC7/ruxVMiaNvsz40x88ky8NqxEmpoxRXFBjjgiy6ZZHjQsRs2dZ48PAgMBAAECgYAAtmwPEvfnwdrLM/AVIFbyzHohBmwiemiY14JUMgAzWRrQtBjT7ko9yu8Mx4mKRs05Saz889n1pfo7WxRoDuGiXAiexTpVgCsjzgFCG2LyZIrih0/X6pQaaBooJMBMr7Y178lJJzVxNmlfearmc7RWXNjPDRPmdn+5biQWxoeEwQJBAOEzZ35hrQbdFywXFF5BxXEiroTsK/Kww8vEc8ifvAV9lEkwFVmswN95yNrFWJNO/NYPc3X1U/4JCCpfInzu/bECQQDfDsKuTaJ8Fpuk1LHaujVri5V9lfsTFdOTHgzZK/k1GpY/b4QNHl7XzjhYOBXxA0pFqec3pVY6nMcLBvOaxsi/AkEApFoUnNqCIXE3WRka51NO2it6fE20ITE6I7si2PcijEtC7krmOgTm2fGSUX8XDqLlyP+Lvzmk69ABopK9o7nzcQJBAMpf1fnWrgjvah9Oal8MetMsPWFH5hgWwXLE3NtcrimkCZVcU+6jE2Gw1CYWHLIiTgyObh1qgKWFAIHcuMBAgnkCQAZJRSteL82wS4d8BSsedqzzDvpyaIysKgGN4YajEii8AjQrDWC4vIiO0DyLUg2PVeHrCCOJBh6hsWjy3GQpvS0=";
	public static final String PUBLIC = " MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

//	// 公司测试私钥
//	// 合作身份者id，以2088开头的16位纯数字
//	public static final String DEFAULT_PARTNER = "2088101178484721";
//	// 收款支付宝账号
//	public static final String DEFAULT_SELLER = "daniel@eims.com.cn";
//	// 商户私钥，自助生成
//	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMOqGeh/q43jE+E0Fly/ipxBu9deGVn0JmTPoYsMKMNLnBxukEyFpZbXRcXMINsZfmrpO49Nc3zcXzW/USNu+TQRjPL2BhnT9t09LDRGawjrdEYunF4FWXCzQuDIhEuVO+N0ZCe+g9zEVvcTij78d2rffwBMC72d6byK4++7ZUshAgMBAAECgYEAumZ4XBgUdQ1ZSPf4Pc9z2zx8GMZaXGnNkciD2xVwV+zWAfgKm/vOLo9StwaqXlBBMNSUmwZr+Q1v3qIuSBVWbWpWj3Lu/ii87qEKEljL/1RCPhhR5BOZ9RfN7+nqVDpWopmiJZMpGBRWQ5EyXAhXY+Sf3b0yS1AP8x9L9gq+j6kCQQDy6S8lbwZ/oJj5Ze2E216n1KbQU2PmBl2HP3Poh2cmysYvd1VVi0TFv3JIq1bF0To7oIJVvAUDjfhmQL++0OQjAkEAzjUuDFi2lMHDdQRAVNf4RI1NAbuDl5QvbrBQEQ9C3hLq7rd4vzLBhEtE9fL4FDE/ZJ5P61a7B2W/7lpTya8V6wJAIAJSUCB9Prwu7E5eKi+uHXeMM8+JTnpwQK4/29hb42S9EOwstoUPnu9HZAQP/yk+/zR7eEi6cGl43iCSI9bq+QJAC95LtDI28ADm+wkvmrKDe46WbA8a19KTmE9VjGXQd2+nTe4JaRfGw5KBeZZuaNscxvHFr8niId7dWR6hy+3j2QJAFdegMEYZ5IvVZr7LZCgmjpGl8ObqIbrEyrodJCW91jFvUVHNsCf+4Z6bsdGgrOfLnKavLvNfSJIu1ZmGS3PV3A==";
//	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDqhnof6uN4xPhNBZcv4qcQbvXXhlZ9CZkz6GLDCjDS5wcbpBMhaWW10XFzCDbGX5q6TuPTXN83F81v1Ejbvk0EYzy9gYZ0/bdPSw0RmsI63RGLpxeBVlws0LgyIRLlTvjdGQnvoPcxFb3E4o+/Hdq338ATAu9nem8iuPvu2VLIQIDAQAB";
}
