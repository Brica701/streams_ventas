package org.iesvdm.ventas.modelo;
// Generated 15 dic. 2022 23:59:33 by Hibernate Tools 5.6.9.Final

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comercial implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
	private Integer id;

	private String nombre;
	private String apellido1;
	private String apellido2;
	private Float comision;

    @OneToMany(mappedBy = "comercial", fetch = FetchType.EAGER)
	private Set<Pedido> pedidos;

}
