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
public class PointDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private Long pointIdx;
    private Long memberId;
    private Long amount;
    private String createdId;

    @Builder
    public PointDetail(Long pointIdx, Long memberId, Long amount, String createdId){
        this.memberId = memberId;
        this.amount = amount;
        this.createdId = createdId;
    }

}
