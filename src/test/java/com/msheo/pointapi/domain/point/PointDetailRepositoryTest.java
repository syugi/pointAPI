package com.msheo.pointapi.domain.point;

import com.msheo.pointapi.common.PointStatus;
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
    public void POINT상세_저장(){
        long memberId = 999L;
        long amount = 2000L;
        long pointId = 1;

        pointDetailRepository.save(PointDetail.builder()
                .memberId(memberId)
                .amount(amount)
                .pointId(pointId)
                .build());

        List<PointDetail> pointDetailList = pointDetailRepository.findAll();

        PointDetail detail = pointDetailList.get(0);
        assertThat(detail.getMemberId()).isEqualTo(memberId);
        assertThat(detail.getAmount()).isEqualTo(amount);
        assertThat(detail.getPointId()).isEqualTo(pointId);
    }

}