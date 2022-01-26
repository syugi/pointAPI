package com.msheo.pointapi.domain.point;

import com.msheo.pointapi.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long detailId;
    private Long pointId;
    private Long memberId;
    private Long amount;
    private String status;
    private String tranDate;
    private String expiryDate;
    private String createdId;


    @Builder
    public PointDetail(Long pointId, Long memberId, Long amount, String status, String tranDate, String expiryDate, String createdId){
        this.pointId = pointId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
        this.tranDate = tranDate;
        this.expiryDate = expiryDate;
        this.createdId = createdId;
    }

    public PointDetail(Long pointId, Long amount){
        this.pointId = pointId;
        this.amount = amount;
    }

    public PointDetail(Point point) {
        this.pointId = pointId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
        this.tranDate = tranDate;
        this.expiryDate = expiryDate;
        this.createdId = createdId;
    }

}
