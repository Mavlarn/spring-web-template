

package com.mtt.myapp.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * Application lifecycle listner.
 *
 * @author Mavlarn
 * @since 3.1
 */
@Service
public class ApplicationListenerBean implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private AppConfig config;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context
	 * .ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

	}
}