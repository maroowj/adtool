package com.muzi.adtool.services;

import com.muzi.adtool.dao.NoticeDAO;
import com.muzi.adtool.domain.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;

@Repository
public class NoticeServiceImpl implements NoticeService{

    @Resource(name = "uploadPath")
    String uploadPath;

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    NoticeDAO noticeDAO;

    @Override
    public int insert(NoticeVO vo) {
        vo.setNotice_seq(getSequenceService.getSeq("notc"));
        return noticeDAO.notice_insert(vo);
    }

    @Override
    public NoticeVO read(String notice_seq) {
        noticeDAO.readCount_update(notice_seq);
        return noticeDAO.notice_read(notice_seq);
    }

    @Transactional
    @Override
    public int update_image_order(NoticeVO vo) {

        noticeDAO.notice_update(vo);
        NoticeVO nvo =  noticeDAO.notice_read(vo.getNotice_seq());
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

        return noticeDAO.notice_update_image_order(vo);
    }

    @Override
    @Transactional
    public int notice_delete(String notice_seq) {
        NoticeVO noticeVO = noticeDAO.notice_read(notice_seq);
        if(noticeVO.getImage1()!=null) {
            new File(uploadPath + "notice/" + noticeVO.getImage1()).delete();
        }
        if(noticeVO.getImage2()!=null) {
            new File(uploadPath + "notice/" + noticeVO.getImage2()).delete();
        }
        if(noticeVO.getImage3()!=null) {
            new File(uploadPath + "notice/" + noticeVO.getImage3()).delete();
        }
        if(noticeVO.getImage4()!=null) {
            new File(uploadPath + "notice/" + noticeVO.getImage4()).delete();
        }if(noticeVO.getImage5()!=null) {
            new File(uploadPath + "notice/" + noticeVO.getImage5()).delete();
        }
        return noticeDAO.notice_delete(notice_seq);
    }
}
