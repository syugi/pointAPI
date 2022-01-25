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

        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        pointService.earnPoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(1000L)
                .build());

        pointService.usePoint(PointSaveRequestDto.builder()
                .memberId(memberId)
                .amount(-1500L)
                .build());


        Point point = pointRepository.findAll().get(2);
        assertThat(point.getMemberId()).isEqualTo(memberId);
        assertThat(point.getAmount()).isEqualTo(-1500L);

        PointDetail detail1 = pointDetailRepository.findAll().get(2);
        assertThat(detail1.getMemberId()).isEqualTo(memberId);
        assertThat(detail1.getAmount()).isEqualTo(-1000L);

        PointDetail detail2 = pointDetailRepository.findAll().get(3);
        assertThat(detail2.getMemberId()).isEqualTo(memberId);
        assertThat(detail2.getAmount()).isEqualTo(-500L);

        long amountSum = pointRepository.amountSum(memberId);
        assertThat(amountSum).isEqualTo(500L);
    }
}