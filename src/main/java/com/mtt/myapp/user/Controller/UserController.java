package com.mtt.myapp.user.Controller;

import com.mtt.myapp.common.controller.BaseController;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.service.UserContext;
import com.mtt.myapp.user.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    protected UserService userService;

	@Autowired
	protected UserContext userContext;

    @Autowired
    protected Mapper beanMapper;

    @ModelAttribute
    public User setCurrentUser() {
        return userContext.getCurrentUser();
    }

    // create flow
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm() {
        return "user/createForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(User user, User newUser) {
        userService.save(newUser);
        return "redirect:/user/create?complete";
    }

    // update flow

    @RequestMapping(value = "update", params = "form", method = RequestMethod.GET)
    public String updateForm(User user, String updatedUserId, Model model) {

        User updateUser = userService.findByUserId(updatedUserId);

        model.addAttribute("user", updateUser);
        return "user/updateForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(User user, User updatedUser) {
        userService.update(updatedUser);
        return "redirect:/user/list";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String userIds) {
        userService.deleteSome(userIds);
        return "redirect:/user/list";
    }

	@RequestMapping("list")
	public String list(@PageableDefault Pageable pageable, Model model) {
		Page<User> page = userService.findAll(pageable);
		model.addAttribute("page", page);
		return "user/list";
	}

	@RequestMapping("search")
	public String search(@ModelAttribute UserSearchForm form,
						 @PageableDefault Pageable pageable, Model model) {
		String name = form.getName();
		String query = StringUtils.hasText(name) ? name : "";
		Page<User> page = userService.findByNameLike(query, pageable);
		model.addAttribute("page", page);
		return "user/list";
	}

	/**
	 * Check if the given user id already exists.
	 *
	 * @param userId userId to be checked
	 * @return success json if true.
	 */
	@PreAuthorize("hasAnyRole('A')")
	@RequestMapping("/api/{userId}/check_exist")
	public HttpEntity<String> checkUserExist(@PathVariable String userId) {
		User user = userService.findByUserId(userId);
		return (user == null) ? successJsonHttpEntity() : errorJsonHttpEntity();
	}
}
