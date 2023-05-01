package com.udea.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IngresoEstudiante.
 */
@Entity
@Table(name = "ingreso_estudiante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngresoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_ingreso")
    private Instant fechaIngreso;

    @OneToOne
    @JoinColumn(unique = true)
    private Estudiante estudiante;

    @OneToOne
    @JoinColumn(unique = true)
    private Carrera carrera;

    @OneToOne
    @JoinColumn(unique = true)
    private Sede sede;

    @JsonIgnoreProperties(value = { "materias" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Pensum pensum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IngresoEstudiante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaIngreso() {
        return this.fechaIngreso;
    }

    public IngresoEstudiante fechaIngreso(Instant fechaIngreso) {
        this.setFechaIngreso(fechaIngreso);
        return this;
    }

    public void setFechaIngreso(Instant fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public IngresoEstudiante estudiante(Estudiante estudiante) {
        this.setEstudiante(estudiante);
        return this;
    }

    public Carrera getCarrera() {
        return this.carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public IngresoEstudiante carrera(Carrera carrera) {
        this.setCarrera(carrera);
        return this;
    }

    public Sede getSede() {
        return this.sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public IngresoEstudiante sede(Sede sede) {
        this.setSede(sede);
        return this;
    }

    public Pensum getPensum() {
        return this.pensum;
    }

    public void setPensum(Pensum pensum) {
        this.pensum = pensum;
    }

    public IngresoEstudiante pensum(Pensum pensum) {
        this.setPensum(pensum);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngresoEstudiante)) {
            return false;
        }
        return id != null && id.equals(((IngresoEstudiante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngresoEstudiante{" +
            "id=" + getId() +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            "}";
    }
}
