import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstudiante } from 'app/shared/model/estudiante.model';
import { getEntities as getEstudiantes } from 'app/entities/estudiante/estudiante.reducer';
import { ICarrera } from 'app/shared/model/carrera.model';
import { getEntities as getCarreras } from 'app/entities/carrera/carrera.reducer';
import { ISede } from 'app/shared/model/sede.model';
import { getEntities as getSedes } from 'app/entities/sede/sede.reducer';
import { IPensum } from 'app/shared/model/pensum.model';
import { getEntities as getPensums } from 'app/entities/pensum/pensum.reducer';
import { IIngresoEstudiante } from 'app/shared/model/ingreso-estudiante.model';
import { getEntity, updateEntity, createEntity, reset } from './ingreso-estudiante.reducer';

export const IngresoEstudianteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estudiantes = useAppSelector(state => state.estudiante.entities);
  const carreras = useAppSelector(state => state.carrera.entities);
  const sedes = useAppSelector(state => state.sede.entities);
  const pensums = useAppSelector(state => state.pensum.entities);
  const ingresoEstudianteEntity = useAppSelector(state => state.ingresoEstudiante.entity);
  const loading = useAppSelector(state => state.ingresoEstudiante.loading);
  const updating = useAppSelector(state => state.ingresoEstudiante.updating);
  const updateSuccess = useAppSelector(state => state.ingresoEstudiante.updateSuccess);

  const handleClose = () => {
    navigate('/ingreso-estudiante' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstudiantes({}));
    dispatch(getCarreras({}));
    dispatch(getSedes({}));
    dispatch(getPensums({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaIngreso = convertDateTimeToServer(values.fechaIngreso);

    const entity = {
      ...ingresoEstudianteEntity,
      ...values,
      estudiante: estudiantes.find(it => it.id.toString() === values.estudiante.toString()),
      carrera: carreras.find(it => it.id.toString() === values.carrera.toString()),
      sede: sedes.find(it => it.id.toString() === values.sede.toString()),
      pensum: pensums.find(it => it.id.toString() === values.pensum.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaIngreso: displayDefaultDateTime(),
        }
      : {
          ...ingresoEstudianteEntity,
          fechaIngreso: convertDateTimeFromServer(ingresoEstudianteEntity.fechaIngreso),
          estudiante: ingresoEstudianteEntity?.estudiante?.id,
          carrera: ingresoEstudianteEntity?.carrera?.id,
          sede: ingresoEstudianteEntity?.sede?.id,
          pensum: ingresoEstudianteEntity?.pensum?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="arqui3RealApp.ingresoEstudiante.home.createOrEditLabel" data-cy="IngresoEstudianteCreateUpdateHeading">
            <Translate contentKey="arqui3RealApp.ingresoEstudiante.home.createOrEditLabel">Create or edit a IngresoEstudiante</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ingreso-estudiante-id"
                  label={translate('arqui3RealApp.ingresoEstudiante.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('arqui3RealApp.ingresoEstudiante.fechaIngreso')}
                id="ingreso-estudiante-fechaIngreso"
                name="fechaIngreso"
                data-cy="fechaIngreso"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="ingreso-estudiante-estudiante"
                name="estudiante"
                data-cy="estudiante"
                label={translate('arqui3RealApp.ingresoEstudiante.estudiante')}
                type="select"
              >
                <option value="" key="0" />
                {estudiantes
                  ? estudiantes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ingreso-estudiante-carrera"
                name="carrera"
                data-cy="carrera"
                label={translate('arqui3RealApp.ingresoEstudiante.carrera')}
                type="select"
              >
                <option value="" key="0" />
                {carreras
                  ? carreras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ingreso-estudiante-sede"
                name="sede"
                data-cy="sede"
                label={translate('arqui3RealApp.ingresoEstudiante.sede')}
                type="select"
              >
                <option value="" key="0" />
                {sedes
                  ? sedes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ingreso-estudiante-pensum"
                name="pensum"
                data-cy="pensum"
                label={translate('arqui3RealApp.ingresoEstudiante.pensum')}
                type="select"
              >
                <option value="" key="0" />
                {pensums
                  ? pensums.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ingreso-estudiante" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default IngresoEstudianteUpdate;
