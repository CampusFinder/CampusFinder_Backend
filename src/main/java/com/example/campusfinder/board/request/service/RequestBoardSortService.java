package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.home.entity.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.campusfinder.board.request.service
 * fileName       : RequestBoardSortService
 * author         : tlswl
 * date           : 2024-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-04        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RequestBoardSortService {

    private final RequestBoardRepository requestBoardRepository;

    /**
     * 카테고리별 게시글 정렬 조회 로직 (최신순, 오래된순)
     * @param categoryType 카테고리 타입 (null이면 전체 조회)
     * @param sortType 정렬 방식 ("latest" - 최신순, "oldest" - 오래된순)
     * @return List<RequestBoardResponseDto> 게시글 목록 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<RequestBoardDto> getRequestBoardsSortedByDate(CategoryType categoryType, String sortType) {
        List<RequestBoard> boards;

        // 정렬 기준 설정 (default: createdAt 기준)
        Sort sort = Sort.by("createdAt");

        if ("latest".equalsIgnoreCase(sortType)) {
            sort = sort.descending(); // 최신순
        } else if ("oldest".equalsIgnoreCase(sortType)) {
            sort = sort.ascending(); // 오래된순
        }

        // 카테고리별 게시글 조회 또는 전체 게시글 조회
        if (categoryType != null) {
            boards = requestBoardRepository.findAllByCategoryType(categoryType, sort);
        } else {
            boards = requestBoardRepository.findAll(sort);
        }

        return boards.stream()
                .map(board -> new RequestBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.getIsUrgent(),
                        board.getMoney(),
                        board.getCategoryType()
                )).collect(Collectors.toList());
    }
}
