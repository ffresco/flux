package ar.com.bancogalicia.pausados.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ws_invocation_result")
public class InvocationWSResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invocationTime")
    private Date fechaInvocacion;

    private String resultado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tarjeta")
    private Tarjeta tarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_batch")
    private Batch batch;




}
