package com.newrelic.labs.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.labs.jmx.agent.JmxArgs;
import com.newrelic.labs.jmx.agent.JmxIntegration;
import com.newrelic.labs.infra.agent.Integration;

public class JmxMain {
	
	private static Logger logger = LoggerFactory.getLogger(JmxMain.class);

	public static void main(String[] args) {
		try {
			Integration<JmxArgs> integration = new JmxIntegration("com.newrelic.labs.nri.jmx", "0.9");
			JmxArgs arguments = new JmxArgs();
			arguments.setupArgs(args);
			integration.run(arguments);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
