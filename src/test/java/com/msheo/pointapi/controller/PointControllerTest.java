package com.msheo.pointapi.controller;

import com.msheo.pointapi.domain.point.Point;
import com.msheo.pointapi.domain.point.PointRepository;
import com.msheo.pointapi.dto.point.PointSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PointControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PointRepository pointRepository;

    @After
    public void tearDown() throws Exception{
        pointRepository.deleteAll();
    }

    @Test
    public void 포인트_적립() throws Exception{

        PointSaveRequestDto requestDto = PointSaveRequestDto.builder()
                .memberId(999L)
                .amount(1000L)
                .createdId("admin")
                .build();

        String url = "http://localhost:" + port + "/point/earn";

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, requestDto, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Point> points = pointRepository.findAll();
        assertThat(points.get(0).getMemberId()).isEqualTo(requestDto.getMemberId());
        assertThat(points.get(0).getAmount()).isEqualTo(requestDto.getAmount());
        assertThat(points.get(0).getCreatedId()).isEqualTo(requestDto.getCreatedId());


    }
}