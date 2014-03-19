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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.common.model.BaseEntity;
import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelAspectTest extends AbstractSystemTransactionalTest {


	@Autowired
	private ModelAspect modelAspect;

	@Test
	public void testModelAspect() {
		JoinPoint joinPoint = mock(JoinPoint.class);
		@SuppressWarnings("unchecked")
		BaseEntity<Object> baseModel = mock(BaseEntity.class);
		when(joinPoint.getArgs()).thenReturn(new Object[] { baseModel });
		modelAspect.beforeSave(joinPoint);
		verify(baseModel, times(1)).setCreatedDate(any(Date.class));
		verify(baseModel, times(1)).setLastModifiedDate(any(Date.class));
	}

}
