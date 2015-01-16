package com.doctor.elasticsearch_1_4_2;

import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
/**
 * 模拟集群，本地jvm
 * @author doctor
 *
 * @time   2015年1月16日 下午3:31:52
 */

public class ClientPractice {

	public static void main(String[] args) throws InterruptedException {
		Node node1 = NodeBuilder.nodeBuilder().clusterName("embedded-1").client(true).local(true).node();//启动
		Node node2 = NodeBuilder.nodeBuilder().clusterName("embedded-2").client(true).local(true).node();//启动
		
		Client client = node1.client();
		Client client2 = node2.client();
		
		
		TimeUnit.SECONDS.sleep(3);
		node1.close();
		node2.close();
	}

}
