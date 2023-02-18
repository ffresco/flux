package ar.com.bancogalicia.pausados.repository;

import ar.com.bancogalicia.pausados.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long> {
}