package com.muzi.adtool.dao;

public interface GetSequenceDAO {

    public String getSeq(String seq_name);

    public String getSeq2(String seq_name);

    public void update(String seq_name);

    public void insert(String seq_name);
}
