package hello;

import org.springframework.stereotype.Component;

import hello.domain.ErrorMessage;
import hello.entity.ErrorMessageEntity;

@Component
public class ErrorMessageConverter {

	ErrorMessageEntity convertDomaintoEntity(ErrorMessage e) {

		return new ErrorMessageEntity(e.getTo(), e.getBody());

	}

	ErrorMessage convertDomaintoEntity(ErrorMessageEntity ee) {

		return new ErrorMessage(ee.getTo(), ee.getBody());

	}
}
