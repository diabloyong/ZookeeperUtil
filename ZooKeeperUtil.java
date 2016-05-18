package com.diablo.zk.util;


//import java.util.concurrent.CountDownLatch;

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
	/**�Ƿ����*/
	private boolean encryption = false;
//	private CountDownLatch latch = new CountDownLatch(1);
	
	/**
	 * 
	 * @param zkUrl ����zookeeper��url
	 * @param seeesion_timeout �Ự��ʱʱ��
	 */
	public ZooKeeperUtil(String zkUrl,int seeesion_timeout) {
		super();
		ZK_SESSION_TIMEOUT = seeesion_timeout;
		this.zkUrl = zkUrl;
		init();
	}
	
	/**
	 * �Ƿ�ͨ���û��˺Ż�ȡ����
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
						LOGGER.info("���������ӳɹ�");
						System.out.println("���������ӳɹ�");
					}else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
						LOGGER.info(path + "�ڵ㴴���ɹ�");
						System.out.println(path + "�ڵ㴴���ɹ�");
					}else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
						LOGGER.info(path + "�ӽڵ���³ɹ�");
						System.out.println(path + "�ӽڵ���³ɹ�");
					}else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
						LOGGER.info(path + "�ڵ���³ɹ�");
						System.out.println(path + "�ڵ���³ɹ�");
					}else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
						LOGGER.info(path + "�ڵ�ɾ���ɹ�");
						System.out.println(path + "�ڵ�ɾ���ɹ�");
					}else if (event.getState() == Event.KeeperState.SyncConnected) {
						 // ���ѵ�ǰ����ִ�е��߳�
//                        latch.countDown();
                    }
				}
			});
			// ʹ��ǰ�̴߳��ڵȴ�״̬
//			latch.await(); 
			while(ZooKeeper.States.CONNECTED != zooKeeper.getState()){
				Thread.sleep(500);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("ZooKeeper��ʼ��ʧ��");
		}
	}
	/**
	 * ����һ����ʱ��������� �ڵ�
	 * @param nodeUrl �ڵ�url
	 * @param data �ڵ�洢������
	 * @return
	 */
	public boolean createTempOrderNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL_SEQUENTIAL); 
				}else {
					// ����һ����ʱ��������� ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "�ڵ��Ѵ��ڳɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
        return false;
	}
	
	/**
	 * ����һ����ʱ��������� �ڵ�
	 * @param nodeUrl �ڵ�url
	 * @param data �ڵ�洢������
	 * @return
	 */
	public boolean createTempNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL); 
				}else {
					// ����һ����ʱ��������� ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "�ڵ��Ѵ��ڳɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * ����һ��������������� �ڵ�
	 * @param nodeUrl �ڵ�url
	 * @param data �ڵ�洢������
	 * @return
	 */
	public boolean createOrderNode(String nodeUrl,String data){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT_SEQUENTIAL); 
				}else {
					// ����һ����ʱ��������� ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "�ڵ��Ѵ��ڳɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * ����һ������������� �ڵ�
	 * @param nodeUrl �ڵ�url
	 * @param data �ڵ�洢������
	 * @return
	 */
	public boolean createNode(String nodeUrl,String data){
		
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				byte[] d = data.getBytes();
				if (encryption) {
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT); 
				}else {
					// ����һ����ʱ��������� ZNode
					zooKeeper.create(nodeUrl, d, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); 
				}
				return true;
			}else {
				LOGGER.info(nodeUrl + "�ڵ��Ѵ��ڳɹ�");
				System.out.println(nodeUrl + "�ڵ��Ѵ��ڳɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return false;
	}
	
	/**
	 * 
	 * @param nodeUrl �ڵ�url
	 * @param data �ڵ�url
	 * @param version �汾
	 * @return
	 */
	public boolean updata(String nodeUrl,String data,int version){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				LOGGER.info(nodeUrl + "�ڵ㲻���ڳɹ�");
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
	 * @param nodeUrl �ڵ�url
	 * @param version �汾
	 * @return
	 */
	public boolean delete(String nodeUrl,int version){
		try {
			if (zooKeeper.exists(nodeUrl, true) == null) {
				LOGGER.info(nodeUrl + "�ڵ㲻���ڳɹ�");
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
	 * ��ȡ�ڵ�����
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
