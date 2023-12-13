package com.example.demo.Service;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Auth.ResponseHandler;
import com.example.demo.Entity.Customer.Customer;
import com.example.demo.Entity.User.User;
import com.example.demo.Redis.RedisServiceImp;
import com.example.demo.Repository.CustomerRepo.CustomerRepository;
import com.example.demo.Repository.UserRepo.RoleRepository;
import com.example.demo.Repository.UserRepo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final RedisServiceImp redisServiceImp;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> getAllCustomer() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ROLE_ADMIN, trả về danh sách tất cả người dùng
                List<Object> customerList = redisServiceImp.getAll("customers");
                if(customerList == null) {
                    customerList = Collections.singletonList(customerRepository.findCustomers());
                    if(customerList != null) {
                        redisServiceImp.saveAll(customerList,"customers");
                    }
                    return ResponseHandler.responseBuilder("Lấy danh sách Customers từ sql thành công", HttpStatus.OK, customerList);
                }
                return ResponseHandler.responseBuilder("Lấy danh sách Customers từ redis thành công", HttpStatus.OK, customerList);
            } else if (hasUserAuthority(authentication)) {
                // Người dùng có quyền ROLE_USER, trả về danh sách người dùng có quyền ROLE_USER
                List<User> userList = userRepository.findUsersByRoleName("ROLE_USER");
                return ResponseHandler.responseBuilder("Lấy danh sách Users \"ROLE_USER\" thành công", HttpStatus.OK, userList);
            } else {
                // Người dùng không có quyền hạn thích hợp
                return ResponseHandler.responseBuilder("Bạn không có quyền truy cập danh sách người dùng", HttpStatus.UNAUTHORIZED, "");
            }
        } else {
            // Không thể xác định thông tin người dùng từ token
            return ResponseHandler.responseBuilder("Lỗi token", HttpStatus.BAD_REQUEST, "");
        }
    }

    @Override
    public ResponseEntity<Object> getCustomerById(Integer id) {
        return ResponseHandler.responseBuilder("Lấy Customer thành công", HttpStatus.OK,customerRepository.findById(id));
    }

    @Override
   public ResponseEntity<Object> addNewCustomer(RegisterRequest request) {
       if (customerRepository.findCustomerByEmail(request.getEmail()).isPresent()) {
           return ResponseHandler.responseBuilder("Email đã tồn tại", HttpStatus.CONFLICT, "");
       }

        // Tạo Custommer mới
        Customer customer = new Customer(null, request.getFullname(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        // Lưu User vào cơ sở dữ liệu
        saveCustomer(customer);

        return ResponseHandler.responseBuilder("Thêm mới Customer thành công", HttpStatus.CREATED, "");
   }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    @Override
    public ResponseEntity<Object> deleteCustomer(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ADMIN, tiếp tục xử lý xóa người dùng
                Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalStateException("Customer có id " + id + " không tồn tại"));
                // Kiểm tra nếu người dùng đang cố gắng xóa chính mình
                String currentCustomerName = authentication.getName();
                if (customer.getEmail().equals(currentCustomerName)) {
                    return ResponseHandler.responseBuilder("Bạn không thể xóa chính mình", HttpStatus.FORBIDDEN, "");
                }
                customer.setDeleted(true);
                return ResponseHandler.responseBuilder("Xóa thành công", HttpStatus.OK, "");
            } else {
                // Người dùng không có quyền hạn thích hợp
                return ResponseHandler.responseBuilder("Bạn không có quyền xóa User này", HttpStatus.UNAUTHORIZED, "");
            }
        } else {
            // Không thể xác định thông tin người dùng từ token
            return ResponseHandler.responseBuilder("Lỗi token", HttpStatus.BAD_REQUEST, "");
        }
    }


    @Override
    public ResponseEntity<Object> updateCustomer(Integer id, String fullname, String email, String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra quyền của người dùng hiện tại
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ROLE_ADMIN, có thể cập nhật thông tin tất cả các người dùng
                Customer customer = customerRepository.findById(id)
                        .orElseThrow(() -> new IllegalStateException("Customer có id " + id + " không tồn tại"));
                updateCustomerFields(customer, fullname, email, password);
                return ResponseHandler.responseBuilder("Cập nhật thành công", HttpStatus.OK, "");
            } else {
                // Người dùng không có quyền hạn thích hợp
                return ResponseHandler.responseBuilder("Bạn không có quyền thực hiện chức năng này", HttpStatus.UNAUTHORIZED, "");
            }
        } else {
            // Không thể xác định thông tin người dùng từ token
            return ResponseHandler.responseBuilder("lỗi token", HttpStatus.BAD_REQUEST, "");
        }
    }

    private boolean hasAdminAuthority(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean hasUserAuthority(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

    private void updateCustomerFields(Customer customer, String fullname, String email, String password) {
        if (fullname != null && !fullname.isEmpty() && !Objects.equals(customer.getFullname(), fullname)) {
            customer.setFullname(fullname);
        }
        if (password != null && !password.isEmpty() && !Objects.equals(customer.getPassword(), password)) {
            customer.setPassword(passwordEncoder.encode(password));
        }
        if (email != null && !email.isEmpty() && !Objects.equals(customer.getEmail(), email)) {
            if (customerRepository.findCustomerByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }
            customer.setEmail(email);
        }
    }

}

