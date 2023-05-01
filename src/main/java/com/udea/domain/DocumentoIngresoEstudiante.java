package com.udea.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentoIngresoEstudiante.
 */
@Entity
@Table(name = "documento_ingreso_estudiante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentoIngresoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_documento")
    private String nombreDocumento;

    @Column(name = "descripcion_documento")
    private String descripcionDocumento;

    @JsonIgnoreProperties(value = { "estudiante", "carrera", "sede", "pensum" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private IngresoEstudiante ingresoEstudiante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentoIngresoEstudiante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return this.nombreDocumento;
    }

    public DocumentoIngresoEstudiante nombreDocumento(String nombreDocumento) {
        this.setNombreDocumento(nombreDocumento);
        return this;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getDescripcionDocumento() {
        return this.descripcionDocumento;
    }

    public DocumentoIngresoEstudiante descripcionDocumento(String descripcionDocumento) {
        this.setDescripcionDocumento(descripcionDocumento);
        return this;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    public IngresoEstudiante getIngresoEstudiante() {
        return this.ingresoEstudiante;
    }

    public void setIngresoEstudiante(IngresoEstudiante ingresoEstudiante) {
        this.ingresoEstudiante = ingresoEstudiante;
    }

    public DocumentoIngresoEstudiante ingresoEstudiante(IngresoEstudiante ingresoEstudiante) {
        this.setIngresoEstudiante(ingresoEstudiante);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoIngresoEstudiante)) {
            return false;
        }
        return id != null && id.equals(((DocumentoIngresoEstudiante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoIngresoEstudiante{" +
            "id=" + getId() +
            ", nombreDocumento='" + getNombreDocumento() + "'" +
            ", descripcionDocumento='" + getDescripcionDocumento() + "'" +
            "}";
    }
}
