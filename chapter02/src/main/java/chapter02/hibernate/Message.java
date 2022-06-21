package chapter02.hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Message {

	private Long id;
	private String text;

	public Message() {
	}

	public Message(String text) {
		this.text = text;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Message that = (Message) o;
		return Objects.equals(id, that.id) && Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, text);
	}

	@Override
	public String toString() {
		return String.format("Message{id=%d,text='%s'}", id, text);
	}
}
