package org.jage.examples.resource_meter;

import java.io.FileWriter;
import java.io.IOException;

public class GephiConnector {

	private static FileWriter gephi;
	private static int edgeCounter = 0;

	static {
		try {
			gephi = new FileWriter("gephi.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void addNode(String id, String name) {
		try {
			gephi.write("<node id=\"" + id + "\" label=\"" + name + "\"/>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void addEdge(String sourceId, String targetId) {

		try {
			if (edgeCounter > 2) {
				gephi.close();
				return;
			}
			gephi.write("<edge id=\"" + edgeCounter + "\" source=\"" + sourceId
					+ "\" target=\"" + targetId + "\" weight=\"2.0\"/>\n");
			edgeCounter++;
			System.out.println(edgeCounter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
