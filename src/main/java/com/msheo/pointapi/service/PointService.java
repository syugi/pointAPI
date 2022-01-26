package com.msheo.pointapi.service;

import com.msheo.pointapi.common.response.BaseResponse;
import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.dto.point.PointResponseDto;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PointService {

    //포인트 전체 조회
    List<Point> findAll();

    //회원별 포인트 적립/사용 내역 조회
    Page<Point> getPointList(Long memberId, PageRequest pageRequest);

    //회원별 포인트 합계 조회
    Long getPointSum(Long memberId);

    //포인트 적립
    Point earnPoint(PointSaveRequestDto dto);

    //포인트 사용
    Point usePoint(PointSaveRequestDto dto);

    //포인트 사용취소
    Point useCancelPoint(PointSaveRequestDto dto);
}
