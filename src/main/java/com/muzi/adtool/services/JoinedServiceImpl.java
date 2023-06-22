package com.muzi.adtool.services;

import com.muzi.adtool.dao.CampaignDAO;
import com.muzi.adtool.dao.JoinedDAO;
import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.JoinedVO;
import com.muzi.adtool.domain.Member_informationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public class JoinedServiceImpl implements JoinedService{

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    JoinedDAO joinedDAO;

    @Autowired
    MemberDAO memberDAO;

    @Autowired
    CampaignDAO campaignDAO;

    @Override
    @Transactional
    public int insert(JoinedVO vo) {
        int result = 0;

        Member_informationVO member_informationVO = memberDAO.infoReadByMemberSeq(vo.getMember_seq());
        CampaignVO campaignVO = campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());
        JoinedVO joinedVO = joinedDAO.read_by_campaign_seq_and_member_seq(vo);
        int multiple = campaignVO.getMultiple_number();
        int chkCount = joinedDAO.chkCount(vo.getCampaign_seq());

//        int joinedBrandCount = joinedDAO.findByMemberAndBrand(vo.getMember_seq(), campaignVO.getBrand_seq());

        String member_country=member_informationVO.getCountry();
        String national_number=member_informationVO.getNational_number();
        if(national_number.equals("+nation")) {
            national_number=null;
        }
        String phone_number=member_informationVO.getPhone();
        if(phone_number.equals("")) {
            phone_number=null;
        }

        boolean phoneChk = false;
        if(national_number!=null && phone_number!=null) {
            phoneChk = true;
        }else {
            phoneChk = false;
        }


        boolean countryChk = false;

        String campaign_country=campaignVO.getCampaign_country();
        ArrayList<String> countryList = new ArrayList<>();
        countryList.add(member_informationVO.getCountry());
        countryList.add(member_informationVO.getCountry2());
        countryList.add(member_informationVO.getCountry3());

        if(member_country!=null) {
            for(String memberCountry:countryList) {
                if(memberCountry.equals(campaign_country)) {
                    countryChk=true;
                    break;
                }
            }
        }

        if(phoneChk) { // 전화번호 유무 검사
            if(countryChk){ // 국가 일치여부 검사
//                if(joinedBrandCount>0) {
//                    result = 6; // 이미 참여중인 캠페인이 신청하려는 캠페인의 브랜드가 겹칠 때,
//                }else {
                    if(campaignVO.getStatus().equals("recruit")){ // 모집중인 캠페인
                        // 로그인 되었을 때
                        if(chkCount >= multiple) {
                            result=0; //인원 초과
                        }else {
                            if(joinedVO!=null){
                                result=3; // 중복 신청
                            }else{
                                vo.setJoined_campaign_seq(getSequenceService.getSeq("join"));
                                vo.setAccept("wait");
                                joinedDAO.insert(vo);
                                result=1;
                            }
                        }
                    }else{
                        result=4; // 진행중이거나 종료된 캠페인
                    }
//                }

            }else{
                if(vo.getMember_seq().equals("") || vo.getMember_seq()==null) {
                    result=2; // 로그아웃 상태
                }else{
                    result=5;
                }
            }
        }else {
            result = 7; // 전화번호 없음
        }

        return result;
    }
}
