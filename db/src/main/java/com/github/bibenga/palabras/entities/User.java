package com.github.bibenga.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Table(name = "\"user\"", uniqueConstraints = {
        @UniqueConstraint(name = "u_application_external_id", columnNames = { "external_id" }),
})
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "is_enabled_flg")
    @ColumnDefault("true")
    private boolean enabled;

    @Column(name = "created_ts", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(name = "modified_ts", nullable = false, columnDefinition = "timestamp with time zone")
    @UpdateTimestamp
    private ZonedDateTime modified;
}
