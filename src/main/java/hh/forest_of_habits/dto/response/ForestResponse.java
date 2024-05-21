package hh.forest_of_habits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForestResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<TreeResponse> trees;
}
