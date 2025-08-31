package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.CrearSolicitudDto;
import co.com.bancolombia.api.dto.LecturaSolicitudDto;
import co.com.bancolombia.model.solicitud.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {
    LecturaSolicitudDto toResponse(Solicitud solicitud);

    Solicitud toModel(CrearSolicitudDto solicitudDto);

}
