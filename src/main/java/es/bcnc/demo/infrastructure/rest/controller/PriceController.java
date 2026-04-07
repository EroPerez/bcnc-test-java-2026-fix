package es.bcnc.demo.infrastructure.rest.controller;

import es.bcnc.demo.application.service.PriceService;
import es.bcnc.demo.infrastructure.mapper.PriceMapper;
import es.bcnc.demo.infrastructure.rest.dto.PriceRequest;
import es.bcnc.demo.infrastructure.rest.dto.PriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que expone el endpoint de consulta de precios.
 * <p>
 * Recibe las peticiones HTTP y delega la lógica de negocio en
 * {@link PriceService}. La validación de los parámetros de entrada
 * se realiza mediante {@code @Valid}.
 * </p>
 */
@Tag(
        name = "Prices",
        description = "Consulta de tarifas de precios aplicables"
                + " por marca, producto y fecha"
)
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    /** Servicio de aplicación para la consulta de precios. */
    private final PriceService priceService;

    /** Mapeador entre modelo de dominio y DTO de respuesta. */
    private final PriceMapper priceMapper;

    /**
     * Devuelve el precio aplicable para una marca, producto y
     * fecha dados.
     *
     * @param request objeto con los parámetros de la consulta
     *                (brandId, productId, applicationDate),
     *                validado automáticamente por Bean Validation
     * @return {@code 200 OK} con el {@link PriceResponse} de la
     *         tarifa aplicable
     */
    @Operation(
            summary = "Obtener tarifa aplicable",
            description = "Devuelve la tarifa vigente de mayor"
                    + " prioridad para la combinación de **marca**,"
                    + " **producto** y **fecha/hora** indicados."
                    + " Si no existe ninguna tarifa para esos"
                    + " parámetros se devuelve HTTP 404."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifa encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PriceResponse.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo tarifa 1",
                                    value = "{"
                                        + "\"productId\": 35455,"
                                        + "\"brandId\": 1,"
                                        + "\"brandName\": \"ZARA\","
                                        + "\"priceList\": 1,"
                                        + "\"startDate\":"
                                        + " \"2020-06-14 00:00:00\","
                                        + "\"endDate\":"
                                        + " \"2020-12-31 23:59:59\","
                                        + "\"price\": 35.50}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de entrada inválidos"
                            + " o ausentes",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Error de validación",
                                    value = "{"
                                        + "\"timestamp\":"
                                        + " \"2020-06-14T10:00:00\","
                                        + "\"status\": 400,"
                                        + "\"error\": \"Bad Request\","
                                        + "\"message\":"
                                        + " \"Validation error\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna tarifa"
                            + " para los parámetros indicados",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Tarifa no encontrada",
                                    value = "{"
                                        + "\"timestamp\":"
                                        + " \"2020-06-14T10:00:00\","
                                        + "\"status\": 404,"
                                        + "\"error\": \"Not Found\","
                                        + "\"message\":"
                                        + " \"No price found\"}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(
                    description = "Parámetros de consulta de precio",
                    required = true)
            @Valid final PriceRequest request
        ) {
        PriceResponse response = priceMapper.toResponse(
                priceService.getApplicablePrice(
                        request.getBrandId(),
                        request.getProductId(),
                        request.getApplicationDate()));

        return ResponseEntity.ok(response);
    }
}
