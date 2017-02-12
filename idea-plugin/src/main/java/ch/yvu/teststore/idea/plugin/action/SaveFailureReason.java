/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */
package ch.yvu.teststore.idea.plugin.action;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SaveFailureReason extends Task.Backgroundable {

	private final String run;
	private final String testName;
	private final String failureReason;
	private final String baseUrl;
	private final HttpClient httpClient = HttpClientBuilder.create().build();

	public SaveFailureReason(
			@Nullable Project project, String run, String testName, String failureReason, String baseUrl) {
		super(project, "Save Failure Reason");
		this.run = run;

		this.testName = testName;
		this.failureReason = failureReason;
		this.baseUrl = baseUrl;
	}

	@Override
	public void run(@NotNull ProgressIndicator indicator) {
		try {
			String encodedTestName = URLEncoder.encode(testName, "UTF-8");
			URL url = new URL(new URL(baseUrl), "/runs/" + run + "/tests/" + encodedTestName + "/0");
			HttpPut put = new HttpPut(url.toString());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("failureReason", failureReason));
			put.setEntity(new UrlEncodedFormEntity(nvps));
			httpClient.execute(put);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
