package com.udea.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SolicitudReingreso.
 */
@Entity
@Table(name = "solicitud_reingreso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SolicitudReingreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_solicitud")
    private Instant fechaSolicitud;

    @Column(name = "motivo")
    private String motivo;

    @OneToOne
    @JoinColumn(unique = true)
    private Estudiante estudiante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SolicitudReingreso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaSolicitud() {
        return this.fechaSolicitud;
    }

    public SolicitudReingreso fechaSolicitud(Instant fechaSolicitud) {
        this.setFechaSolicitud(fechaSolicitud);
        return this;
    }

    public void setFechaSolicitud(Instant fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public SolicitudReingreso motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public SolicitudReingreso estudiante(Estudiante estudiante) {
        this.setEstudiante(estudiante);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitudReingreso)) {
            return false;
        }
        return id != null && id.equals(((SolicitudReingreso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitudReingreso{" +
            "id=" + getId() +
            ", fechaSolicitud='" + getFechaSolicitud() + "'" +
            ", motivo='" + getMotivo() + "'" +
            "}";
    }
}
