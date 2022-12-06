package com.its.board.service;

import com.its.board.DTO.BoardDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Long save(BoardDTO boardDTO) {
        return boardRepository.save(BoardEntity.toSaveEntity(boardDTO)).getId();
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        }
        return boardDTOList;
    }


    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional // springframework, 동기화. 안걸면 에러남
    public void updateHits(Long id) {
        boardRepository.updateHits(id);

    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);

        if (optionalBoardEntity.isPresent()) {
            return BoardDTO.toDTO(optionalBoardEntity.get());
        } else {
            return null;
        }
    }

    public void update(BoardDTO boardDTO) {
       BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
       boardRepository.save(boardEntity);
    }
}
