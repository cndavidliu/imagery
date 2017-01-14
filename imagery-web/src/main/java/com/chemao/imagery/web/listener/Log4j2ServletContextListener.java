package com.chemao.imagery.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.web.Log4jWebSupport;

public class Log4j2ServletContextListener implements ServletContextListener {
	
	private ServletContext context;
	private LoggerContext loggerConext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.context = sce.getServletContext();
		this.context.log("Log4j2ServletContextListener ensuring that Log4j starts up properly.");
		this.loggerConext = Configurator.initialize(this.context.getServletContextName(),
				Log4j2ServletContextListener.class.getClassLoader(), 
				this.context.getInitParameter("log4jConfiguration"), 
				this.context);
		this.context.setAttribute(Log4jWebSupport.CONTEXT_ATTRIBUTE, this.loggerConext);
		ContextAnchor.THREAD_CONTEXT.set(this.loggerConext);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (this.context == null || this.loggerConext == null) {
			return;
		}
		
		this.context.log("Log4j2ServletContextListener ensuring that Log4j shuts down properly.");
		
		ContextAnchor.THREAD_CONTEXT.remove();
		this.context.removeAttribute(Log4jWebSupport.CONTEXT_ATTRIBUTE);
		
		this.loggerConext.stop();
		this.loggerConext.setExternalContext(null);
		this.loggerConext = null;
	}
}
