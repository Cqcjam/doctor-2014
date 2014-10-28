package com.doctor.kafkajstrom.bolt;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.doctor.kafkajstrom.log.manager.LogManager;
import com.doctor.kafkajstrom.log.manager.imp.LogManagerImp;
import com.doctor.kafkajstrom.util.SpringUtil;

public class KafkaBolt extends BaseRichBolt {
	private static final Logger LOG = LoggerFactory.getLogger(KafkaBolt.class);

	private static final long serialVersionUID = 1L;
	private String classPathConfigLocation;

	LogManager logManagerImp;
	private ApplicationContext applicationContext = null;

	public KafkaBolt(String classPathConfigLocation) {
		this.classPathConfigLocation = classPathConfigLocation;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// 多线程启动，只需初始化一次
		synchronized (this) {
			if (applicationContext == null) {

				applicationContext = SpringUtil.of(classPathConfigLocation);
				logManagerImp = applicationContext.getBean(LogManagerImp.class);
				LOG.info("----------------------------------------KafkaBolt prepare");
			}
		}

	}

	@Override
	public void execute(Tuple input) {
		byte[] byteByField = input.getBinaryByField("bytes");
		String message = new String(byteByField, StandardCharsets.UTF_8);

		logManagerImp.write(message);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

}
