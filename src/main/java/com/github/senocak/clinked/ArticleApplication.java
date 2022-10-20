package com.github.senocak.clinked;

import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.repository.ArticleRepository;
import com.github.senocak.clinked.repository.RoleRepository;
import com.github.senocak.clinked.repository.UserRepository;
import com.github.senocak.clinked.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
// changed
public class ArticleApplication implements CommandLineRunner {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("EXECUTING : command line runner");

		Role role1 = Role.builder().name(AppConstants.RoleName.ROLE_ADMIN).build();
		Role role2 = Role.builder().name(AppConstants.RoleName.ROLE_USER).build();
		roleRepository.save(role1);
		roleRepository.save(role2);

		User user1 = User.builder()
				.name("anil")
				.username("anil1")
				.email("anil1@senocak.com")
				.roles(Collections.singleton(role1))
				.password(passwordEncoder.encode("anil1"))
				.build();
		userRepository.save(user1);

		User user2 = User.builder()
				.name("anil")
				.username("anil2")
				.email("anil2@senocak.com")
				.roles(Collections.singleton(role2))
				.password(passwordEncoder.encode("anil2"))
				.build();
		userRepository.save(user2);

		Article article1 = Article.builder()
				.id(UUID.randomUUID().toString())
				.title("Article Title 1")
				.slug(AppConstants.toSlug("Article Title 1"))
				.content("Article Content 1")
				.author("Article Author 1")
				.publish("2010-01-01T12:00:00+01:00")
				.user(user1)
				.build();
		articleRepository.save(article1);
	}
}
