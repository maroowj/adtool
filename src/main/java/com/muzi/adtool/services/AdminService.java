package com.muzi.adtool.services;

import com.muzi.adtool.domain.*;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface AdminService {
    public int joined_member_list_update(JoinedVO vo);
    public int status_update_and_payment_insert(JoinedVO vo);
    public int reservation_campaign_insert(Reservation_campaignVO vo);

    public int insert_point_by_recommender(PointVO vo);
    public int insert_point_by_campaign(PointVO vo);
    public int withdraw_point_by_done(PointVO vo);

    // 브랜드 삭제
    public int brand_delete(String brand_seq);

    // 가입회원 엑셀
    public void memberExcelDownload(Criteria cri, HttpServletResponse response);

    // 참가자 명단 엑셀
    public void joinedMemberExcelDownload(String campaign_seq, HttpServletResponse response);

    // 캠페인 리뷰 엑셀
    public void campaignReviewExcel(String campaign_seq, HttpServletResponse response) throws IOException;

    // 캠페인 정산 엑셀
    public void campaignCalExcel(String campaign_seq, HttpServletResponse response) throws IOException;

    // 회원 삭제(탈퇴 처리)
    public int member_delete(String member_seq);
}
