package com.jivitHealcare.Controller;

import com.jivitHealcare.Configuration.AmazonS3Config;
import com.jivitHealcare.Entity.*;
import com.jivitHealcare.Repo.CleamRequestDao;
import com.jivitHealcare.Repo.HospitalDao;
import com.jivitHealcare.Repo.RoleDao;
import com.jivitHealcare.Security.JwtAuthenticationFilter;
import com.jivitHealcare.Security.JwtHelper;
import com.jivitHealcare.service.*;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin

public class HospitalController {

    private final S3Client s3Client;
    private final String bucketName = "studycycle";
    private final String spaceUrl = "https://studycycle.blr1.digitaloceanspaces.com/";

    @Autowired
    private CleamRequestService cleamRequestService;
    @Autowired
     private HospitalDao hospitalDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private HealthCheckupService healthCheckupService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtHelper jwtUtil;

    @Autowired
    private CleamRequestDao cleamRequestDao;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private AmazonS3Config amazonS3Config;

    public HospitalController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) throws BadRequestException {
        Hospital hospital = hospitalDao.findByEmail(email);

        if (hospital == null) {
            // Create a new user if not found
            hospital = new Hospital();
            hospital.setEmail(email);

            // Assign a default role
            Role role = roleDao.findByRoleName("Hospital")
                   .orElseThrow(() -> new RuntimeException("Role not found"));
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);
            hospital.setRole(userRoles);
            hospitalDao.save(hospital);
        }



        // Check if OTP exists and if it is expired
        if (hospital.getOtp() != null && hospital.getExpirationTime().isAfter(LocalDateTime.now())) {
            return ResponseEntity.ok("OTP already sent and valid.");
        }

        // Generate and save new OTP
        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        otpService.sendOtp(otp, email);

        return ResponseEntity.ok("OTP sent to your email.");
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpService.validateOtp(email, otp)) {
            Hospital hospital = adminService.findByEmail(email);
            if (hospital == null) {
                //   user = new User();
                hospital.setEmail(email);
                // Assign a default role
                Role role = roleDao.findById("User")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(role);
                hospital.setRole(userRoles);
                hospitalDao.save(hospital);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(email);

            return ResponseEntity.ok(new JwtResponse(email, token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }


    @PostMapping("/createCleamRequest")
    @PreAuthorize("hasRole('Hospital')")
    public ResponseEntity<CleamRequest> createCleamRequest(
            @RequestParam String employeeName,
            @RequestParam String patientName,
            @RequestParam String healthCardNo,
            @RequestParam String departmentName,
            @RequestParam String mobileNo,
            @RequestParam String relationWithEmployee,
            @RequestParam String address,
            @RequestParam String chiefComplaints,
            @RequestParam String provisionalDiagnosis,
            @RequestParam String planOfTreatmentMedical,
            @RequestParam String planOfTreatmentSurgical,
            @RequestParam String grAilment,
            @RequestParam String grAilmentCode,
            @RequestParam String dateOfAdmission,
            @RequestParam String expectedLengthOfStay,
            @RequestParam String classOfAccommodation,
            @RequestParam String perDayRoomRent,
            @RequestParam String expectedCostInvestigation,
            @RequestParam String medicinesConsumablesCost,
            @RequestParam String doctorFeeSurgeonAss,
            @RequestParam String surgeonAnesthetistVisitCharges,
            @RequestParam String totalExpenseHospitalization,
            @RequestParam String nameOfDoctor,
            @RequestParam String doctorRegistrationNumber,
            @RequestParam Boolean alcoholAbuse,
            @RequestParam Boolean mlcFirCopy,
            @RequestParam("aadharCard") MultipartFile aadharCardFile,
            @RequestParam("jivatHealthCard") MultipartFile jivatHealthCardFile,
            @RequestParam("salaryACCheque") MultipartFile salaryACChequeFile,
            @RequestParam("promissoryNote") MultipartFile promissoryNoteFile,
            @RequestParam("cutumnbpramanpatra") MultipartFile cutumnbpramanpatraFile,
            @RequestParam("parishitdocument") MultipartFile parishitdocumentFile)


    //    @RequestParam Long hospitalId)
    {

        try {
            // Upload files to DigitalOcean Spaces (or any other storage)
            String aadharCard = uploadFileToSpace(aadharCardFile);
            String jivatHealthCard = uploadFileToSpace(jivatHealthCardFile);
            String salaryACCheque = uploadFileToSpace(salaryACChequeFile);
            String promissoryNote = uploadFileToSpace(promissoryNoteFile);
            String cutumnbpramanpatra =uploadFileToSpace(cutumnbpramanpatraFile);
            String parishitdocument =uploadFileToSpace(parishitdocumentFile);

            String email = JwtAuthenticationFilter.CURRENT_USER;
            Hospital hospital1 = hospitalDao.findByEmail(email);
            if (hospital1 == null) {
                throw new RuntimeException("User not found");
            }

//int totalExpenseHospitalization=(expectedLengthOfStay*perDayRoomRent)+expectedCostInvestigation+medicinesConsumablesCost+doctorFeeSurgeonAss+surgeonAnesthetistVisitCharges;

            // Create a new CleamRequest object and set the parameters
            CleamRequest cleamRequest = new CleamRequest();
            cleamRequest.setEmployeeName(employeeName);
            cleamRequest.setPatientName(patientName);
            cleamRequest.setHealthCardNo(healthCardNo);
            cleamRequest.setDepartmentName(departmentName);
            cleamRequest.setMobileNo(mobileNo);
            cleamRequest.setRelationWithEmployee(relationWithEmployee);
            cleamRequest.setAddress(address);
            cleamRequest.setChiefComplaints(chiefComplaints);
            cleamRequest.setProvisionalDiagnosis(provisionalDiagnosis);
            cleamRequest.setPlanOfTreatmentMedical(planOfTreatmentMedical);
            cleamRequest.setPlanOfTreatmentSurgical(planOfTreatmentSurgical);
            cleamRequest.setGrAilment(grAilment);
            cleamRequest.setGrAilmentCode(grAilmentCode);
            cleamRequest.setDateOfAdmission(dateOfAdmission);
            cleamRequest.setExpectedLengthOfStay(expectedLengthOfStay);
            cleamRequest.setClassOfAccommodation(classOfAccommodation);
            cleamRequest.setPerDayRoomRent(perDayRoomRent);
            cleamRequest.setExpectedCostInvestigation(expectedCostInvestigation);
            cleamRequest.setMedicinesConsumablesCost(medicinesConsumablesCost);
            cleamRequest.setDoctorFeeSurgeonAss(doctorFeeSurgeonAss);
            cleamRequest.setSurgeonAnesthetistVisitCharges(surgeonAnesthetistVisitCharges);
            cleamRequest.setTotalExpenseHospitalization(totalExpenseHospitalization);
            cleamRequest.setNameOfDoctor(nameOfDoctor);
            cleamRequest.setDoctorRegistrationNumber(doctorRegistrationNumber);
            cleamRequest.setAlcoholAbuse(alcoholAbuse);
            cleamRequest.setMlcFirCopy(mlcFirCopy);
            cleamRequest.setHospital(hospital1);

            // Set the file URLs after upload
            cleamRequest.setAadharCard(aadharCard);
            cleamRequest.setJivatHealthCard(jivatHealthCard);
            cleamRequest.setSalaryACCheque(salaryACCheque);
            cleamRequest.setPromissoryNote(promissoryNote);
            cleamRequest.setParishitdocument(parishitdocument);
            cleamRequest.setCutumnbpramanpatra(cutumnbpramanpatra);


            // Save the CleamRequest
            CleamRequest createdCleamRequest = cleamRequestService.createCleamRequest(cleamRequest);

            // Return the response
            return new ResponseEntity<>(createdCleamRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String uploadFileToSpace(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(file.getOriginalFilename());

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path tempFile = Files.createTempFile("upload-", fileName);

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Upload to DigitalOcean Spaces
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl("public-read")
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        // Clean up temporary file
        Files.deleteIfExists(tempFile);

        // Return the file URL
        return spaceUrl + fileName;
    }


    @PutMapping("/updateCleamRequest/{id}")
    @PreAuthorize("hasRole('Hospital')")
    public ResponseEntity<CleamRequest> updateCleamRequest(
            @PathVariable Long id,
            @RequestParam(required = false) String massage, // Changed to RequestParam
            @RequestParam("finalbill") MultipartFile finalbillFile,
            @RequestParam("dischargecard") MultipartFile dischargecardFile) {

        try {
            // Find the existing CleamRequest by ID
            CleamRequest existingCleamRequest = cleamRequestService.getCleamRequestById(id);
            if (existingCleamRequest == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Get the current user (Hospital) from the JWT token
            String email = JwtAuthenticationFilter.CURRENT_USER;
            Hospital hospital1 = hospitalDao.findByEmail(email);
            if (hospital1 == null) {
                throw new RuntimeException("User not found");
            }

            // Upload the new files if provided
            if (!finalbillFile.isEmpty()) {
                String finalbill = uploadFileToSpace(finalbillFile);
                existingCleamRequest.setFinalbill(finalbill);
            }
            if (!dischargecardFile.isEmpty()) {
                String dischargecard = uploadFileToSpace(dischargecardFile);
                existingCleamRequest.setDischargecard(dischargecard);
            }

            // Update the message if provided
            if (massage != null && !massage.isEmpty()) {
                existingCleamRequest.setMassage(massage);
            }

            existingCleamRequest.setHospital(hospital1);  // Ensure hospital is updated

            // Save the updated CleamRequest
            CleamRequest updatedCleamRequest = cleamRequestService.updateCleamRequest(existingCleamRequest);

            // Return the updated CleamRequest as response
            return new ResponseEntity<>(updatedCleamRequest, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hospitalCleamRequests")
    @PreAuthorize("hasAnyRole('Hospital')")
    public ResponseEntity<List<CleamRequest>> getAllCleamRequestByHospital() {
        List<CleamRequest> cleamRequests = cleamRequestService.getAllCleamRequestByHospital();
        return ResponseEntity.ok(cleamRequests);
    }
    // New API to get CleamRequest by id
    @GetMapping("/cleamRequest/{id}")
    @PreAuthorize("hasAnyRole('Hospital')")
    public ResponseEntity<CleamRequest> getCleamRequestById(@PathVariable Long id) {
        CleamRequest cleamRequest = cleamRequestService.getCleamRequestById(id);

        if (cleamRequest != null) {
            return ResponseEntity.ok(cleamRequest);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if CleamRequest not found
        }
    }
    @GetMapping("/adminCleamRequests")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<CleamRequest>> getCleamRequests() {
        List<CleamRequest> cleamRequests = cleamRequestService.getAllCleamRequest();
        return ResponseEntity.ok(cleamRequests);
    }

//    @PutMapping("/updateStatusAuthorized/{id}")
//    @PreAuthorize("hasRole('Admin')")
//    public ResponseEntity<CleamRequest> updateCleamRequestStatusAuthorized(@PathVariable Long id,@RequestBody CleamRequest cleamRequest
//            ) {
//
//        try {
//            // Update the status of the CleamRequest
//            CleamRequest updatedRequest = cleamRequestService.cleamRequestService(id, String.valueOf(cleamRequest));
//            return ResponseEntity.ok(updatedRequest);
//
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Error
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Error
//        }
//    }

    @PutMapping("/updateStatusAuthorized/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<CleamRequest> updateCleamRequestStatusAuthorized(@PathVariable("id") Long id) {
        return cleamRequestService.updateStatusAuthorized(id);

    }

    @PutMapping("/updateStatusRejected/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<CleamRequest> updateCleamRequestStatusRejected(@PathVariable("id") Long id){
        return cleamRequestService.updateStatusRejected(id);
    }

    @PostMapping("/healthCheckupReqest")
    @PreAuthorize("hasRole('Hospital')")
    public ResponseEntity<HealthCheckupRequest> createHealthCheckupRequest(
            @RequestParam String hospital,
            @RequestParam String cardNo,
            @RequestParam String employeeName,
            @RequestParam String depermentName,
            @RequestParam String pesentName,
            @RequestParam String location,
            @RequestParam String coupon,
            @RequestParam("doc1") MultipartFile doc1,

            @RequestParam(required = false, defaultValue = "Pending") String status) {

        try {

            // फाईल्स DigitalOcean Spaces मध्ये अपलोड करणे
            String doc1Url = uploadFileToSpace(doc1);
            String email = JwtAuthenticationFilter.CURRENT_USER;
            Hospital hospital1 = hospitalDao.findByEmail(email);
            if (hospital1 == null) {
                throw new RuntimeException("User not found");
            }

            HealthCheckupRequest healthCheckupRequest = new HealthCheckupRequest();
            healthCheckupRequest.setHospital(hospital);
            healthCheckupRequest.setCardNo(cardNo);
            healthCheckupRequest.setEmployeeName(employeeName);
            healthCheckupRequest.setDepermentName(depermentName);
            healthCheckupRequest.setPesentName(pesentName);
            healthCheckupRequest.setLocation(location);
            healthCheckupRequest.setCoupon(coupon);
            healthCheckupRequest.setDoc1(doc1Url);
            healthCheckupRequest.setStatus(status);
           // healthCheckupRequest.setHospital(String.valueOf(hospital1));

            healthCheckupRequest.setHospitalid(hospital1);

            HealthCheckupRequest savedRequest = healthCheckupService.healthCheckupRequest(healthCheckupRequest);
            return ResponseEntity.ok(savedRequest);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Error
        }
    }


    @PutMapping("/healthCheckupAuthorized/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<HealthCheckupRequest> updateHealthCheckupRequestStatusAuthorized(@PathVariable("id") Long id) {
        return healthCheckupService.updateStatusAuthorized(id);

    }

    @PutMapping("/healthCheckupRejected/{id}")
    @PreAuthorize("hasRole('Admin')")
    public HealthCheckupRequest updateHealthCheckupRequestStatusRejected(@PathVariable("id") Long id){
        return healthCheckupService.updateStatusRejected(id);
    }
    @PostMapping("/tecketRaise")
    @PreAuthorize("hasRole('Hospital')")
    public ResponseEntity<Ticket> ticketRaise(@RequestBody Ticket ticket){
        Ticket ticket1= ticketService.ticketRaise(ticket);
        return  ResponseEntity.ok(ticket1);
    }

    @GetMapping("/RaiseTicketInfo")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Ticket>> getRaisedTicketsInfo() {
        List<Ticket> tickets = ticketService.ticketRaiseInfo(); // Fetch raised tickets from the service layer
        return ResponseEntity.ok(tickets); // Return the list of tickets wrapped in ResponseEntity
    }
}
