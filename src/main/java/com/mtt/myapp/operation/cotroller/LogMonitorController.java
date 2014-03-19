package com.mtt.myapp.operation.cotroller;


import static com.mtt.myapp.common.util.CollectionUtils.buildMap;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.mtt.myapp.common.controller.BaseController;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Log monitor controller.
 *
 * This class runs with {@link org.apache.commons.io.input.Tailer} implementation. Whenever the underlying log file is changed. this class gets the
 * changes. and keep them(max 10000 byte) in the memory. Whenever user requests the log, it returns latest changes with
 * the index of the log.
 *
 * This is only available in the non-clustered instance.
 *
 * @author JunHo Yoon
 */
@Controller
@RequestMapping("/operation/log")
@PreAuthorize("hasAnyRole('A')")
public class LogMonitorController extends BaseController {

	private static final int LOGGER_BUFFER_SIZE = 10000;

	/**
	 * Buffer to store the latest log.
	 */
	private volatile StringBuffer stringBuffer = new StringBuffer(LOGGER_BUFFER_SIZE);

	private Tailer tailer;

	private long count = 0;
	private long modification = 0;

	/**
	 * Initialize.
	 */
	@PostConstruct
	public void init() {
		initTailer();
	}

	/**
	 * Initialize the {@link Tailer}.
	 */
	private synchronized void initTailer() {
		File logFile = getLogFile();
		if (tailer != null) {
			tailer.stop();
		}
		tailer = Tailer.create(logFile, new TailerListenerAdapter() {
			/**
			 * Handles a line from a Tailer.
			 *
			 * @param line
			 *            the line.
			 */
			public void handle(String line) {
				synchronized (this) {
					if (stringBuffer.length() + line.length() > 5000) {
						count++;
						modification = 0;
						stringBuffer = new StringBuffer();
					}
					modification++;
					if (stringBuffer.length() > 0) {
						stringBuffer.append("<br>");
					}
					stringBuffer.append(line.replace("\n", "<br>"));
				}
			}
		}, 1000, true);
	}

	/**
	 * Get the log file.
	 *
	 * @return log file
	 */
	File getLogFile() {
		String logFileName = "mtt.log";
		return new File(home.getDirectory(), logFileName);
	}

	/**
	 * Finalize bean.
	 */
	@PreDestroy
	public void destroy() {
		tailer.stop();
	}

	/**
	 * Return the logger first page.
	 *
	 * @param model model
	 * @return operation/logger
	 */
	@RequestMapping("")
	public String getOne(Model model) {
		model.addAttribute("verbose", config.isVerbose());
		return "operation/logger";
	}

	/**
	 * Get the last log in the form of json.
	 *
	 * @return log json
	 */
	@RequestMapping("/last")
	public HttpEntity<String> getLast() {
		return toJsonHttpEntity(buildMap("index", count, "modification", modification, "log", stringBuffer));
	}

	/**
	 * Turn on verbose log mode.
	 *
	 * @param verbose true if verbose mode
	 * @return success message if successful
	 */
	@RequestMapping("/verbose")
	public HttpEntity<String> enableVerbose(@RequestParam(value = "verbose", defaultValue = "false") Boolean verbose) {
		config.resetLoggerContext(verbose);
		initTailer();
		return toJsonHttpEntity(buildMap("success", true));
	}

}
