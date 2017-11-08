package hello;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
@EnableTransactionManagement
public class JmsConfig {
	@Value("${project.mq.host}")
	private String host;
	@Value("${project.mq.port}")
	private Integer port;
	@Value("${project.mq.queue-manager}")
	private String queueManager;
	@Value("${project.mq.channel}")
	private String channel;
	@Value("${project.mq.username}")
	private String username;
	@Value("${project.mq.password}")
	private String password;
	@Value("${project.mq.queue}")
	private String queue;
	@Value("${project.mq.receive-timeout}")
	private long receiveTimeout;

	@Bean
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setHostName(host);
		try {
			mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			mqQueueConnectionFactory.setCCSID(1208);
			mqQueueConnectionFactory.setChannel(channel);
			mqQueueConnectionFactory.setPort(port);
			mqQueueConnectionFactory.setQueueManager(queueManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mqQueueConnectionFactory;
	}

	@Bean
	UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(
			MQQueueConnectionFactory mqQueueConnectionFactory) {
		UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
		userCredentialsConnectionFactoryAdapter.setUsername(username);
		userCredentialsConnectionFactoryAdapter.setPassword(password);
		userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
		return userCredentialsConnectionFactoryAdapter;
	}

	@Bean
	@Primary
	public CachingConnectionFactory cachingConnectionFactory(
			UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
		cachingConnectionFactory.setSessionCacheSize(500);
		cachingConnectionFactory.setReconnectOnException(true);
		return cachingConnectionFactory;
	}

	@Bean
	public PlatformTransactionManager jmsTransactionManager(CachingConnectionFactory cachingConnectionFactory) {
		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
		jmsTransactionManager.setConnectionFactory(cachingConnectionFactory);
		return jmsTransactionManager;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	@Bean
	@Primary
	public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) throws JMSException {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		jmsTemplate.setReceiveTimeout(receiveTimeout);
		return jmsTemplate;
	}
}
