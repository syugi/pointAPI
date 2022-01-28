package com.msheo.pointapi.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {

    //회원별 포인트 합계 조회
    @Query("SELECT sum(d.amount) as amount FROM PointDetail d WHERE d.memberId = ?1")
    Optional<Long> amountSum(Long memberId);

    //사용처리 대상 조회
    @Query(value = "SELECT new com.msheo.pointapi.domain.point.PointDetail( "+
            "d.orgPointId, SUM(d.amount)) "+
            "FROM PointDetail d "+
            "WHERE d.memberId = ?1 " +
            "GROUP BY d.orgPointId " +
            "ORDER BY d.tranDate")
    List<PointDetail> findAllForUse(Long memberId);

    //사용취소처리 대상 조회
    List<PointDetail> findByPointId(Long pointId);
}
