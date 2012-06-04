package org.jage.examples.resource_meter;

import java.io.FileWriter;
import java.io.IOException;

public class GephiConnector {

	private static FileWriter gephi;
	private static int nodeCounter = 0;
	private static int edgeCounter = 0;
	private static StringBuilder nodes = new StringBuilder();
	private static StringBuilder edges = new StringBuilder();

	static {
		try {
			gephi = new FileWriter("gephi.gexf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void intro() {
		try {
			gephi.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<gexf xmlns:viz=\"http:///www.gexf.net/1.1draft/viz\" version=\"1.1\" xmlns=\"http://www.gexf.net/1.1draft\">"
					+ "<meta lastmodifieddate=\"2010-03-03+23:44\">"
					+ "<creator>Gephi 0.7</creator>"
					+ "</meta>"
					+ "<graph defaultedgetype=\"directed\" idtype=\"string\" type=\"static\">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void outro() {
		try {
			gephi.write("</graph> </gexf>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void addNode(String id, String name) {
		nodes.append("<node id=\"" + id + "\" label=\"" + name + "\"/>\n");
		nodeCounter++;
	}

	public static synchronized void addEdge(String sourceId, String targetId) {

		try {
			if (edgeCounter == 2) {
				intro();
				gephi.write("<nodes count=\"" + nodeCounter + "\">");
				gephi.write(nodes.toString());
				gephi.write("</nodes>");
				gephi.write("<edges count=\"" + edgeCounter + "\">");
				gephi.write(edges.toString());
				gephi.write("</edges>");
				outro();
				gephi.close();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		edges.append("<edge id=\"" + edgeCounter + "\" source=\"" + sourceId
				+ "\" target=\"" + targetId + "\" weight=\"2.0\"/>\n");
		edgeCounter++;
		System.out.println(edgeCounter);

	}

}
