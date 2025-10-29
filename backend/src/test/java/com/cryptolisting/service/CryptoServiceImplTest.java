package com.cryptolisting.service;

import com.cryptolisting.dto.CryptoRequest;
import com.cryptolisting.dto.CryptoResponse;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.CryptoAsset;
import com.cryptolisting.entity.UserRole;
import com.cryptolisting.exception.DuplicateResourceException;
import com.cryptolisting.exception.UnauthorizedOperationException;
import com.cryptolisting.repository.CryptoAssetRepository;
import com.cryptolisting.service.filter.CryptoFilterCriteria;
import com.cryptolisting.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoServiceImplTest {

    @Mock
    private CryptoAssetRepository cryptoAssetRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CryptoServiceImpl cryptoService;

    private AppUser owner;

    @BeforeEach
    void setUp() {
        owner = new AppUser("alice", "hashed", Set.of(UserRole.ROLE_USER));
    }

    @Test
    void findAllShouldReturnMappedResponses() {
        CryptoAsset asset = new CryptoAsset("ETH", "Ethereum", "Smart contracts", "Layer1",
                BigDecimal.TEN, OffsetDateTime.now(), true, owner);
        when(cryptoAssetRepository.findAll(any(Specification.class))).thenReturn(List.of(asset));

        List<CryptoResponse> result = cryptoService.findAll(CryptoFilterCriteria.builder().withName("eth").build());

        assertThat(result).hasSize(1);
    }

    @Test
    void createShouldPersistNewAsset() {
        CryptoRequest request = createRequest();
        when(cryptoAssetRepository.existsBySymbolIgnoreCase(anyString())).thenReturn(false);
        when(userService.findByUsername("alice"))
                .thenReturn(owner);
        when(cryptoAssetRepository.save(any(CryptoAsset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        cryptoService.create(request, "alice");

        ArgumentCaptor<CryptoAsset> captor = ArgumentCaptor.forClass(CryptoAsset.class);
        verify(cryptoAssetRepository).save(captor.capture());
        CryptoAsset saved = captor.getValue();
        assertThat(saved.getSymbol()).isEqualTo("ETH");
        assertThat(saved.getCreatedBy()).isEqualTo(owner);
    }

    @Test
    void createShouldFailWhenSymbolExists() {
        CryptoRequest request = createRequest();
        when(cryptoAssetRepository.existsBySymbolIgnoreCase(anyString())).thenReturn(true);

        assertThatThrownBy(() -> cryptoService.create(request, "alice"))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void deleteShouldRemoveWhenOwnerMatches() {
        UUID id = UUID.randomUUID();
        CryptoAsset asset = new CryptoAsset("ETH", "Ethereum", "Smart contracts", "Layer1",
                BigDecimal.TEN, OffsetDateTime.now(), true, owner);
        doReturn(Optional.of(asset)).when(cryptoAssetRepository).findById(id);

        cryptoService.delete(id, "alice");

        verify(cryptoAssetRepository).delete(asset);
    }

    @Test
    void deleteShouldRejectWhenNotOwner() {
        UUID id = UUID.randomUUID();
        AppUser bob = new AppUser("bob", "hashed", Set.of(UserRole.ROLE_USER));
        CryptoAsset asset = new CryptoAsset("ETH", "Ethereum", "Smart contracts", "Layer1",
                BigDecimal.TEN, OffsetDateTime.now(), true, bob);
        doReturn(Optional.of(asset)).when(cryptoAssetRepository).findById(id);

        assertThatThrownBy(() -> cryptoService.delete(id, "alice"))
                .isInstanceOf(UnauthorizedOperationException.class);
    }

    private CryptoRequest createRequest() {
        CryptoRequest request = new CryptoRequest();
        request.setSymbol("eth");
        request.setName("Ethereum");
        request.setDescription("Smart contracts");
        request.setCategory("Layer1");
        request.setMarketCap(BigDecimal.TEN);
        return request;
    }
}
