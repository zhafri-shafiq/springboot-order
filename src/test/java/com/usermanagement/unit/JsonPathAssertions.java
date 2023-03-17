package com.usermanagement.unit;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class JsonPathAssertions {

    @Test
    public void userAssertion() {
        //given
        String userStr = "{\"id\":1," +
                "\"name\":\"test user 1\"," +
                "\"email\":\"testuser1@email.com\"," +
                "\"gender\":\"FEMALE\"," +
                "\"status\":\"ACTIVE\"," +
                "\"createdAt\":\"2022-09-01 03:20:21\"," +
                "\"updatedAt\":\"2022-04-07 01:59:59\"}";

        //when
        Map<String, String> userMap = JsonPath.read(userStr, "$");

        //then
        Assertions.assertNotNull(userMap);
        Assertions.assertInstanceOf(Map.class, userMap);
        Assertions.assertEquals("test user 1", userMap.get("name"));
        Assertions.assertEquals("FEMALE", userMap.get("gender"));
    }

    @Test
    public void todoWithTodoTypeAssertions() {
        //given
        String todoStr = "{\"id\":134," +
                "\"title\":\"Do Laundry\"," +
                "\"done\":false," +
                "\"dateCreated\":\"09/01/2022 01:20\"," +
                "\"dateDone\":null," +
                "\"dueDate\":\"10/01/2022 02:00\"," +
                "\"lastUpdated\":null," +
                "\"type\":{\"description\":\"Todo for Personal Work\",\"code\":\"PERSONAL\"}}";

        //when
        Map<String, String> todoMap = JsonPath.read(todoStr, "$");
        Map<String, String> todoTypeMap = JsonPath.read(todoStr, "$.type");

        //then
        Assertions.assertNotNull(todoMap);
        Assertions.assertNotNull(todoTypeMap);
        Assertions.assertEquals("PERSONAL", todoTypeMap.get("code"));
    }
}
