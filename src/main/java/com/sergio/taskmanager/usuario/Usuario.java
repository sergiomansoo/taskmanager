package com.sergio.taskmanager.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String nome;
    @Column(nullable = false,length = 100,unique = true)
    private String email;
    @Column(length=30)
    private String senha;
    @Column(nullable = false)
    private LocalDateTime data_criacao;
    @PrePersist
    public void prePersist(){
        if(data_criacao==null){
            this.data_criacao=LocalDateTime.now();
        }
    }

}
