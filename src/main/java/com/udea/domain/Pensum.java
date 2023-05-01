package com.udea.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pensum.
 */
@Entity
@Table(name = "pensum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pensum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Long numero;

    @ManyToMany
    @JoinTable(
        name = "rel_pensum__materias",
        joinColumns = @JoinColumn(name = "pensum_id"),
        inverseJoinColumns = @JoinColumn(name = "materias_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pensums" }, allowSetters = true)
    private Set<Materia> materias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pensum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return this.numero;
    }

    public Pensum numero(Long numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Set<Materia> getMaterias() {
        return this.materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }

    public Pensum materias(Set<Materia> materias) {
        this.setMaterias(materias);
        return this;
    }

    public Pensum addMaterias(Materia materia) {
        this.materias.add(materia);
        materia.getPensums().add(this);
        return this;
    }

    public Pensum removeMaterias(Materia materia) {
        this.materias.remove(materia);
        materia.getPensums().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pensum)) {
            return false;
        }
        return id != null && id.equals(((Pensum) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pensum{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            "}";
    }
}
