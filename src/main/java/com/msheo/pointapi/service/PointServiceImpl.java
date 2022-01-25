package com.msheo.pointapi.service;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;

    //포인트 전체 조회
    @Override
    public List<Point> findAll(){
        return pointRepository.findAll();
    }

    //회원별 포인트 적립/사용 내역 조회
    @Override
    public List<Point> getPointList(Long memberId) {
        //페이징처리
        //total , page , points[]
        return pointRepository.findListByMemberId(memberId);
    }

    //회원별 포인트 합계 조회
    @Override
    public Point getPointSum(Long memberId){
        long amountSum = pointRepository.amountSum(memberId);
        return Point.builder()
                .memberId(memberId)
                .amount(amountSum)
                .build();
    }

    //포인트 적립
    @Override
    public Point savePoint(PointSaveRequestDto dto){
        return pointRepository.save(dto.toEntity());
    }

    //포인트 사용
    @Override
    public Point usePoint(PointSaveRequestDto dto){
        return pointRepository.save(dto.toEntity());
    }

    //포인트 사용취소
    @Override
    public Point useCancelPoint(PointSaveRequestDto dto){
        return pointRepository.save(dto.toEntity());
    }
}
