package com.jivitHealcare.Controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.jivitHealcare.Entity.*;
import com.jivitHealcare.Repo.AppointmentDao;
import com.jivitHealcare.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private HospitalpaymenetService hospitalPaymentService;
    @Autowired
    private AppointmentDao appointmentDao;


    @Autowired
    private HealthCheckupService healthCheckupService;

    @Autowired
    private OtpService emailService;

    @PostConstruct
    public void initRoleAndUser() {
        adminService.initRoleAndUser();
    }

        @PostMapping({"/registerAmin"})
        public AdminRegister registerNewUser(@RequestBody AdminRegister adminRegister) {
        return adminService.registerNewAdmin(adminRegister);
    }
    @PostMapping("/auth/login")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @PostMapping("/addHospital")
    @PreAuthorize("hasRole('Admin')")
    public Hospital addHospital(@RequestBody Hospital hospital){
        return adminService.addHospital(hospital);
    }

    @GetMapping("/hospitals")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        List<Hospital> hospitals = adminService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("/AllhospitalsList")
    public ResponseEntity<List<Hospital>> AllhospitalsList() {
        List<Hospital> hospitals = adminService.AllhospitalsList();
        return ResponseEntity.ok(hospitals);
    }

    // GET endpoint to retrieve a hospital by ID
    @GetMapping("/hospital/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable Long id) {
        Hospital hospital = adminService.getHospitalById(id);
        if (hospital != null) {
            return ResponseEntity.ok(hospital);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT endpoint to update a hospital
    @PutMapping("/hospital/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long id, @RequestBody Hospital hospital) {
        Hospital updatedHospital = adminService.updateHospital(id, hospital);
        if (updatedHospital != null) {
            return ResponseEntity.ok(updatedHospital);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE endpoint to delete a hospital
    @DeleteMapping("/hospital/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        boolean isRemoved = adminService.deleteHospital(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



@PostMapping("/addHospitalPayment")
@PreAuthorize("hasRole('Admin')")
public ResponseEntity<HospitalPayment> addHospitalPayment(@RequestBody HospitalPayment hospitalPayment) {
    try {
        HospitalPayment savedHospitalPayment = hospitalPaymentService.addHospitalPayment(hospitalPayment);

        // Create email content
        String subject = "New Hospital Payment Record Added";
        String body = "Please find the attached PDF with hospital payment details.";

        // Define the directory and file name
        String pdfDirectory = "E:\\jivitPDFDownload";
        String pdfFileName = "hospitalPaymentDetails_" + savedHospitalPayment.getHospitalID() + ".pdf";
        String pdfFilePath = pdfDirectory + "\\" + pdfFileName;

        // Ensure the directory exists
        File dir = new File(pdfDirectory);
        if (!dir.exists()) {
            boolean dirsCreated = dir.mkdirs();
            if (!dirsCreated) {
                throw new IOException("Failed to create directory: " + pdfDirectory);
            }
        }

        // Generate PDF with payment details
        generatePaymentDetailsPDF(savedHospitalPayment, pdfFilePath);

        // Log the PDF file path
        System.out.println("PDF generated at: " + pdfFilePath);

        // Send email with PDF attachment
        emailService.sendEmailWithAttachment(savedHospitalPayment.getEmail(), subject, body, pdfFilePath);

        // Optionally, clean up the generated PDF after sending
        cleanupGeneratedPDF(pdfFilePath);

        return new ResponseEntity<>(savedHospitalPayment, HttpStatus.CREATED);
    } catch (Exception e) {
        // Log the exception details
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



    public void generatePaymentDetailsPDF(HospitalPayment hospitalPayment, String pdfFilePath) throws DocumentException, IOException {
        // Ensure the directory exists
        File pdfDirectory = new File("E:\\jivitPDFDownload");
        if (!pdfDirectory.exists()) {
            pdfDirectory.mkdirs();  // Create the directory if it doesn't exist
        }

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

        document.open();

        // Add a welcoming title
        document.add(new Paragraph("✨ Welcome to Jivit Healthcare ✨", FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD)));
        document.add(new Paragraph("____________________________________________________\n\n"));

        // Add a warm greeting
        document.add(new Paragraph(String.format("Dear ,\nHospital: %s", hospitalPayment.getHospitalName()), FontFactory.getFont(FontFactory.HELVETICA, 14)));

        // document.add(new Paragraph("Dear Valued User,\n" + hospitalPayment.getHospitalName(), FontFactory.getFont(FontFactory.HELVETICA, 14)));
        document.add(new Paragraph("Thank you for trusting us with your healthcare needs! Your support means the world to us. 💖\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));

        // Add payment details header
        document.add(new Paragraph("Here are your Hospital Payment Details:", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
        document.add(new Paragraph("____________________________________________________\n\n"));

        // Create a table for payment details
        PdfPTable table = new PdfPTable(2); // 2 columns
        table.setWidthPercentage(100); // Set the table width to 100%
        table.setSpacingBefore(10f); // Space before the table
        table.setSpacingAfter(10f); // Space after the table

        // Define table headers
        table.addCell("Detail");
        table.addCell("Information");

        // Add payment details to the table
        table.addCell("🌟 Hospital Name");
        table.addCell(hospitalPayment.getHospitalName());

        table.addCell("💰 Amount Paid");
        table.addCell(String.valueOf(hospitalPayment.getAmount()));

        table.addCell("🔍 Transaction No");
        table.addCell(hospitalPayment.getTransactionNo());

        table.addCell("🏦 Bank Name");
        table.addCell(hospitalPayment.getBankName());

        table.addCell("📍 Bank Location");
        table.addCell(hospitalPayment.getBankLocation());

        table.addCell("🏢 Branch");
        table.addCell(hospitalPayment.getBranch());

        table.addCell("👤 Employee Name");
        table.addCell(hospitalPayment.getEmployeeName());

        table.addCell("🧑‍⚕️ Patient Name");
        table.addCell(hospitalPayment.getPatientName());

        // Add the table to the document
        document.add(table);

        // Add a closing message
        document.add(new Paragraph("\nThank you once again for choosing Jivit Healthcare! We are here for your health and well-being. If you have any questions, feel free to reach out!\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Best Regards,\nThe Jivit Healthcare Team..\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));

        document.close();
    }

    public void cleanupGeneratedPDF(String pdfFilePath) throws IOException {
        Files.deleteIfExists(Paths.get(pdfFilePath));
    }

    // GET API to fetch all hospital payments
    @GetMapping("/getAllHospitalPayments")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<HospitalPayment>> getAllHospitalPayments() {
        List<HospitalPayment> hospitalPayments = hospitalPaymentService.getAllHospitalPayments();
        return new ResponseEntity<>(hospitalPayments, HttpStatus.OK);
    }

    @PostMapping("/addBenificiary")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<AddBenificiary> addBenificiary(@RequestBody AddBenificiary addBenificiary) {
        AddBenificiary savedEmployee = adminService.addBenificiary(addBenificiary);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    @GetMapping("/benificiaries")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<AddBenificiary>> getAllBenificiaries() {
        List<AddBenificiary> benificiaries = adminService.getAllBenificiaries();
        return new ResponseEntity<>(benificiaries, HttpStatus.OK);
    }

    // New method to get a beneficiary by ID
    @GetMapping("/benificiaries/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<AddBenificiary> getBenificiaryById(@PathVariable Long id) {
        AddBenificiary benificiary = adminService.getBenificiaryById(id);

        if (benificiary != null) {
            return new ResponseEntity<>(benificiary, HttpStatus.OK);
        } else {
            // If beneficiary not found, return 404 Not Found status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateBenificiary/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<AddBenificiary> updateBenificiary(@PathVariable Long id, @RequestBody AddBenificiary updatedBenificiary) {
        AddBenificiary benificiary = adminService.updateBenificiary(id, updatedBenificiary);
        return ResponseEntity.ok(benificiary);
    }
    @GetMapping("/hospitalHeathCheckupList")
    @PreAuthorize("hasRole('Hospital')")
    public  ResponseEntity<List<HealthCheckupRequest>> getAllHeathCheckupList(){
     List <HealthCheckupRequest> healthCheckupRequest=healthCheckupService.getAllHeathCheckupList();
     return ResponseEntity.ok(healthCheckupRequest);

    }

    @GetMapping("/adminHeathCheckupList")
    @PreAuthorize("hasRole('Admin')")
    public  ResponseEntity<List<HealthCheckupRequest>> adminHeathCheckupList(){
        List <HealthCheckupRequest> healthCheckupRequest=healthCheckupService.adminHeathCheckupList();
        return ResponseEntity.ok(healthCheckupRequest);

    }

    @GetMapping("/getAllHeathCheckup/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<HealthCheckupRequest> getAllHeathCheckupById(@PathVariable Long id) {
        HealthCheckupRequest  healthCheckupRequest = healthCheckupService.getHeathCheckupById(id);
        if (healthCheckupRequest != null) {
            return ResponseEntity.ok(healthCheckupRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/updateHealthCheckupStatusAuthorized/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<HealthCheckupRequest> updateHealthCheckupRequestStatusAuthorized(@PathVariable("id") Long id) {
        return healthCheckupService.updateHealthCheckupRequestStatusAuthorized(id);

    }

    @PutMapping("/updateHealthCheckupStatusReject/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<HealthCheckupRequest> updateHealthCheckupRequestStatusReject(@PathVariable("id") Long id) {
        return healthCheckupService.updateHealthCheckupRequestStatusReject(id);

    }


    @PostMapping("/appointment")
    public ResponseEntity<Appointment> createContact(@RequestBody Appointment appointment) {
        Appointment savedContact = appointmentDao.save(appointment);
        return ResponseEntity.ok(savedContact);
    }
    @GetMapping("/Allappointment")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Appointment>> getAllContacts() {
        List<Appointment> contacts = appointmentDao.findAll();
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/AppoinmentStatusAuthorized/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Appointment> updateAppoinmentStatusAuthorized(@PathVariable("id") Long id) {
        return healthCheckupService.updateAppoinmentStatusAuthorized(id);

    }

    @PutMapping("/AppoinmentStatusReject/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Appointment> updateAppoinmentStatusReject(@PathVariable("id") Long id) {
        return healthCheckupService.updateAppoinmentStatusReject(id);

    }
}

