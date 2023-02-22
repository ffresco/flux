package ar.com.bancogalicia.pausados.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tarjetas")
@Data
@ToString
public class Tarjeta {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @CsvBindByName(column = "TARJETA")
    private String cardNumber;
    @CsvBindByName(column = "MARCA")
    private String brand;
    @CsvBindByName(column = "VENCIMIENTO")
    private String dueDate;
    @CsvBindByName(column = "TITULAR")
    private String ownerName;
    @CsvBindByName(column = "BLOQUEAR")
    private Integer blocked;

    @Column(name = "procesada")
    private Integer procesada;
    @PrePersist
    void preInsert() {
        if (this.procesada == null)
            this.procesada = 0;
    }
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date createAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date updateAt;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "tarjeta")
    private List<InvocationWSResult> invocationWSResults;




}