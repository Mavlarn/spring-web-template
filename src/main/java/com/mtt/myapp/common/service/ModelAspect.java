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
package com.mtt.myapp.common.service;

import java.util.Date;

import com.mtt.myapp.common.model.BaseEntity;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.service.UserContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Aspect to inject the created/modified user and date to the model.
 *
 * @author Mavlarn
 * @since 1.0
 */
@Aspect
@Service
public class ModelAspect {

	public static final String EXECUTION_SAVE = "execution(* org.ngrinder.**.*Service.save*(..))";

	@Autowired
	private UserContext userContext;


	/**
	 * Inject the created/modified user and date to the model. It's only applied
	 * in the servlet context.
	 *
	 * @param joinPoint joint point
	 */
	@Before(EXECUTION_SAVE)
	public void beforeSave(JoinPoint joinPoint) {
		for (Object object : joinPoint.getArgs()) {

			if (object instanceof BaseEntity) {
				BaseEntity<?> model = (BaseEntity<?>) object;
				Date lastModifiedDate = new Date();
				model.setLastModifiedDate(lastModifiedDate);
				User currentUser = userContext.getCurrentUser();
				model.setLastModifiedUser(currentUser);
				if (!model.exist() || model.getCreatedUser() == null) {
					model.setCreatedDate(lastModifiedDate);
					model.setCreatedUser(currentUser);
				}
			}
		}
	}

}
