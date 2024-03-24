package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.TreeDTO;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;

import java.time.LocalDateTime;

public class TreeMapper {
    public static Tree toTree(TreeDTO dto, Long id) {
        return Tree.builder()
                .id(id)
                .name(dto.getName())
                .createdAt(dto.getCreatedAt() == null ? LocalDateTime.now() : dto.getCreatedAt())
                .description(dto.getDescription())
                .limit(dto.getLimit())
                .type(dto.getType())
                .period(dto.getPeriod())
                .build();
    }
    public static Tree toTree(TreeDTO dto) {
        return Tree.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt() == null ? LocalDateTime.now() : dto.getCreatedAt())
                .description(dto.getDescription())
                .limit(dto.getLimit())
                .type(dto.getType())
                .period(dto.getPeriod())
                .build();
    }

    public static TreeDTO toTreeDto(Tree tree) {
        return TreeDTO.builder()
                .id(tree.getId())
                .name(tree.getName())
                .createdAt(tree.getCreatedAt() == null ? LocalDateTime.now() : tree.getCreatedAt())
                .description(tree.getDescription())
                .limit(tree.getLimit())
                .type(tree.getType())
                .period(tree.getPeriod())
                .build();
    }
}
