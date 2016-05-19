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
	                    	nodeListener(); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
	                    }
	                }
	            });
	            for (String node : nodeList) {
	            	 // 获取 nodeUrl子节点中的数据
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
