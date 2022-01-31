package juras.sales;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import juras.sales.cart.CartItem;
import juras.sales.offerting.Offer;
import juras.sales.ordering.CustomerData;
import juras.sales.ordering.DummyPaymentGateway;
import juras.sales.ordering.JpaReservationStorage;
import juras.sales.ordering.Reservation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
public class JpaReservationStorageTest {

    @Autowired
    JpaReservationStorage jpaReservationStorage;

    @Test
    void itAllowToStoreAndLoadReservation() {
        //A
        Reservation reservation = exampleReservation();
        //A
        jpaReservationStorage.save(reservation);
        Reservation loaded = jpaReservationStorage.load(reservation.getId()).get();
        //A
        assertEquals(reservation.getId(), loaded.getId());
        assertEquals(reservation.getPaymentUrl(), loaded.getPaymentUrl());
        assertEquals(reservation.getCustomerEmail(), loaded.getCustomerEmail());
        assertTrue(reservation.getTotal().compareTo(loaded.getTotal()) == 0, "wrong total number");
    }

    private Reservation exampleReservation() {
        Reservation newReservation = Reservation.of(
                new Offer(BigDecimal.valueOf(100.10), Collections.emptyList()),
                Arrays.asList(
                        new CartItem("product-1", 1),
                        new CartItem("product-2", 3)
                ),
                new CustomerData("john.doe@example.com", "john", "doe")
        );

        newReservation.registerPayment(new DummyPaymentGateway());

        return newReservation;
    }
}
