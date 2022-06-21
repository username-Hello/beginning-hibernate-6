package chapter03.hibernate;

import javax.persistence.*;

@Entity
public class Ranking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Person observer;

	@ManyToOne
	private Person subject;

	@ManyToOne
	private Skill skill;

	@Column
	private Integer ranking;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getObserver() {
		return observer;
	}

	public void setObserver(Person observer) {
		this.observer = observer;
	}

	public Person getSubject() {
		return subject;
	}

	public void setSubject(Person subject) {
		this.subject = subject;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
}
