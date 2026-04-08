package es.bcnc.demo.infrastructure.repository;

import es.bcnc.demo.infrastructure.entity.BrandEntity;
import es.bcnc.demo.infrastructure.entity.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaPriceRepositoryTest {

    @Autowired
    private JpaBrandRepository jpaBrandRepository;

    @Autowired
    private JpaPriceRepository jpaPriceRepository;

    private BrandEntity zara;

    @BeforeEach
    void setUp() {
        zara = jpaBrandRepository.save(BrandEntity.builder().id(1L).name("ZARA").build());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");
        jpaPriceRepository.saveAndFlush(PriceEntity.builder().brand(zara)
                .startDate(LocalDateTime.parse("2020-06-14-00.00.00", fmt))
                .endDate(LocalDateTime.parse("2020-12-31-23.59.59", fmt))
                .priceList(1L).productId(35455L).priority(0).price(35.50).currency("EUR").build());
        jpaPriceRepository.saveAndFlush(PriceEntity.builder().brand(zara)
                .startDate(LocalDateTime.parse("2020-06-14-15.00.00", fmt))
                .endDate(LocalDateTime.parse("2020-06-14-18.30.00", fmt))
                .priceList(2L).productId(35455L).priority(1).price(25.45).currency("EUR").build());
        jpaPriceRepository.saveAndFlush(PriceEntity.builder().brand(zara)
                .startDate(LocalDateTime.parse("2020-06-15-00.00.00", fmt))
                .endDate(LocalDateTime.parse("2020-06-15-11.00.00", fmt))
                .priceList(3L).productId(35455L).priority(1).price(30.50).currency("EUR").build());
        jpaPriceRepository.saveAndFlush(PriceEntity.builder().brand(zara)
                .startDate(LocalDateTime.parse("2020-06-15-16.00.00", fmt))
                .endDate(LocalDateTime.parse("2020-12-31-23.59.59", fmt))
                .priceList(4L).productId(35455L).priority(1).price(38.95).currency("EUR").build());
    }

    @Test
    void shouldReturnHighestPriorityPriceWhenOverlapping() {
        // 16:00 del 14/06 solapa tarifa 1 (prioridad 0) y tarifa 2 (prioridad 1)
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Optional<PriceEntity> result = jpaPriceRepository.findApplicablePrice(1L, 35455L, date);

        assertThat(result).isPresent();
        assertThat(result.get().getPriority()).isEqualTo(1);
        assertThat(result.get().getPriceList()).isEqualTo(2L);
        assertThat(result.get().getPrice()).isEqualTo(25.45);
        assertThat(result.get().getBrand().getName()).isEqualTo("ZARA");
    }

    @Test
    void shouldReturnEmptyWhenNoPriceFound() {
        LocalDateTime date = LocalDateTime.of(2019, 1, 1, 0, 0);
        Optional<PriceEntity> result = jpaPriceRepository.findApplicablePrice(1L, 35455L, date);

        assertThat(result).isEmpty();
    }
}
