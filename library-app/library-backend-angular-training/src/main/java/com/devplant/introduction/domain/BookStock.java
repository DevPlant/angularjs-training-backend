package com.devplant.introduction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "book", "user" })
@NoArgsConstructor
public class BookStock {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Long reservationTimestamp;

	private Long pickupTimestamp;

	private Long returnTimestamp;

	private boolean pickedUp;

	@ManyToOne
	@JsonIgnore
	private Book book;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "user_id", updatable = false, insertable = false)
	private Long userId;

	public BookStock(Book book) {
		this.book = book;
	}
}
