/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.mtt.myapp.user.controller;


import static org.fest.assertions.Assertions.assertThat;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.user.Controller.UserController;
import com.mtt.myapp.user.Controller.UserSearchForm;
import com.mtt.myapp.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

public class UserControllerTest extends AbstractSystemTransactionalTest {

	@Autowired
	private UserController userController;

	@Test
	public void testGetAll() {
		Pageable page = new PageRequest(1, 10);
		Model model = new ExtendedModelMap();
		userController.list(page, model);

		model = new ExtendedModelMap();
		userController.list(page, model);

		model = new ExtendedModelMap();
		UserSearchForm form = new UserSearchForm();
		form.setName("searchName");
		userController.search(form, page, model);
	}

	@Test
	public void testSearch() {
		Pageable page = new PageRequest(1, 10);
		Model model = new ExtendedModelMap();
		UserSearchForm form = new UserSearchForm();
		form.setName("searchName");
		userController.search(form, page, model);
	}

	@Test
	public void testGetOne() {
		ModelMap model = new ModelMap();
	}

	@Test
	public void testSave() {
		// test update
		ModelMap model = new ModelMap();
	}

	@Test
	public void testUpdate() {
		// test update the role of current user.
		ModelMap model = new ModelMap();
	}

	private void saveTestUser(String userId, String userName) {
		User newUser = new User();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDelete() {
		// save new user for test
		saveTestUser("NewUserId1", "NewUserName1");
		saveTestUser("NewUserId2", "NewUserName2");
		saveTestUser("NewUserId3", "NewUserName3");

		Pageable page = new PageRequest(0, 10);
		Model model = new ExtendedModelMap();

		// search
		UserSearchForm form = new UserSearchForm();
		form.setName("NewUserName");
		userController.search(form, page, model);
		Page userList = (Page<User>) model.asMap().get("users");
		assertThat(userList.getContent().size()).isSameAs(3);

		// test to delete one
		userController.delete("NewUserId1");
		model = new ExtendedModelMap();
		userController.search(form, page, model);
		userList = (Page<User>) model.asMap().get("users");
		assertThat(userList.getContent().size()).isSameAs(2);

		// test to delete more
		model = new ExtendedModelMap();
		userController.delete("NewUserId2,NewUserId3");
		userController.search(form, page, model);
		userList = (Page<User>) model.asMap().get("users");
		assertThat(userList.getContent().size()).isSameAs(0);
	}

	@Test
	public void testDuplication() {
		HttpEntity<String> rtnStr = userController.checkUserExist("not-exist");
		assertThat(rtnStr.getBody()).isEqualTo(userController.returnSuccess());

		rtnStr = userController.checkUserExist(getTestUser().getUserId());
		assertThat(rtnStr.getBody()).isEqualTo(userController.returnError());
	}

}
