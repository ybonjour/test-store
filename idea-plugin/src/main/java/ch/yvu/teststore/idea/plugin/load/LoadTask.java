package ch.yvu.teststore.idea.plugin.load;

import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.model.Model;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public abstract class LoadTask<V extends Model> extends Task.Backgroundable {

	private List<V> results;
	private LoadListener loadListener;
	protected final HttpClient httpClient = HttpClientBuilder.create().build();
	private final String baseUrl;

	public LoadTask(@Nls(capitalization = Nls.Capitalization.Title) @NotNull String title, String baseUrl) {
		super(null, title);
		this.baseUrl = baseUrl;
	}

	protected String getBaseUrl() {
		return baseUrl;
	}

	public void setLoadListener(LoadListener listener) {
		this.loadListener = listener;
	}

	@Override
	public void run(@NotNull ProgressIndicator indicator) {
		try {
			results = fetch();
		} catch(Throwable t) {
			loadListener.onError(t);
		}
	}

	@Override
	public void onSuccess() {
		if (results == null) {
			loadListener.onError(new NullPointerException("result was null"));
			return;
		}

		loadListener.onSuccess(results.stream().map((V m) -> (Model) m).collect(toList()));
	}

	public abstract List<V> fetch();

	protected String getJson(String path) {
		try {
			URL url = new URL(new URL(baseUrl), path);
			HttpGet get = new HttpGet(url.toString());


			HttpResponse response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if ( status != 200) {
				System.err.println("ERROR: Unexpected status code (" + status + ")");
				return null;
			}

			return readBody(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String readBody(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}


	public interface LoadListener {

		void onSuccess(List<Model> results);

		void onError(Throwable error);
	}
}
