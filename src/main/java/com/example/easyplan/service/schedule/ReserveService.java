package com.example.easyplan.service.schedule;

import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.chat.ChatRoomUser;
import com.example.easyplan.domain.entity.schedule.Reserve;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.ChatRoomRepository;
import com.example.easyplan.repository.ChatRoomUserRepository;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.repository.schedule.ReserveRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReserveService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;

    /*public List<Reserve> getReserves(Long userId){
        return reserveRepository.findAllByUserId(userId);

    }*/

    public String createReserve(Long roomId, String content){

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();

        Reserve reserve = Reserve.builder()
                .content(content.substring(12, content.length() - 2))
                .chatRoom(chatRoom)
                .build();

        reserveRepository.save(reserve);
        return "success";
    }

    public void deleteReserve(Long id){
        reserveRepository.deleteById(id);
    }

    public List<Reserve> getReserves(Long usersId) {
        List<Reserve> reserveList = new ArrayList<>();

        User user = userRepository.findById(usersId).get();

        List<ChatRoom> chatRooms = user.getChatRooms();
        for (ChatRoom chatRoom : chatRooms) {
            List<Reserve> reserves = reserveRepository.findAllByChatRoomId(chatRoom.getId());
            for (Reserve reserve : reserves) {
                log.info("chatRoom 예비 일정 id: " + reserve.getId());
                reserveList.add(reserve);
            }
        }
        List<ChatRoomUser> chatRoomUsers = user.getChatRoomUsers();
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            List<Reserve> reserves = reserveRepository.findAllByChatRoomId(chatRoomUser.getChatRoom().getId());
            for (Reserve reserve : reserves) {
                log.info("chatRoomUser 예비 일정 id: " + reserve.getId());
                reserveList.add(reserve);
            }
        }

        return reserveList;
    }

    @Transactional
    public void putReserve(Long id) {
        Reserve reserve = reserveRepository.findById(id).get();
        reserve.setType(1);
    }
}
