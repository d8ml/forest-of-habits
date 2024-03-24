package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.TreeDTO;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {

    private final ForestRepository forestRepository;
    private final TreeRepository treeRepository;

    @Override
    public List<TreeDTO> getAllByForestId(Long forestId) {
        if (forestRepository.existsById(forestId)) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", forestId));
        }
        return treeRepository.findAllByForestId(forestId).stream().map(TreeMapper::toTreeDto).toList();
    }

    @Override
    public TreeDTO getById(Long id) {
        return TreeMapper.toTreeDto(treeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Дерево с id = %d не найдено", id))));
    }

    @Override
    public TreeDTO create(TreeDTO dto) {
        if (forestRepository.existsById(dto.getForestId())) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", dto.getForestId()));
        }
        return TreeMapper.toTreeDto(treeRepository.save(TreeMapper.toTree(dto)));
    }

    @Override
    public TreeDTO update(Long id, TreeDTO dto) {
        if (treeRepository.existsById(id)) {
            throw new NotFoundException(String.format("Дерево с id = %d не найдено", id));
        }
        if (forestRepository.existsById(dto.getForestId())) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", dto.getForestId()));
        }
        return TreeMapper.toTreeDto(treeRepository.save(TreeMapper.toTree(dto, id)));
    }

    @Override
    public void delete(Long id) {
        if (treeRepository.existsById(id)) {
            throw new NotFoundException(String.format("Дерево с id = %d не найдено", id));
        }
        forestRepository.deleteById(id);
    }
}
