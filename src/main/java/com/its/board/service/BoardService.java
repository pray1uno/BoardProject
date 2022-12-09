package com.its.board.service;

import com.its.board.DTO.BoardDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.BoardFileEntity;
import com.its.board.repository.BoardFileRepository;
import com.its.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
//        if (boardDTO.getBoardFile().isEmpty()) { // 파일이 1개일때만 가능한 조건
        if (boardDTO.getBoardFile() == null || boardDTO.getBoardFile().size() == 0) {
            System.out.println("파일없음");
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            return boardRepository.save(boardEntity).getId();
//        } else { // 파일이 1개일때
//            System.out.println("파일있음");
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            String originalFileName = boardFile.getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
//            String savePath = "D:\\spring_boot_img\\" + storedFileName;
//            boardFile.transferTo(new File(savePath));
//
//            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
//            Long saveId = boardRepository.save(boardEntity).getId();
//
//            BoardEntity entity = boardRepository.findById(saveId).get();
//            BoardFileEntity boardFileEntity = BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
//            boardFileRepository.save(boardFileEntity);
//            return saveId;
//        }
        } else { // 파일이 여러개 일때
            System.out.println("파일있음");
            // 게시글 정보를 먼저 저장하고 해당 게시글의 entity를 가져옴
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(boardEntity).getId();
            // 파일이 담긴 List를 반복문으로 접근하여 하나씩 이름 가져오고, 저장용 이름 만들고
            // 로컬 경로에 저장하고 board_file_table에 저장
            BoardEntity entity = boardRepository.findById(saveId).get();
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "D:\\spring_boot_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
            return saveId;
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        // findAll desc 정렬하는 방법

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

    @Transactional // 부모엔티티에서 자식엔티티를 직접 가져올 때 필요
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

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // 사용자가 요청하는 페이지 값 보다 하나 적은 값이어야 함
        final int pageLimit = 3;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // Page<> << spring에서 제공하는 인터페이스
        Page<BoardDTO> boardList = boardEntities.map(
                // boardEntities에 담긴 boardEntity 객체를 board에 담아서
                // boardDTO 객체로 하나씩 옮겨 담는 과정
                board -> new BoardDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getCreatedTime(),
                        board.getBoardHits()
                )
        );
        return boardList;
    }
}
