import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CreateUserDto;
import dto.GetUserDto;
import dto.UpdateUserDto;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
@DisplayName("Управление пользователями")
public class UserManagementTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "https://reqres.in/";

    @Test
    @DisplayName("Создание пользователя")
    public void when_createNewUser_expect_statusCode201andReturnedUserData() {
//        arrange
        var client = new HttpClient(baseUrl);
        var relevantUrl = "/api/users";
        var name = "Jhon";
        var job = "CEO";
        var userData = new CreateUserDto(name, job);
//        action
        var createUserResponse = client.doPost(relevantUrl, userData);
        var bodyResponse = getBody(createUserResponse.getEntity(), CreateUserDto.class);

        Assertions.assertAll(
                () -> assertEquals("Response status code", 201, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertEquals("Name", userData.getName(), bodyResponse.getName()),
                () -> assertEquals("Job", userData.getJob(), bodyResponse.getJob()),
                () -> assertNotNull("Id", bodyResponse.getId()),
                () -> assertNotNull("CreatedAt", bodyResponse.getCreatedAt())
        );
    }

    @Test
    @DisplayName("Получение информации о пользователе по id")
    public void when_getExistentUserById_expect_receivedStatusCode200AndUserData() {
//        arrange
        var client = new HttpClient(baseUrl);
        var relevantUrl = "/api/users/";
        var userId = "12";
//        action
        var createUserResponse = client.doGet(relevantUrl + userId);
        var bodyResponse = getBody(createUserResponse.getEntity(), GetUserDto.class);

        Assertions.assertAll(
                () -> assertEquals("Response status code", 200, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertNotNull("Data", bodyResponse.getData()),
                () -> assertNotNull("Ad", bodyResponse.getAd()),
                () -> assertEquals("Id", userId, bodyResponse.getData().getId()),
                () -> assertNotNull("Avatar", bodyResponse.getData().getAvatar()),
                () -> assertNotNull("Email", bodyResponse.getData().getEmail()),
                () -> assertNotNull("FirstName", bodyResponse.getData().getFirst_name()),
                () -> assertNotNull("LastName", bodyResponse.getData().getLast_name()),
                () -> assertNotNull("Company", bodyResponse.getAd().getCompany()),
                () -> assertNotNull("Text", bodyResponse.getAd().getText()),
                () -> assertNotNull("Url", bodyResponse.getAd().getUrl())
        );
    }

    @Test
    @DisplayName("Обновить информацию о пользователе")
    public void given_existentUser_when_updateUserData_expect_receivedStatusCode200AndUpdatedUserData() {
//        arrange
        var client = new HttpClient(baseUrl);
        var relevantUrl = "/api/users/";
        var userId = "12";
        var name = "newJhon";
        var job = "newCEO";
        var userData = new UpdateUserDto(name, job);
//        action
        var updateUserResponse = client.doPut(relevantUrl + userId, userData);
        var bodyResponse = getBody(updateUserResponse.getEntity(), UpdateUserDto.class);

        Assertions.assertAll(
                () -> assertEquals("Response status code", 200, updateUserResponse.getStatusLine().getStatusCode()),
                () -> assertEquals("Name", name, bodyResponse.getName()),
                () -> assertEquals("Job", job, bodyResponse.getJob())
        );
    }

    @Test
    @DisplayName("Удалить пользователе")
    public void when_deleteExistentUser_expect_statusCode204() {
//        arrange
        var client = new HttpClient(baseUrl);
        var relevantUrl = "/api/users/";
        var userId = "12";
//        action
        var updateUserResponse = client.doDelete(relevantUrl + userId);

        assertEquals("Response status code", 204, updateUserResponse.getStatusLine().getStatusCode());
    }

    private <T> T getBody(HttpEntity response, Class<T> clazz) {
        T responseData = null;
        try {
            var bodyString = EntityUtils.toString(response, "UTF-8");
            responseData = mapper.readValue(bodyString, clazz);

            System.out.println("Received data " + bodyString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

}
