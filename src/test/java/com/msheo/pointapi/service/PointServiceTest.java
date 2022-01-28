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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Test
    public void 포인트_적립_사용_내역_조회(){
        long memberId = 999L;
        PageRequest pageRequest = PageRequest.of(0,5);

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        //사용
        pointService.usePoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(-1000L)
                .build());

        Page<Point> list1 = pointService.getPointList(memberId, pageRequest);
        assertThat(list1.getContent().get(0).getMemberId()).isEqualTo(memberId);
        assertThat(list1.getContent().get(0).getAmount()).isEqualTo(1000L);

        Page<Point> list2 = pointService.getPointList(memberId, pageRequest);
        assertThat(list2.getContent().get(1).getMemberId()).isEqualTo(memberId);
        assertThat(list2.getContent().get(1).getAmount()).isEqualTo(-1000L);
    }

    @Test
    public void 포인트_사용취소내역_미조회(){
        long memberId = 999L;
        PageRequest pageRequest = PageRequest.of(0,5);

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

        Page<Point> list = pointService.getPointList(memberId, pageRequest);
        assertThat(list.getContent().size()).isEqualTo(1);

    }

    @Test
    public void 포인트_합계_조회(){
        long memberId = 999L;
        long amount1 = 1000L;
        long amount2 = 1500L;

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount1)
                .build());

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount2)
                .build());

        Long pointSum = pointService.getPointSum(memberId);

        assertThat(pointSum).isEqualTo(amount1+amount2);
    }

    @Test
    public void 포인트_합계_조회_2(){
        long memberId = 999L;
        long amount1 = 1000L;
        long amount2 = 1500L;

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount1)
                .build());

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount2)
                .build());

        //적립 - 다른회원 적립, 합계미포함
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(888L)
                .amount(amount2)
                .build());

        Long pointSum = pointService.getPointSum(memberId);

        assertThat(pointSum).isEqualTo(amount1+amount2);
    }

    @Test
    public void 포인트_합계_조회_3(){
        long memberId = 999L;
        long amount1 = 1000L;
        long amount2 = 1500L;
        long useAmount = -2000L;

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount1)
                .build());

        //적립
        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(amount2)
                .build());

        //사용
        pointService.usePoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(useAmount)
                .build());

        Long pointSum = pointService.getPointSum(memberId);

        assertThat(pointSum).isEqualTo((amount1+amount2)+useAmount);
    }
}