package com.jivitHealcare.service;

import com.jivitHealcare.Controller.ResourceNotFoundException;
import com.jivitHealcare.Entity.*;
import com.jivitHealcare.Repo.*;
//import org.apache.logging.log4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {


  //  private static final Logger logger = (Logger) LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminDao adminDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private BenificiaeyDao benificiaeyDao;
    @Autowired
   private BenificiaryCardDependentsDao benificiaryCardDependentsDao;

    @Autowired

    private OtpService otpService;

    public void initRoleAndUser() {


        // Create roles
        if (!roleDao.existsById("Admin")) {
            Role adminRole = new Role();
            adminRole.setRoleName("Admin");
            adminRole.setRoleDescription("Admin role");
            roleDao.save(adminRole);
        }

        if (!roleDao.existsById("Hospital")) {
            Role userRole = new Role();
            userRole.setRoleName("Hospital");
            userRole.setRoleDescription("Hospital role");
            roleDao.save(userRole);
        }
    }

    public AdminRegister registerNewAdmin(AdminRegister adminRegister) {
        Role role = roleDao.findById("Admin").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        adminRegister.setRole(userRoles);
        adminRegister.setPassword(getEncodedPassword(adminRegister.getPassword()));

        adminDao.save(adminRegister);
        return adminRegister;
    }
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Hospital addHospital(Hospital hospital) {
     //   logger.info("Starting addHospital for hospital name: {}", hospital.getEmail());



        Role adminRole = roleDao.findById("Hospital").orElseThrow(() -> new RuntimeException("Role not found"));
        // Set the role to the hospital
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        hospital.setRole(roles); // Set the fetched role

        Hospital savedHospital =  hospitalDao.save(hospital);

        otpService.sendHospitalAddedEmail(hospital);

        return savedHospital;
    }

    // Method to retrieve a hospital by ID
    public Hospital getHospitalById(Long id) {
        return hospitalDao.findById(id).orElse(null);
    }

    // Method to retrieve all hospitals
    public List<Hospital> getAllHospitals() {
        return hospitalDao.findAll();
    }

    // Method to update a hospital
    public Hospital updateHospital(Long id, Hospital hospital) {
        if (hospitalDao.existsById(id)) {
            hospital.setId(id);
            return hospitalDao.save(hospital);
        } else {
            return null;
        }
    }

    // Method to delete a hospital
    public boolean deleteHospital(Long id) {
        if (hospitalDao.existsById(id)) {
            hospitalDao.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public AddBenificiary addBenificiary(AddBenificiary addBenificiary) {

        // Find the maximum id and increment it for the new beneficiary
        Long lastId = benificiaeyDao.findMaxId();
        Long newId = (lastId == null) ? 1L : lastId + 1;

        // Generate the card number
        String cardNo = "JHMS" + newId + Year.now().getValue();
        addBenificiary.setCardNo(cardNo);

        // Save the beneficiary object and retrieve the saved instance
        AddBenificiary savedBenificiary = benificiaeyDao.save(addBenificiary);

        // Set the relationship in the dependent objects
        for (BenificiaryCardDependents bcd : addBenificiary.getBenificiaryCardDependents()) {
            bcd.setAddBenificiary(savedBenificiary);
        }

        // Save all the dependent objects
        benificiaryCardDependentsDao.saveAll(addBenificiary.getBenificiaryCardDependents());

        // Return the saved beneficiary object
        return savedBenificiary;
    }



    //   return benificiaeyDao.save(addBenificiary);


    public Hospital findByEmail(String email) {
        return hospitalDao.findByEmail(email);
    }

    public List<AddBenificiary> getAllBenificiaries() {
       return benificiaeyDao.findAll();
    }

    public AddBenificiary getBenificiaryById(Long id) {
        return benificiaeyDao.findById(id).orElse(null);

    }


    public AddBenificiary updateBenificiary(Long id, AddBenificiary updatedBenificiary) {
        // Fetch the existing beneficiary by ID
        AddBenificiary existingBenificiary = benificiaeyDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benificiary not found"));

        // Update basic fields of the beneficiary
        existingBenificiary.setFullName(updatedBenificiary.getFullName());
        existingBenificiary.setAddress(updatedBenificiary.getAddress());
        existingBenificiary.setPhoneNumber(updatedBenificiary.getPhoneNumber());
        existingBenificiary.setDateOfJoining(updatedBenificiary.getDateOfJoining());
        existingBenificiary.setDateOfRetirement(updatedBenificiary.getDateOfRetirement());
        existingBenificiary.setDateOfBirth(updatedBenificiary.getDateOfBirth());
        existingBenificiary.setCardIssueDate(updatedBenificiary.getCardIssueDate());
        existingBenificiary.setAadharNo(updatedBenificiary.getAadharNo());
        existingBenificiary.setDepartmentName(updatedBenificiary.getDepartmentName());
        existingBenificiary.setDepartmentLocation(updatedBenificiary.getDepartmentLocation());
        existingBenificiary.setDesignation(updatedBenificiary.getDesignation());
        existingBenificiary.setCardNo(updatedBenificiary.getCardNo());

        // Get current dependents
        List<BenificiaryCardDependents> currentDependents = existingBenificiary.getBenificiaryCardDependents();

        // Remove existing dependents if necessary to avoid duplicates
        currentDependents.clear();

        // Update or add new dependents
        for (BenificiaryCardDependents updatedDependent : updatedBenificiary.getBenificiaryCardDependents()) {
            if (updatedDependent.getId() != null) {
                // Find the existing dependent by ID and update it
                BenificiaryCardDependents existingDependent = benificiaryCardDependentsDao.findById(updatedDependent.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Dependent not found"));

                existingDependent.setName(updatedDependent.getName());
                existingDependent.setGender(updatedDependent.getGender());
                existingDependent.setAge(updatedDependent.getAge());
                existingDependent.setRelation(updatedDependent.getRelation());

                // Add updated dependent to the existing beneficiary
                benificiaryCardDependentsDao.save(existingDependent);
                //existingBenificiary.getBenificiaryCardDependents().add(existingDependent);
            } else {
                // If no ID, it's a new dependent, so create and add it
                updatedDependent.setAddBenificiary(existingBenificiary); // Link it to the beneficiary
                existingBenificiary.getBenificiaryCardDependents().add(updatedDependent);
            }
        }

        // Save the updated beneficiary (with dependents)
        return benificiaeyDao.save(existingBenificiary);
    }



    public List<Hospital> AllhospitalsList() {
        return hospitalDao.findAll();
    }
}