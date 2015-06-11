package com.artglp;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailServlet extends HttpServlet {

	private static final long serialVersionUID = -6933019976891263100L;

	private static final Logger log = Logger.getLogger(MailServlet.class
			.getName());

	public MailServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String message = request.getParameter("message");
		String capt = request.getParameter("capt");

		log.log(Level.INFO, "Session for contact "
				+ request.getSession().getId());

		if (capt == null
				|| !capt.equals(request.getSession().getAttribute(
						"dns_security_code"))) {
			log.log(Level.WARNING, capt == null ? "Captcha parameter null"
					: "Captcha code!");
			throw new ServletException(capt == null ? "Captcha parameter null"
					: "Captcha code!");
		}

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = name + "\n\n" + message + "\n\n" + email + "\n"
				+ phone;

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("@gmail.com",
					"Your admin"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"@gmail.com", "Your name"));
			msg.setSubject("Message of " + name);
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (Exception e) {
			response.setContentType("text/plain");
			response.getWriter().println(
					"Something went wrong. Please try again.");
			throw new ServletException(e);
		}

		/*
		 * response.setContentType("text/plain"); response.getWriter().println(
		 * "Thank you for your feedback. An Email has been send out.");
		 */
	}
	
}
