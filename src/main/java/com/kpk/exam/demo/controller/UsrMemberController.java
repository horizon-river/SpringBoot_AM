package com.kpk.exam.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kpk.exam.demo.service.MemberService;
import com.kpk.exam.demo.util.Ut;
import com.kpk.exam.demo.vo.Member;
import com.kpk.exam.demo.vo.ResultData;
import com.kpk.exam.demo.vo.Rq;

@Controller
public class UsrMemberController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private Rq rq;
	
	// 액션 메서드
	@RequestMapping("usr/member/doJoin")
	@ResponseBody
	public ResultData<Member> doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		if (Ut.empty(loginId)) {
			return ResultData.from("F-1", "아이디를 입력해주세요.");
		}
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "비밀번호를 입력해주세요.");
		}
		if (Ut.empty(name)) {
			return ResultData.from("F-3", "이름을 입력해주세요.");
		}
		if (Ut.empty(nickname)) {
			return ResultData.from("F-4", "닉네임을 입력해주세요.");
		}
		if (Ut.empty(cellphoneNum)) {
			return ResultData.from("F-5", "전화번호를 입력해주세요.");
		}
		if (Ut.empty(email)) {
			return ResultData.from("F-6", "이메일을 입력해주세요.");
		}
		// S-1
		// 회원가입 완료
		// F-1~8
		// 실패
		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNum, email);
		
		if (joinRd.isFail()) {
			return (ResultData) joinRd;
		}
		
		Member member = memberService.getMemberById((int)joinRd.getData1());
		
		return ResultData.newData(joinRd, "member", member);
	}
	
	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login"; 
	}
	
	@RequestMapping("usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, @RequestParam(defaultValue = "/") String afterLoginUri) {
		
		if (rq.isLogined()) {
			return Ut.jsHistoryBack("이미 로그인된 상태입니다.");
		}
		
		if (Ut.empty(loginId)) {
			return Ut.jsHistoryBack("아이디를 입력해주세요.");
		}
		
		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("비밀번호를 입력해주세요.");
		}
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null) {
			return Ut.jsHistoryBack("아이디를 잘못 입력했습니다.");
		}
		
		if (member.getLoginPw().equals(loginPw) == false) {
			return Ut.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		rq.login(member);
		
		return Ut.jsReplace(Ut.f("%s님 환영합니다.", member.getNickname()), afterLoginUri);
	}
	
	@RequestMapping("usr/member/doLogout")
	@ResponseBody
	public String doLogout() {
		
		rq.logout();
		
		return Ut.jsReplace("로그아웃 되었습니다.", "/");
	}
	
	@RequestMapping("usr/member/myPage")
	public String showMyPage() {
		return "usr/member/myPage";
	}

	@RequestMapping("usr/member/checkPassword")
	public String showCheckPassword() {
		return "usr/member/checkPassword";
	}
	
	@RequestMapping("usr/member/doCheckPassword")
	@ResponseBody
	public String doCheckPassword(String loginPw, String replaceUri) {
		if(Ut.empty(loginPw)) {
			return rq.jsHistoryBack("비밀번호를 입력해주세요.");
		}
		
		if(rq.getLoginedMember().getLoginPw().equals(loginPw) == false) {
			return rq.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		if(replaceUri.equals("../member/modify")) {
			String memberModifyAuthKey = memberService.genMemberModifyAuthKey(rq.getLoginedMemberId());
			
			replaceUri += "?memberModifyAuthKey=" + memberModifyAuthKey;
		}
		
		return Ut.jsReplace("", replaceUri);
	}
	
	@RequestMapping("usr/member/modify")
	public String showModify(String memberModifyAuthKey) {
		if(Ut.empty(memberModifyAuthKey)) {
			return rq.jsHistoryBackOnView("회원 수정 인증코드가 필요합니다.");
		}
		
		ResultData checkMemeberModifyAuthKeyRd = memberService.checkMemeberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);
		
		if(checkMemeberModifyAuthKeyRd.isFail()) {
			return rq.jsHistoryBackOnView(checkMemeberModifyAuthKeyRd.getMsg());
		}
		
		return "usr/member/modify";
	}
	
	@RequestMapping("usr/member/doModify")
	@ResponseBody
	public String doModify(String memberModifyAuthKey, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		if(Ut.empty(memberModifyAuthKey)) {
			return rq.jsHistoryBackOnView("회원 수정 인증코드가 필요합니다.");
		}
		
		ResultData checkMemeberModifyAuthKeyRd = memberService.checkMemeberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);
		
		if(checkMemeberModifyAuthKeyRd.isFail()) {
			return rq.jsHistoryBackOnView(checkMemeberModifyAuthKeyRd.getMsg());
		}
		
		if(Ut.empty(loginPw)) {
			loginPw = null;
		}
		
		if(Ut.empty(name)) {
			return rq.jsHistoryBack("이름을 입력해주세요.");
		}
		
		if(Ut.empty(nickname)) {
			return rq.jsHistoryBack("닉네임을 입력해주세요.");
		}
		
		if(Ut.empty(cellphoneNum)) {
			return rq.jsHistoryBack("전화번호를 입력해주세요.");
		}
		
		if(Ut.empty(email)) {
			return rq.jsHistoryBack("이메일을 입력해주세요.");
		}
		
		ResultData modifyRd = memberService.modify(rq.getLoginedMemberId(), loginPw, name, nickname, cellphoneNum, email);
		
		return rq.jsReplace(modifyRd.getMsg(), "/");
		
	}
}