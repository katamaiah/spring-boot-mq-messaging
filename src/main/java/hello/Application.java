
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsOperations;

import hello.domain.ErrorMessage;

@SpringBootApplication
@EnableJms
public class Application {

	public static void main(String[] args) {
		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		JmsOperations jmsOperations = context.getBean(JmsOperations.class);

		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		jmsOperations.convertAndSend("TESTQUEUE0", new ErrorMessage("xyz", "hello"));

	}

}
