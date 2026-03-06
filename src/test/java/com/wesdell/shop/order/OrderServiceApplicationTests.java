package com.wesdell.shop.order;

import com.wesdell.shop.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.mysql.MySQLContainer;
import org.wiremock.spring.EnableWireMock;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "inventory.url=http://localhost:${wiremock.server.port}"
)
@EnableWireMock
@ActiveProfiles("test")
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

	@Test
	void Given_AnOrder_When_UserWantsToSave_Then_OrderSavedOnDB() {
		String orderRequestBody = """
                {
                    "skuCode": "iphone15",
                    "quantity": 100,
                    "price": 1001
                }
                """;

        InventoryClientStub.callInventoryStub("iphone15", 100);

        String orderResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderRequestBody)
                .when()
                .post("/api/orders")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().asString();

        MatcherAssert.assertThat(orderResponse, Matchers.is("Order created successfully"));
	}

}
