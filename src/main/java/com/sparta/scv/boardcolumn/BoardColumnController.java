package com.sparta.scv.boardcolumn;

import com.sparta.scv.annotation.BoardMemberOnly;
import com.sparta.scv.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardColumnController {

    private final BoardColumnService boardColumnService;

    @BoardMemberOnly
    @GetMapping("/boardcolumns")
    public ResponseEntity<List<BoardColumnResponseDto>> getColumns(@RequestBody GetColumnsRequestDto requestDto,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardColumnResponseDto> responseDtoList = boardColumnService.getColumns(requestDto);
        return ResponseEntity.status(201).body(responseDtoList);
    }

    @PostMapping("/boards/{boardId}/boardcolumns")
    @BoardMemberOnly
    public ResponseEntity<ResultResponseDto> createColumn(@PathVariable Long boardId,
                                                          @RequestBody BoardColumnRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long boardColumnId = boardColumnService.createColumn(requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardColumnId));
    }

    @BoardMemberOnly
    @PutMapping("/boardcolumns/update-name/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnName(@PathVariable Long boardcolumnId,
                                                              @RequestBody NameUpdateDto requestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardColumnService.updateColumnName(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @BoardMemberOnly
    @PutMapping("/boardcolumns/update-position/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnPosition(@PathVariable Long boardcolumnId,
                                                                  @RequestBody PositionUpdateDto requestDto,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardColumnService.updateColumnPosition(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @BoardMemberOnly
    @DeleteMapping("/boardcolumns/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> deleteColumn(@PathVariable Long boardcolumnId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardColumnService.deleteColumn(boardcolumnId);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }
}
