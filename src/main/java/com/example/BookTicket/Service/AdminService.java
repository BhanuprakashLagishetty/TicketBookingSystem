package com.example.BookTicket.Service;

import com.example.BookTicket.Converting.AllConversions;
import com.example.BookTicket.Entity.*;
//import com.example.BookTicket.Models.SeatsModel;
import com.example.BookTicket.Models.*;
import com.example.BookTicket.Repository.admin_Repo;
import com.example.BookTicket.Repository.train_Repo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService implements AdminInterface{
    @Autowired
    private train_Repo trainRepo;
    @Autowired
    private admin_Repo adminRepo;
    @Autowired
    private AllConversions convert;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String addTrain(TrainModel trainModel)
    {
        Train train=convert.trainModelToEntity(trainModel);
        List<TrainRuns>trainRuns=train.getDays();
        trainRuns.stream().forEach((e)->{
            e.setTrain(train);
        });
        trainRepo.save(train);
        return "Succesfully Added train";

    }
//    public List<TrainModel> displayTrainOnLocations(String arrivalLocation,String departureLocation,LocalDate bookingDate)
//    {
//        List<Train>trainsList=trainRepo.findAll();
//        List<TrainModel>trainModelList=new ArrayList<>();
//        trainsList.stream().forEach((e)->{
//            TrainModel trainModel=convert.trainToTrainModel(e);
//            trainModelList.add(trainModel);
//        });
//        List<TrainModel>filterdTrainsModelList=new ArrayList<>();
//        long arrivalNumber=0;
//        long departureNumber=0;
//        boolean flag=false;
//        for(TrainModel e: trainModelList)
//        {
//            Set<Stations>stations=e.getStations();
//            List<Stations> sortedList = stations.stream().sorted(Comparator.comparingInt(Stations::getKm)).collect(Collectors.toList());
//            for(Stations stations1: sortedList) {
//                if (stations1.getStationName().equals(arrivalLocation)) {
//                    arrivalNumber = stations1.getId();
//                    flag=true;
//                }
//                if(stations1.getStationName().equals(departureLocation) && flag)
//                {
//                    departureNumber=stations1.getId();
//                }
//            }
//            if(arrivalNumber!=0 && departureNumber!=0)
//            {
//                System.out.println("This one of the train"+e);
//                filterdTrainsModelList.add(e);
//            }
//            arrivalNumber=0;
//            departureNumber=0;
//        }
//
//        updateTrainAvailabilitySeats(filterdTrainsModelList,bookingDate);
//
//        return filterdTrainsModelList;
//    }

    @Override
    public void removeTrain(Long id)
    {
        try{
        Train train=trainRepo.findById(id).get();
            Set<User> users=train.getTrain_Passangers();
            for (User user1:users)
            {
                Set<Train>trainList=user1.getTrain();
                if(trainList.contains(train))
                {
                    trainList.remove(train);
                    user1.setTrain(trainList);
                }
            }
            train.setSeats(null);
            train.setStations(null);
            train.setWatitingList(null);
            train.setTrain_Passangers(new HashSet<>());
            trainRepo.deleteById(id);
        }
        catch (Exception e){

        }


    }
    @Override
    public List<TrainModel> displayAllTrains()
    {
        List<Train>trainList=trainRepo.findAll();
        List<TrainModel>trainModelList=new ArrayList<>();
        trainList.stream().forEach((e)->{
           TrainModel trainModel= convert.trainToTrainModel(e);
           trainModelList.add(trainModel);
        });
        return trainModelList;
    }
    @Override
    public Train addingTicketsToTrain(Long trainId, SeatsModel seatsModel) {
        Train train = trainRepo.findById(trainId).orElse(null);

        Seat seat=convert.seatModelToSeat(seatsModel);
        System.out.println(seat.isAvailable());
        if (train == null) {
            return null; // Handle train not found scenario
        }
        Set<Seat> existingSeats = train.getSeats();
        seat.setTrain(train);
        existingSeats.add(seat);
        int count = 0;
        for (Seat e : existingSeats) {
            if (e.isAvailable()) {
                count++;
            }
            e.setTrain(train); // Update the train reference for each seat
        }
        train.setAvailableSeats(count);
        trainRepo.save(train);
        return train;
    }
    @Override
    public String addingStationsToTrain(Long trainId, Stations stations)
    {
        Train train=trainRepo.findById(trainId).orElse(null);
        if(train!=null)
        {
            Set<Stations> stations1=train.getStations();
            stations.setTrain(train);
            stations1.add(stations);
            train.setStations(stations1);
            trainRepo.save(train);
            return "Station added succesfully";
        }
        else {
            return "Train id is not present";
        }
    }

    @Override
    public Set<SeatsModel> displayTrainTickets(Long trainId) {
        return trainRepo.findById(trainId)
                .map(train -> train.getSeats().stream()
                        .filter(seat -> seat.getBookingDates().isEmpty())
                        .map(convert::seatToSeatModel)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override

    public Set<Stations> DisplayIntermediateStations(Long trainId) {
        Train train=trainRepo.findById(trainId).orElse(null);
        if(train!=null)
        {
            return train.getStations();
        }
        else{
            return new HashSet<>();
        }

    }

    @Override
    public boolean checkAdminLogin(AdminModel adminModel) {
        System.out.println("admin");
        if(adminRepo.existsByUsername(adminModel.getUsername())) {
            Admin admin = adminRepo.findByUsername(adminModel.getUsername());
            return passwordEncoder.matches(adminModel.getPassword(), admin.getPassword());
        }
        return false;
    }

    //used to search for killo meters
    @Override
    public PriceGenerationModel findKilometers(String arrivalStation, String departureStation, @NotNull List<TrainModel> trainModelList) {
        TrainModel train=trainModelList.get(0);
        Set<Stations>stations=train.getStations();
        PriceGenerationModel priceGenerationModel=new PriceGenerationModel();
        for(Stations stations1:stations)
        {
            if(stations1.getStationName().equals(arrivalStation))
            {
                priceGenerationModel.setFrom(arrivalStation);
                priceGenerationModel.setStartKm(stations1.getKm());
            }
            if(stations1.getStationName().equals(departureStation))
            {
                priceGenerationModel.setTo(departureStation);
                priceGenerationModel.setDepartureKm(stations1.getKm());
            }
        }
        return priceGenerationModel;
    }


}
