package org.iesvdm.ventas.repositorio;

import org.iesvdm.ventas.modelo.Comercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercialRepository extends JpaRepository<Comercial, Integer> {
}
