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
    private Long orgPointId;


    @Builder
    public PointDetail(Long pointId, Long memberId, Long amount, String status, String tranDate, String expiryDate, String createdId, Long orgPointId){
        this.pointId = pointId;
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
        this.tranDate = tranDate;
        this.expiryDate = expiryDate;
        this.createdId = createdId;
        this.orgPointId = orgPointId;
    }

    public PointDetail(Long pointId, Long amount){
        this.pointId = pointId;
        this.amount = amount;
    }

}
