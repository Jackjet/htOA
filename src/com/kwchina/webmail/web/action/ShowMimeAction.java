package com.kwchina.webmail.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.mail.BodyPart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.ui.html.HTMLImage;
import com.kwchina.webmail.util.MessageParserCommon;
import com.kwchina.webmail.web.bean.CurrentInfor;
import com.kwchina.webmail.web.service.MailBasicService;

public class ShowMimeAction extends MailDispatchAction {

	private static Log log = LogFactory.getLog(ShowMimeAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ShowMimeAction.list' method...");
		}

		// Mail Session
		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");
		if (mailSession == null) {
			/** @todo 重复提交？ */
			return null;
		}

		int msgnr = 1;
		int part = 1;

		// 页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		part = Integer.parseInt(http_header.getContent("part"));

		msgnr = Integer.parseInt(http_header.getContent("serialNo"));

		CurrentInfor current = MailBasicService.getCurrentFolder(request);
		String folderhash = current.getId();

		try {
			BodyPart bodyPart = mailSession.getMIMEPart(folderhash, msgnr, part);

			if (bodyPart != null) {
				String name = MessageParserCommon.getISOFileName(bodyPart);
				
				try {
					name= new String(name.getBytes("ISO-8859-1"),"gb2312");
					name = MimeUtility.decodeText(name);
				} catch (Exception e) {
					System.err.println(e);
				}

				InputStream is = bodyPart.getInputStream();
				InputStream in = new BufferedInputStream(is);
				int size = bodyPart.getSize();
				ByteStore ba = ByteStore.getBinaryFromIS(in, size);
				in = new ByteArrayInputStream(ba.getBytes());
				size = in.available();

				ByteStore b = ByteStore.getBinaryFromIS(in, size);
				b.setName(name);

				StringTokenizer tok = new StringTokenizer(bodyPart.getContentType(), ";");
				b.setContentType(tok.nextToken().toLowerCase());

				HTMLImage content = new HTMLImage(b);

				response.setDateHeader("Date:", System.currentTimeMillis());
				response.setDateHeader("Expires", System.currentTimeMillis() + 300000);
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "must-revalidate");

				response.setHeader("Content-Type", content.getContentType());
				response.setHeader("Content-Transfer-Encoding", content.getContentEncoding());
				response.setHeader("Content-Length", "" + content.size());
				response.setHeader("Connection", "Keep-Alive");
				String fileName = new String(content.cont.getName().getBytes("GB2312"), "ISO8859_1");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				int chunk_size = 8192;
				int offset = 0;

				while (offset + chunk_size < content.size()) {
					out.write(content.toBinary(), offset, chunk_size);
					offset += chunk_size;
				}

				out.write(content.toBinary(), offset, content.size() - offset);
				out.flush();

				out.close();
			}
			
			return null;

		} catch (Exception ex) {
			//ex.toString();
			ex.printStackTrace();
			return null;
		}
	}
}
