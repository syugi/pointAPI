package com.msheo.pointapi.controller;

import com.msheo.pointapi.common.ResponseMessage;
import com.msheo.pointapi.common.StatusCode;
import com.msheo.pointapi.common.response.BaseResponse;
import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.dto.point.PointAmountSumResponseDto;
import com.msheo.pointapi.dto.point.PointResponseDto;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import com.msheo.pointapi.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS, points), HttpStatus.OK);
    }

    //포인트 사용/적립 내역 조회
    @GetMapping("/point/list/{memberId}")
    public ResponseEntity<?> getPointList(@PathVariable Long memberId, @RequestParam int page, @RequestParam Optional<Integer> totalPage) {
        //회원번호 체크
        if(memberId == null){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.NO_MEMBER_ID), HttpStatus.OK);
        }

        PageRequest pageRequest = PageRequest.of(page,totalPage.orElse(5));
        Page<Point> points = pointService.getPointList(memberId, pageRequest);
        return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS, points), HttpStatus.OK);
    }

    //포인트 합계 조회
    @GetMapping("/point/sum/{memberId}")
    public ResponseEntity<?> getPointSum(@PathVariable Long memberId) {

        //회원번호 체크
        if(memberId == null){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.NO_MEMBER_ID), HttpStatus.OK);
        }

        long amountSum = pointService.getPointSum(memberId);
        Point point = Point.builder()
                .memberId(memberId)
                .amount(amountSum)
                .build();
        return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS, new PointAmountSumResponseDto(point)), HttpStatus.OK);
    }

    //포인트 적립
    @PostMapping("/point/earn")
    public ResponseEntity<?> earnPoint(@RequestBody PointSaveRequestDto dto){
        log.info("earn dto : "+dto);

        //회원번호 체크
        if(dto.getMemberId() == null){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.NO_MEMBER_ID), HttpStatus.OK);
        }

        //포인트 금액 체크
        if(dto.getAmount() == null || dto.getAmount() <= 0){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.EARN_AMOUNT_NOT), HttpStatus.OK);
        }

        Point point = pointService.earnPoint(dto);
        return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS, new PointResponseDto(point)), HttpStatus.OK);
    }

    //포인트 사용
    @PostMapping("/point/use")
    public ResponseEntity<?> usePoint(@RequestBody PointSaveRequestDto dto){
        log.info("use dto : "+dto);

        //회원번호 체크
        if(dto.getMemberId() == null){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.NO_MEMBER_ID), HttpStatus.OK);
        }

        //포인트 금액 체크
        if(dto.getAmount() == null || dto.getAmount() >= 0){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.USE_AMOUNT_NOT), HttpStatus.OK);
        }

        //사용가능여부 체크
        long usePoint  = dto.getAmount();
        long amountSum = pointService.getPointSum(dto.getMemberId());
        log.info("usePoint : "+usePoint+" amountSum : "+amountSum);
        if(usePoint * -1 > amountSum){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.AMOUNT_OVER), HttpStatus.OK);
        }

        Point point = pointService.usePoint(dto);
        return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS, new PointResponseDto(point)), HttpStatus.OK);
    }

    //포인트 사용 취소
    @PostMapping("/point/use/cancel/{pointId}")
    public ResponseEntity<?> useCancelPoint(@PathVariable Long pointId){
        // 체크
        if(pointId == null){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.NO_POINT_ID), HttpStatus.OK);
        }

        log.info("useCancel pointId : "+pointId);
        Optional<Point> point = pointService.findById(pointId);
        if(point.isEmpty()){
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.USE_CANCEL_FAIL), HttpStatus.OK);
        }

        int resultCnt =  pointService.useCancelPoint(point.get());
        if(resultCnt > 0) {
            return new ResponseEntity<>(BaseResponse.res(StatusCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(BaseResponse.res(StatusCode.FAIL, ResponseMessage.USE_CANCEL_FAIL), HttpStatus.OK);
        }
    }
}
