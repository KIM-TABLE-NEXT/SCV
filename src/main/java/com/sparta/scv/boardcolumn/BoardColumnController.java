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
    public ResponseEntity<List<BoardColumnResponseDto>> getColumns(@RequestBody BoardIdRequestDto requestDto) {
        List<BoardColumnResponseDto> responseDtoList = boardColumnService.getColumns(requestDto);
        return ResponseEntity.status(201).body(responseDtoList);
    }

    @PostMapping("/boardcolumns")
    @BoardMemberOnly
    public ResponseEntity<ResultResponseDto> createColumn(@RequestBody BoardColumnRequestDto requestDto) {
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
    @PutMapping("/boardcolumns/update-position/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnPosition(@RequestBody PositionUpdateDto requestDto,
                                                                  @PathVariable Long boardcolumnId) {
        boardColumnService.updateColumnPosition(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @BoardMemberOnly
    @DeleteMapping("/boardcolumns/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> deleteColumn(@RequestBody BoardIdRequestDto requestDto,
                                                          @PathVariable Long boardcolumnId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boardColumnService.deleteColumn(boardcolumnId, requestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }
}
