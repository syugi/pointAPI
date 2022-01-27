package com.msheo.pointapi.domain.point;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointRepositoryTest {

    @Autowired
    PointRepository pointRepository;

    @After
    public void cleanUp(){
        pointRepository.deleteAll();
    }

    @Test
    public void POINT_저장(){
        long memberId = 999L;
        long amount = 2000L;
        pointRepository.save(Point.builder()
                .memberId(memberId)
                .amount(amount)
                .build());

        List<Point> pointList = pointRepository.findAll();

        Point points = pointList.get(0);
        assertThat(points.getMemberId()).isEqualTo(memberId);
        assertThat(points.getAmount()).isEqualTo(amount);
    }

}