package com.iot.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.authentication.JwtTokenProvider;
import com.iot.dao.IDeviceDao;
import com.iot.entity.DeviceEntity;
import com.iot.mqtt.AuthResponse;
import com.iot.mqtt.CollectDataModel;
import com.iot.mqtt.AuthRequest;
import com.iot.payloads.JwtAuthRequest;
import com.iot.payloads.JwtAuthResponse;
import com.iot.service.ISensorService;

@Configuration
@EnableAsync
@EnableScheduling
public class MQTTConfig {
	private static final Logger logger = Logger.getLogger(MQTTConfig.class);
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private IDeviceDao deviceDao;
	@Autowired
	private ISensorService sensorService;

	private static Set<Long> activeDevices = new HashSet<Long>();
	private static Set<Long> tmpActiveDevices = new HashSet<Long>();

	/*
	 * initialDelay: Thời gian task bắt đầu chạy(180s) fixedDelay: thời gian delay
	 * sau mỗi lần chạy hoàn thành rồi lặp lại(60s)
	 */
	@Async
	@Scheduled(initialDelay = 60 * 1000, fixedDelay = 180 * 1000)
	public void doSomething() {
		tmpActiveDevices.addAll(activeDevices);
		activeDevices.clear();
		System.out.println("KeepAlive: " + tmpActiveDevices);
		deviceDao.updateKeepAlive(new ArrayList<Long>(tmpActiveDevices));
		tmpActiveDevices.clear();
	}

