package com.msheo.pointapi.dto.point;

import com.msheo.pointapi.domain.point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointSaveRequestDto {
    private Long memberId;
    private Long amount;
    private String createdId;

    @Builder
    public PointSaveRequestDto(Long memberId, Long amount, String createdId){
        this.memberId = memberId;
        this.amount = amount;
        this.createdId = createdId;
    }

    public Point toEntity(){
        return Point.builder()
                .memberId(memberId)
                .amount(amount)
                .createdId(createdId)
                .build();
    }
}
