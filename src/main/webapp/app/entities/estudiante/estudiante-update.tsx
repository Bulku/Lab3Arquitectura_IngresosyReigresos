import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstudiante } from 'app/shared/model/estudiante.model';
import { getEntity, updateEntity, createEntity, reset } from './estudiante.reducer';

export const EstudianteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estudianteEntity = useAppSelector(state => state.estudiante.entity);
  const loading = useAppSelector(state => state.estudiante.loading);
  const updating = useAppSelector(state => state.estudiante.updating);
  const updateSuccess = useAppSelector(state => state.estudiante.updateSuccess);

  const handleClose = () => {
    navigate('/estudiante');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaNacimiento = convertDateTimeToServer(values.fechaNacimiento);

    const entity = {
      ...estudianteEntity,
      ...values,
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
          fechaNacimiento: displayDefaultDateTime(),
        }
      : {
          ...estudianteEntity,
          fechaNacimiento: convertDateTimeFromServer(estudianteEntity.fechaNacimiento),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="arqui3RealApp.estudiante.home.createOrEditLabel" data-cy="EstudianteCreateUpdateHeading">
            <Translate contentKey="arqui3RealApp.estudiante.home.createOrEditLabel">Create or edit a Estudiante</Translate>
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
                  id="estudiante-id"
                  label={translate('arqui3RealApp.estudiante.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('arqui3RealApp.estudiante.nombre')}
                id="estudiante-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.estudiante.apellido')}
                id="estudiante-apellido"
                name="apellido"
                data-cy="apellido"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.estudiante.fechaNacimiento')}
                id="estudiante-fechaNacimiento"
                name="fechaNacimiento"
                data-cy="fechaNacimiento"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('arqui3RealApp.estudiante.correo')}
                id="estudiante-correo"
                name="correo"
                data-cy="correo"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.estudiante.direccion')}
                id="estudiante-direccion"
                name="direccion"
                data-cy="direccion"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/estudiante" replace color="info">
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

export default EstudianteUpdate;
