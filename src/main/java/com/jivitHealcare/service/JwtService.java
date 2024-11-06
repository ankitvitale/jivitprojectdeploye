package com.jivitHealcare.service;

import com.jivitHealcare.Entity.AdminRegister;
import com.jivitHealcare.Entity.Hospital;
import com.jivitHealcare.Entity.JwtRequest;
import com.jivitHealcare.Entity.JwtResponse;
import com.jivitHealcare.Repo.AdminDao;
import com.jivitHealcare.Repo.HospitalDao;
import com.jivitHealcare.Security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtHelper jwtUtil;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();
        String otp = jwtRequest.getOtp();

        authenticate(email, password, otp);

        UserDetails userDetails = loadUserByUsername(email);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        // Determine if the authenticated entity is an admin or a hospital
        AdminRegister admin = adminDao.findByEmail(email);
        if (admin != null) {
            return new JwtResponse(email, newGeneratedToken);
        } else {
            Hospital hospital = hospitalDao.findByEmail(email);
            if (hospital != null) {
                return new JwtResponse(email, newGeneratedToken);
            } else {
                throw new Exception("User not found with email: " + email);
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null ) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        AdminRegister admin = adminDao.findByEmail(email);
        Hospital hospital = hospitalDao.findByEmail(email);

        if (admin == null && hospital == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Collection<GrantedAuthority> authorities = new HashSet<>();
        String password = null;

        if (hospital != null) {
            hospital.getRole().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            });
            password = hospital.getOtp(); // Make sure this is how you handle OTP (consider hashing or securing it appropriately)
        } else if (admin != null) {
            admin.getRole().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            });
            password = admin.getPassword(); // Ensure this password is hashed
        }

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }

    private void authenticate(String email, String password, String otp) throws Exception {
        try {
            // Ensure OTP is verified here if necessary
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
