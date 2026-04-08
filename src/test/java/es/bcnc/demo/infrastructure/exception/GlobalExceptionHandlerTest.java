package es.bcnc.demo.infrastructure.exception;

import es.bcnc.demo.application.service.PriceService;
import es.bcnc.demo.infrastructure.mapper.PriceMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceService priceService;

    @MockitoBean
    private PriceMapper priceMapper;

    /**
     * Verifica que cuando el servicio lanza {@code PriceNotFoundException}
     * la respuesta es HTTP 404 con el mensaje correspondiente en el cuerpo JSON.
     */
    @Test
    void shouldReturn404WhenPriceNotFound() throws Exception {
        when(priceService.getApplicablePrice(any(), any(), any()))
                .thenThrow(new PriceNotFoundException("Price not found"));

        mockMvc.perform(get("/api/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Price not found"));
    }

    /**
     * Verifica que cuando el servicio lanza {@code BrandNotFoundException}
     * la respuesta es HTTP 404.
     */
    @Test
    void shouldReturn404WhenBrandNotFound() throws Exception {
        when(priceService.getApplicablePrice(any(), any(), any()))
                .thenThrow(new BrandNotFoundException(99L));

        mockMvc.perform(get("/api/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No brand found for id=99"));
    }

    /**
     * Verifica que cuando se envía un parámetro con formato inválido
     * la respuesta es HTTP 400 Bad Request.
     */
    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("applicationDate", "invalid")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }
}
