package HackerGround.WEIN.api.controller;

import HackerGround.WEIN.domain.board.Board;
import HackerGround.WEIN.domain.member.Member;
import HackerGround.WEIN.dto.board.BoardDeleteRequest;
import HackerGround.WEIN.dto.board.BoardModifyRequest;
import HackerGround.WEIN.dto.board.BoardRequest;
import HackerGround.WEIN.dto.board.BoardResponse;
import HackerGround.WEIN.dto.heart.HeartRequest;
import HackerGround.WEIN.dto.member.MemberRequest;
import HackerGround.WEIN.dto.member.MemberResponse;
import HackerGround.WEIN.model.response.CommonResult;
import HackerGround.WEIN.model.response.ListResult;
import HackerGround.WEIN.model.response.SingleResult;
import HackerGround.WEIN.service.BoardService;
import HackerGround.WEIN.service.HeartService;
import HackerGround.WEIN.service.MemberService;
import HackerGround.WEIN.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardApiController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final ResponseService responseService;

    private final HeartService heartService;

    /**
     *
     * 단건 조회
     */
    @GetMapping("/board/{id}")
    public SingleResult<BoardResponse> getOneBoard(@PathVariable Long id) throws Exception {
        Board board = boardService.findById(id);
        board.updateViewCount();
        BoardResponse boardResponse = BoardResponse.toDto(board);
        return responseService.getSingleResult(boardResponse);
    }

    /**
     *
     * 전체 조회
     */
    @GetMapping("/board")
    public ListResult<BoardResponse> getBoards() {
        List<Board> all = boardService.findAll();
        List<BoardResponse> responseList = all.stream().map(BoardResponse::toDto).collect(Collectors.toList());
        return responseService.getListResult(responseList);
    }

    /**
     *
     * 클래스 등록
     */
    @PostMapping("/board/create")
    public CommonResult save(@Validated @RequestBody BoardRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return responseService.getFailResult();
        }

        boardService.register(request);
        return responseService.getSuccessResult();
    }

    /**
     *
     * 클래스 수정
     *
     */
    @PutMapping("/board/{id}")
    public CommonResult modify(@PathVariable("id") Long id,
                               @Validated @RequestBody BoardModifyRequest request,
                               BindingResult bindingResult) throws Exception {

        if(bindingResult.hasErrors()) {
            return responseService.getFailResult();
        }

        Board board = boardService.findById(id);
        Member member = memberService.findByToken(request.getToken());

        if(!board.getMember().equals(member)) {
            return responseService.getFailResult();
        }

        board.modify(request);
        return responseService.getSuccessResult();
    }

    /**
     *
     * 클래스 삭제
     */

    @DeleteMapping("/board/{id}")
    public CommonResult delete(@PathVariable("id") Long id,
                               @Validated BoardDeleteRequest request,
                               BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return responseService.getFailResult();
        }

        boardService.delete(id,request);
        return responseService.getSuccessResult();
    }


    /**
     *
     * 클래스 좋아요 기능
     */
    @PostMapping("/board/{id}")
    public CommonResult like(@PathVariable("id") Long id,
                             @Validated HeartRequest request,
                             BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return responseService.getFailResult();
        }

        heartService.clickHeart(id,request);
        return responseService.getSuccessResult();
    }


}
