package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.TreeDTO;

import java.util.List;

public interface TreeService {
    List<TreeDTO> getAllByForestId(Long forestId);

    TreeDTO getById(Long id);

    TreeDTO create(TreeDTO forest);

    TreeDTO update(Long id, TreeDTO forest);

    void delete(Long id);
}
