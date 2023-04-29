package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists

        //check for valid payment mode
        mode = mode.toUpperCase();
        if(!mode.equals("CASH") && !mode.equals("CARD") && !mode.equals("UPI")){
            throw new Exception("Payment mode not detected");
        }

        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Spot spot = reservation.getSpot();

        //check bill
        int bill = spot.getPricePerHour() * reservation.getNumberOfHours();
        if(amountSent < bill){
            throw new Exception("Insufficient Amount");
        }

        //make payment and set parameters
        Payment payment = new Payment();
        payment.setPaymentCompleted(true);

        PaymentMode paymentMode;
        if(mode.equals("CASH")){
            paymentMode = PaymentMode.CASH;
        }
        else if(mode.equals("CARD")){
            paymentMode = PaymentMode.CARD;
        }
        else{
            paymentMode = PaymentMode.UPI;
        }
        payment.setPaymentMode(paymentMode);
        payment.setReservation(reservation);

        reservation.setPayment(payment);
        return paymentRepository2.save(payment);
    }
}
