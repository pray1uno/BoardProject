package com.its.board.service;

import com.its.board.DTO.BoardDTO;
import com.its.board.DTO.CommentDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId()).get();
        CommentEntity commentEntity = CommentEntity.toSaveCommentEntity(boardEntity, commentDTO);
        Long id = commentRepository.save(commentEntity).getId();
        return id;
    }

    @Transactional // 2번 사용 시
    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id = ?
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        // 1. comment_table에서 직접 해당 게시글의 댓글 목록을 가져오기
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        // 2. BoardEntity를 조회해서 댓글 목록 가져오기
//        List<CommentEntity> commentEntityList1 = boardEntity.getCommentEntityList();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }
        return commentDTOList;
    }
}



// 수업전 작업한 내용
//    public void save(CommentDTO commentDTO) {
//        Optional<BoardEntity> entity = boardRepository.findById(commentDTO.getBoardId());
//
//        BoardEntity boardEntity = entity.get();
//
//        CommentEntity commentEntity = CommentEntity.toSaveCommentEntity(boardEntity, commentDTO);
//        commentRepository.save(commentEntity);
//
//    }



//    public List<CommentDTO> findAll(Long boardId) {
//        List<CommentEntity> result = commentRepository.findAllById(boardId);
//
//        List<CommentDTO> list = new ArrayList<>();
//
//        for (CommentEntity entity: result) {
//            list.add(CommentDTO.toCommentDTO(entity));
//        }
//        return list;
//    }
//}
