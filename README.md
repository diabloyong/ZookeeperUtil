# ZookeeperUtil
该工具类主要是封装原zookeeper基本操作
	/**
	 * 创建一个临时性且有序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createTempOrderNode(String nodeUrl,String data)
	/**
	 * 创建一个临时性且无序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createTempNode(String nodeUrl,String data)
	/**
	 * 创建一个永久性且有序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createOrderNode(String nodeUrl,String data)
	/**
	 * 创建一个永久性无序的 节点
	 * @param nodeUrl 节点url
	 * @param data 节点存储的数据
	 * @return
	 */
	public boolean createNode(String nodeUrl,String data)
	/**
	 * 
	 * @param nodeUrl 节点url
	 * @param data 节点url
	 * @param version 版本
	 * @return
	 */
	public boolean updata(String nodeUrl,String data,int version)
	/**
	 * 
	 * @param nodeUrl 节点url
	 * @param version 版本
	 * @return
	 */
	public boolean delete(String nodeUrl,int version)
	/**
	 * 获取节点数据
	 * @param nodeUrl
	 * @return
	 */
	public String getNodeDate(String nodeUrl)

