package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em;  //Repository는 EntityManager를 구형해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDTO> subscribeList(int principalId, int pageUserId) {
		//쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");   //끝에 한칸 띄어주어야 한다.안하면 오류가날수도있다
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append("if((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ? ");  //세미클론 첨부하면 안됨 중요!!
		
		//물음표  principal.getUser().getId()
		//물음표  principal.getUser().getId()
		// 마지막 물음표 pageUserid
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
				
		//쿼리 실행(qlrm 라이브러리 필요 = Dto에 DB결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDTO> subscribeDto = result.list(query, SubscribeDTO.class);
		
		return subscribeDto;
	}
	
	@Transactional
	public void  doSubscribe(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void cancelSubscribe(int fromUserId, int toUserId) {
		subscribeRepository.mCancelSubscribe(fromUserId, toUserId);
	}
}
