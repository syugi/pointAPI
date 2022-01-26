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
    private String desc;
    private String tranDate;
    private String expiryDate;
    private String createdId;
    private String status;

    @Builder
    public PointSaveRequestDto(Long memberId, Long amount, String desc, String tranDate, String createdId){
        this.memberId = memberId;
        this.amount = amount;
        this.desc = desc;
        this.tranDate = tranDate;
        this.createdId = createdId;
    }

    public Point toEntity(){
        return Point.builder()
                .memberId(memberId)
                .amount(amount)
                .desc(desc)
                .tranDate(tranDate)
                .expiryDate(expiryDate)
                .createdId(createdId)
                .status(status)
                .build();
    }
}
