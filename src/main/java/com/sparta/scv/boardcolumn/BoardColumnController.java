package com.sparta.scv.boardcolumn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardColumnController {

    private final BoardColumnService boardColumnService;

    @GetMapping("/boardcolumns")
    public ResponseEntity<List<BoardColumnResponseDto>> getColumns(@RequestBody GetColumnsRequestDto requestDto) {
        List<BoardColumnResponseDto> responseDtoList = boardColumnService.getColumns(requestDto);
        return ResponseEntity.status(201).body(responseDtoList);
    }

    @PostMapping("/boardcolumns")
    public ResponseEntity<ResultResponseDto> createColumn(@RequestBody BoardColumnRequestDto requestDto) {
        Long boardColumnId = boardColumnService.createColumn(requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardColumnId));
    }

    @PutMapping("/boardcolumns/update-name/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnName(@PathVariable Long boardcolumnId,
                                                              @RequestBody NameUpdateDto requestDto) {
        boardColumnService.updateColumnName(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @PutMapping("/boardcolumns/update-position/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> updateColumnPosition(@PathVariable Long boardcolumnId,
                                                                  @RequestBody PositionUpdateDto requestDto) {
        boardColumnService.updateColumnPosition(boardcolumnId, requestDto);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardcolumnId));
    }

    @DeleteMapping("/boardcolumns/{boardcolumnId}")
    public ResponseEntity<ResultResponseDto> deleteColumn(@PathVariable Long boardColumnId) {
        boardColumnService.deleteColumn(boardColumnId);
        return ResponseEntity.status(201).body(new ResultResponseDto(201, "OK", boardColumnId));
    }
}
