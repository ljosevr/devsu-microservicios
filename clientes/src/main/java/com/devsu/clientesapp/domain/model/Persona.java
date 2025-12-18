package com.devsu.clientesapp.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String genero;

    @Column
    private Integer edad;

    @Column(unique = true, nullable = false, length = 20)
    private String identificacion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;
}

