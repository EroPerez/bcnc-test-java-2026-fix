package es.bcnc.demo.application.service;

import es.bcnc.demo.application.service.impl.PriceServiceImpl;
import es.bcnc.demo.domain.model.Price;
import es.bcnc.demo.domain.port.PriceRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepositoryPort repositoryPort;

    @InjectMocks
    private PriceServiceImpl priceService;

    private Price domainPrice;

    @BeforeEach
    void setUp() {
        domainPrice = Price.builder()
                .brandId(1L)
                .brandName("ZARA")
                .productId(35455L)
                .priceList(1L)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .price(35.50)
                .currency("EUR")
                .build();
    }

    @Test
    void shouldReturnPriceWhenFound() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        when(repositoryPort.findApplicablePrice(35455L, 1L, date)).thenReturn(domainPrice);

        Price result = priceService.getApplicablePrice(1L, 35455L, date);

        assertThat(result.getBrandId()).isEqualTo(1L);
        assertThat(result.getBrandName()).isEqualTo("ZARA");
        assertThat(result.getProductId()).isEqualTo(35455L);
        assertThat(result.getPriceList()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(35.50);
        assertThat(result.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
        assertThat(result.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        assertThat(result.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        when(repositoryPort.findApplicablePrice(35455L, 1L, date))
                .thenThrow(new RuntimeException("No price found for brandId=1, productId=35455"));

        assertThatThrownBy(() -> priceService.getApplicablePrice(1L, 35455L, date))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No price found");
    }
}
