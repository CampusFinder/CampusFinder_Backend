package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.request.RequestSearchDto;
import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.board.student.dto.StudentBoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.campusfinder.board.request.dto.request
 * fileName       : ReqeustSearchService
 * author         : tlswl
 * date           : 2024-10-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-11        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestSearchService {

    private final RequestBoardRepository requestBoardRepository;

    @Transactional(readOnly = true)
    public List<RequestBoardDto> searchRequestBoards(RequestSearchDto searchDto) {
        List<RequestBoard> boards;

        // 카테고리 리스트가 비어있을 경우 전체 카테고리에서 검색
        if (searchDto.categories() == null || searchDto.categories().isEmpty()) {
            // 제목 또는 내용에 키워드가 포함된 게시글 전체 조회
            boards = requestBoardRepository.findAllByTitleContainingOrContentContaining(
                    searchDto.keyword(), searchDto.keyword()
            );
        } else {
            // 키워드가 포함된 게시글 중, 특정 카테고리들 내에서 조회
            boards = requestBoardRepository.searchByKeywordAndCategoryTypes(
                    searchDto.keyword(), searchDto.categories()
            );
        }

        // 결과 DTO로 변환
        return boards.stream()
                .map(board -> new RequestBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.getIsUrgent(),
                        board.getMoney(),
                        board.getCategoryType()
                ))
                .collect(Collectors.toList());
    }
}
