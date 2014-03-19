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

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Class description.
 *
 * @author Mavlarn
 * @since 1.0
 */
public class FileDownloadUtilsTest {

	@Test
	public void testDownloadFileHttpServletResponseString() throws IOException {
		File downFile = new ClassPathResource("TEST_USER.zip").getFile();
		String filePath = downFile.getAbsolutePath();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		FileDownloadUtil.downloadFile(resp, filePath);
		String lengthHeader = resp.getHeader("Content-Length");

		assertThat(lengthHeader).isEqualTo(String.valueOf(downFile.length()));
	}
	
	@Test
	public void testDownloadNotExistFile() throws IOException {
		File downFile = null;
		HttpServletResponse resp = new MockHttpServletResponse();
		boolean result = FileDownloadUtil.downloadFile(resp, downFile);
		assertThat(result).isFalse();
		
		downFile = new File("Not-existed-file");
		result = FileDownloadUtil.downloadFile(resp, downFile);
		assertThat(result).isFalse();
	}

}
