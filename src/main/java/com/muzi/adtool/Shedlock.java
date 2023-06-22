package com.muzi.adtool;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shedlock")
@Getter
@Setter
@NoArgsConstructor
public class Shedlock {

	@Id
	@Column(nullable = false, length = 64)
	private String name;

	@Column(nullable = false, columnDefinition = "timestamp(3)")
	private Date lockUntil;

	@Column(nullable = false, columnDefinition = "timestamp(3) default CURRENT_TIMESTAMP(3)")
	private Date lockedAt;

	@Column(nullable = false, length = 255)
	private String lockedBy;

	@Builder
	public Shedlock(String name, Date lockUntil, Date lockedAt, String lockedBy) {
		this.name = name;
		this.lockUntil = lockUntil;
		this.lockedAt = lockedAt;
		this.lockedBy = lockedBy;
	}
}
