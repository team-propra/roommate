package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.values.domain.BookingDays;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.values.domain.ItemName;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.values.forms.BookDataForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DomainService
//mediate between Repository, domain; map forms to domain-objects/data
public class RoomDomainService {

    public IRoomRepository roomRepository;
    IItemRepository itemRepository;

    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
    }

    public void addDummieRooms() {
        Room room1 = new Room(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a"), "12");
        room1.addItem(new ItemName("Chair"));
        Room room2 = new Room(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"), "13");
        room2.addItem(new ItemName("Table"));
        roomRepository.add(room1);
        roomRepository.add(room2);
    }

    public List<List<Boolean>> convertRoomCalendarDaysTo2dMatrix(UUID roomID, int stepSize) throws NotFoundRepositoryException {
        Room room = (Room) roomRepository.findRoomByID(roomID);

        List<Boolean> convertedMonday = room.monday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedTuesday = room.tuesday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedWednesday = room.wednesday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedThursday = room.thursday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedFriday = room.friday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedSaturday = room.saturday.convertToSpecificStepSize(stepSize);
        List<Boolean> convertedSunday = room.sunday.convertToSpecificStepSize(stepSize);

        List<List<Boolean>> reservedSlots = new ArrayList<>();
        reservedSlots.add(convertedMonday);
        reservedSlots.add(convertedTuesday);
        reservedSlots.add(convertedWednesday);
        reservedSlots.add(convertedThursday);
        reservedSlots.add(convertedFriday);
        reservedSlots.add(convertedSaturday);
        reservedSlots.add(convertedSunday);

        return  reservedSlots;
    }


    public BookDataForm addBookingsToForm(List<String> checkedDays, BookDataForm form){
        for (String checkedDay : checkedDays) {

            if(checkedDay.contains("-X")) {
                String[] daytime = checkedDay.split("-");
                System.out.println("Zeile: " + daytime[0]);
                System.out.println("Tag: " + daytime[1]);
                System.out.println("Checked day " + checkedDay);

                int timeIndex = Integer.parseInt(daytime[0]);
                int day = Integer.parseInt(daytime[1]);//0=monday, 1=tuesday...
                switch(day){
                    case 0:
                        form.bookingDays().mondayBookings.add(timeIndex, true);
                        break;
                    case 1:
                        form.bookingDays().tuesdayBookings.add(timeIndex, true);
                        break;
                    case 2:
                        form.bookingDays().wednesdayBookings.add(timeIndex, true);
                        break;
                    case 3:
                        form.bookingDays().thursdayBookings.add(timeIndex, true);
                        break;
                    case 4:
                        form.bookingDays().fridayBookings.add(timeIndex, true);
                        break;
                    case 5:
                        form.bookingDays().saturdayBookings.add(timeIndex, true);
                        break;
                    case 6:
                        form.bookingDays().sundayBookings.add(timeIndex, true);
                        break;

                }
            }
        }
        return form;
    }

    public void addBooking(BookDataForm bookDataForm) throws Exception{
        Room room = (Room) roomRepository.findRoomByID(UUID.fromString(bookDataForm.roomID()));

        int stepSize = bookDataForm.bookingDays().stepsize;

        room.monday.addBooking(bookDataForm.bookingDays().mondayBookings, stepSize);
        room.tuesday.addBooking(bookDataForm.bookingDays().tuesdayBookings,stepSize);
        room.wednesday.addBooking(bookDataForm.bookingDays().wednesdayBookings, stepSize);
        room.thursday.addBooking(bookDataForm.bookingDays().thursdayBookings, stepSize);
        room.friday.addBooking(bookDataForm.bookingDays().fridayBookings, stepSize);
        room.saturday.addBooking(bookDataForm.bookingDays().saturdayBookings, stepSize);
        room.sunday.addBooking(bookDataForm.bookingDays().sundayBookings, stepSize);
    }

    public void addRoom(IRoom room) {
        roomRepository.add(room);
    }

    

    public void removeRoom(IRoom room) {roomRepository.remove(room.getRoomID());}

    public Collection<IRoom> getRooms() {
        return roomRepository.findAll().stream()
                .map(iroom -> (IRoom) new Room(iroom.getRoomID(), iroom.getRoomNumber()))
                .toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            IRoom roomByID = roomRepository.findRoomByID(roomID);
            return new Room(roomByID.getRoomID(), roomByID.getRoomNumber());
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }

    public void saveAll(List<IRoom> rooms) {
        roomRepository.saveAll(rooms);
    }

    public List<ItemName> getItems() {
        return itemRepository.getItems();
    }


}
