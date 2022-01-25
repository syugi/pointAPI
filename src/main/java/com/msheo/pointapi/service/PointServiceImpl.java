package com.msheo.pointapi.service;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.domain.point.PointDetail;
import com.msheo.pointapi.domain.point.PointDetailRepository;
import com.msheo.pointapi.dto.point.PointResponseDto;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;
    private final PointDetailRepository pointDetailRepository;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

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
    public PointResponseDto earnPoint(PointSaveRequestDto dto){

        //적립내역 저장
        dto.setStatus("적립");
        Point point = savePoint(dto);

        //적립상세 저장
        PointDetail detail = pointDetailRepository.save(PointDetail.builder()
                .memberId(point.getMemberId())
                .amount(point.getAmount())
                .status(point.getStatus())
                .tranDate(point.getTranDate())
                .expiryDate(point.getExpiryDate())
                .createdId(point.getCreatedId())
                .pointId(point.getPointId())
                .build());

        return new PointResponseDto(point);
    }

    //포인트 사용
    @Transactional
    @Override
    public Point usePoint(PointSaveRequestDto dto){

        long usePoint = dto.getAmount();

        //사용가능여부 체크
        long amountSum = pointRepository.amountSum(dto.getMemberId());
        log.info("usePoint : "+usePoint+" amountSum : "+amountSum);
        if(usePoint * -1 > amountSum){
            new IllegalArgumentException("포인트 잔액이 부족합니다.");
            return dto.toEntity();
        }

        //사용내역 저장
        dto.setStatus("사용");
        Point point = savePoint(dto);

        //사용대상 조회
        List<PointDetail> points = pointDetailRepository.findAllForUse(point.getMemberId());

        //사용상세 저장
        PointDetail pointDt;
        for(int i=0; i<points.size(); i++){
            pointDt = points.get(i);
            long amount = pointDt.getAmount();
            if(amount <= 0) continue;

            if(usePoint + amount <= 0){
                usePoint += amount;
            }else{
                amount += usePoint;
                usePoint = 0;
            }

            pointDt.setAmount(amount * -1);
            pointDt.setStatus("사용");
            pointDt.setMemberId(point.getMemberId());
            pointDt.setTranDate(point.getTranDate());
            pointDt.setExpiryDate(point.getExpiryDate());
            pointDetailRepository.save(pointDt);

            if(usePoint == 0) break;
        }

        return point;
    }

    public Point savePoint(PointSaveRequestDto dto){
        String tranDate = dto.getTranDate();
        if(tranDate == null || tranDate.isEmpty()){
            tranDate = LocalDate.now().format(dateFormatter);
        }
        String expiryDate = LocalDate.parse(tranDate, dateFormatter).plusYears(1).format(dateFormatter);

        Point point = pointRepository.save(Point.builder()
                .memberId(dto.getMemberId())
                .amount(dto.getAmount())
                .desc(dto.getDesc())
                .createdId(dto.getCreatedId())
                .status(dto.getStatus())
                .tranDate(tranDate)
                .expiryDate(expiryDate)
                .build());

        return point;
    }


    //포인트 사용취소
    @Override
    public Point useCancelPoint(PointSaveRequestDto dto){
        return pointRepository.save(dto.toEntity());
    }
}
