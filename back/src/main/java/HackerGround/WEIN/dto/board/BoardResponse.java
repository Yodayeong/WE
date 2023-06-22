package HackerGround.WEIN.dto.board;

import HackerGround.WEIN.domain.board.Board;
import HackerGround.WEIN.domain.comment.Review;
import HackerGround.WEIN.dto.review.ReviewResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardResponse {

    private String boardTitle;
    private String description;
    private LocalDateTime createTime;
    private Integer viewCount;
    private Integer heartCount;

    public BoardResponse(Board board) {
        this.boardTitle = board.getBoardTitle();
        this.description = board.getDescription();
        this.createTime = board.getCreateTime();
        this.viewCount = board.getViewCount();
        this.heartCount = board.getHeartCount();
    }

    public static BoardResponse toDto(Board board) {
        return new BoardResponse(board);
    }
}
