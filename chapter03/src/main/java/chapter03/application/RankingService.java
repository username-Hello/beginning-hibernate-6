package chapter03.application;

public interface RankingService {

	void addRanking(String subject, String observer, String skill, int rank);

	void getRankingFor(String subject, String skill);

}
