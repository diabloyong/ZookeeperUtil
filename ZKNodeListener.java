package com.diablo.zk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;


/**
 * @author Zoujingyong
 *
 */
public class ZKNodeListener {
	
	private ZooKeeperUtil util;
	private String nodeUrl;
	private volatile Map<String, String> values = new HashMap<String, String>();
	
	public ZKNodeListener(ZooKeeperUtil util, String nodeUrl) {
		super();
		this.util = util;
		this.nodeUrl = nodeUrl;
		watchNode();
	}
	
	private void watchNode() {
	        try {
	            List<String> nodeList = util.getZooKeeper().getChildren(nodeUrl, new Watcher() {
	                @Override
	                public void process(WatchedEvent event) {
	                	//子节点改变
	                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
	                        watchNode(); 
	                    }
	                }
	            });
	            List<String> dataList = new ArrayList<String>();
	            Map<String, String> value = new HashMap<String, String>();
	            for (String node : nodeList) {
	            	 // nodeUrl的子节点中的数据
	                String data = util.getNodeDate(nodeUrl + "/" + node);
	                dataList.add(data);
	                value.put(node, data);
	            }
	            values = value;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	   }
	
	/**
	 * @return the values
	 */
	public Map<String, String> getValues() {
		return values;
	}
	
}
