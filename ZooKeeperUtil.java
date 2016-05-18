package com.diablo.zk.util;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Zoujingyong
 *
 */
public class ZooKeeperUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperUtil.class);
	private int ZK_SESSION_TIMEOUT = 3000;
	private String zkUrl;
	private ZooKeeper zooKeeper;
	/**是否加密*/
	private boolean encryption = false;
//	private CountDownLatch latch = new CountDownLatch(1);
	
	/**
	 * 
	 * @param zkUrl 连接zookeeper的url
	 * @param seeesion_timeout 会话超时时间
	 */
	public ZooKeeperUtil(String zkUrl,int seeesion_timeout) {
		super();
		ZK_SESSION_TIMEOUT = seeesion_timeout;
		this.zkUrl = zkUrl;
		init();
	}
	
	/**
	 * 是否通过用户账号获取数据
	 * @param connectUserName
	 * @param connectPassword
	 */
	public void isEncryption(String connectUserName,String connectPassword){
		zooKeeper.addAuthInfo(connectUserName, connectPassword.getBytes());
		encryption = true;
	}
	
	public ZooKeeperUtil(String zkUrl) {
		super();
		this.zkUrl = zkUrl;
		init();
	}

	private  void init(){
		try {
			zooKeeper = new ZooKeeper(zkUrl, ZK_SESSION_TIMEOUT, new Watcher(){
				@Override
				public void process(WatchedEvent event) {	
					String path = event.getPath();
					if (event.getType() == Watcher.Event.EventType.None) {
						LOGGER.info("服务器连接成功");
					}else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
						LOGGER.info(path + "节点创建成功");
					}else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
						LOGGER.info(path + "子节点更新成功");
					}else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
						LOGGER.info(path + "节点更新成功");
					}else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
						LOGGER.info(path + "节点删除成功");
					}
				}
			});
			while(ZooKeeper.States.CONNECTED != zooKeeper.getState()){
				Thread.sleep(500);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("ZooKeeper初始化失败");
		}
	}
	/**
	 * 创建一个临时性且有序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createTempOrderNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL_SEQUENTIAL); 
				}else {
					// 创建一个临时性且有序的 ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "节点已存在成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
        return false;
	}
	
	/**
	 * 创建一个临时性且无序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createTempNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL); 
				}else {
					// 创建一个临时性且有序的 ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "节点已存在成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * 创建一个永久性且有序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createOrderNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT_SEQUENTIAL); 
				}else {
					// 创建一个临时性且有序的 ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "节点已存在成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * 创建一个永久性无序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createNode(String nodeUrl,String data){
		
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT); 
				}else {
					// 创建一个临时性且有序的 ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "节点已存在成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * 
	 * @param nodeUrl 节点url
	 * @param data 节点url
	 * @param version 版本
	 * @return
	 */
	public boolean updata(String nodeUrl,String data,int version){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				LOGGER.info(nodeUrl + "节点不存在成功");
				return false;
			}else {
				zooKeeper.setData(nodeUrl, data.getBytes(), version);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return true;
	}
	
	/**
	 * 
	 * @param nodeUrl 节点url
	 * @param version 版本
	 * @return
	 */
	public boolean delete(String nodeUrl,int version){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				LOGGER.info(nodeUrl + "节点不存在成功");
				return false;
			}else {
				zooKeeper.delete(nodeUrl, version);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 获取节点数据
	 * @param nodeUrl
	 * @return
	 */
	public String getNodeDate(String nodeUrl){
		String value = null;
		try {
			value = new String(zooKeeper.getData(nodeUrl, false, null));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return value;
	}
	
	public void close(){
		try {
			zooKeeper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the zooKeeper
	 */
	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}
}
