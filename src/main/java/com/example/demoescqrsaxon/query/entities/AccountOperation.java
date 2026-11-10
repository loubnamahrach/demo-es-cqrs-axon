package com.example.demoescqrsaxon.query.entities;

import com.example.demoescqrsaxon.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant date;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private String currency;
    @ManyToOne
    private Account account;
}
