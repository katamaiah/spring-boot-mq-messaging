package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import hello.domain.ErrorMessage;

@Component
public class Receiver {
	
	
	@Autowired
	ErrorMessageRepository repo;
	
	@Autowired
	ErrorMessageConverter emc;

	@JmsListener(destination = "TESTQUEUE0", containerFactory = "myFactory")
	public void receiveMessage(ErrorMessage e){
		  System.out.println("received message from MQ:"+e.getTo()+":"+e.getBody());
		  
		
		  System.out.println("store error message to db:"+repo.save(emc.convertDomaintoEntity(e)));
		  
		  System.out.println("fetching all error messages:"+ repo.findAll());
		  
		}
	
	
	
	
}
