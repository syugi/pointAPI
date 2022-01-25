package com.msheo.pointapi.service;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;

import java.util.List;

public interface PointService {

    //포인트 전체 조회
    List<Point> findAll();

    //회원별 포인트 적립/사용 내역 조회
    List<Point> getPointList(Long memberId);

    //회원별 포인트 합계 조회
    Point getPointSum(Long memberId);

    //포인트 적립
    Point savePoint(PointSaveRequestDto dto);

    //포인트 사용
    Point usePoint(PointSaveRequestDto dto);

    //포인트 사용취소
    Point useCancelPoint(PointSaveRequestDto dto);
}
