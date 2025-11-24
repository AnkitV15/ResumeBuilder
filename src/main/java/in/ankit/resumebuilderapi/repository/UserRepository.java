package in.ankit.resumebuilderapi.repository;

import in.ankit.resumebuilderapi.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String s);

    Optional<User> findByVerificationToken(String verificationToken);
}
