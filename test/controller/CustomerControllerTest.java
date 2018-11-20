package controller;

import controllers.routes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavel Borissiouk (pborissiouk@bluesoft.net.pl)
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.managment.*", "javax.crypto.*"})
public class CustomerControllerTest extends WithApplication {

    @Test
    public void testStartParkingPeriod() {

        Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), () -> {

            Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(
                    routes.CustomerController.startParkingPeriod("1973-HE7", "REGULAR"));
            Result result = Helpers.route(mockActionRequest);
            assertEquals(200, result.status());

        });

    }

    @Test
    public void testGetActualParkingAmount() {

        Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), () -> {

            Helpers.route(Helpers.fakeRequest(
                    routes.CustomerController.startParkingPeriod("1973-HE7", "REGULAR"))
            );

            Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(
                    routes.CustomerController.getActualParkingAmount("1973-HE7"));
            Result result = Helpers.route(mockActionRequest);
            assertEquals(200, result.status());

        });

    }

    @Test
    public void testStopParkingPeriod() {

        Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), () -> {

            Helpers.route(Helpers.fakeRequest(
                    routes.CustomerController.startParkingPeriod("1973-HE7", "REGULAR"))
            );

            Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(
                    routes.CustomerController.stopParkingPeriod("1973-HE7"));
            Result result = Helpers.route(mockActionRequest);
            assertEquals(200, result.status());

        });

    }

}
