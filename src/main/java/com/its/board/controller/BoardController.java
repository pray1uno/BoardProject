package com.its.board.controller;

import com.its.board.DTO.BoardDTO;
import com.its.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/save")
    public String saveForm() {
        return "boardPages/boardSave";
    }

    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    @GetMapping("/board/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "boardPages/boardList";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id,
                           Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("findById", boardDTO);
        return "boardPages/boardDetail";
    }

    @GetMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    @GetMapping("/board/delete/")
    public String exDelete(@RequestParam("id") Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    @GetMapping("/board/update/")
    public String updateForm(@RequestParam("id") Long id,
                             Model model) {
       BoardDTO boardDTO  = boardService.findById(id);
       model.addAttribute("update", boardDTO);

       return "boardPages/boardUpdate";
    }

    @PostMapping("/board/update/")
    public String update(@ModelAttribute BoardDTO boardDTO) {
        boardService.update(boardDTO);
        return "redirect:/board/";
    }


}
