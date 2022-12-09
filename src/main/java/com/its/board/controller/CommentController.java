package com.its.board.controller;

import com.its.board.DTO.CommentDTO;
import com.its.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

//    @PostMapping("/comment/save")
//    public @ResponseBody List<CommentDTO> CommentSave(@ModelAttribute CommentDTO commentDTO) {
//        commentService.save(commentDTO);
//        List<CommentDTO> result = commentService.findAll(commentDTO.getBoardId());
//        return result;
//    }

    @PostMapping("/comment/save")
    public ResponseEntity save(@RequestBody CommentDTO commentDTO) {
        commentService.save(commentDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
}
