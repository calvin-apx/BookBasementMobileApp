package com.example.bookbasement_02.Models;

public class Donate extends Appointment{

    private int donation_id;
    private int appointment_id;

    public int getDonation_id() {
        return donation_id;
    }
    public void setDonation_id(int donation_id) {
        this.donation_id = donation_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

}
