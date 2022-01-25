package com.msheo.pointapi.service;

import com.msheo.pointapi.domain.point.Point;
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

    @After
    public void cleanup(){
        pointRepository.deleteAll();
    }

    @Test
    public void 포인트_적립(){
        PointSaveRequestDto dto = PointSaveRequestDto.builder()
                .memberId(999L)
                .amount(1000L)
                .build();

        pointService.savePoint(dto);

        Point point = pointRepository.findAll().get(0);
        assertThat(point.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(point.getAmount()).isEqualTo(dto.getAmount());
    }

    @Test
    public void 포인트_사용(){
        PointSaveRequestDto dto = PointSaveRequestDto.builder()
                .memberId(999L)
                .amount(-1000L)
                .build();

        pointService.savePoint(dto);

        Point point = pointRepository.findAll().get(0);
        assertThat(point.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(point.getAmount()).isEqualTo(dto.getAmount());
    }
}