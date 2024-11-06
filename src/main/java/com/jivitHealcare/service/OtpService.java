package com.jivitHealcare.service;
//
//import com.jivitHealcare.Entity.Hospital;
//import com.jivitHealcare.Entity.Role;
//import com.jivitHealcare.Repo.HospitalDao;
//import com.jivitHealcare.Repo.RoleDao;
//import com.jivitHealcare.Security.JwtHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Random;
//import java.util.Set;
//
//@Service
//public class OtpService {
//
//
//    @Autowired
//    private RoleDao roleDao;
//
//
//    @Autowired
//    private JwtHelper jwtHelper;
//
//    @Autowired
//    private HospitalDao hospitalDao;
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//    @Value("${spring.mail.username}")
//    private String emailUsername;
//
//    @Value("${spring.mail.password}")
//    private String emailPassword;
//
//    @Value("${spring.mail.host}")
//    private String emailHost;
//
//    @Value("${spring.mail.port}")
//    private int emailPort;
//
//    private static final int OTP_LENGTH = 6;
//    private static final int OTP_EXPIRATION_MINUTES = 3;
//
//    public String generateOtp() {
//        Random random = new Random();
//        StringBuilder otp = new StringBuilder();
//        for (int i = 0; i < OTP_LENGTH; i++) {
//            otp.append(random.nextInt(10));
//        }
//        return otp.toString();
//    }
//
//    public void saveOtp(String email, String otp) {
//        Hospital otpEntity = hospitalDao.findByEmail(email);
//        if (otpEntity == null) {
//            otpEntity = new Hospital();
//            otpEntity.setEmail(email);
//        }
//
//        otpEntity.setOtp(otp);
//        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
//        Hospital hospital=new Hospital();
//        Role role = roleDao.findById("Hospital")
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(role);
//        hospital.setRole(userRoles);
//        hospitalDao.save(otpEntity);
//
//    }
//
//    public boolean isOtpExpired(String email) {
//
//        Hospital otpEntity = hospitalDao.findByEmail(email);
//        if (otpEntity == null) {
//            return true;
//        }
//        return otpEntity.getExpirationTime().isBefore(LocalDateTime.now());
//    }
//    public void sendOtp(String otp, String destination) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(destination);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP code is: " + otp);
//        javaMailSender.send(message);
//    }
//    public boolean validateOtp(String email, String otp) {
//        Optional<Hospital> otpEntity = hospitalDao.findByEmailAndOtp(email, otp);
//        if (otpEntity.isPresent()) {
//            if (otpEntity.get().getExpirationTime().isAfter(LocalDateTime.now())) {
//                return true;
//            } else {
//                hospitalDao.delete(otpEntity.get());
//            }
//        }
//        return false;
//    }
//}





import com.jivitHealcare.Entity.Hospital;
import com.jivitHealcare.Entity.Role;
import com.jivitHealcare.Repo.HospitalDao;
import com.jivitHealcare.Repo.RoleDao;
import com.jivitHealcare.Security.JwtHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class OtpService {


    @Autowired
    private RoleDao roleDao;


    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Value("${spring.mail.host}")
    private String emailHost;

    @Value("${spring.mail.port}")
    private int emailPort;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 30;

    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public void saveOtp(String email, String otp) {
        Hospital otpEntity = hospitalDao.findByEmail(email);
        if (otpEntity == null) {
            otpEntity = new Hospital();
            otpEntity.setEmail(email);
        }

        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
        Hospital user=new Hospital();
        Role role = roleDao.findById("Hospital")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        hospitalDao.save(otpEntity);

    }

    public boolean isOtpExpired(String email) {

        Hospital otpEntity = hospitalDao.findByEmail(email);
        if (otpEntity == null) {
            return true;
        }
        return otpEntity.getExpirationTime().isBefore(LocalDateTime.now());
    }
    public void sendOtp(String otp, String destination) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destination);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);
    }
    public boolean validateOtp(String email, String otp) {
        Optional<Hospital> otpEntity = hospitalDao.findByEmailAndOtp(email, otp);
        if (otpEntity.isPresent()) {
            if (otpEntity.get().getExpirationTime().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                hospitalDao.delete(otpEntity.get());
            }
        }
        return false;
    }


//    public void sendSimpleEmail(String email, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject(subject);
//        message.setText(body);
//        message.setFrom("vitaleankit18@gmail.com");
//        javaMailSender.send(message);
//    }

    public void sendEmailWithAttachment(String email, String subject, String body, String pdfFilePath) throws MessagingException, FileNotFoundException {

        if (pdfFilePath == null || pdfFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Attachment path cannot be null or empty");
        }

        // Log the attachment path
        System.out.println("Attachment Path: " + pdfFilePath);

        File attachment = new File(pdfFilePath);

        if (!attachment.exists()) {
            throw new FileNotFoundException("Attachment file not found at " + pdfFilePath);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("vitaleankit18@gmail.com");        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body);

        // Attach the file
        FileSystemResource file = new FileSystemResource(attachment);
        helper.addAttachment(file.getFilename(), file);

        javaMailSender.send(mimeMessage);

        // Log successful email sending
        System.out.println("Email sent successfully to " + email);
    }

    void sendHospitalAddedEmail(Hospital hospital) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(hospital.getEmail());
        message.setSubject("Hospital Register Successfully");

        String emailBody = "Dear " + hospital.getHospitalName() + ",\n\n" +
                "Your hospital has been successfully empanell in Jivit Healthcare & Medical Services.Pvt.Ltd\n" +
                "Your JivitHealthCare login ID is: " + hospital.getEmail() + "\n" +
                "And your password is the OTP sent to your email: " + hospital.getEmail() + "\n\n" +
                "Thank you once again for choosing Jivit Healthcare! We are here for your health and well-being.\n" +
                "If you have any questions, feel free to reach out!\n\n" +
                "\n\nBest Regards,\nThe Jivit Healthcare Team";

        message.setText(emailBody);

        // Send the email
        javaMailSender.send(message);
    }


}





