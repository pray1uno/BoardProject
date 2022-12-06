package com.its.board;

import com.its.board.DTO.BoardDTO;
import com.its.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

//    @Test
//    @Transactional
//    @Rollback(value = true)
//    @DisplayName("save Test")
//    public void saveTest() {
//        BoardDTO boardDTO = new BoardDTO();
//
//        boardDTO.setBoardWriter("제타");
//        boardDTO.setBoardTitle("15일 쇼케이스");
//        boardDTO.setBoardPass("1234");
//        boardDTO.setBoardContents("겨울패치 흥행기원");
//
//        Long saveId = boardService.save(boardDTO);
//        BoardDTO saveBoard = boardService.findById(saveId);
//
//        assertThat(boardDTO.getBoardWriter()).isEqualTo(saveBoard.getBoardWriter());
//    }

    private BoardDTO newBoard(int i) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("데프트" + i);
        boardDTO.setBoardTitle("중요한 것은 꺾이지 않는 마음" + i);
        boardDTO.setBoardPass("1234" + i);
        boardDTO.setBoardContents("10년만의 우승" + i);
        return boardDTO;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("글작성 테스트")
    public void boardSaveTest() {
        BoardDTO boardDTO = newBoard(1);
        Long saveId = boardService.save(boardDTO);
        BoardDTO findBoard = boardService.findById(saveId);
        assertThat(boardDTO.getBoardWriter()).isEqualTo(findBoard.getBoardWriter());

    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("글작성 여러개")
    public void saveList() {
        for (int i=1; i<=20; i++) {
            boardService.save(newBoard(i));
        }

//        const temp = (id) => {
//            console.log(id);
//        }
        // 이것도 반복문임
        IntStream.rangeClosed(21, 40).forEach(i -> {
            boardService.save(newBoard(i));
        });
    }
}
