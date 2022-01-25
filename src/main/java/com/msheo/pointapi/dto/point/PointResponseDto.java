package com.msheo.pointapi.dto.point;

import com.msheo.pointapi.domain.point.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class PointResponseDto {

    private Long pointId;
    private Long memberId;
    private Long amount;
    private String createdId;
    private String createdDate;

    public PointResponseDto(Point point){
        this.pointId = point.getPointId();
        this.memberId = point.getMemberId();
        this.amount = point.getAmount();
        this.createdDate = toStringDateTime(point.getCreatedDate());
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }

}
