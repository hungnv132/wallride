package org.wallride.web.controller.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wallride.core.domain.User;
import org.wallride.core.service.UserService;
import org.wallride.web.support.DomainObjectSelectModel;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserSelectController {

	@Inject
	private UserService userService;

	@RequestMapping(value="/{language}/users/select")
	public @ResponseBody List<DomainObjectSelectModel> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		UserSearchForm form = new UserSearchForm();
		form.setKeyword(keyword);
		Page<User> users = userService.readUsers(form.toUserSearchRequest());

		List<DomainObjectSelectModel> results = new ArrayList<>();
		if (users.hasContent()) {
			for (User user : users) {
				DomainObjectSelectModel model = new DomainObjectSelectModel(user.getId(), user.toString());
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/users/select/{id}", method= RequestMethod.GET)
	public @ResponseBody DomainObjectSelectModel select(
			@PathVariable String language,
			@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		User user = userService.readUserById(id);
		if (user == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelectModel model = new DomainObjectSelectModel(user.getId(), user.toString());
		return model;
	}
}
