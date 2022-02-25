package make.web.member.repository;

import make.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Member findByPhone(String phone);

    Member findByNameAndPhone(String name, String phone);

    Member findByNameAndEmail(String name, String email);
}
