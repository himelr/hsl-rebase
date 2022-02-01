package com.example.hsl_rebase_mono.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

	@CreatedDate
	@Column(name = "created_date", nullable = false)
	@JsonIgnore
	private ZonedDateTime createdDate = ZonedDateTime.now();

	@LastModifiedDate
	@Column(name = "last_modified_date")
	@JsonIgnore
	private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

}
