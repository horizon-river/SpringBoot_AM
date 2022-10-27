package com.kpk.exam.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kpk.exam.demo.service.MemberService;
import com.kpk.exam.demo.service.ReactionPointService;
import com.kpk.exam.demo.util.Ut;
import com.kpk.exam.demo.vo.Member;
import com.kpk.exam.demo.vo.ResultData;
import com.kpk.exam.demo.vo.Rq;

@Controller
public class UsrReactionPointController {
	
	@Autowired
	private ReactionPointService reactionPointService;
	@Autowired
	private Rq rq;
	
	@RequestMapping("/usr/reactionPoint/doGoodReaction")
	@ResponseBody
	public String doGoodReaction(String relTypeCode, int relId, String replaceUri) {
		boolean actorCanMakeReaction = reactionPointService.actorCanMakeReaction(rq.getLoginedMemberId() ,relTypeCode, relId);
		
		if (actorCanMakeReaction == false) {
			return rq.jsHistoryBack("이미 처리되었습니다.");
		}
		
		reactionPointService.addGoodReactionPoint(rq.getLoginedMemberId() ,relTypeCode, relId);
		
		return rq.jsReplace("좋아요!", replaceUri); 
	}
	
	@RequestMapping("/usr/reactionPoint/doBadReaction")
	@ResponseBody
	public String doBadReaction(String relTypeCode, int relId, String replaceUri) {
		boolean actorCanMakeReaction = reactionPointService.actorCanMakeReaction(rq.getLoginedMemberId() ,relTypeCode, relId);
		
		if (actorCanMakeReaction == false) {
			return rq.jsHistoryBack("이미 처리되었습니다.");
		}
		
		reactionPointService.addBadReactionPoint(rq.getLoginedMemberId() ,relTypeCode, relId);
		
		return rq.jsReplace("싫어요!", replaceUri); 
	}
}