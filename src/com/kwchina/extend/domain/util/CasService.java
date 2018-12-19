package com.kwchina.extend.domain.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class CasService {

	public static String getTicket(final String server, final String username, final String password, final String service) {
		notNull(server, "server must not be null");
		notNull(username, "username must not be null");
		notNull(password, "password must not be null");
		notNull(service, "service must not be null");

		return getServiceTicket(server, getTicketGrantingTicket(server, username, password), service);
	}

	/**
	 * 取得ST
	 * 
	 * @param server
	 * @param ticketGrantingTicket
	 * @param service
	 */
	private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service) {
		if (ticketGrantingTicket == null)
			return null;

		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);

		post.setRequestBody(new NameValuePair[] { new NameValuePair("service", service) });

		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();

			switch (post.getStatusCode()) {
			case 200:
				return response;

			default:
				warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
				//info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
				break;
			}
		}

		catch (final IOException e) {
			warning(e.getMessage());
		}

		finally {
			post.releaseConnection();
		}

		return null;
	}

	/**
	 * @param server
	 * @param username
	 * @param password
	 */
	private static String getTicketGrantingTicket(final String server, final String username, final String password) {
		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server);

		post.setRequestBody(new NameValuePair[] { new NameValuePair("username", username), new NameValuePair("password", password) });

		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			//info("TGT=" + response);
			switch (post.getStatusCode()) {
			case 201: {
				final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);

				if (matcher.matches())
					return matcher.group(1);

				warning("Successful ticket granting request, but no ticket found!");
				//info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
				break;
			}

			default:
				warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
				//info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
				break;
			}
		}

		catch (final IOException e) {
			warning(e.getMessage());
		}

		finally {
			post.releaseConnection();
		}

		return null;
	}

	private static void ticketValidate(String serverValidate, String serviceTicket, String service) {
		notNull(serviceTicket, "paramter 'serviceTicket' can not be null");
		notNull(service, "paramter 'service' can not be null");

		final HttpClient client = new HttpClient();
		GetMethod post = null;

		try {
			post = new GetMethod(serverValidate + "?" + "ticket=" + serviceTicket + "&service=" + URLEncoder.encode(service, "UTF-8"));
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			//info(response);
			switch (post.getStatusCode()) {
			case 200: {
				//票信息Validate成功，返回用户信息
//				<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>
//					<cas:authenticationSuccess>
//						<cas:user>testuser5</cas:user>
//					</cas:authenticationSuccess>
//				</cas:serviceResponse>
				info("成功取得用户数据");
			}
			default: {

			}
			}

		} catch (Exception e) {
			warning(e.getMessage());
		} finally {
			// 释放资源
			post.releaseConnection();
		}

	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	private static void logout(String server, String ticketGrantingTicket) {
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(server+"/"+ticketGrantingTicket);
		
		try {
			client.executeMethod(method);
			switch (method.getStatusCode()) {
			case 200:
				info("Logged out");
				break;
			default:
				warning("Invalid response code (" + method.getStatusCode() + ") from CAS server!");
				info("Response: $response");
				break;
			}
		} catch (final IOException e) {
			warning(e.getMessage());
		} finally {
			method.releaseConnection();
		}
	}

	public static void main(final String[] args) throws Exception {
		final String server = "http://222.68.245.243/cas/v1/tickets";
		final String username = "testoa1";
		final String password = "HTpassword1234";
		// service的值是认证客户端应用地址，
		// 如果是C/S应用，填一个可访问的server地址，不能为空
		// http://localhost:7001
		final String service = "http://localhost";
		final String proxyValidate = "http://222.68.245.243/cas/proxyValidate";
		//用户验证获取票信息
		String ticket = getTicket(server, username, password, service);
		System.out.println("ticket===="+ticket);
		//票信息validate，成功则可获得录用户名
		ticketValidate(proxyValidate, ticket, service);
		//用户登出
		logout(server,ticket);

	}

	private static void warning(String msg) {
		System.out.println(msg);
	}

	private static void info(String msg) {
		System.out.println(msg);
	}

}