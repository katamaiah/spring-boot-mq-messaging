package hello;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.entity.ErrorMessageEntity;

//Spring data jpa repo

@Repository
@EnableJpaRepositories(basePackageClasses= {ErrorMessageEntity.class})
public interface ErrorMessageRepository extends CrudRepository<ErrorMessageEntity, Long> {



}
