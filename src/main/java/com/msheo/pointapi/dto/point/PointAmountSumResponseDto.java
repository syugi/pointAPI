package com.msheo.pointapi.dto.point;

import com.msheo.pointapi.domain.point.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class PointAmountSumResponseDto {

    private Long memberId;
    private Long amountSum;

    public PointAmountSumResponseDto(Point point){
        this.memberId = point.getMemberId();
        this.amountSum = point.getAmount();
    }
}
