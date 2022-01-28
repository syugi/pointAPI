package com.msheo.pointapi.domain.point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {

    //회원별 포인트 적립/사용 내역 조회
    @Query(value = "SELECT p " +
            "FROM Point p " +
            "WHERE p.memberId = ?1 AND p.cancelYn <> 'Y'")

    Page<Point> findListByMemberId(Long memberId, Pageable pageable);
}
