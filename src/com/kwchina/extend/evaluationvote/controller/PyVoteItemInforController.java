package com.kwchina.extend.evaluationvote.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.common.controller.BasicController;
import com.kwchina.extend.evaluationvote.service.PyItemInforManager;
import com.kwchina.extend.evaluationvote.service.PyVoteItemInforManager;

@Controller
@RequestMapping("/evaluationvote/pyVoteItemInfor.do")
public class PyVoteItemInforController extends BasicController{
	
	@Resource
	private PyItemInforManager pyItemInforManager;
	
	@Resource
	private PyVoteItemInforManager pyVoteItemInforManager;
	
	
}