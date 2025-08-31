package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CrearSolicitudDto;
import co.com.bancolombia.api.dto.LecturaSolicitudDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final String PATH_SOLICITUD = "/api/v1/solicitudes";
    private final Handler solicitudHandler;

    @Bean
    @RouterOperations({
            // Crear solicitudes
            @RouterOperation(
                    path = PATH_SOLICITUD,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveRequest",
                    operation = @Operation(
                            operationId = "saveSolicitud",
                            summary = "Crear una Solicitud",
                            tags = {"Solicitud"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de la solicitud a crear",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CrearSolicitudDto.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo creación solicitud",
                                                    value = "{\n" +
                                                            "\"monto\":1000000,\n" +
                                                            "\"plazo\":12,\n" +
                                                            "\"documentoIdentificacion\":\"1003717195\",\n" +
                                                            "\"idTipoPrestamo\":\"24a2ae52-30e8-47c9-9036-d1c69a91e6d7\"\n" +
                                                            "}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "solicitud creada exitosamente",
                                            content = @Content(schema = @Schema(implementation = LecturaSolicitudDto.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }

                    )
            ),
            // Listar solicitudes
            @RouterOperation(
                    path = PATH_SOLICITUD,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenFindAllRequest",
                    operation = @Operation(
                            operationId = "findAllRequest",
                            summary = "Listar todas las solicitudes",
                            tags = {"Solicitudes"},
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Lista de solicitudes",
                                    content = @Content(
                                            array = @ArraySchema(schema = @Schema(implementation = LecturaSolicitudDto.class))
                                    )
                            )
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(
                POST(PATH_SOLICITUD), solicitudHandler::listenSaveRequest)
                .andRoute(GET(PATH_SOLICITUD), solicitudHandler::listenFindAllRequest);
    }
}
