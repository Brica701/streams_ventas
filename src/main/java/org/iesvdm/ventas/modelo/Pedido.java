package org.iesvdm.ventas.modelo;
// Generated 15 dic. 2022 23:59:33 by Hibernate Tools 5.6.9.Final

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Pedido implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
	private Comercial comercial;
	private double total;
	private Date fecha;


}