	/*
	 * // Of course , you can define the Executor too
	 * 
	 * @Bean public Executor taskExecutor() { return new SimpleAsyncTaskExecutor();
	 * }
	 * 
	 * @Bean public TaskScheduler taskScheduler() { return new
	 * ConcurrentTaskScheduler(); }
	 */
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		return executor;
	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		return scheduler;
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(new String[] { "tcp://broker.hivemq.com:1883" });
		factory.setUserName("username");
		factory.setPassword("password");
		factory.setCleanSession(true);
		factory.setKeepAliveInterval(10);
		return factory;
	}

	// subscriber
	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("clientId1",
				mqttClientFactory(), "iot/authentication");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(outbount());
		adapter.setAutoStartup(true);
		adapter.setTaskScheduler(taskScheduler()); // cái hay nhất là ở đây vì khi chạy mqtt subscriber cần tạo 1 task
													// để chạy nhé !!!
		adapter.start();
		return adapter;
	}
	
	@Bean
	public MessageProducer inbound2() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("clientId2",
				mqttClientFactory(), "iot/collectdata");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(outbount());
		adapter.setAutoStartup(true);
		adapter.setTaskScheduler(taskScheduler()); // cái hay nhất là ở đây vì khi chạy mqtt subscriber cần tạo 1 task
													// để chạy nhé !!!
		adapter.start();
		return adapter;
	}

	// subscriber
	@Bean
	public MessageProducer inbound3() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("clientId3",
				mqttClientFactory(),"iot/getTokenCollectData");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(outbount());
		adapter.setAutoStartup(true);
		adapter.setTaskScheduler(taskScheduler()); // cái hay nhất là ở đây vì khi chạy mqtt subscriber cần tạo 1 task
													// để chạy nhé !!!
		adapter.start();
		return adapter;
	}
	
	// subscriber
		@Bean
		public MessageProducer inbound4() {
			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("clientId4",
					mqttClientFactory(), "iot/keepAlive");
			adapter.setCompletionTimeout(5000);
			adapter.setConverter(new DefaultPahoMessageConverter());
			adapter.setQos(2);
			adapter.setOutputChannel(outbount());
			adapter.setAutoStartup(true);
			adapter.setTaskScheduler(taskScheduler()); // cái hay nhất là ở đây vì khi chạy mqtt subscriber cần tạo 1 task
														// để chạy nhé !!!
			adapter.start();
			return adapter;
		}

	// callback subscribe
	@Bean
	public PublishSubscribeChannel outbount() {
		PublishSubscribeChannel psc = new PublishSubscribeChannel();
		psc.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String received_topic = (String) message.getHeaders().get(MqttHeaders.TOPIC);
				// Xử lý topic nhận được ở đây
				System.out.println(
						"***Headers:" + message.getHeaders()+" Message: " + message.getPayload());
				if (received_topic.equals("iot/authentication")) {
					// bao gồm user_token(Người tạo device) và deviceId;
					Long deviceID = 0l;
					try {
						AuthRequest authModel = new ObjectMapper().readValue(message.getPayload().toString(),
								AuthRequest.class);
						deviceID = authModel.getDeviceId();
						if (StringUtils.isNotBlank(authModel.getToken())
								&& tokenProvider.validateJwtToken(authModel.getToken())) {
							List<String> tmp = tokenProvider.parseTokenAuthenActiveData(authModel.getToken());
							// Sau khi xác thực thì ta sẽ chuyển alive của device=1 tức là nó sẵn sàng
							if (authModel.getDeviceId() == Long.parseLong(tmp.get(1))) {
								DeviceEntity device = deviceDao.findByIdAndUsername(tmp.get(0),
										authModel.getDeviceId());
								if (device != null) {
									String topic_result = "iot/authentication_result_" + device.getId();
									String jwt = tokenProvider.generateTokenCollectData(device.getUserEntity().getId(),
											device.getId());
									device.setAlive(1);
									device.setToken_collect_data(jwt);
									device.setUpdated_at(new Date());
									if (deviceDao.update(device) != null) {
										// nếu xác thực thành công
										System.out.println(device.getToken_collect_data());
										AuthResponse data = new AuthResponse();
										data.setToken(jwt);
										data.setDeviceId(device.getId());
										data.setReply("true");
										String jsonData = new ObjectMapper().writeValueAsString(data);
										Message<String> messageauth = MessageBuilder.withPayload(jsonData)
												.setHeader(MqttHeaders.TOPIC, topic_result).build();
										mqqtMessageHandler().handleMessage(messageauth);
										return;
									}

								}
							}
						}

					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					String topic_result = "iot/authentication_result_" + deviceID;
					String data = "false";
					Message<String> messageauth = MessageBuilder.withPayload(data)
							.setHeader(MqttHeaders.TOPIC, topic_result).build();
					mqqtMessageHandler().handleMessage(messageauth);
					return;
				}
				if (received_topic.equals("iot/keepAlive")) {
					try {
						AuthRequest authModel = new ObjectMapper().readValue(message.getPayload().toString(),
								AuthRequest.class);
						if (StringUtils.isNotBlank(authModel.getToken())
								&& tokenProvider.validateJwtToken(authModel.getToken())) {
							List<String> tmp = tokenProvider.parseTokenAuthenActiveData(authModel.getToken());
							// Sau khi xác thực thì ta sẽ chuyển alive của device=1 tức là nó sẵn sàng
							if (authModel.getDeviceId() == Long.parseLong(tmp.get(1))) {
								DeviceEntity device = deviceDao.findByIdAndUsername(tmp.get(0),
										authModel.getDeviceId());
								if (device != null) {
									if (device.getToken_auth().equals(authModel.getToken())) {
										activeDevices.add(device.getId());
									}
								}
							}
						}
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					return;
				}
				if (received_topic.equals("iot/collectdata")) {
					try {
						CollectDataModel collectData = new ObjectMapper().readValue(message.getPayload().toString(),
								CollectDataModel.class);

						if (StringUtils.isNotBlank(collectData.getToken())
								&& tokenProvider.validateJwtToken(collectData.getToken())) {

							List<Long> ids = tokenProvider.parseTokenCollectData(collectData.getToken());
							if (ids != null && ids.size() == 2) {
								DeviceEntity device = deviceDao.findByIdAndUserID(ids.get(0), ids.get(1));
								if (device != null && device.getAlive() == 1
										&& device.getId() == collectData.getDeviceId()
										&& device.getToken_collect_data().equals(collectData.getToken())) {
									sensorService.saveCollectData(collectData);
								}
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					return;
				}
				if (received_topic.equals("iot/getTokenCollectData")) {
					Long idtmp = 0l;
					try {
						AuthRequest getToken = new ObjectMapper().readValue(message.getPayload().toString(),
								AuthRequest.class);
						if (StringUtils.isNotBlank(getToken.getToken()) && getToken.getActive().equals("true")) {
							idtmp = getToken.getDeviceId();
							DeviceEntity device = deviceDao.findByIdUser(idtmp);
							if (device != null && device.getId() == getToken.getDeviceId()
									&& device.getToken_collect_data().equals(getToken.getToken())) {
								// generate token collect data mới cho user:
								AuthResponse res = new AuthResponse();
								res.setDeviceId(device.getId());
								res.setReply("true");
								String tokenCollect = tokenProvider
										.generateTokenCollectData(device.getUserEntity().getId(), device.getId());
								// Cập nhật lại trong db
								device.setToken_collect_data(tokenCollect);
								device.setAlive(1);
								device.setUpdated_at(new Date());
								System.out.println(tokenCollect);
								
								deviceDao.update(device);

								res.setToken(tokenCollect);
								String topic_result = "iot/getTokenCollectData_result_" + device.getId();
								String jsonData = new ObjectMapper().writeValueAsString(res);
								Message<String> messageauth = MessageBuilder.withPayload(jsonData)
										.setHeader(MqttHeaders.TOPIC, topic_result).build();
								mqqtMessageHandler().handleMessage(messageauth);
								return;
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					try {
						AuthResponse res = new AuthResponse();
						res.setReply("false");
						String topic_result = "iot/getTokenCollectData_result_" + idtmp;
						String jsonData;
						jsonData = new ObjectMapper().writeValueAsString(res);
						Message<String> messageauth = MessageBuilder.withPayload(jsonData)
								.setHeader(MqttHeaders.TOPIC, topic_result).build();
						mqqtMessageHandler().handleMessage(messageauth);
					} catch (JsonProcessingException e) {
					}
					return;
				}
			}
		});
		return psc;
	}

	// publisher
	@Bean
	public MqttPahoMessageHandler mqqtMessageHandler() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("clientId", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("demo01");
		return messageHandler;
	}

}
