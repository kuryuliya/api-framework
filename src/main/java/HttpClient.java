import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.nio.charset.Charset;

public class HttpClient {
    private final String baseUri;
    private final CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpResponse response;

    public HttpClient(String baseUri) {
        this.baseUri = baseUri;
        httpClient.start();
    }

    private FutureCallback<HttpResponse> futureCallback = new FutureCallback<HttpResponse>() {
        public void completed(HttpResponse httpResponse) {
            System.out.println("Request COMPLETED");
        }

        public void failed(Exception e) {
            System.out.println("Request Failed Due to : " + e.getMessage());
        }

        public void cancelled() {
            System.out.println("Request CANCELLED");
        }
    };

    public HttpResponse doGet(String url) {
        var request = new HttpGet(baseUri + url);
        var execute = httpClient.execute(request, futureCallback);
        try {
            response = execute.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public HttpResponse doPost(String url, Object body) {
        var request = new HttpPost(baseUri + url);
        var json = serializeToJson(body);
        var entity = new StringEntity(json, Charset.defaultCharset());
        request.setEntity(entity);

        var execute = httpClient.execute(request, futureCallback);
        try {
            response = execute.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public HttpResponse doPut(String url, Object body) {
        var request = new HttpPut(baseUri + url);
        var json = serializeToJson(body);
        var entity = new StringEntity(json, Charset.defaultCharset());
        request.setEntity(entity);

        var execute = httpClient.execute(request, futureCallback);
        try {
            response = execute.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public HttpResponse doDelete(String url) {
        var request = new HttpDelete(baseUri + url);
        var execute = httpClient.execute(request, futureCallback);
        try {
            response = execute.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String serializeToJson(Object content) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
