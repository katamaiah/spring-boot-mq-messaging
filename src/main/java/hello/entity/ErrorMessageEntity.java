package hello.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ErrorMessageEntity {
	
	 @Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private Long id;

    private String to;
    private String body;

    public ErrorMessageEntity() {
    }

    public ErrorMessageEntity(String to, String body) {
        this.to = to;
        this.body = body;
    }
    
    public Long getId() {
		return id;
	}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("Email{to=%s, body=%s}", getTo(), getBody());
    }

}
