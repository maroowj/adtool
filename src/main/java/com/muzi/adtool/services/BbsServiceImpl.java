package com.muzi.adtool.services;

import com.muzi.adtool.dao.BbsDAO;
import com.muzi.adtool.domain.BbsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;

@Repository
public class BbsServiceImpl implements BbsService{

    @Resource(name = "uploadPath")
    String uploadPath;

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    BbsDAO bbsDAO;

    @Override
    public int insert(BbsVO vo) {
        vo.setBbs_seq(getSequenceService.getSeq("bseq"));
        return bbsDAO.bbs_insert(vo);
    }

    @Override
    public BbsVO read(String bbs_seq) {
        bbsDAO.readCount_update(bbs_seq);
        return bbsDAO.bbs_read(bbs_seq);
    }

    @Transactional
    @Override
    public int update_image_order(BbsVO vo) {

        bbsDAO.bbs_update(vo);
        BbsVO nvo =  bbsDAO.bbs_read(vo.getBbs_seq());
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add(nvo.getImage1());
        imgList.add(nvo.getImage2());
        imgList.add(nvo.getImage3());
        imgList.add(nvo.getImage4());
        imgList.add(nvo.getImage5());
        ArrayList<String> newOrderList = new ArrayList<>();
//        System.out.println("imgList="+imgList);
        for(String arg:imgList){
            if(!arg.equals("")){
                newOrderList.add(arg);
            }
        }
//        System.out.println("newOrderList="+newOrderList);
        int newOrderListSize = newOrderList.size();
        switch (newOrderListSize){
            case 1:
                vo.setImage1(newOrderList.get(0));
                vo.setImage2(null);
                vo.setImage3(null);
                vo.setImage4(null);
                vo.setImage5(null);
                break;
            case 2:
                vo.setImage1(newOrderList.get(0));
                vo.setImage2(newOrderList.get(1));
                vo.setImage3(null);
                vo.setImage4(null);
                vo.setImage5(null);
                break;
            case 3:
                vo.setImage1(newOrderList.get(0));
                vo.setImage2(newOrderList.get(1));
                vo.setImage3(newOrderList.get(2));
                vo.setImage4(null);
                vo.setImage5(null);
                break;
            case 4:
                vo.setImage1(newOrderList.get(0));
                vo.setImage2(newOrderList.get(1));
                vo.setImage3(newOrderList.get(2));
                vo.setImage4(newOrderList.get(3));
                vo.setImage5(null);
                break;
            case 5:
                vo.setImage1(newOrderList.get(0));
                vo.setImage2(newOrderList.get(1));
                vo.setImage3(newOrderList.get(2));
                vo.setImage4(newOrderList.get(3));
                vo.setImage5(newOrderList.get(4));
                break;
        }

        return bbsDAO.bbs_update_image_order(vo);
    }

    @Override
    @Transactional
    public int notice_delete(String bbs_seq) {
        BbsVO bbsVO = bbsDAO.bbs_read(bbs_seq);
        if(bbsVO.getImage1()!=null && !bbsVO.getImage1().equals("")) {
            new File(uploadPath + "help/" + bbsVO.getImage1()).delete();
        }
        if(bbsVO.getImage2()!=null && !bbsVO.getImage2().equals("")) {
            new File(uploadPath + "help/" + bbsVO.getImage2()).delete();
        }
        if(bbsVO.getImage3()!=null && !bbsVO.getImage3().equals("")) {
            new File(uploadPath + "help/" + bbsVO.getImage3()).delete();
        }
        if(bbsVO.getImage4()!=null && !bbsVO.getImage4().equals("")) {
            new File(uploadPath + "help/" + bbsVO.getImage4()).delete();
        }
        if(bbsVO.getImage5()!=null && !bbsVO.getImage5().equals("")) {
            new File(uploadPath + "help/" + bbsVO.getImage5()).delete();
        }
        return bbsDAO.bbs_delete(bbs_seq);
    }
}
