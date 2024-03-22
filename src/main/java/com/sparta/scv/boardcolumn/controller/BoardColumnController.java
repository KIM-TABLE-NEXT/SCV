package com.sparta.scv.boardcolumn.controller;

import com.sparta.scv.annotation.BoardMemberOnly;
import com.sparta.scv.boardcolumn.dto.BoardColumnRequestDto;
import com.sparta.scv.boardcolumn.dto.BoardIdRequestDto;
import com.sparta.scv.boardcolumn.dto.Columns;
import com.sparta.scv.boardcolumn.dto.NameUpdateDto;
import com.sparta.scv.boardcolumn.dto.PositionUpdateDto;
import com.sparta.scv.boardcolumn.dto.ResultResponseDto;
import com.sparta.scv.boardcolumn.service.BoardColumnService;
import com.sparta.scv.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardColumnController {

    private final BoardColumnService boardColumnService;

    @BoardMemberOnly
    @GetMapping("/boardcolumns")
    public ResponseEntity<Columns> getColumns(@RequestBody BoardIdRequestDto requestDto) {
//        List<BoardColumnResponseDto> responseDtoList = boardColumnService.getColumns(requestDto);
        Columns columns = boardColumnService.getColumns(requestDto);
//        return ResponseEntity.status(200).body(responseDtoList);
        return ResponseEntity.status(200).body(columns);
    }

    @BoardMemberOnly
    @PostMapping
    @Operation(summary = "컬럼 생성", description = "새로운 컬럼을 생성합니다.")
    public ResponseEntity<ResultResponseDto> createColumn(
        @RequestBody BoardColumnRequestDto requestDto) {
        Long boardColumnId = boardColumnService.createColumn(requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardColumnId));
    }

    @BoardMemberOnly
    @PutMapping("/boardcolumns/update-name/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnName(@RequestBody NameUpdateDto requestDto,
        @PathVariable Long boardcolumnId) {
        boardColumnService.updateColumnName(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @BoardMemberOnly
    @PutMapping("/update-position/{boardcolumnId}")
    @Operation(summary = "컬럼 순서 이동", description = "컬럼의 순서를 변경합니다.")
    public ResponseEntity<ResultResponseDto> updateColumnPosition(
        @RequestBody PositionUpdateDto requestDto,
        @PathVariable Long boardcolumnId) {
        boardColumnService.updateColumnPosition(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @BoardMemberOnly
    @DeleteMapping("/boardcolumns/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> deleteColumn(@RequestBody BoardIdRequestDto requestDto,
        @PathVariable Long boardcolumnId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boardColumnService.deleteColumn(requestDto.getBoardId(), boardcolumnId,
            userDetails.getUser());
        return ResponseEntity.status(200).body(new ResultResponseDto(200, "OK", boardcolumnId));
    }
}
