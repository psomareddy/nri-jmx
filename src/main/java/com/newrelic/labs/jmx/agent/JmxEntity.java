package com.newrelic.labs.jmx.agent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.labs.infra.agent.Entity;
import com.newrelic.labs.infra.agent.MetricSet;
import com.newrelic.labs.infra.agent.SourceType;

public class JmxEntity<T> extends Entity<JmxArgs> {

	private static Logger logger = LoggerFactory.getLogger(JmxEntity.class);
	private JmxConnectionInfo connectionConfig = null;
	private MBeanServerConnection mbeanServerConnection;

	public JmxEntity(String entityName) {
		super(entityName);
	}
	
	@Override
	public String getEntityType() {
		return "JMX";
	}
	
	@Override
	protected JmxArgs populateBean(Map<String, Object> instanceProperties) throws Exception {
		JmxArgs args = new JmxArgs();
		
		if (instanceProperties.get("host") != null) {
			String host = (String) instanceProperties.get("host");
			args.setHost(host);	
		}
		
		if (instanceProperties.get("port") != null) {
			Integer port = (Integer) instanceProperties.get("port");
			args.setPort(port);
		}
		
		if (instanceProperties.get("login") != null) {
			Boolean login = (Boolean) instanceProperties.get("login");
			args.setLogin(login);	
		}
		
		if (instanceProperties.get("username") != null) {
			String username = (String) instanceProperties.get("username");
			args.setUsername(username);	
		}
		
		if (instanceProperties.get("password") != null) {
			String password = (String) instanceProperties.get("password");
			args.setPassword(password);	
		}

		return args;
	}
	
	@Override
	protected void validate(JmxArgs parsedArgs) throws Exception {
		
		if (parsedArgs.getHost() == null) {
			throw new Exception("JMX host parameter must be specified");
		} 
		
	}

	@Override
	protected void init(JmxArgs parsedArgs) throws Exception {

		JmxConnectionInfo jmxConnectionConfig = new JmxConnectionInfo();
		jmxConnectionConfig.setName("");
		jmxConnectionConfig.setHost(parsedArgs.getHost());
		jmxConnectionConfig.setPort(parsedArgs.getPort());
		jmxConnectionConfig.setLogin(parsedArgs.getLogin());
		jmxConnectionConfig.setUser(parsedArgs.getUsername());
		jmxConnectionConfig.setPassword(parsedArgs.getPassword());
		this.connectionConfig  = jmxConnectionConfig;
		

	}
	
	public JMXConnector connect() throws IOException {
		JMXConnector jmxConnector = null;
		String jmxURL = "service:jmx:rmi:///jndi/rmi://" + connectionConfig.getHost() + ":" + connectionConfig.getPort()
				+ "/jmxrmi";
		
		logger.debug("Calling pollCycle for url " + jmxURL);
		JMXServiceURL url = new JMXServiceURL(jmxURL);
		Map<String, Object> env = null;
		boolean login = connectionConfig.isLogin();
		if (login == true) {
			env = new HashMap<String, Object>();
			String[] creds = { connectionConfig.getUser(), connectionConfig.getPassword() };
			logger.info("Setting user to " + connectionConfig.getUser());
			// logger.info("Setting password to " + serverConfig.getPassword());
			env.put(JMXConnector.CREDENTIALS, creds);
		} else {
			//logger.info("No credentials set");
			env = null;
		}
		jmxConnector = JMXConnectorFactory.connect(url, env);
		return jmxConnector;
	}
	
	public void disconnect(JMXConnector jmxConnector) {
		if (jmxConnector != null) {
			try {
				jmxConnector.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void poll() throws Exception {
		System.out.println("Polling " + connectionConfig.getHost());
		
		JMXConnector jmxConnector = connect();
		
		mbeanServerConnection = jmxConnector.getMBeanServerConnection();
		Set<ObjectName> objNames = mbeanServerConnection.queryNames(null, null);
		for (Iterator<ObjectName> iterator = objNames.iterator(); iterator.hasNext();) {
			ObjectName objectName = (ObjectName) iterator.next();
			System.out.print(objectName.getDomain() + " :  ");
			System.out.println(objectName.getKeyPropertyListString());
		}
		MetricSet metricset = newMetricSet("JMXMetric");
		metricset.setAttribute("Domain", "WhatDomain");
		metricset.setMetric("mymetric", 9.0, SourceType.GAUGE);
		
		disconnect(jmxConnector);
		

		
	}
	
	@Override
	public void listen() throws Exception {

	}

	@Override
	public void cleanup() {
		logger.info("shutting down.");
	}

}
