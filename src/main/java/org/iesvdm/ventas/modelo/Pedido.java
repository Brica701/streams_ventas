package org.iesvdm.ventas.modelo;
// Generated 15 dic. 2022 23:59:33 by Hibernate Tools 5.6.9.Final

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
	private Integer id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_comercial")
	private Comercial comercial;
	private double total;
	private Date fecha;


}
