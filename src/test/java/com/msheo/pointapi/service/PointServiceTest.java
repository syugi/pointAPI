package com.msheo.pointapi.service;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointDetail;
import com.msheo.pointapi.domain.point.PointDetailRepository;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @After
    public void cleanup(){
        pointRepository.deleteAll();
        pointDetailRepository.deleteAll();
    }

    @Test
    public void 포인트_적립(){
        PointSaveRequestDto dto = PointSaveRequestDto.builder()
                .memberId(999L)
                .amount(1000L)
                .build();

        pointService.earnPoint(dto);

        Point point = pointRepository.findAll().get(0);
        assertThat(point.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(point.getAmount()).isEqualTo(dto.getAmount());

        PointDetail detail = pointDetailRepository.findAll().get(0);
        assertThat(detail.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(detail.getAmount()).isEqualTo(dto.getAmount());
    }

    @Test
    public void 포인트_사용(){

        long memberId = 999L;

        //적립
        Point point1 = pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        Point point2 = pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        //사용
        Point usePoint = pointService.usePoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(-1500L)
                .build());


        Point rsUsePoint = pointRepository.findById(usePoint.getPointId()).get();
        assertThat(rsUsePoint.getMemberId()).isEqualTo(memberId);
        assertThat(rsUsePoint.getAmount()).isEqualTo(-1500L);

        PointDetail detail1 = pointDetailRepository.findAll().get(2);
        assertThat(detail1.getMemberId()).isEqualTo(memberId);
        assertThat(detail1.getAmount()).isEqualTo(-1000L);
        assertThat(detail1.getOrgPointId()).isEqualTo(point1.getPointId());

        PointDetail detail2 = pointDetailRepository.findAll().get(3);
        assertThat(detail2.getMemberId()).isEqualTo(memberId);
        assertThat(detail2.getAmount()).isEqualTo(-500L);
        assertThat(detail2.getOrgPointId()).isEqualTo(point2.getPointId());

        long amountSum = pointDetailRepository.amountSum(memberId).orElse(0L);
        assertThat(amountSum).isEqualTo(500L);
    }

    @Test
    public void 포인트_사용_취소(){

        long memberId = 999L;

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        //사용
        Point usePoint = pointService.usePoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(-1000L)
                .build());

        //사용취소
        Optional<Point> point = pointService.findById(usePoint.getPointId());
        assertThat(point.isPresent()).isTrue();
        pointService.useCancelPoint(point.get());

        Point p = pointRepository.findById(usePoint.getPointId()).get();
        assertThat(p.getCancelYn()).isEqualTo("Y");

        PointDetail detail = pointDetailRepository.findAll().get(2);
        assertThat(detail.getAmount()).isEqualTo(1000L);
        assertThat(detail.getPointId()).isEqualTo(usePoint.getPointId());
        assertThat(detail.getMemberId()).isEqualTo(memberId);

        long amountSum = pointService.getPointSum(memberId);
        assertThat(amountSum).isEqualTo(1000L);
    }
}