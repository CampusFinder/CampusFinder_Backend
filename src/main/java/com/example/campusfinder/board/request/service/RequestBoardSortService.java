package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.home.entity.CategoryType;
import com.example.campusfinder.user.entity.Role;
import com.example.campusfinder.user.repository.UserRepository;
import com.example.campusfinder.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;

    /**
     * 카테고리별 또는 전체 게시글 시간순 정렬 조회 메서드
     * @param categoryType 조회할 카테고리 타입 (null이면 전체 조회)
     * @param sortType 정렬 방식 ("true"이면 최신순, "false"이면 오래된순)
     * @param roleProfessorOnly 교수님 게시글만 조회 여부
     * @return 정렬된 게시글 목록
     */
    public List<RequestBoardDto> getRequestBoardsSortedByDate(CategoryType categoryType, String sortType, boolean roleProfessorOnly) {
        List<RequestBoard> boards;

        // 카테고리별 또는 전체 게시글 조회
        if (categoryType != null) {
            boards = requestBoardRepository.findAllByCategoryType(categoryType);
        } else {
            boards = requestBoardRepository.findAll();
        }

        // 교수님 게시글만 조회 옵션이 활성화된 경우 필터링
        if (roleProfessorOnly) {
            boards = boards.stream()
                    .filter(board -> userRepository.findByNickname(board.getNickname())
                            .map(UserEntity::getRole)
                            .filter(role -> role == Role.PROFESSOR)  // Enum 타입으로 Role 비교
                            .isPresent())  // Role이 PROFESSOR인 경우만 필터링
                    .collect(Collectors.toList());
        }

        // 정렬 방식에 따라 게시글 목록을 정렬 (true: 최신순, false: 오래된순)
        if (Boolean.parseBoolean(sortType)) { // 최신순
            boards.sort((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));
        } else { // 오래된순
            boards.sort((b1, b2) -> b1.getCreatedAt().compareTo(b2.getCreatedAt()));
        }

        // DTO 변환 및 반환
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