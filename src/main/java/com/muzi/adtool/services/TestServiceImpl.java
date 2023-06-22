package com.muzi.adtool.services;

import com.muzi.adtool.dao.TestDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

	private final TestDAO testDAO;


}