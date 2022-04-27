package com.newrelic.labs.jmx.agent;

import com.beust.jcommander.Parameter;
import com.newrelic.labs.infra.agent.Arguments;

public class JmxArgs extends Arguments {

	@Parameter(names = "-host", description = "Host name of the JMX Server")
	private String host = "127.0.0.1";
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Parameter(names = "-port", description = "Port number")
	private Integer port = 1414;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	@Parameter(names = "-login", description = "is Login required")
	private Boolean login = false;

	public Boolean getLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}

	@Parameter(names = "-username", description = "Username of JMX Server")
	private String username = "";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Parameter(names = "-password", description = "Password of JMX Server", password = true)
	private String password = "";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
