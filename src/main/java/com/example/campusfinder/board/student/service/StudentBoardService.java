package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentBoardRequestDto;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.entity.StudentBoardImage;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.home.entity.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentBoardService {

    private final StudentBoardRepository studentBoardRepository;

    // 학생 찾기 글 작성 로직
    public StudentBoardDto createStudentBoard(StudentBoardRequestDto requestDto, String nickname) {
        // 이미지 리스트의 첫 번째 이미지를 썸네일로 설정
        String thumbnailImage = (requestDto.imageUrls() != null && !requestDto.imageUrls().isEmpty())
                ? requestDto.imageUrls().get(0)
                : null;

        // StudentBoard 엔티티 생성
        StudentBoard studentBoard = StudentBoard.builder()
                .title(requestDto.title())
                .nickname(nickname) // 작성자 닉네임 설정
                .thumbnailImage(thumbnailImage)
                .isNearCampus(requestDto.isNearCampus())
                .categoryType(requestDto.categoryType())
                .content(requestDto.content()) // 의뢰 내용
                .meetingType(requestDto.meetingType()) // 대면, 비대면 여부
                .build();

        // StudentBoard 저장
        StudentBoard savedBoard = studentBoardRepository.save(studentBoard);

        // 이미지 리스트가 있으면 StudentBoardImage 엔티티로 변환하여 저장
        if (requestDto.imageUrls() != null) {
            for (String imageUrl : requestDto.imageUrls()) {
                StudentBoardImage image = StudentBoardImage.builder()
                        .imageUrl(imageUrl)
                        .studentBoard(savedBoard)
                        .build();
                savedBoard.addImage(image); // StudentBoard에 이미지 추가
            }
        }

        return new StudentBoardDto(
                savedBoard.getBoardIdx(),
                savedBoard.getTitle(),
                savedBoard.getNickname(),
                savedBoard.getThumbnailImage(),
                savedBoard.isNearCampus(),
                savedBoard.getCategoryType()
        );
    }

    // 학생 찾기 글 조회 로직
    @Transactional(readOnly = true)
    public List<StudentBoardDto> getStudentBoardListByCategory(CategoryType categoryType) {
        return studentBoardRepository.findAllByCategoryType(categoryType).stream()
                .map(studentBoard -> new StudentBoardDto(
                        studentBoard.getBoardIdx(),
                        studentBoard.getTitle(),
                        studentBoard.getNickname(),
                        studentBoard.getThumbnailImage(),
                        studentBoard.isNearCampus(),
                        studentBoard.getCategoryType()
                ))
                .toList();
    }
}
