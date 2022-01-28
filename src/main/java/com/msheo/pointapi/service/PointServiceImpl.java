package com.msheo.pointapi.service;

import com.msheo.pointapi.common.PointStatus;
import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.domain.point.PointDetail;
import com.msheo.pointapi.domain.point.PointDetailRepository;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;
    private final PointDetailRepository pointDetailRepository;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    @Override
    public Optional<Point> findById(Long pointId) {
        return pointRepository.findById(pointId);
    }

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
        long amountSum = pointDetailRepository.amountSum(memberId).orElse(0L);
        return amountSum;
    }

    //포인트 적립
    @Transactional
    @Override
    public Point earnPoint(PointSaveRequestDto dto){

        //적립내역 저장
        dto.setStatus(PointStatus.EARN);
        Point point = savePoint(dto);

        //적립상세 저장
        PointDetail detail = pointDetailRepository.save(PointDetail.builder()
                .memberId(point.getMemberId())
                .amount(point.getAmount())
                .tranDate(point.getTranDate())
                .expiryDate(point.getExpiryDate())
                .createdId(point.getCreatedId())
                .status(point.getStatus())
                .pointId(point.getPointId())
                .orgPointId(point.getPointId())
                .build());

        return point;
    }

    //포인트 사용
    @Transactional
    @Override
    public Point usePoint(PointSaveRequestDto dto){

        //사용내역 저장
        dto.setStatus(PointStatus.USE);
        Point point = savePoint(dto);

        //사용대상 조회
        List<PointDetail> points = pointDetailRepository.findAllForUse(point.getMemberId());

        for(PointDetail pd : points){
            log.info("points pointId : "+pd.getPointId()+" amount : "+pd.getAmount());
        }
        long usePoint = dto.getAmount() * -1; // -1000 -> 1000

        //사용상세 저장
        PointDetail pointDt;
        for(int i=0; i<points.size(); i++){
            pointDt = points.get(i);
            long amount = pointDt.getAmount();
            if(amount <= 0) continue;

            if(usePoint - amount >= 0){
                usePoint -= amount;
            }else{
                amount = usePoint;
                usePoint = 0;
            }

            pointDetailRepository.save(PointDetail.builder()
                    .amount(amount * -1)
                    .status(PointStatus.USE)
                    .orgPointId(pointDt.getPointId())
                    .pointId(point.getPointId())
                    .memberId(point.getMemberId())
                    .tranDate(point.getTranDate())
                    .expiryDate(point.getExpiryDate())
                    .createdId(point.getCreatedId())
                    .build());

            if(usePoint <= 0) break;
        }

        return point;
    }

    //포인트 저장
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
    @Transactional
    @Override
    public int useCancelPoint(Point point){
        int resultCnt = 0;

        //취소여부 저장
        point.setCancelYn("Y");
        pointRepository.save(point);

        //사용취소 대상 조회
        List<PointDetail> points = pointDetailRepository.findByPointId(point.getPointId());

        for(PointDetail pointDt : points){
            pointDetailRepository.save(PointDetail.builder()
                    .amount(pointDt.getAmount() * -1)
                    .status(PointStatus.USE_CANCEL)
                    .pointId(pointDt.getPointId())
                    .orgPointId(pointDt.getOrgPointId())
                    .memberId(pointDt.getMemberId())
                    .tranDate(pointDt.getTranDate())
                    .expiryDate(pointDt.getExpiryDate())
                    .build());
            resultCnt++;
        }
        return resultCnt;
    }



}
