package ch.yvu.teststore.idea.plugin.load;

import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.model.Model;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class LoadTask<V extends Model> extends Task.Backgroundable {

	private List<V> results;
	private LoadListener loadListener;
	protected final HttpClient httpClient = new HttpClient();

	public LoadTask(@Nls(capitalization = Nls.Capitalization.Title) @NotNull String title) {
		super(null, title);
	}

	public void setLoadListener(LoadListener listener) {
		this.loadListener = listener;
	}

	@Override
	public void run(@NotNull ProgressIndicator indicator) {
		results = fetch();
	}

	@Override
	public void onSuccess() {
		if (results == null)
			loadListener.onError(new NullPointerException("result was null"));
		loadListener.onSuccess(results.stream().map((V m) -> (Model) m).collect(toList()));
	}

	@Override
	public void onThrowable(@NotNull Throwable error) {
		loadListener.onError(error);
	}

	public abstract List<V> fetch();

	protected String getJson(String path) {
		try {
			URL url = new URL(new URL("http://localhost:8080"), path);
			GetMethod get = new GetMethod(url.toString());
			int status = httpClient.executeMethod(get);
			if (status != 200) {
				System.err.println("ERROR: Unexpected status code (" + status + ")");
				return null;
			}

			return new String(get.getResponseBody());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public interface LoadListener {

		void onSuccess(List<Model> results);

		void onError(Throwable error);
	}
}
