package com.newrelic.labs.jmx.agent;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.labs.infra.agent.Entity;
import com.newrelic.labs.infra.agent.Integration;

public class JmxIntegration extends Integration<JmxArgs> {
	
	private static Logger logger = LoggerFactory.getLogger(JmxIntegration.class);

	public JmxIntegration(String integrationName, String integrationVersion) {
		super(integrationName, integrationVersion);
	}

	@Override
	public void init() throws Exception {

	}

	@Override
	public Entity<JmxArgs> newEntity(String name) throws Exception {
		return new JmxEntity<JmxArgs>(name);
	}
	
	@Override
	public boolean isListener() {
		return false;
	}

}
