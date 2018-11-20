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

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavel Borissiouk (pborissiouk@bluesoft.net.pl)
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.managment.*", "javax.crypto.*"})
public class OwnerControllerTest extends WithApplication {

    @Test
    public void testStopParkingPeriod() {

        Helpers.running(Helpers.fakeApplication(Helpers.inMemoryDatabase()), () -> {

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");

            Helpers.route(Helpers.fakeRequest(
                    routes.CustomerController.startParkingPeriod("1973-HE7", "REGULAR"))
            );

            Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(
                    routes.OwnerController.getEarnedAmountForDay(simpleDateFormat.format(date)));
            Result result = Helpers.route(mockActionRequest);
            assertEquals(200, result.status());

        });

    }

}
