package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.alert.AlertResponse;
import edu.usta.groccy.entity.Alert;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    AlertResponse toResponse(Alert alert);

    List<AlertResponse> toResponseList(List<Alert> alerts);
}