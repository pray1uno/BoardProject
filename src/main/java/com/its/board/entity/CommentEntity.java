package com.its.board.entity;

import com.its.board.DTO.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column(length = 200, nullable = false)
    private String commentContents;

    @Column
    private LocalDateTime commentCreatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity; // 이렇게 연관관계 지정을 했기 때문에 ↓ 매개변수 넘길 때 부모 엔티티가 와야함

    public static CommentEntity toSaveCommentEntity(BoardEntity entity, CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setCommentCreatedTime(commentDTO.getCommentCreatedTime());
        commentEntity.setBoardEntity(entity);
        return commentEntity;
    }

}
