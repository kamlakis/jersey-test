package net.lakis.jersey_test;

import org.glassfish.grizzly.http.server.StaticHttpHandler;

public class StaticFileServlet extends StaticHttpHandler {

	public void setRootPath(String path)
	{
		super.addDocRoot(path);
	}
}
