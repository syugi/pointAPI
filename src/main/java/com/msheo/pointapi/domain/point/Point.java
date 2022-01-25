package com.msheo.pointapi.domain.point;

import com.msheo.pointapi.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pointId;
    private Long memberId;
    private Long amount;
    private String status;
    private String desc;
    private String tranDate;
    private String expiryDate;
    private String createdId;

    @Builder
    public Point(Long memberId, Long amount, String status, String desc, String tranDate, String expiryDate, String createdId){
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
        this.desc = desc;
        this.tranDate = tranDate;
        this.expiryDate = expiryDate;
        this.createdId = createdId;
    }

    @Override
    public String toString() {
        return "Point{" +
                "pointId=" + pointId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", createdId='" + createdId + '\'' +
                '}';
    }
}
