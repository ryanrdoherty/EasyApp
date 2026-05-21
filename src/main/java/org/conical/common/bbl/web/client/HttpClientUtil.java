package org.conical.common.bbl.web.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultSchemePortResolver;

/**
 * Utilities to assist web service clients
 * 
 * @author rdoherty
 */
public class HttpClientUtil {
	
	/**
	 * Creates an HttpClient pre-configured to pass requests over HTTPS.  This is a 'no security' client,
	 * so while the data will be encrypted, it would be easy for a malicious user to fool calling apps
	 * into believing they are contacting one server when in fact their requests are being intercepted.
	 * 
	 * On the plus side, the client will support unsigned certificates without even blinking.
	 * 
	 * @return SSL-ready HttpClient
	 */
	public static HttpClient getSslCapableHttpClient() {
		try {
			SSLContext sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
			sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
				@Override public X509Certificate[] getAcceptedIssuers() { return null; }
				@Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
				@Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
			}}, null);
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
			SchemePortResolver scheme = new DefaultSchemePortResolver();
			HttpClient httpClient = HttpClientBuilder.create()
					.setSchemePortResolver(scheme)
					.setSSLSocketFactory(socketFactory)
					.build();
			return httpClient;
		}
		catch (NoSuchAlgorithmException e) {
			throw new BBLWebClientException("Unable to create SSLContext using TLS algorithm.", e);
		}
		catch (KeyManagementException e) {
		    throw new BBLWebClientException("Unable to create SSLContext with 'no-check' TrustManager", e);
		}
	}
}
