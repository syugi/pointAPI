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
public class PointDetailRepositoryTest {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointDetailRepository pointDetailRepository;

    @After
    public void cleanUp(){
        pointRepository.deleteAll();
        pointDetailRepository.deleteAll();
    }



    @Test
    public void POINT_회원별_합계_조회(){
        long memberId = 999L;
        long amount1 = 1000L;
        long amount2 = 2000L;
        pointRepository.save(Point.builder()
                .memberId(memberId)
                .amount(amount1)
                .build());

        pointRepository.save(Point.builder()
                .memberId(memberId)
                .amount(amount2)
                .build());

        pointRepository.save(Point.builder()
                .memberId(memberId+11)
                .amount(amount1+amount2)
                .build());

        Long amountSum = pointDetailRepository.amountSum(memberId).orElse(0L);

        assertThat(amountSum).isEqualTo(amount1+amount2);
    }
}