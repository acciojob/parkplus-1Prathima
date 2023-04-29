package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        //create a new spot in the parkingLot with given id
        //the spot type should be the next biggest type in case the number of wheels are not 2 or 4, for 4+ wheels, it is others

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        SpotType spotType;
        if(numberOfWheels == 2){
            spotType = SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels == 4){
            spotType = SpotType.FOUR_WHEELER;
        }
        else{
            spotType = SpotType.OTHERS;
        }
        spot.setSpotType(spotType);
        spot.setOccupied(false);
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);
//        parkingLot.getSpotList().add(spot);
        List<Spot> spots = parkingLot.getSpotList();
        if(spots==null){
            spots = new ArrayList<>();
        }
        spots.add(spot);
        parkingLot.setSpotList(spots);

//        parkingLotRepository1.save(parkingLot);
//        return spot;
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {
        //delete a spot from given parking lot
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();

        parkingLot.getSpotList().remove(spot);
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        //update the details of a spot
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spots = parkingLot.getSpotList();
        for(Spot spot : spots){
            spotRepository1.delete(spot);
        }
        parkingLotRepository1.delete(parkingLot);
    }
}
