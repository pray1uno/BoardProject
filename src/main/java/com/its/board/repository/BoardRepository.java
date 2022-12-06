package com.its.board.repository;

import com.its.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // 인터페이스 이기 때문에 추상 메서드만 선언 가능
    // update board_table set board_hits = board_hits +1 where id=?
    @Modifying // update, delete 쿼리의 경우 써주는 어노테이션
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id= :id")
    // entity 기준으로 작성, 약칭을 반드시 붙일 것. 약칭으로 접근. entity 컬럼 이름으로 접근
    void updateHits(@Param("id") Long id); // Param 값이 where : 와 일치
    // @Query(value = "update board_table set board_hits = board_hits +1 where id= :id", nativeQuery = true) // 이렇게도 가능

}
