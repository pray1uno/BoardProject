package com.its.board.controller;

import com.its.board.DTO.BoardDTO;
import com.its.board.DTO.CommentDTO;
import com.its.board.service.BoardService;
import com.its.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/board/save")
    public String saveForm() {
        return "boardPages/boardSave";
    }

    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    @GetMapping("/board/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "boardPages/boardList";
    }

    // /board?page=1
    @GetMapping("/board")
    public String paging(@PageableDefault(page = 1)Pageable pageable,
                         Model model) {
        Page<BoardDTO> boardDTOList = boardService.paging(pageable);
        model.addAttribute("boardList", boardDTOList);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOList.getTotalPages();

        // 삼항연산자 (if/else를 간략하게 표현한 것)
        int test = 10;
        int num = (test > 5) ? test: 100;
        if (test > 5) {
            num = test;
        } else {
            num = 100;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardPages/paging";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id,
                           Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        if (commentDTOList.size() > 0) {
            model.addAttribute("commentList", commentDTOList);
        } else {
            model.addAttribute("commentList", "empty");
        }
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

    @PutMapping("/board/{id}")
    public ResponseEntity updateByAxios(@RequestBody BoardDTO boardDTO) {
        boardService.update(boardDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
