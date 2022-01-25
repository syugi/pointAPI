package com.msheo.pointapi.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    //사용처리 대상 포인트 조회
    @Query(value = "SELECT new com.msheo.pointapi.domain.point.PointDetail( "+
            "d.pointId, SUM(d.amount)) "+
            "FROM PointDetail d "+
            "WHERE d.memberId = ?1 " +
//            "AND d.expiryDate > current_date " +
            "GROUP BY d.pointId " +
            "ORDER BY d.tranDate")
    public List<PointDetail> findAllForUse(Long memberId);

    @Query(value = "SELECT d FROM PointDetail d WHERE d.detailId = ?1 ")
    public PointDetail findByDetailId(Long detailId);
}
