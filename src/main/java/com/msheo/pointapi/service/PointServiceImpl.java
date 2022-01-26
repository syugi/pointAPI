package com.msheo.pointapi.service;

import com.msheo.pointapi.common.PointStatus;
import com.msheo.pointapi.common.ResponseMessage;
import com.msheo.pointapi.common.StatusCode;
import com.msheo.pointapi.common.response.BaseResponse;
import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.domain.point.PointDetail;
import com.msheo.pointapi.domain.point.PointDetailRepository;
import com.msheo.pointapi.dto.point.PointResponseDto;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Point> getPointList(Long memberId, PageRequest pageRequest) {
        return pointRepository.findListByMemberId(memberId, pageRequest);
    }

    //회원별 포인트 합계 조회
    @Override
    public Long getPointSum(Long memberId){
        long amountSum = pointRepository.amountSum(memberId).orElse(0L);
        return amountSum;
    }

    //포인트 적립
    @Override
    public Point earnPoint(PointSaveRequestDto dto){

        //적립내역 저장
        dto.setStatus(PointStatus.EARN);
        Point point = savePoint(dto);

        //적립상세 저장
        PointDetail detail = pointDetailRepository.save(new PointDetail(point));

        return point;
    }

    //포인트 사용
    @Transactional
    @Override
    public Point usePoint(PointSaveRequestDto dto){

        long usePoint = dto.getAmount();

        //사용내역 저장
        dto.setStatus(PointStatus.USE);
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
            pointDt.setStatus(PointStatus.USE);
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
            dto.setTranDate(tranDate);
        }
        String expiryDate = LocalDate.parse(tranDate, dateFormatter).plusYears(1).format(dateFormatter);
        dto.setExpiryDate(expiryDate);

        Point point = pointRepository.save(dto.toEntity());

        return point;
    }


    //포인트 사용취소
    @Override
    public Point useCancelPoint(PointSaveRequestDto dto){
        return pointRepository.save(dto.toEntity());
    }



}
