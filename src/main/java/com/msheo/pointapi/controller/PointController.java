package com.msheo.pointapi.controller;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.dto.point.PointResponseDto;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import com.msheo.pointapi.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @GetMapping("/")
    public String test(){
        return "test";
    }

    //포인트 전체 조회
    @GetMapping("/points")
    public ResponseEntity<?> points() {
        List<Point> points = pointService.findAll();
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    //포인트 사용/적립 내역 조회
    @GetMapping("/point/list/{memberId}")
    public ResponseEntity<?> getPointList(@PathVariable Long memberId) {
        List<Point> points = pointService.getPointList(memberId);
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    //포인트 합계 조회
    @GetMapping("/point/sum/{memberId}")
    public ResponseEntity<?> getPointSum(@PathVariable Long memberId) {
        Point point = pointService.getPointSum(memberId);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }

    //포인트 적립
    @PostMapping("/point/earn")
    public ResponseEntity<?> earnPoint(@RequestBody PointSaveRequestDto dto){
        log.info("earn dto : "+dto);
        PointResponseDto response = pointService.earnPoint(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //포인트 사용
    @PostMapping("/point/use")
    public ResponseEntity<?> usePoint(@RequestBody PointSaveRequestDto dto){
        log.info("use dto : "+dto);
        Point point =  pointService.usePoint(dto);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }

    //포인트 사용 취소
    @PostMapping("/point/use/cancel")
    public ResponseEntity<?> useCancelPoint(@RequestBody PointSaveRequestDto dto){
        log.info("use dto : "+dto);
        Point point =  pointService.useCancelPoint(dto);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }
}
