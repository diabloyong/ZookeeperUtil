package com.diablo.zk.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author Zoujingyong
 *
 */
public class ZKNodeListener {
	
	private Map<String, String> childrenNodeDate = new HashMap<String, String>();
	private String nodeUrl;
	private ZooKeeper zooKeeper;
	
	public ZKNodeListener(ZooKeeper zooKeeper,String nodeUrl) {
		super();
		this.nodeUrl = nodeUrl;
		this.zooKeeper = zooKeeper;
	}

	public void nodeListener(){
	     try {
	            List<String> nodeList = zooKeeper.getChildren(nodeUrl, new Watcher() {
	                @Override
	                public void process(WatchedEvent event) {
	                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
	                    	nodeListener(); // ���ӽڵ��б仯�������µ��ø÷�����Ϊ�˻�ȡ�����ӽڵ��е����ݣ�
	                    }
	                }
	            });
	            for (String node : nodeList) {
	            	 // ��ȡ nodeUrl�ӽڵ��е�����
	                byte[] data = zooKeeper.getData(nodeUrl + "/" + node, false, null);
	                childrenNodeDate.put(node, new String(data));
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	
	/**
	 * @return the nodeDate
	 */
	public Map<String, String> getNodeDate() {
		return childrenNodeDate;
	}
}
