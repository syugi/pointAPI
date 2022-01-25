package com.msheo.pointapi.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    //회원별 포인트 적립/사용 내역 조회
    @Query("SELECT p "+
            "FROM Point p "+
            "WHERE p.memberId = ?1")
    //to-do 페이징처리, 사용취소는 조회되지않음
    public List<Point> findListByMemberId(Long memberId);

    //회원별 포인트 합계 조회
    @Query("SELECT sum(p.amount) as amount FROM Point p WHERE p.memberId = ?1")
    public Long amountSum(Long memberId);


}
