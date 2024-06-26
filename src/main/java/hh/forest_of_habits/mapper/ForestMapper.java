package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ForestMapper {
    public abstract List<ForestResponse> mapAll(List<Forest> s);

    @Mapping(target = "totalNumberTrees", expression = "java(s.getTrees() == null ? 0 : s.getTrees().size())")
    public abstract ForestResponse map(Forest s);

    public abstract Forest map(ForestRequest s);

    public abstract void update(@MappingTarget Forest forest, ForestRequest request);
}
