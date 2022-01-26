package com.msheo.pointapi.domain.point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    //회원별 포인트 적립/사용 내역 조회
    @Query("SELECT p "+
            "FROM Point p "+
            "WHERE p.memberId = ?1")
    //to-do 사용취소는 조회되지않음
    public Page<Point> findListByMemberId(Long memberId, Pageable pageable);

    //회원별 포인트 합계 조회
    @Query("SELECT sum(p.amount) as amount FROM Point p WHERE p.memberId = ?1")
    public Optional<Long> amountSum(Long memberId);

}
