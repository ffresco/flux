package ar.com.bancogalicia.pausados.repository;

import ar.com.bancogalicia.pausados.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByProcesada(@Nullable Integer procesada);
}