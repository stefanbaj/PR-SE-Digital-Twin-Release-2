package com.example.DigitalTwin.controller;

import java.util.List;

import com.example.DigitalTwin.dto.DeviceDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DigitalTwin.dto.RoomDto;
import com.example.DigitalTwin.model.Room;
import com.example.DigitalTwin.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody RoomDto roomDetails) {
        Room createdRoom = roomService.createRoom(roomDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    //	@PutMapping("/{roomId}")
//	public ResponseEntity<Room> updateRoom(@PathVariable("roomId") Long id,@Valid @RequestBody RoomDto roomDetails) {
//		Room updatedRoom = roomService.updateRoom(id, roomDetails);
//		return ResponseEntity.ok(updatedRoom);
//	}
    @PutMapping
    public ResponseEntity<?> updateRoom(@Valid @RequestBody RoomDto roomDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(roomService.updateRoom(roomDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong.");
        }

    }

//	@PutMapping
//	public ResponseEntity<Room> updateRoom(@Valid @RequestBody RoomDto roomDetails) {
//		Room createdRoom = roomService.createRoom(roomDetails);
//		return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
//	}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("roomId") Long id) {
        RoomDto room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<RoomDto> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/report")
    public ResponseEntity<Resource> getFile() {
        String filename = "room_report.csv";
        InputStreamResource file = new InputStreamResource(roomService.generateRoomReport());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv")).body(file);
    }

    @GetMapping("/random/{roomId}")
    public ResponseEntity<Room> addRandomDataToRoom(@PathVariable("roomId") Long roomId) {
        Room room = roomService.addRandomDataToRoom(roomId);
        return ResponseEntity.ok(room);
    }
}
