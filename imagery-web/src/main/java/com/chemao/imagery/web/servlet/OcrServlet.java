package com.chemao.imagery.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chemao.imagery.processor.TesseractException;
import com.chemao.imagery.processor.TesseractParameterException;
import com.chemao.imagery.processor.orc.Tesseract;

public class OcrServlet  extends HttpServlet{

	private static final long serialVersionUID = 1175417941319390106L;
	private static Logger logger = LogManager.getLogger(OcrServlet.class);
	
	private Tesseract tesseract = new Tesseract();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		resp.setHeader("content-type", "text/plain;charset=UTF-8");
		String address = req.getParameter("image");
		if (address == null)
			return;
		String result;
		try {
			result = this.tesseract.doOcr(address);
		} catch (TesseractException e) {
			logger.error("image[{}]", address, e);
			resp.sendError(500, e.getMessage());
			return;
		} catch (TesseractParameterException e) {
			logger.warn("parameter error:{} image:{}", e.getMessage(), address);
			resp.sendError(400, e.getMessage());
			return;
		}
		logger.info("image address:{}, result:{}", address, result);
		resp.getWriter().print(result);
		resp.getWriter().flush();
		resp.getWriter().close();
	}
}
