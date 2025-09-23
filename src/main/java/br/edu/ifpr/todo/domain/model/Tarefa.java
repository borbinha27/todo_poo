package br.edu.ifpr.todo.domain.model;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "tarefas")
public class Tarefa {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String nome;
private String descricao;
@Enumerated(EnumType.STRING)
private TodoStatus status = TodoStatus.A_FAZER;
private LocalDate dataCriacao;
private LocalDate dataEntrega;
private Boolean importante = false;

public Tarefa() {
}
//getters e setters
}