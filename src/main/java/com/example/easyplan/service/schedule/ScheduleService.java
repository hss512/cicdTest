package com.example.easyplan.service.schedule;

import com.example.easyplan.domain.entity.schedule.Reserve;
import com.example.easyplan.domain.entity.schedule.Schedule;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.repository.schedule.ReserveRepository;
import com.example.easyplan.repository.schedule.ScheduleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ReserveRepository reserveRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public void registSchedule(Long reserveId, Long userId, String time) {
        Reserve reserve = reserveRepository.findById(reserveId).get();
        User user = userRepository.findById(userId).get();
        Schedule schedule = new Schedule();
        schedule.setReserveId(reserveId);
        schedule.setContent(reserve.getContent());
        schedule.setUser(user);
        String[] split = time.split("-");
        String date = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        schedule.setSendMailTime(date);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void sendMails() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("today: " + today);
        List<Schedule> allBySendEmailTime = scheduleRepository.findAllBySendMailTime(today);
        for (Schedule schedule : allBySendEmailTime) {
            log.info("schedule_id: " + schedule.getId());
        }

        List<Long> ids = allBySendEmailTime.stream().map(Schedule::getId).toList();

        scheduleRepository.deleteSchedulesByIds(ids);

        for (Schedule schedule : allBySendEmailTime) {
            sendMail(schedule);
        }
    }

    public void sendMail(Schedule schedule) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        log.info(schedule.getUser().getEmail());
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(schedule.getUser().getEmail());
            mimeMessageHelper.setSubject("여행 날짜가 곧 다가옵니다! 일정을 확인하세요");
            mimeMessageHelper.setText(schedule.getContent());
            javaMailSender.send(mimeMessage);

            log.info("Send Mail Success");
        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteByReserveId(id);
        Reserve reserve = reserveRepository.findById(id).get();
        reserve.setType(0);
    }
}
