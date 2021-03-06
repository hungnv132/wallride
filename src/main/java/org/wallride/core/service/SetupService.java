package org.wallride.core.service;

import org.joda.time.LocalDateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wallride.core.domain.Blog;
import org.wallride.core.domain.BlogLanguage;
import org.wallride.core.domain.Setting;
import org.wallride.core.domain.User;
import org.wallride.core.repository.BlogRepository;
import org.wallride.core.repository.SettingRepository;
import org.wallride.core.repository.UserRepository;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class SetupService {

	@Resource
	private UserRepository userRepository;
	@Resource
	private BlogRepository blogRepository;

	@CacheEvict(value = {"users", "blogs"}, allEntries = true)
	public User setup(SetupRequest request) {
		LocalDateTime now = new LocalDateTime();

		User user = new User();
		user.setLoginId(request.getLoginId());

		StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		user.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));

		user.getName().setFirstName(request.getName().getFirstName());
		user.getName().setLastName(request.getName().getLastName());
		user.setEmail(request.getEmail());

		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		user = userRepository.saveAndFlush(user);

		Blog blog = new Blog();
		blog.setCode("default");
		blog.setDefaultLanguage(request.getDefaultLanguage());
		blog.setMediaUrlPrefix(request.getMediaUrlPrefix());
		blog.setMediaPath(request.getMediaPath());
		blog.setCreatedAt(now);
		blog.setCreatedBy(user.toString());
		blog.setUpdatedAt(now);
		blog.setUpdatedBy(user.toString());

		BlogLanguage defaultLanguage = new BlogLanguage();
		defaultLanguage.setBlog(blog);
		defaultLanguage.setLanguage(request.getDefaultLanguage());
		defaultLanguage.setTitle(request.getWebsiteTitle());
		defaultLanguage.setCreatedAt(now);
		defaultLanguage.setCreatedBy(user.toString());
		defaultLanguage.setUpdatedAt(now);
		defaultLanguage.setUpdatedBy(user.toString());

		Set<BlogLanguage> blogLanguages = new HashSet<>();
		blogLanguages.add(defaultLanguage);

		for (String language : request.getLanguages()) {
			BlogLanguage blogLanguage = new BlogLanguage();
			blogLanguage.setBlog(blog);
			blogLanguage.setLanguage(language);
			blogLanguage.setTitle(request.getWebsiteTitle());
			blogLanguage.setCreatedAt(now);
			blogLanguage.setCreatedBy(user.toString());
			blogLanguage.setUpdatedAt(now);
			blogLanguage.setUpdatedBy(user.toString());

			blogLanguages.add(blogLanguage);
		}
		blog.setLanguages(blogLanguages);

		blogRepository.saveAndFlush(blog);

		return user;
	}
}
