package chapter03.application;

import com.bh6.hibernate.util.SessionUtil;

public class HibernateRankingService implements RankingService {

	@Override
	public void addRanking(String subject, String observer, String skill, int rank) {
		try (var session = SessionUtil.getSession()) {

		}
	}

	@Override
	public void getRankingFor(String subject, String skill) {

	}
}
