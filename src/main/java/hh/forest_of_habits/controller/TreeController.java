package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.TreeDTO;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tree")
public class TreeController {
    private final TreeService treeService;

    @GetMapping("/all/{id}")
    public List<TreeDTO> getAllByForestId(@PathVariable Long id) {
        return treeService.getAllByForestId(id);
    }

    @PostMapping
    TreeDTO create(@RequestBody TreeDTO dto) {
        return treeService.create(dto);
    }

    @GetMapping("/{id}")
    TreeDTO getById(@PathVariable Long id) {
        return treeService.getById(id);
    }

    @PutMapping("/{id}")
    TreeDTO update(@PathVariable Long id, @RequestBody TreeDTO dto) {
        return treeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        treeService.delete(id);
    }

}
