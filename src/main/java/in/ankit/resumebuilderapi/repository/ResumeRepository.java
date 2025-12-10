package in.ankit.resumebuilderapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.ankit.resumebuilderapi.document.Resume;

public interface ResumeRepository extends MongoRepository<Resume, String> {

    public List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);

    public Optional<Resume> findByUserIdAndId(String userId, String id);
}
