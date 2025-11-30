package ru.urfu.teamproject.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.urfu.teamproject.Entity.Asset;
import ru.urfu.teamproject.Entity.User;
import ru.urfu.teamproject.dto.*;
import ru.urfu.teamproject.service.AssetService;
import ru.urfu.teamproject.service.UserService;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class ApiController {

    private final UserService userService;
    private final AssetService assetService;

    // -------- user_verify --------
    @PostMapping("/user_verify")
    public ResponseEntity<?> userVerify(@Valid @RequestBody AuthRequest request,
                                        HttpServletResponse response) {

        Optional<User> userOpt = userService.authenticate(request.getLogin(), request.getPassword());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthErrorResponse("Ошибка аутентификации"));
        }

        User user = userOpt.get();

// user_id как был — норм, это просто число
        Cookie userIdCookie = new Cookie("user_id", user.getId().toString());
        userIdCookie.setPath("/");
        userIdCookie.setHttpOnly(true);
        response.addCookie(userIdCookie);

// А вот username нужно закодировать, чтобы не было пробелов/кириллицы в сыром виде
        String encodedUsername = URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8);
        Cookie usernameCookie = new Cookie("username", encodedUsername);
        usernameCookie.setPath("/");
        response.addCookie(usernameCookie);

        return ResponseEntity.ok(new AuthSuccessResponse(user.getFullName(), user.getId()));
    }

    // -------- get_all_activites --------
    // Запрос пустой, имя пользователя берётся из куки "username"
    @GetMapping("/get_all_activites")
    public ActivitiesResponse getAllActivities(
            @CookieValue(name = "username", required = false) String encodedUsername) {

        String username = null;
        if (encodedUsername != null) {
            username = URLDecoder.decode(encodedUsername, StandardCharsets.UTF_8);
        }

        List<AssetDto> assets;
        if (username != null) {
            User owner = userService.findByFullName(username).orElse(null);
            if (owner != null) {
                assets = assetService.findByOwner(owner);
            } else {
                assets = List.of();
            }
        } else {
            assets = assetService.findAll();
        }

        return new ActivitiesResponse(assets);
    }

    // -------- user_browse --------
    @PostMapping("/user_browse")
    public ResponseEntity<?> userBrowse(@Valid @RequestBody UserBrowseRequest request) {
        // по ФИО берём пользователя
        Optional<User> userOpt = userService.findByFullName(request.getOwner());
        if (userOpt.isEmpty()) {
            // можно вернуть пустой список или 404
            return ResponseEntity.ok(new UserBrowseResponse(List.of()));
        }

        List<AssetDto> assets = assetService.findByOwner(userOpt.get());
        return ResponseEntity.ok(new UserBrowseResponse(assets));
    }

    // -------- get_all_status --------
    @PostMapping("/get_all_status")
    public ResponseEntity<?> getAllStatus(@Valid @RequestBody GetAllStatusRequest request) {
        List<String> statuses = assetService.getAllStatusesByType(request.getType_object());
        return ResponseEntity.ok(new GetAllStatusResponse(statuses));
    }

    // -------- change_status --------
    @PostMapping("/change_status")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody ChangeStatusRequest request) {
        assetService.changeStatus(request.getInventory_number(), request.getNew_status());
        return ResponseEntity.ok(new ChangeStatusResponse("Ok"));
    }

    // -------- get_all_type_object --------
    @GetMapping("/get_all_type_object")
    public ResponseEntity<?> getAllTypeObject() {
        List<String> types = assetService.getAllTypes();
        return ResponseEntity.ok(new GetAllTypesResponse(types));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(
                    request.getLogin(),
                    request.getPassword(),
                    request.getFullName()
            );
            return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getFullName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthErrorResponse(e.getMessage()));
        }
    }


    @PostMapping("/add_active")
    public ResponseEntity<?> addActive(@Valid @RequestBody CreateAssetRequest request) {
        try {
            Asset asset = assetService.createAsset(request);
            return ResponseEntity.ok(new CreateAssetResponse("Ok", asset.getInventoryNumber()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthErrorResponse(e.getMessage()));
        }
    }




}