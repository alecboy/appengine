package com.artglp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.toyz.litetext.FontUtils;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.images.ImagesService.OutputEncoding;

public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = -8686694405042292508L;

	private static final Logger log = Logger.getLogger(CaptchaServlet.class
			.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		log.info(getServletName() + " started");

		//String fontname = "Lucida-Bold-Italic";
		String text = "15975";

		//FontUtils fm = new FontUtils();

		//byte[] bmp_data = fm.doRender(text, fontname);
		byte[] bmp_data = "0000".getBytes();

		log.info("BMP image created");

		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		Image bmpImage = ImagesServiceFactory.makeImage(bmp_data);

		Transform flipit = ImagesServiceFactory.makeVerticalFlip();
		Transform crop = ImagesServiceFactory.makeCrop(0, 0, 0.7, 0.5);

		bmpImage = imagesService.applyTransform(flipit, bmpImage,
				OutputEncoding.JPEG);
		bmpImage = imagesService.applyTransform(crop, bmpImage,
				OutputEncoding.JPEG);

		log.info("JPEG image created");

		request.getSession().setAttribute("dns_security_code", text);
		log.log(Level.INFO, "New session for contact "
				+ request.getSession().getId());

		response.setContentType("image/jpg");
		ByteArrayInputStream io = new ByteArrayInputStream(
				bmpImage.getImageData());

		ServletOutputStream svout = response.getOutputStream();
		int c = -1;
		while ((c = io.read()) != -1) {
			svout.write(c);
		}
		io.close();
		svout.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

}
