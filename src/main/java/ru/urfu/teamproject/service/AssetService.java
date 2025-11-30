package ru.urfu.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.teamproject.Entity.Asset;
import ru.urfu.teamproject.Entity.AssetStatus;
import ru.urfu.teamproject.Entity.AssetType;
import ru.urfu.teamproject.Entity.User;
import ru.urfu.teamproject.dto.AssetDto;
import ru.urfu.teamproject.dto.CreateAssetRequest;
import ru.urfu.teamproject.repository.AssetRepository;
import ru.urfu.teamproject.repository.AssetStatusRepository;
import ru.urfu.teamproject.repository.AssetTypeRepository;
import ru.urfu.teamproject.repository.UserActiveRepository;
import ru.urfu.teamproject.Entity.UserActive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetStatusRepository statusRepository;
    private final AssetTypeRepository typeRepository;
    private final UserActiveRepository userActiveRepository;
    private final UserService userService;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String resolveOwnerFullName(Asset asset) {
        return userActiveRepository
                .findFirstByActiveAndIsApprovedTrue(asset)
                .map(UserActive::getUser)
                .map(User::getFullName)
                .orElse(null);
    }

    private AssetDto toDto(Asset asset) {
        return AssetDto.builder()
                .name(asset.getName())
                .status(asset.getStatus().getName())
                .date_create(asset.getDateCreate().format(FORMATTER))
                .owner(resolveOwnerFullName(asset))
                .inventory_number(asset.getInventoryNumber())
                .type_object(asset.getTypeObject().getName())
                .serial_number(asset.getSerialNumber())
                .build();
    }

    public List<AssetDto> findByOwner(User owner) {
        return userActiveRepository.findByUserAndIsApprovedTrue(owner).stream()
                .map(UserActive::getActive)
                .map(this::toDto)
                .toList();
    }

    public List<AssetDto> findAll() {
        return assetRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void changeStatus(String inventoryNumber, String newStatusName) {
        Asset asset = assetRepository.findByInventoryNumber(inventoryNumber)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        boolean isSoft = asset.getTypeObject().isSoft();

        AssetStatus newStatus = statusRepository
                .findByNameAndIsSoft(newStatusName, isSoft)
                .orElseThrow(() -> new IllegalArgumentException("Status not found for this type"));

        asset.setStatus(newStatus);
        assetRepository.save(asset);
    }

    public List<String> getAllStatusesByType(String typeName) {
        AssetType type = typeRepository.findByName(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Type not found"));

        return statusRepository.findByIsSoft(type.isSoft()).stream()
                .map(AssetStatus::getName)
                .toList();
    }

    public List<String> getAllTypes() {
        return typeRepository.findAll().stream()
                .map(AssetType::getName)
                .toList();
    }

    @Transactional
    public Asset createAsset(CreateAssetRequest dto) {
        // 1. Тип
        AssetType type = typeRepository.findByName(dto.getType_object())
                .orElseThrow(() -> new IllegalArgumentException("Тип актива не найден: " + dto.getType_object()));

        // 2. Статус (по имени + isSoft типа)
        AssetStatus status = statusRepository.findByNameAndIsSoft(dto.getStatus(), type.isSoft())
                .orElseThrow(() -> new IllegalArgumentException("Статус не найден для данного типа: " + dto.getStatus()));

        // 3. Создаём сам актив

        assetRepository.findByInventoryNumber(dto.getInventory_number())
                .ifPresent(a -> {
                    throw new IllegalArgumentException(
                            "Актив с таким инвентарным номером уже существует: " + dto.getInventory_number()
                    );
                });

        Asset asset = Asset.builder()
                .name(dto.getName())
                .typeObject(type)
                .status(status)
                .inventoryNumber(dto.getInventory_number())
                .serialNumber(dto.getSerial_number())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .dateCreate(LocalDateTime.now())
                .build();

        Asset saved = assetRepository.save(asset);

        // 4. Привязка к владельцу (если указан)
        if (dto.getOwner() != null && !dto.getOwner().isBlank()) {
            userService.findByFullName(dto.getOwner()).ifPresent(user -> {
                UserActive ua = UserActive.builder()
                        .active(saved)
                        .user(user)
                        .isApproved(true)
                        .build();
                userActiveRepository.save(ua);
            });
        }

        return saved;
    }



}