package controller;

import controllers.routes;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavel Borissiouk (pborissiouk@bluesoft.net.pl)
 */
public class OperatorControllerTest extends WithApplication {

    @Test
    public void testStopParkingPeriod() {

        Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), () -> {

            Helpers.route(Helpers.fakeRequest(
                    routes.CustomerController.startParkingPeriod("1973-HE7", "REGULAR"))
            );

            Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(
                    routes.OperatorController.getIsVehicleRegistered("1973-HE7"));
            Result result = Helpers.route(mockActionRequest);
            assertEquals(200, result.status());

        });

    }

}
