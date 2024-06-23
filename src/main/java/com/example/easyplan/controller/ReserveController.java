package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ResponseReserveDTO;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.schedule.Reserve;
import com.example.easyplan.repository.ChatRoomRepository;
import com.example.easyplan.service.schedule.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class ReserveController {

    private final ReserveService reserveService;
    private final ChatRoomRepository chatRoomRepository;

    /*@GetMapping("/reserves")
    private ResponseEntity<?> getReserves(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reserve> reserves = reserveService.getReserves(Long.parseLong(id));
        return ResponseEntity.ok(reserves);
    }*/

    @PostMapping("/reserve/{roomId}")
    public String postReserve(@PathVariable("roomId") Long roomId, @RequestBody String content){

        reserveService.createReserve(roomId, content);

        return "예비 일정 등록 성공";
    }

    @GetMapping("/reserve")
    public ResponseEntity<?> getReserves(){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Reserve> reserves = reserveService.getReserves(userId);

        return ResponseEntity.ok(reserves);
    }

    @DeleteMapping("/reserve/{id}")
    public ResponseEntity<?> deleteReserve(@PathVariable String id){
        reserveService.deleteReserve(Long.parseLong(id));
        return ResponseEntity.ok("success");
    }

    @PutMapping("/reserve/{id}")
    public String putReserve(@PathVariable String id){
        reserveService.putReserve(Long.parseLong(id));
        return "success";
    }
}
