package com.sergio.taskmanager.usuario;

import com.sergio.taskmanager.usuario.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(length=255)
    private String senha;
    @Column(nullable = false)
    private LocalDateTime data_criacao;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @PrePersist
    public void prePersist(){
        if(data_criacao==null){
            this.data_criacao=LocalDateTime.now();
        }
    }

}
