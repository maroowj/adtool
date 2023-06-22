package com.muzi.adtool.services;

import com.muzi.adtool.dao.GetSequenceDAO;
import com.muzi.adtool.domain.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class GetSequenceServiceImpl implements GetSequenceService{

    @Autowired
    GetSequenceDAO getSequenceDAO;

    @Transactional
    @Override
    public String getSeq(String seq_name) {
        String newSeq = null;
        String seqName = getSequenceDAO.getSeq2(seq_name);

        if(seqName == null) {
            newSeq = seq_name + "_0000000001";
            getSequenceDAO.insert(seq_name);
        }else {
            int seq_no = Integer.parseInt(seqName);
            seq_no = seq_no+1;
            String formSeq = String.format("%010d", seq_no);
            newSeq = seq_name + "_" + formSeq;
            getSequenceDAO.update(seq_name);
        }
        return newSeq;
    }
}
