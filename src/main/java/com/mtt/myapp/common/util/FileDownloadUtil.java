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

import java.io.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File download utilities.
 * 
 * @author JunHo Yoon
 * @since 3.0
 */
public abstract class FileDownloadUtil {

	private static final int FILE_DOWNLOAD_BUFFER_SIZE = 4096;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadUtil.class);

	/**
	 * Provide file download from the given file path.
	 * @param response {@link javax.servlet.http.HttpServletResponse}
	 * @param desFilePath file path
	 * @return true if succeeded
	 */
	public static boolean downloadFile(HttpServletResponse response, String desFilePath) {
		File desFile = new File(desFilePath);
		return downloadFile(response, desFile);
	}

	/**
	 * Provide file download from the given file path.
	 * @param response {@link javax.servlet.http.HttpServletResponse}
	 * @param desFile file path
	 * @return true if succeeded
	 */
	public static boolean downloadFile(HttpServletResponse response, File desFile) {
		if (desFile == null || !desFile.exists()) {
			return false;
		}
		boolean result = true;
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + desFile.getName());
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Length", "" + desFile.length());
		InputStream fis = null;
		byte[] buffer = new byte[FILE_DOWNLOAD_BUFFER_SIZE];
		OutputStream toClient = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(desFile));
			toClient = new BufferedOutputStream(response.getOutputStream());
			int readLength;
			while (((readLength = fis.read(buffer)) != -1)) {
				toClient.write(buffer, 0, readLength);
			}
			toClient.flush();
		} catch (FileNotFoundException e) {
			LOGGER.error("file not found:" + desFile.getAbsolutePath(), e);
			result = false;
		} catch (IOException e) {
			LOGGER.error("read file error:" + desFile.getAbsolutePath(), e);
			result = false;
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(toClient);
		}
		return result;
	}
}
