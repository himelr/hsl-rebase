package com.example.hsl_rebase_mono.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"email"})
})
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;

	@NotBlank
	@Column(name = "password")
	@Size(min = 5, max = 255)
	private String password;

	@NotBlank
	@Email
	@Column(name = "email", length = 50)
	private String email;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Set<Role> roles = new HashSet<>();

}
