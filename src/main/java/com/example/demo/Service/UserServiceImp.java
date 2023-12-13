package com.example.demo.Service;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Auth.ResponseHandler;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User.User;
import com.example.demo.Redis.RedisServiceImp;
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

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImp implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final RedisServiceImp redisServiceImp;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> getAllUser() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ROLE_ADMIN, trả về danh sách tất cả người dùng
                List<Object> userList = redisServiceImp.getAll("users");
                if(userList == null) {
                    userList = Collections.singletonList(userRepository.findUsers());
                    if(userList != null) {
                        redisServiceImp.saveAll(userList,"users");
                    }
                    return ResponseHandler.responseBuilder("Lấy danh sách Users từ sql thành công", HttpStatus.OK, userList);
                }
                return ResponseHandler.responseBuilder("Lấy danh sách Users từ redis thành công", HttpStatus.OK, userList);
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
    public ResponseEntity<Object> getUserById(Integer userId) {
        return ResponseHandler.responseBuilder("Lấy User thành công", HttpStatus.OK,userRepository.findById(userId));
    }

    @Override
   public ResponseEntity<Object> addNewUser(RegisterRequest request) {
       if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
           return ResponseHandler.responseBuilder("Email đã tồn tại", HttpStatus.CONFLICT, "");
       }
        // Kiểm tra xem Role đã tồn tại trong cơ sở dữ liệu hay chưa
        Role role = roleRepository.findRoleByName("ROLE_ADMIN");
        if (role == null) {
            // Nếu Role chưa tồn tại, tạo mới và lưu vào cơ sở dữ liệu
            role = new Role("ROLE_ADMIN");
            roleRepository.save(role);
        }

        // Tạo User mới
        User user = new User(null, request.getFullname(), request.getEmail(), passwordEncoder.encode(request.getPassword()), new HashSet<>());
        user.setRoles(Collections.singleton(role)); // Gán vai trò Role cho User

        // Lưu User vào cơ sở dữ liệu
        saveUser(user);

        return ResponseHandler.responseBuilder("Thêm mới User thành công", HttpStatus.CREATED, "");
   }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public ResponseEntity<Object> deleteUser(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ADMIN, tiếp tục xử lý xóa người dùng
                User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User có id " + userId + " không tồn tại"));
                // Kiểm tra nếu người dùng đang cố gắng xóa chính mình
                String currentUsername = authentication.getName();
                if (user.getUsername().equals(currentUsername)) {
                    return ResponseHandler.responseBuilder("Bạn không thể xóa chính mình", HttpStatus.FORBIDDEN, "");
                }
                user.setDeleted(true);
                return ResponseHandler.responseBuilder("Xóa thành công", HttpStatus.OK, "");
            } else if (hasUserAuthority(authentication)) {
                // Người dùng có quyền ROLE_USER, chỉ có thể xóa người dùng có role ROLE_USER
                User currentUser = userRepository.findUserByEmail(authentication.getName())
                        .orElseThrow(() -> new IllegalStateException("Không thể xác định thông tin người dùng hiện tại"));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalStateException("User có id " + userId + " không tồn tại"));

                if (currentUser.getRoles().contains("ROLE_USER") && user.getRoles().contains("ROLE_USER")) {
                    // Người dùng có quyền ROLE_USER chỉ có thể xóa người dùng có quyền ROLE_USER
                    if (currentUser.getId().equals(user.getId())) {
                        // Người dùng không thể tự xóa chính mình
                        return ResponseHandler.responseBuilder("Bạn không thể xóa chính mình", HttpStatus.FORBIDDEN, "");
                    } else {
                        user.setDeleted(true);
                        return ResponseHandler.responseBuilder("Xóa thành công", HttpStatus.OK, "");
                    }
                } else {
                    // Người dùng không có quyền hạn thích hợp
                    return ResponseHandler.responseBuilder("Bạn không có quyền thực hiện chức năng này", HttpStatus.UNAUTHORIZED, "");
                }
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
    public ResponseEntity<Object> updateUser(Integer userId, String fullname, String email, String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra quyền của người dùng hiện tại
        if (authentication != null && authentication.isAuthenticated()) {
            if (hasAdminAuthority(authentication)) {
                // Người dùng có quyền ROLE_ADMIN, có thể cập nhật thông tin tất cả các người dùng
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalStateException("User có id " + userId + " không tồn tại"));
                updateUserFields(user, fullname, email, password);
                return ResponseHandler.responseBuilder("Cập nhật thành công", HttpStatus.OK, "");
            } else if (hasUserAuthority(authentication)) {
                // Người dùng có quyền ROLE_USER, chỉ có thể cập nhật thông tin người dùng có role ROLE_USER
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalStateException("User có id " + userId + " không tồn tại"));
                if (user.getId().equals(userId) && user.getRoles().contains("ROLE_USER")) {
                    updateUserFields(user, fullname, email, password);
                    return ResponseHandler.responseBuilder("Cập nhật thành công", HttpStatus.OK, "");
                } else {
                    // Người dùng không có quyền hạn thích hợp
                    return ResponseHandler.responseBuilder("Bạn không có quyền thực hiện chức năng này", HttpStatus.UNAUTHORIZED, "");
                }
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

    private void updateUserFields(User user, String fullname, String email, String password) {
        if (fullname != null && !fullname.isEmpty() && !Objects.equals(user.getFullname(), fullname)) {
            user.setFullname(fullname);
        }
        if (password != null && !password.isEmpty() && !Objects.equals(user.getPassword(), password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (email != null && !email.isEmpty() && !Objects.equals(user.getEmail(), email)) {
            if (userRepository.findUserByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }
            user.setEmail(email);
        }
    }

}

