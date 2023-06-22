package com.muzi.adtool.services;

import com.muzi.adtool.dao.CampaignDAO;
import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Reservation_campaignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CampaignServiceImpl implements CampaignService{

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    CampaignDAO campaignDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public int insert(CampaignVO vo) {
        vo.setCampaign_seq(getSequenceService.getSeq("camp"));
        vo.setStatus("recruit");
        return campaignDAO.campaign_insert(vo);
    }

    @Transactional
    @Override
    public int update_image_order(CampaignVO vo) {
        /*
        System.out.println("1="+vo.getCampaign_img1());
        System.out.println("2="+vo.getCampaign_img2());
        System.out.println("3="+vo.getCampaign_img3());
        System.out.println("4="+vo.getCampaign_img4());
        System.out.println("5="+vo.getCampaign_img5());
        */

        campaignDAO.campaign_update(vo);
        CampaignVO cvo =  campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add(cvo.getCampaign_img1());
        imgList.add(cvo.getCampaign_img2());
        imgList.add(cvo.getCampaign_img3());
        imgList.add(cvo.getCampaign_img4());
        imgList.add(cvo.getCampaign_img5());
        ArrayList<String> newOrderList = new ArrayList<>();
//        System.out.println("imgList="+imgList);
        for(String arg:imgList){
           if(!arg.equals("")){
                newOrderList.add(arg);
            }
        }
        int newOrderListSize = newOrderList.size();
        switch (newOrderListSize){
            case 1:
                vo.setCampaign_img1(newOrderList.get(0));
                vo.setCampaign_img2(null);
                vo.setCampaign_img3(null);
                vo.setCampaign_img4(null);
                vo.setCampaign_img5(null);
                break;
            case 2:
                vo.setCampaign_img1(newOrderList.get(0));
                vo.setCampaign_img2(newOrderList.get(1));
                vo.setCampaign_img3(null);
                vo.setCampaign_img4(null);
                vo.setCampaign_img5(null);
                break;
            case 3:
                vo.setCampaign_img1(newOrderList.get(0));
                vo.setCampaign_img2(newOrderList.get(1));
                vo.setCampaign_img3(newOrderList.get(2));
                vo.setCampaign_img4(null);
                vo.setCampaign_img5(null);
                break;
            case 4:
                vo.setCampaign_img1(newOrderList.get(0));
                vo.setCampaign_img2(newOrderList.get(1));
                vo.setCampaign_img3(newOrderList.get(2));
                vo.setCampaign_img4(newOrderList.get(3));
                vo.setCampaign_img5(null);
                break;
            case 5:
                vo.setCampaign_img1(newOrderList.get(0));
                vo.setCampaign_img2(newOrderList.get(1));
                vo.setCampaign_img3(newOrderList.get(2));
                vo.setCampaign_img4(newOrderList.get(3));
                vo.setCampaign_img5(newOrderList.get(4));
                break;
        }

        return campaignDAO.campaign_update_image_order(vo);
    }

    @Override
    @Transactional
    public void reservationCampaignPublish() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String searchDate = sdf.format(new Date());
//        System.out.println("!!! "+searchDate);
        List<Reservation_campaignVO> reservationList = campaignDAO.reservationCampaignList(searchDate);

        if(reservationList.size()!=0) {
            for(Reservation_campaignVO rvo : reservationList) {
//                System.out.println(">>>rvo"+rvo.toString());
                CampaignVO cvo = new CampaignVO();
                cvo.setCampaign_seq(getSequenceService.getSeq("camp"));
                cvo.setBrand_seq(rvo.getBrand_seq());
                cvo.setCampaign_title(rvo.getCampaign_title());
                cvo.setCampaign_country(rvo.getCampaign_country());
                cvo.setType(rvo.getType());
                cvo.setNeed_number(rvo.getNeed_number());
                cvo.setMultiple_number(rvo.getMultiple_number());
                cvo.setCampaign_description(rvo.getCampaign_description());
                cvo.setStatus(rvo.getStatus());
                cvo.setOriginal_price(rvo.getOriginal_price());
                cvo.setDiscount_price(rvo.getDiscount_price());
                cvo.setCampaign_img1(rvo.getCampaign_img1());
                cvo.setCampaign_img2(rvo.getCampaign_img2());
                cvo.setCampaign_img3(rvo.getCampaign_img3());
                cvo.setCampaign_img4(rvo.getCampaign_img4());
                cvo.setCampaign_img5(rvo.getCampaign_img5());

                campaignDAO.campaign_insert(cvo);
//                System.out.println(">>>cvo"+cvo.toString());
                campaignDAO.updatePublishStatus(rvo.getRe_campaign_seq());
            }
        }
    }

}
