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
package com.mtt.myapp.common.util;


import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mtt.myapp.common.controller.BaseController;
import org.junit.Test;

/**
 * Class description.
 *
 * @author Mavlarn
 * @since
 */
public class JSONUtilTest extends BaseController {

	JsonParser parser = new JsonParser();

	@Test
	public void testReturnSuccessString() {
		String rtnStr = returnSuccess("return message");
		JsonObject json = (JsonObject)parser.parse(rtnStr);
		assertThat(json.get(JSON_SUCCESS).getAsBoolean()).isTrue();
		assertThat(json.get(JSON_MESSAGE).getAsString()).contains("return message");
	}

	@Test
	public void testReturnErrorString() {
		String rtnStr = returnError("return message");
		JsonObject json = (JsonObject)parser.parse(rtnStr);
		assertThat(!json.get(JSON_SUCCESS).getAsBoolean()).isTrue();
		assertThat(json.get(JSON_MESSAGE).getAsString()).contains("return message");
	}

	@Test
	public void testReturnSuccess() {
		String rtnStr = returnSuccess();
		JsonObject json = (JsonObject)parser.parse(rtnStr);
		assertThat(json.get(JSON_SUCCESS).getAsBoolean()).isTrue();
	}

	@Test
	public void testReturnError() {
		String rtnStr = returnError();
		JsonObject json = (JsonObject)parser.parse(rtnStr);
		assertThat(!json.get(JSON_SUCCESS).getAsBoolean()).isTrue();
	}

	@Test
	public void testToJsonListOfQ() {
		List<Integer> intList = new ArrayList<Integer>();
		intList.add(1);
		intList.add(2);
		intList.add(3);
		toJson(intList);
	}

	@Test
	public void testToJsonMapOfStringObject() {
		Map<String, Object> intMap = new HashMap<String, Object>();
		intMap.put("kay1", 1);
		intMap.put("kay2", 2);
		JsonObject json = (JsonObject)parser.parse(toJson(intMap));
		assertThat(json.get("kay1").getAsInt()).isSameAs(1);
		assertThat(json.get("kay2").getAsInt()).isSameAs(1);
	}

}
