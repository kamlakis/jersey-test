package net.lakis.jersey_test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainerProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import net.lakis.jersey_test.controller.HomeController;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws Exception {

		// app configuration
		int maxPoolSize = 20;
		int corePoolSize = 20;
		int queueLimit = -1;
		int port = 10024;
		int runnersCount = 3;

		// http server
		// final HttpServer server = HttpServer.createSimpleServer("/", port);// port
		HttpServer server = new HttpServer();

		// TCPNIOTransport transport =
		// server.getListeners().iterator().next().getTransport();

		// WebSocketAddOn webSocketAddon = new WebSocketAddOn();

		// for (ServletNetworkListener servletNetworkListener :
		// config.getNetworkListeners()) {
		// if (servletNetworkListener.getPort() == 0) {
		// throw new IllegalStateException("Port parameter can not be equal to zero");
		// }

		NetworkListener listener = new NetworkListener("namelist", "0.0.0.0", new PortRange(port, port));
		// listener.registerAddOn(webSocketAddon);

		server.addListener(listener);

		TCPNIOTransport transport = listener.getTransport();

		/*
		 * queueLimit: The maximum number of pending tasks that may be queued
		 * maxPoolSize : The maximum number threads that may be maintained by this
		 * thread pool corePoolSize : The initial number of threads that will be present
		 * with the thread pool is created
		 */
		ThreadPoolConfig config = ThreadPoolConfig.defaultConfig().setPoolName("worker-thread-")
				.setCorePoolSize(corePoolSize).setMaxPoolSize(maxPoolSize)
				.setQueueLimit(queueLimit)/* same as default */;

		transport.configureBlocking(false);
		transport.setSelectorRunnersCount(runnersCount);
		transport.setWorkerThreadPoolConfig(config);
		transport.setIOStrategy(WorkerThreadIOStrategy.getInstance());
		transport.setTcpNoDelay(true);

		// server configuration
		final ServerConfiguration serverConfig = server.getServerConfiguration();

		// serverConfig.addHttpHandler(new GrizzlyHandler(), "/servlet");
		// serverConfig.addHttpHandler(new CreateTrackerApi(), "/yelli/create");
		serverConfig.setJmxEnabled(true);

		// add JAX-WS
		// HttpHandler httpHandler = new JaxwsHandler(new TestSoap());
		// serverConfig.addHttpHandler(httpHandler, "/soap");

		StaticFileServlet staticFileServlet = new StaticFileServlet();
		staticFileServlet.setRootPath("/Users/kam/web/");
		serverConfig.addHttpHandler(staticFileServlet, "/filed");

		final Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>();
		Class<CheckNumber> clazz = CheckNumber.class;
		Object servlet = clazz.getConstructor().newInstance();
		instances.put(clazz, servlet);
		instances.put(JspMvcFeature.class, new JspMvcFeature());

		// final AbstractBinder binder = new AbstractBinder() {
		// @Override
		// public void configure() {
		// for (Entry<Class<?>, Object> entry : instances.entrySet())
		// bind(entry.getValue()).to(entry.getKey());
		// }
		// };
 		final ResourceConfig rc = new ResourceConfig().register(JacksonFeature.class)
				.property(JspMvcFeature.TEMPLATE_BASE_PATH, "/Users/kam/web/jsp")
				.register(org.glassfish.jersey.server.mvc.jsp.JspMvcFeature.class).register(HomeController.class);
 		// .register(JsonProvider2.class);//.register(binder);// .reg();

		final ResourceConfig rc1 = new AppConfig();

		GrizzlyHttpContainer httpContainer = new GrizzlyHttpContainerProvider()
				.createContainer(GrizzlyHttpContainer.class, rc1);
		serverConfig.addHttpHandler(httpContainer, "/");

		// start server
		server.start();

		while (true)
			Thread.sleep(Long.MAX_VALUE);
	}
}
